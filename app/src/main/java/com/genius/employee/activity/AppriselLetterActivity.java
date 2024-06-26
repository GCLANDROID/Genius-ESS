package com.genius.employee.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity*/;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import im.delight.android.webview.AdvancedWebView;

public class AppriselLetterActivity extends AppCompatActivity implements AdvancedWebView.Listener {
    AdvancedWebView web_view;
    Button btnAccept,btnReject;
    String path;
    AlertDialog alerDialog1,alerDialog2,aletDialog3;
    SignaturePad canvasLL;
    File f;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    Bitmap bitmap;
    int signFlag;
    CheckBox chek_box;
    Pref pref;
    String url;
    MotionEvent eventt;
    Button btnsave;
    FileOutputStream fo ;
    String id;
    String jrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprisel_letter);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(AppriselLetterActivity.this);
        path=getIntent().getStringExtra("path");
        web_view=(AdvancedWebView)findViewById(R.id.web_view);
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        web_view.setListener(AppriselLetterActivity.this, AppriselLetterActivity.this);
        web_view.setMixedContentAllowed(false);
        try {
            url = "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(path, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        web_view.loadUrl(url);


        web_view.setWebViewClient(new WebViewClient() {


            public void onPageFinished(WebView view, String url) {

                progressDialog.dismiss();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                //Your code to do
                view.loadUrl(url);
                progressDialog.dismiss();
            }
        });





        btnAccept=(Button)findViewById(R.id.btnAccept);
        btnAccept.setEnabled(false);
        chek_box=(CheckBox) findViewById(R.id.chek_box);
        btnReject=(Button)findViewById(R.id.btnReject);
        id=getIntent().getStringExtra("id");
        jrCode=getIntent().getStringExtra("jrCode");



    }

    private void onClick(){
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureAlert();
            }
        });

        chek_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnAccept.setEnabled(true);

                } else {
                    btnAccept.setEnabled(false);
                }
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    rejectioneAlert();


            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        web_view.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        web_view.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        web_view.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        web_view.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!web_view.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

    private void signatureAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AppriselLetterActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_acceptance, null);
        dialogBuilder.setView(dialogView);

        final LinearLayout llPad=(LinearLayout)dialogView.findViewById(R.id.llPad);


        canvasLL = (SignaturePad) dialogView.findViewById(R.id.canvasLL);
        Button btnclear = (Button) dialogView.findViewById(R.id.btnclear);
        btnsave = (Button) dialogView.findViewById(R.id.btnsave);
        btnsave.setEnabled(false);

        canvasLL.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                btnsave.setEnabled(true);

            }

            @Override
            public void onClear() {
                btnsave.setEnabled(false);
            }
        });




        final EditText etRemarks=(EditText)dialogView.findViewById(R.id.etRemarks);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasLL.clear();
                alerDialog1.dismiss();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = saveBitMap(AppriselLetterActivity.this, llPad);    //which view you want to pass that view as parameter
                if (file != null) {
                    Log.i("TAG", "Drawing saved to the gallery!");

                    if (jrCode.equals("Jr.")){
                        postAcceptance(etRemarks.getText().toString(),file);
                    }else {
                         postAcceptancesnr(etRemarks.getText().toString(),file);
                    }


                } else {
                    Log.i("TAG", "Oops! Image could not be saved.");
                   // Toast.makeText(AppriselLetterActivity.this, "Oops! Image could not be saved.", Toast.LENGTH_LONG).show();
                }
            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    private void rejectioneAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AppriselLetterActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_reject, null);
        dialogBuilder.setView(dialogView);

        Button btnsave = (Button) dialogView.findViewById(R.id.btnsave);
        final EditText etRemarks=(EditText)dialogView.findViewById(R.id.etRemarks);


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etRemarks.getText().toString().length()>0) {
                    aletDialog3.dismiss();
                    if (jrCode.equals("Jr.")) {
                        postReject(etRemarks.getText().toString());
                    }else {
                        postRejectsnr(etRemarks.getText().toString());
                    }
                }else {
                    Toast.makeText(AppriselLetterActivity.this,"Please Enter Remarks",Toast.LENGTH_LONG).show();
                }



            }
        });


        aletDialog3 = dialogBuilder.create();
        aletDialog3.setCancelable(true);
        Window window = aletDialog3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        aletDialog3.show();
    }


    public String saveImage(Bitmap myBitmap) {
        signFlag = 1;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY /*iDyme folder*/);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh", wallpaperDirectory.toString());
        }


            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");


            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);

            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();


    }

    private void postAcceptance(String remarks,File f) {
        final ProgressDialog progressDialog=new ProgressDialog(AppriselLetterActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.upload(APi.sBulkAppLetterSaveDocReceivedApi)
                .addMultipartParameter("EmployeeID", pref.getSecureEmpId())
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("AcceptenceStatus", "1")
                .addMultipartFile("SignFile", f)
                .addMultipartParameter("DocumentType", id)
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        String ResponseCode = job1.optString("responseCode");

                        if (ResponseCode.equals("1")) {

                            successAlert("Thank You for Accepting Appraisal Letter");

                        }else
                        {
                            Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("errort",error.toString());

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void postAcceptancesnr(String remarks,File f) {
        final ProgressDialog progressDialog=new ProgressDialog(AppriselLetterActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.upload(APi.sSrAppLetterSaveDocReceivedApi)
                .addMultipartParameter("EmployeeID", pref.getSecureEmpId())
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("AcceptenceStatus", "1")
                .addMultipartFile("SignFile", f)
                .addMultipartParameter("DocumentType", id)
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        String ResponseCode = job1.optString("responseCode");

                        if (ResponseCode.equals("1")) {

                            successAlert("Thank You for Accepting Appraisal Letter");

                        }else
                        {
                            Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("errort",error.toString());

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void postReject(String remarks) {
        final ProgressDialog progressDialog=new ProgressDialog(AppriselLetterActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.upload(APi.sBulkAppLetterSaveDocReceivedApi)
                .addMultipartParameter("EmployeeID", pref.getSecureEmpId())
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("AcceptenceStatus", "0")
                .addMultipartFile("SignFile", f)
                .addMultipartParameter("DocumentType", id)
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        String ResponseCode = job1.optString("responseCode");

                        if (ResponseCode.equals("1")) {

                            successAlert("Appraisal Letter has been Rejected successfully");

                        }else
                        {


                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("errort",error.toString());

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
                    }
                });
    }

    private void postRejectsnr(String remarks) {
        final ProgressDialog progressDialog=new ProgressDialog(AppriselLetterActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.upload(APi.sSrAppLetterSaveDocReceivedApi)
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("AcceptenceStatus", "0")
                .addMultipartFile("SignFile", f)
                .addMultipartParameter("DocumentType", id)
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        String ResponseCode = job1.optString("responseCode");

                        if (ResponseCode.equals("1")) {

                            successAlert("Appraisal Letter has been Rejected successfully");

                        }else
                        {


                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("errort",error.toString());

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
                    }
                });
    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AppriselLetterActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog2.dismiss();
                Intent intent = new Intent(getApplicationContext(), EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);


        alerDialog2 = dialogBuilder.create();
        alerDialog2.setCancelable(false);
        Window window = alerDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog2.show();
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    // used for scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}