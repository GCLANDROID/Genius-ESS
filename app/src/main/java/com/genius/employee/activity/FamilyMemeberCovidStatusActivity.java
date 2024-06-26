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
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
/*import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.genius.employee.adapter.FamilyCovidAdapter;
import com.genius.employee.model.FamilyCovidModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;
import com.google.android.cameraview.LongImageBackCameraActivity;
import com.google.android.cameraview.LongImageCameraActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FamilyMemeberCovidStatusActivity extends AppCompatActivity {
    Spinner spFamilyMemberYesNo,spFamilyMember;
    ArrayList<String>familyMemberYesNoList=new ArrayList<String>();
    String familyMemberYesNo="";
    LinearLayout llForm;
    ArrayList<String>familyMemberList=new ArrayList<String>();

    Button btnCovidFile,btnVaccinationFile;
    AlertDialog al2,al3;
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
    AlertDialog alerDialog1,alert;
    Button btnAddFamilyMember;
    String familyMember="";
    LinearLayout llFamily;
    ArrayList<FamilyCovidModel>itemList=new ArrayList<FamilyCovidModel>();
    RecyclerView rvFamilyMember;
    ProgressDialog progressDialog;
    LinearLayout llAge;
    ArrayList<String>ageGroupList=new ArrayList<String>();
    Spinner spAge;
    String age="Above 18 Years";
    LinearLayout llVaccine;
    AlertDialog al4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_memeber_covid_status);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(FamilyMemeberCovidStatusActivity.this);
        progressDialog=new ProgressDialog(FamilyMemeberCovidStatusActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        spFamilyMemberYesNo=(Spinner)findViewById(R.id.spFamilyMemberYesNo);
        familyMemberYesNoList.add("Yes");
        familyMemberYesNoList.add("No");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (FamilyMemeberCovidStatusActivity.this, android.R.layout.simple_spinner_item,
                        familyMemberYesNoList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFamilyMemberYesNo.setAdapter(spinnerArrayAdapter);

        llForm=(LinearLayout)findViewById(R.id.llForm);

        spFamilyMember=(Spinner)findViewById(R.id.spFamilyMember);

        familyMemberList.add("Please Select");
        familyMemberList.add("Father");
        familyMemberList.add("Mother");
        familyMemberList.add("Husband");
        familyMemberList.add("Wife");
        familyMemberList.add("Son");
        familyMemberList.add("Daughter");
        familyMemberList.add("Brother");
        familyMemberList.add("Sister");
        familyMemberList.add("Cousins");
        familyMemberList.add("Uncle");
        familyMemberList.add("Aunty");
        familyMemberList.add("Nephew");
        familyMemberList.add("Niece");
        familyMemberList.add("Father-in-law");
        familyMemberList.add("Mother-in-law");
        familyMemberList.add("PG Companion");
        familyMemberList.add("Friend");
        familyMemberList.add("Other");

        ArrayAdapter<String> spinnerFamilyArrayAdapter = new ArrayAdapter<String>
                (FamilyMemeberCovidStatusActivity.this, android.R.layout.simple_spinner_item,
                        familyMemberList); //selected item will look like a spinner set from XML
        spinnerFamilyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFamilyMember.setAdapter(spinnerFamilyArrayAdapter);

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


        ArrayAdapter<String> covidspinnerArrayAdapter = new ArrayAdapter<String>
                (FamilyMemeberCovidStatusActivity.this, android.R.layout.simple_spinner_item,
                        covidList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCovid.setAdapter(covidspinnerArrayAdapter);

        tvCovidDate=(TextView)findViewById(R.id.tvCovidDate);

        llVaccinationDate=(LinearLayout)findViewById(R.id.llVaccinationDate);

        spVaccine=(Spinner)findViewById(R.id.spVaccine);

        vaccinationList.add("Please Select");
        vaccinationList.add("Vaccinated (1st Dose)");
        vaccinationList.add("Registered in Cowin app");

        ArrayAdapter<String> spinnerArrayAdapterVaccine = new ArrayAdapter<String>
                (FamilyMemeberCovidStatusActivity.this, android.R.layout.simple_spinner_item,
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
        btnAddFamilyMember=(Button)findViewById(R.id.btnAddFamilyMember);
        llFamily=(LinearLayout)findViewById(R.id.llFamily);
        rvFamilyMember=(RecyclerView)findViewById(R.id.rvFamilyMember);
        rvFamilyMember.setLayoutManager(new GridLayoutManager(this, 3));
        llAge=(LinearLayout)findViewById(R.id.llAge);

        spAge=(Spinner)findViewById(R.id.spAge);
        ageGroupList.add("Above 18 Years");
        ageGroupList.add("Below 18 Years");

        ArrayAdapter<String> spinnerArrayAdapterAge = new ArrayAdapter<String>
                (FamilyMemeberCovidStatusActivity.this, android.R.layout.simple_spinner_item,
                        ageGroupList); //selected item will look like a spinner set from XML
        spinnerArrayAdapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAge.setAdapter(spinnerArrayAdapterAge);

        llVaccine=(LinearLayout)findViewById(R.id.llVaccine);
    }

    private void onClick(){
        spFamilyMemberYesNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                familyMemberYesNo=familyMemberYesNoList.get(position);
                if (familyMemberYesNo.equals("Yes")){
                    llForm.setVisibility(View.VISIBLE);
                }else {
                    llForm.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spFamilyMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    familyMember=familyMemberList.get(position);
                    if (familyMember.equals("Father")||familyMember.equals("Mother")||familyMember.equals("Husband")||familyMember.equals("Wife")||familyMember.equals("Uncle")||familyMember.equals("Aunty")||familyMember.equals("Father-in-law")||familyMember.equals("Mother-in-law")||familyMember.equals("PG Companion")||familyMember.equals("Friend")||familyMember.equals("Other") ||familyMember.equals("Friend")){
                        llAge.setVisibility(View.GONE);
                    }else {
                        llAge.setVisibility(View.VISIBLE);
                    }

                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1){
                    vaccinationList.add("Not Applicable");
                    ArrayAdapter<String> spinnerArrayAdapterVaccine = new ArrayAdapter<String>
                            (FamilyMemeberCovidStatusActivity.this, android.R.layout.simple_spinner_item,
                                    vaccinationList); //selected item will look like a spinner set from XML
                    spinnerArrayAdapterVaccine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spVaccine.setAdapter(spinnerArrayAdapterVaccine);

                }else {

                    ArrayAdapter<String> spinnerArrayAdapterVaccine = new ArrayAdapter<String>
                            (FamilyMemeberCovidStatusActivity.this, android.R.layout.simple_spinner_item,
                                    vaccinationList); //selected item will look like a spinner set from XML
                    spinnerArrayAdapterVaccine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spVaccine.setAdapter(spinnerArrayAdapterVaccine);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        llVaccine.setVisibility(View.VISIBLE);
                        btnVaccinationFile.setText("Upload Vaccination Certificate");
                    }else if (vaccinationStatus.equals("Not Applicable")){
                       llVaccine.setVisibility(View.GONE);
                    }else {
                        tvVaccDate.setText("Registration Date");
                        btnVaccinationFile.setText("Upload Vaccine Registration Document");
                        llVaccine.setVisibility(View.VISIBLE);
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

        btnAddFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSubmit();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveandfinishcheck();
            }
        });
    }

    private void fileOpenDilog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FamilyMemeberCovidStatusActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_file, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llCamera=(LinearLayout)dialogView.findViewById(R.id.llCamera);
        LinearLayout llGallery=(LinearLayout)dialogView.findViewById(R.id.llGallery);
        LinearLayout llPDF=(LinearLayout)dialogView.findViewById(R.id.llPDF);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageBackCameraActivity.launch(FamilyMemeberCovidStatusActivity.this);

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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FamilyMemeberCovidStatusActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_file, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llCamera=(LinearLayout)dialogView.findViewById(R.id.llCamera);
        LinearLayout llGallery=(LinearLayout)dialogView.findViewById(R.id.llGallery);
        LinearLayout llPDF=(LinearLayout)dialogView.findViewById(R.id.llPDF);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageBackCameraActivity.launchVaccination(FamilyMemeberCovidStatusActivity.this);

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
                            String filePath = getRealPathFromURIPath(uri, FamilyMemeberCovidStatusActivity.this);
                            file = new File(filePath);
                            compressedImageFile = new ImageZipper(FamilyMemeberCovidStatusActivity.this)
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
                        compressedImageFile = new ImageZipper(FamilyMemeberCovidStatusActivity.this)
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
                        compressVaccineFile = new ImageZipper(FamilyMemeberCovidStatusActivity.this)
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
                            String filePath = getRealPathFromURIPath(uri, FamilyMemeberCovidStatusActivity.this);
                            vaccineFile = new File(filePath);
                            compressVaccineFile = new ImageZipper(FamilyMemeberCovidStatusActivity.this)
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(FamilyMemeberCovidStatusActivity.this,
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(FamilyMemeberCovidStatusActivity.this,
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
       if (!familyMember.equals("")){
           covidPostiveorNegativeCheck();

       }else {
           Toast.makeText(FamilyMemeberCovidStatusActivity.this,"Please Select Relationship",Toast.LENGTH_LONG).show();
       }

    }

    private void covidPostiveorNegativeCheck(){
        if (covidStatus.equals("Positive") || covidStatus.equals("Recovered")){
            if (!covidDate.equals("")){
                if (flag==1){
                    nACheck();

                }else {
                    Toast.makeText(FamilyMemeberCovidStatusActivity.this,"Please Upload Report",Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(FamilyMemeberCovidStatusActivity.this,"Please Select Positive/Recovered Date",Toast.LENGTH_LONG).show();
            }
        }else {
           nACheck();
        }


    }

    private void nACheck(){
        if (vaccinationStatus.equals("Not Applicable")){

        }else {
            if (!vaccinationStatus.equals("")){
                if (!vaccineDate.equals("")){
                    if (flag2==1){



                    }else {
                        Toast.makeText(FamilyMemeberCovidStatusActivity.this,"Please Upload Vaccination Document",Toast.LENGTH_LONG).show();

                    }

                }else {
                    Toast.makeText(FamilyMemeberCovidStatusActivity.this,"Please Select Vaccination Date/Registration Date",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(FamilyMemeberCovidStatusActivity.this,"Please Select Vaccination Status",Toast.LENGTH_LONG).show();


            }
        }
    }





    public void familyDetailsShow(String covidStaus, String covidDate, String covidfileName, final String covidFileUrl, String vaccineStatus, String vaccineDate, String vaccineFileName, final String vaccineFileUrl ){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FamilyMemeberCovidStatusActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_family_covid, null);
        dialogBuilder.setView(dialogView);
        TextView tvCovidStatus=(TextView)dialogView.findViewById(R.id.tvCovidStatus);
        tvCovidStatus.setText(covidStaus);

        TextView tvCovidDate=(TextView)dialogView.findViewById(R.id.tvCovidDate);
        tvCovidDate.setText(covidDate);

        TextView tvcovidDocName=(TextView)dialogView.findViewById(R.id.tvcovidDocName);
        tvcovidDocName.setText(covidfileName);

        TextView tvVaccineStatus=(TextView)dialogView.findViewById(R.id.tvVaccineStatus);
        tvVaccineStatus.setText(vaccineStatus);

        TextView tvVaccineDate=(TextView)dialogView.findViewById(R.id.tvVaccineDate);
        tvVaccineDate.setText(vaccineDate);

        TextView tvVaccineDoc=(TextView)dialogView.findViewById(R.id.tvVaccineDoc);
        tvVaccineDoc.setText(vaccineFileName);

        ImageView imgVaccine=(ImageView)dialogView.findViewById(R.id.imgVaccine);
        ImageView imgCovid=(ImageView)dialogView.findViewById(R.id.imgCovid);



        imgCovid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!covidFileUrl.equals("")){
                    Uri uri = Uri.parse(covidFileUrl); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        imgVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!vaccineFileUrl.equals("")){
                    Uri uri = Uri.parse(vaccineFileUrl); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        Button btnOK=(Button) dialogView.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
    private void saveandfinishcheck(){
        if (familyMemberYesNo.equals("Yes")){
            if (itemList.size()>0){

            }else {
                Toast.makeText(FamilyMemeberCovidStatusActivity.this,"Please add family member who are statying with you",Toast.LENGTH_LONG).show();
            }
        }else {

        }
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FamilyMemeberCovidStatusActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent = new Intent(getApplicationContext(), EDashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Thank You For the Covid Declaration");


        alert = dialogBuilder.create();
        alert.setCancelable(false);
        Window window = alert.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert.show();
    }

    private void showErrorDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FamilyMemeberCovidStatusActivity.this, R.style.CustomDialogNew);
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