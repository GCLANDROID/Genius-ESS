package com.genius.employee.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.developers.imagezipper.ImageZipper;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.cameraview.LongImageBackCameraActivity;
import com.google.android.cameraview.LongImageCameraActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class SelfCovidDecActivity extends AppCompatActivity {
    Button btnCovidFile,btnVaccinationFile;
    AlertDialog al2,al3,al4;
    ImageView imgPic,imgPDF;
    File file,compressedImageFile,vaccineFile,compressVaccineFile;
    int flag,flag2;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int REQUEST_GALLERY_CODE_VACCINE = 201;
    Uri uri;
    Spinner spCovid,spVaccine;
    ArrayList<String>covidList=new ArrayList<String>();
    LinearLayout llCovid;
    String covidStatus="";
    TextView tvCovidDate;
    LinearLayout llVaccinationDate;
    ArrayList<String>vaccinationList=new ArrayList<String>();
    String vaccinationStatus="";
    ImageView imgVaccinationPic,imgVaccinationPDF;
    Button btnSubmit;
    String covidDate="";
    String vaccineDate="";
    TextView tvDate,tvVaccineDate,tvVaccDate;
    ImageView imgCovidDate,imgVaccineDate;
    Pref pref;
    AlertDialog alerDialog1;
    TextView tvViewReport;
    Button btnSecndDose,btnBoosterDose;
    




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_covid_dec);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(SelfCovidDecActivity.this);
        llCovid=(LinearLayout)findViewById(R.id.llCovid);
        btnCovidFile=(Button)findViewById(R.id.btnCovidFile);
        btnVaccinationFile=(Button)findViewById(R.id.btnVaccinationFile);

        imgPic=(ImageView)findViewById(R.id.imgPic);
        imgPDF=(ImageView)findViewById(R.id.imgPDF);

        spCovid=(Spinner)findViewById(R.id.spCovid);

        covidList.add("Please Select");
        covidList.add("Not Effected");
        covidList.add("Positive");
        covidList.add("Recovered");


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (SelfCovidDecActivity.this, android.R.layout.simple_spinner_item,
                        covidList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCovid.setAdapter(spinnerArrayAdapter);

        tvCovidDate=(TextView)findViewById(R.id.tvCovidDate);

        llVaccinationDate=(LinearLayout)findViewById(R.id.llVaccinationDate);

        spVaccine=(Spinner)findViewById(R.id.spVaccine);

        vaccinationList.add("Please Select");
        vaccinationList.add("Vaccinated (1st Dose)");
        vaccinationList.add("Registered in Cowin app");

        ArrayAdapter<String> spinnerArrayAdapterVaccine = new ArrayAdapter<String>
                (SelfCovidDecActivity.this, android.R.layout.simple_spinner_item,
                        vaccinationList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVaccine.setAdapter(spinnerArrayAdapterVaccine);

        imgVaccinationPDF=(ImageView)findViewById(R.id.imgVaccinationPDF);
        imgVaccinationPic=(ImageView)findViewById(R.id.imgVaccinationPic);

        btnSubmit=(Button)findViewById(R.id.btnSubmit);

        tvDate=(TextView)findViewById(R.id.tvDate);

        imgCovidDate=(ImageView)findViewById(R.id.imgCovidDate);

        tvVaccineDate=(TextView)findViewById(R.id.tvVaccineDate);
        imgVaccineDate=(ImageView)findViewById(R.id.imgVaccineDate);

        tvVaccDate=(TextView)findViewById(R.id.tvVaccDate);

        tvViewReport=(TextView)findViewById(R.id.tvViewReport);

        btnSecndDose=(Button)findViewById(R.id.btnSecndDose);
        btnBoosterDose=(Button)findViewById(R.id.btnBoosterDose);







    }

    private void onClick(){
        btnSecndDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(SelfCovidDecActivity.this,SecondDoseCertificateActivity.class);
              intent.putExtra("doseflag","sec");
              startActivity(intent);

            }
        });

        btnBoosterDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelfCovidDecActivity.this,SecondDoseCertificateActivity.class);
                intent.putExtra("doseflag","booster");
                startActivity(intent);

            }
        });
        imgCovidDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCovidDatePicker();
            }
        });

        imgVaccineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVaccineDatePicker();
            }
        });
        btnCovidFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileOpenDilog();
            }
        });

        spCovid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    covidStatus=covidList.get(position);

                    if (covidStatus.equals("Positive") || covidStatus.equals("Recovered")){
                        llCovid.setVisibility(View.VISIBLE);
                    }else {
                        llCovid.setVisibility(View.GONE);
                    }

                    if (covidStatus.equals("Positive")){
                        tvCovidDate.setText("Positive Report Date:");
                        btnCovidFile.setText("Upload Positive Report");
                    }else {
                        tvCovidDate.setText("Negative Report Date:");
                        btnCovidFile.setText("Upload Negative Report/Doctor's Fit Certificate");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spVaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    vaccinationStatus=vaccinationList.get(position);
                    if (vaccinationStatus.equals("Vaccinated (1st Dose)")){
                        tvVaccDate.setText("Vaccination Date");
                        btnVaccinationFile.setText("Upload Vaccination Certificate");
                    }else {
                        tvVaccDate.setText("Registration Date");
                        btnVaccinationFile.setText("Upload Vaccine Registration Document");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnVaccinationFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileOpenDilogForVaccination();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSubmit();
            }
        });

        tvViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelfCovidDecActivity.this,CovidStatusReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }



    private void fileOpenDilog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelfCovidDecActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_file, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llCamera=(LinearLayout)dialogView.findViewById(R.id.llCamera);
        LinearLayout llGallery=(LinearLayout)dialogView.findViewById(R.id.llGallery);
        LinearLayout llPDF=(LinearLayout)dialogView.findViewById(R.id.llPDF);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageBackCameraActivity.launch(SelfCovidDecActivity.this);

            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });

        llPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCovidPDFPickerdialog();
            }
        });

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al2.dismiss();
            }
        });


        al2 = dialogBuilder.create();
        al2.setCancelable(true);
        Window window = al2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al2.show();


    }

    private void fileOpenDilogForVaccination() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelfCovidDecActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_file, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llCamera=(LinearLayout)dialogView.findViewById(R.id.llCamera);
        LinearLayout llGallery=(LinearLayout)dialogView.findViewById(R.id.llGallery);
        LinearLayout llPDF=(LinearLayout)dialogView.findViewById(R.id.llPDF);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageBackCameraActivity.launchVaccination(SelfCovidDecActivity.this);

            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntentForVaccine();
            }
        });

        llPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVaccinePDFPickerdialog();
            }
        });

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al3.dismiss();
            }
        });


        al3 = dialogBuilder.create();
        al3.setCancelable(true);
        Window window = al3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al3.show();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_GALLERY_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        try {
                            uri = data.getData();
                            String filePath = getRealPathFromURIPath(uri, SelfCovidDecActivity.this);
                            file = new File(filePath);
                            compressedImageFile = new ImageZipper(SelfCovidDecActivity.this)
                                    .setQuality(80)
                                    .setMaxWidth(250)
                                    .setMaxHeight(250)
                                    .compressToFile(file);
                            imageStream = getContentResolver().openInputStream(uri);
                            Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            imgPic.setVisibility(View.VISIBLE);
                            imgPic.setImageBitmap(bm);
                            flag = 1;
                            al2.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_2:


                if (resultCode == RESULT_OK && requestCode == LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_2) {
                    file = (File) data.getExtras().get("picture");
                    try {
                        compressedImageFile = new ImageZipper(SelfCovidDecActivity.this)
                                .setQuality(80)
                                .setMaxWidth(250)
                                .setMaxHeight(250)
                                .compressToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    imgPic.setVisibility(View.VISIBLE);
                    imgPic.setImageBitmap(putImage);
                    flag = 1;
                    al2.dismiss();

                }
                break;

            case LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_3:


                if (resultCode == RESULT_OK && requestCode == LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_3) {
                    vaccineFile = (File) data.getExtras().get("picture");
                    try {
                        compressVaccineFile = new ImageZipper(SelfCovidDecActivity.this)
                                .setQuality(80)
                                .setMaxWidth(250)
                                .setMaxHeight(250)
                                .compressToFile(vaccineFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    imgVaccinationPic.setVisibility(View.VISIBLE);
                    imgVaccinationPic.setImageBitmap(putImage);
                    flag2 = 1;
                    al3.dismiss();

                }
                break;


            case REQUEST_GALLERY_CODE_VACCINE:
                if (resultCode == Activity.RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        try {
                            uri = data.getData();
                            String filePath = getRealPathFromURIPath(uri, SelfCovidDecActivity.this);
                            vaccineFile = new File(filePath);
                            compressVaccineFile = new ImageZipper(SelfCovidDecActivity.this)
                                    .setQuality(80)
                                    .setMaxWidth(250)
                                    .setMaxHeight(250)
                                    .compressToFile(vaccineFile);
                            imageStream = getContentResolver().openInputStream(uri);
                            Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            imgVaccinationPic.setVisibility(View.VISIBLE);
                            imgVaccinationPic.setImageBitmap(bm);
                            flag2 = 1;
                            al3.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;


        }
    }

    private void galleryIntent() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
    }

    private void galleryIntentForVaccine() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE_VACCINE);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
        return cropImg;
    }

    private void showCovidPDFPickerdialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"pdf", "PDF"};

        final FilePickerDialog dialog = new FilePickerDialog(this, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                dialog.dismiss();
                if (files.length > 0) {
                    Log.d("arpan", files[0]);
                    imgPic.setVisibility(View.GONE);
                    flag=1;
                    imgPDF.setVisibility(View.VISIBLE);
                    compressedImageFile = new File(files[0]);
                    al2.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void showVaccinePDFPickerdialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"pdf", "PDF"};

        final FilePickerDialog dialog = new FilePickerDialog(this, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                dialog.dismiss();
                if (files.length > 0) {
                    Log.d("arpan", files[0]);
                    imgVaccinationPic.setVisibility(View.GONE);
                    imgVaccinationPDF.setVisibility(View.VISIBLE);
                    flag2=1;
                    compressVaccineFile = new File(files[0]);
                    al3.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void showCovidDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(SelfCovidDecActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String mmonth = null;
                        int md = (monthOfYear + 1);
                        if (md == 1) {
                            mmonth = "Jan";
                        } else if (md == 2) {
                            mmonth = "Feb";
                        } else if (md == 3) {
                            mmonth = "Mar";
                        } else if (md == 4) {
                            mmonth = "Apr";
                        } else if (md == 5) {
                            mmonth = "May";
                        } else if (md == 6) {
                            mmonth = "Jun";
                        } else if (md == 7) {
                            mmonth = "Jul";
                        } else if (md == 8) {
                            mmonth = "Aug";
                        } else if (md == 9) {
                            mmonth = "Sep";
                        } else if (md == 10) {
                            mmonth = "Oct";
                        } else if (md == 11) {
                            mmonth = "Nov";
                        } else if (md == 12) {
                            mmonth = "Dec";
                        }
                        covidDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;

                        tvDate.setText(dayOfMonth + "-" + mmonth + "-" + year);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


    }

    private void showVaccineDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(SelfCovidDecActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String mmonth = null;
                        int md = (monthOfYear + 1);
                        if (md == 1) {
                            mmonth = "Jan";
                        } else if (md == 2) {
                            mmonth = "Feb";
                        } else if (md == 3) {
                            mmonth = "Mar";
                        } else if (md == 4) {
                            mmonth = "Apr";
                        } else if (md == 5) {
                            mmonth = "May";
                        } else if (md == 6) {
                            mmonth = "Jun";
                        } else if (md == 7) {
                            mmonth = "Jul";
                        } else if (md == 8) {
                            mmonth = "Aug";
                        } else if (md == 9) {
                            mmonth = "Sep";
                        } else if (md == 10) {
                            mmonth = "Oct";
                        } else if (md == 11) {
                            mmonth = "Nov";
                        } else if (md == 12) {
                            mmonth = "Dec";
                        }
                        vaccineDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;

                        tvVaccineDate.setText(dayOfMonth + "-" + mmonth + "-" + year);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


    }

    private void finalSubmit(){
        if (!covidStatus.equals("")){
           covidPostiveorNegativeCheck();

        }else {
            Toast.makeText(SelfCovidDecActivity.this,"Please Select Your Covid Status",Toast.LENGTH_LONG).show();
        }
    }

    private void covidPostiveorNegativeCheck(){
        if (covidStatus.equals("Positive") || covidStatus.equals("Recovered")){
            if (!covidDate.equals("")){
                if (flag==1){
                    if (!vaccinationStatus.equals("")){
                        if (!vaccineDate.equals("")){
                            if (flag2==1){



                            }else {
                                Toast.makeText(SelfCovidDecActivity.this,"Please Upload Vaccination Document",Toast.LENGTH_LONG).show();

                            }

                        }else {
                            Toast.makeText(SelfCovidDecActivity.this,"Please Select Vaccination Date/Registration Date",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(SelfCovidDecActivity.this,"Please Select Vaccination Status",Toast.LENGTH_LONG).show();


                    }

                }else {
                    Toast.makeText(SelfCovidDecActivity.this,"Please Upload Report",Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(SelfCovidDecActivity.this,"Please Select Positive/Recovered Date",Toast.LENGTH_LONG).show();
            }
        }else {
            if (!vaccinationStatus.equals("")){
                if (!vaccineDate.equals("")){
                    if (flag2==1){



                    }else {
                        Toast.makeText(SelfCovidDecActivity.this,"Please Upload Vaccination Document",Toast.LENGTH_LONG).show();

                    }

                }else {
                    Toast.makeText(SelfCovidDecActivity.this,"Please Select Vaccination Date/Registration Date",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(SelfCovidDecActivity.this,"Please Select Vaccination Status",Toast.LENGTH_LONG).show();


            }
        }
    }



    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelfCovidDecActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        TextView tvOk=(TextView)dialogView.findViewById(R.id.tvOk);
        tvOk.setText("Upload Family Member(s)/PG Companion Status");
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                Intent intent = new Intent(getApplicationContext(), FamilyMemeberCovidStatusActivity.class);
                startActivity(intent);
                finish();

            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your Own Covid Status has been  Saved Successfully.");


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showErrorDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelfCovidDecActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al4.dismiss();

            }
        });

        //tvText

        TextView tvText = (TextView) dialogView.findViewById(R.id.tvText);
        tvText.setText("It has not been 17 days since tested positive.");
        al4 = dialogBuilder.create();
        al4.setCancelable(false);
        Window window = al4.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al4.show();


    }
}