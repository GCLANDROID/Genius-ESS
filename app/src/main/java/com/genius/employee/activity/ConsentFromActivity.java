package com.genius.employee.activity;

import static maes.tech.intentanim.CustomIntent.customType;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.employee.R;
import com.genius.employee.signatureview.SignatureView;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.ShowDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConsentFromActivity extends AppCompatActivity {
    private static final String TAG = "ConsentFromActivity";
    TextView txtConsentText;
    ProgressDialog progressBar;
    CardView cvConsentView;
    ConstraintLayout clDeclaration;
    CheckBox cbMyDeclaration;
    Pref pref;
    AlertDialog alerDialog1;
    File wallpaperDirectory;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_from);
        initView();
        getConsentText();
    }

    private void initView() {
        pref = new Pref(this);
        txtConsentText = findViewById(R.id.txtConsentText);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        cvConsentView = findViewById(R.id.cvConsentView);
        clDeclaration = findViewById(R.id.clDeclaration);
        cbMyDeclaration = findViewById(R.id.cbMyDeclaration);

        cbMyDeclaration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   openConsentSignaturePopup();
                }
            }
        });
    }

    private void getConsentText() {
        progressBar.show();
        AndroidNetworking.get("https://cloud.geniusconsultant.com/GeniusESS/api/v2/consent/html")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.dismiss();
                        JSONObject jsonObject = response;
                        String responseText = jsonObject.optString("responseText");
                        boolean responseStatus=jsonObject.optBoolean("responseStatus");
                        if (responseStatus){
                            String responseData =  jsonObject.optString("responseData");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                txtConsentText.setText(Html.fromHtml(responseData, Html.FROM_HTML_MODE_LEGACY));
                            } else {
                                txtConsentText.setText(Html.fromHtml(responseData));
                            }
                            clDeclaration.setVisibility(View.VISIBLE);
                            cvConsentView.setVisibility(View.VISIBLE);
                        } else {
                            clDeclaration.setVisibility(View.GONE);
                            cvConsentView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        clDeclaration.setVisibility(View.GONE);
                        cvConsentView.setVisibility(View.GONE);
                        progressBar.dismiss();
                    }
                });
    }

    void openConsentSignaturePopup(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ConsentFromActivity.this,R.style.TransparentDialog);
        View view = getLayoutInflater().inflate(R.layout.consent_signature_dialog_layout, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        Button btnClear = bottomSheetDialog.findViewById(R.id.btnClear);
        Button btnSubmit = bottomSheetDialog.findViewById(R.id.btnSubmit);
        SignatureView signatureView = bottomSheetDialog.findViewById(R.id.canvasLL);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signatureView.isBitmapEmpty()){
                    Toast.makeText(ConsentFromActivity.this, "Please give your signature", Toast.LENGTH_SHORT).show();
                } else {
                    File signatureFile = saveImage(signatureView.getSignatureBitmap());
                    saveConsentSignature(signatureFile);
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });

        bottomSheetDialog.show();
    }

    private void saveConsentSignature(File signatureFile) {

        progressBar.setMessage("Please wait, loading...");
        progressBar.show();

        Log.e(TAG, "Employee ID: " + pref.getSecureEmpId());
        Log.e(TAG, "Signature File: " + signatureFile.getAbsolutePath());
        Log.e(TAG, "Signature Exists: " + signatureFile.exists());
        Log.e(TAG, "Signature Size: " + signatureFile.length());

        AndroidNetworking.upload(APi.Employee_Consent)
                .addMultipartParameter("employeeId", pref.getSecureEmpId())
                .addMultipartFile("signature", signatureFile)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.dismiss();

                        Log.d(TAG, "Response: " + response);

                        String responseText = response.optString("responseText");
                        boolean responseStatus = response.optBoolean("responseStatus");

                        if (responseStatus) {
                            successAlert(responseText);
                        } else {
                            ShowDialog.showAlertDialog(
                                    ConsentFromActivity.this,
                                    responseText,
                                    new ShowDialog.ResultListener() {
                                        @Override
                                        public void onSuccess() {
                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        progressBar.dismiss();

                        Log.e(TAG, "Error Body: " + anError.getErrorBody());
                        Log.e(TAG, "Error Detail: " + anError.getErrorDetail());

                        Toast.makeText(
                                ConsentFromActivity.this,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private File saveImage(Bitmap bitmap) {

        // Create Handcare folder inside app-specific storage
        wallpaperDirectory =  new File(getExternalFilesDir(null), IMAGE_DIRECTORY);

        // Create directory if it doesn't exist
        if (!wallpaperDirectory.exists()) {
            if (!wallpaperDirectory.mkdirs()) {
                Log.e("SAVE_IMAGE", "Failed to create directory.");
                return null;
            }
        }

        // Create image file
        File imageFile = new File(
                wallpaperDirectory,
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(imageFile);

            // Save as JPEG
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            fos.flush();

            Log.d("SAVE_IMAGE", "Saved Successfully");
            Log.d("SAVE_IMAGE", "Path : " + imageFile.getAbsolutePath());
            Log.d("SAVE_IMAGE", "Exists : " + imageFile.exists());
            Log.d("SAVE_IMAGE", "Size : " + imageFile.length());

            return imageFile;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConsentFromActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                Intent intent = new Intent(ConsentFromActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                customType(ConsentFromActivity.this, "left-to-right");
                finish();
            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
}
