package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.vipul.hp_hp.library.Layout_to_Image;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.appcompat.app.AppCompatActivity;

public class CovidFormActivity extends AppCompatActivity {

    TextView tvScript, tvName;
    Pref pref;
    Button btnsave;

    SignaturePad canvasLL;
    File f;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    Bitmap bitmap;
    int signFlag;
    FileOutputStream fo;
    Button btnclear;
    RelativeLayout rlPad;
    String companyName;
    int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_form);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(CovidFormActivity.this);
        rlPad = (RelativeLayout) findViewById(R.id.rlPad);
        companyName = android.os.Build.MANUFACTURER;
        version = Build.VERSION.SDK_INT;
        tvScript = (TextView) findViewById(R.id.tvScript);
        tvScript.setText("Dear Member, \n\nYou are aware that there has been severe surge in Covid19 Cases Across India. This has impacted heavily on our Personal  and Work Lives. \n \nWe as a Company is concerned about your Wellbeing & Safety. \n \nThus, under the current circumstances, it becomes very important to keep our Corporate HR informed about the status of you & your family, with regard to the Covid19 Updates. \n \nIn case, if an Employee/family member/people at Close proximity is being  affected with Covid, it is the prime responsibility of the Employee to take up the onus for the treatment and hospitalization (If Required). \n \nIn Case of any further help is required, the Company is always beside you to render necessary Support wherever needed.\n\nThus Corporate HR Team request you to fill in the form carefully and share the Honest Status to ensure safety & Security for all.");


        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText("I " + pref.getEmpName() + " declare that all evidences and declaration made here are Correct. In case of any Negligence Found , the Corporate HR Team would be compelled to take necessary steps.");

        btnsave = (Button) findViewById(R.id.btnsave);
        btnclear = (Button) findViewById(R.id.btnclear);

        canvasLL = (SignaturePad) findViewById(R.id.canvasLL);

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

    }

    private void onClick() {
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    File file = saveBitMap(CovidFormActivity.this, rlPad);    //which view you want to pass that view as parameter
                    if (file != null) {
                        Log.i("TAG", "Drawing saved to the gallery!");


                    } else {
                        Log.i("TAG", "Oops! Image could not be saved.");
                       // Toast.makeText(CovidFormActivity.this, "Oops! Image could not be saved.", Toast.LENGTH_LONG).show();
                    }


               /* bitmap = canvasLL.getSignatureBitmap();
                String path = saveImage(bitmap);
                Log.d("path", path);
                  postAcceptance(f);*/


            }
        });

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasLL.clear();

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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
                FileOutputStream oStream = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
                oStream.flush();
                oStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }




            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);

            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();


    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "out", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
