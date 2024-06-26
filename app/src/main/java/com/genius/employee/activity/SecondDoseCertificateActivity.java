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
import android.text.TextUtils;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.genius.employee.adapter.SeconDoseMemberAdapter;
import com.genius.employee.utility.APi;
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

public class SecondDoseCertificateActivity extends AppCompatActivity {
    Pref pref;
    ProgressDialog progressDialog;
    Spinner spFamilyMember;
    ArrayList<String>familyMemberList=new ArrayList<>();
    Button btnVaccinationFile;
    LinearLayout llVaccinationDate;
    Spinner spVaccine,spVaccineName,spAge;
    ArrayList<String>vaccinationList=new ArrayList<>();
    ImageView imgVaccinationPDF,imgVaccinationPic;
    Button btnSubmit,btnAddFamilyMember;
    TextView tvVaccineDate,tvVaccDate;
    ImageView imgVaccineDate;
    RecyclerView rvFamilyMember;
    String familyMember;
    String vaccinationStatus;
    AlertDialog al3;
    private static final int REQUEST_GALLERY_CODE_VACCINE = 201;
    Uri uri;
    File vaccineFile,compressVaccineFile;
    int flag2=0;
    String vaccineDate;
    ArrayList<String>vaccineNamelist=new ArrayList<>();
    String vaccineName="";
    EditText etOther;
    ArrayList<String>ageGroupList=new ArrayList<>();
    String age="";
    AlertDialog alerDialog1,alertDialog;
    LinearLayout llFamily;
    JSONArray jsonArray=new JSONArray();
    String doseflag;
    String api;
    TextView tvToolBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_dose_certificate);
        initView();
        onClick();
    }


    private void initView(){
        pref=new Pref(SecondDoseCertificateActivity.this);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        doseflag=getIntent().getStringExtra("doseflag");
        progressDialog=new ProgressDialog(SecondDoseCertificateActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);




        spAge=(Spinner)findViewById(R.id.spAge);
        ageGroupList.add("Above 18 Years");
        ageGroupList.add("Below 18 Years");

        ArrayAdapter<String> spinnerArrayAdapterAge = new ArrayAdapter<String>
                (SecondDoseCertificateActivity.this, android.R.layout.simple_spinner_item,
                        ageGroupList); //selected item will look like a spinner set from XML
        spinnerArrayAdapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAge.setAdapter(spinnerArrayAdapterAge);

        spFamilyMember=(Spinner)findViewById(R.id.spFamilyMember);

        familyMemberList.add("Please Select");
        familyMemberList.add("Self");
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
                (SecondDoseCertificateActivity.this, android.R.layout.simple_spinner_item,
                        familyMemberList); //selected item will look like a spinner set from XML
        spinnerFamilyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFamilyMember.setAdapter(spinnerFamilyArrayAdapter);


        btnVaccinationFile=(Button)findViewById(R.id.btnVaccinationFile);




        llVaccinationDate=(LinearLayout)findViewById(R.id.llVaccinationDate);

        spVaccine=(Spinner)findViewById(R.id.spVaccine);

        if (doseflag.equals("sec")){
            vaccinationList.add("Vaccinated (2nd Dose)");
            tvToolBar.setText("Vaccine 2nd Dose");
        }else {
            vaccinationList.add("Vaccinated Booster Dose");
            tvToolBar.setText("Vaccine Booster Dose");

        }



        ArrayAdapter<String> spinnerArrayAdapterVaccine = new ArrayAdapter<String>
                (SecondDoseCertificateActivity.this, android.R.layout.simple_spinner_item,
                        vaccinationList); //selected item will look like a spinner set from XML
        spinnerArrayAdapterVaccine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVaccine.setAdapter(spinnerArrayAdapterVaccine);

        imgVaccinationPDF=(ImageView)findViewById(R.id.imgVaccinationPDF);
        imgVaccinationPic=(ImageView)findViewById(R.id.imgVaccinationPic);

        btnSubmit=(Button)findViewById(R.id.btnSubmit);

        tvVaccineDate=(TextView)findViewById(R.id.tvVaccineDate);
        imgVaccineDate=(ImageView)findViewById(R.id.imgVaccineDate);

        tvVaccDate=(TextView)findViewById(R.id.tvVaccDate);
        btnAddFamilyMember=(Button)findViewById(R.id.btnAddFamilyMember);

        rvFamilyMember=(RecyclerView)findViewById(R.id.rvFamilyMember);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(SecondDoseCertificateActivity.this, LinearLayoutManager.VERTICAL, false);

        rvFamilyMember.setLayoutManager(layoutManager);

        spVaccineName=(Spinner)findViewById(R.id.spVaccineName);

        vaccineNamelist.add("Please Select");
        vaccineNamelist.add("Covishield");
        vaccineNamelist.add("Covaxin");
        vaccineNamelist.add("Sputnik v");
        vaccineNamelist.add("Other");

        ArrayAdapter<String> spinnerArrayAdapterVaccineName = new ArrayAdapter<String>
                (SecondDoseCertificateActivity.this, android.R.layout.simple_spinner_item,
                        vaccineNamelist); //selected item will look like a spinner set from XML
        spinnerArrayAdapterVaccineName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVaccineName.setAdapter(spinnerArrayAdapterVaccineName);
        etOther=(EditText)findViewById(R.id.etOther);
        llFamily=(LinearLayout)findViewById(R.id.llFamily);
    }

    private void onClick(){
        spAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age=ageGroupList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spVaccineName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    vaccineName=vaccineNamelist.get(position);
                    if (vaccineName.equals("Other")){
                        etOther.setVisibility(View.VISIBLE);
                    }else {
                        etOther.setVisibility(View.GONE);
                    }
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


                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        imgVaccineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVaccineDatePicker();
            }
        });




        spVaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    vaccinationStatus=vaccinationList.get(position);
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
                if (TextUtils.isEmpty(familyMember)){
                    Toast.makeText(SecondDoseCertificateActivity.this,"Please Select Relationship",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(vaccineName)){
                    Toast.makeText(SecondDoseCertificateActivity.this,"Please Select Vaccine Name",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(vaccineDate)){
                    Toast.makeText(SecondDoseCertificateActivity.this,"Please Select Vaccination Date",Toast.LENGTH_LONG).show();
                    return;
                }
                if (flag2==0){
                    Toast.makeText(SecondDoseCertificateActivity.this,"Please upload vaccination certificate",Toast.LENGTH_LONG).show();
                    return;
                }

                postFuncion();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successAlertFinsl();
            }
        });
    }



    private void fileOpenDilogForVaccination() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SecondDoseCertificateActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_file, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llCamera=(LinearLayout)dialogView.findViewById(R.id.llCamera);
        LinearLayout llGallery=(LinearLayout)dialogView.findViewById(R.id.llGallery);
        LinearLayout llPDF=(LinearLayout)dialogView.findViewById(R.id.llPDF);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageBackCameraActivity.launchVaccination(SecondDoseCertificateActivity.this);

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



            case LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_3:


                if (resultCode == RESULT_OK && requestCode == LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_3) {
                    vaccineFile = (File) data.getExtras().get("picture");
                    try {
                        compressVaccineFile = new ImageZipper(SecondDoseCertificateActivity.this)
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
                            String filePath = getRealPathFromURIPath(uri, SecondDoseCertificateActivity.this);
                            vaccineFile = new File(filePath);
                            compressVaccineFile = new ImageZipper(SecondDoseCertificateActivity.this)
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



    private void showVaccineDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(SecondDoseCertificateActivity.this,
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

    private void postFuncion(){
        if (vaccineName.equals("Other")){
            vaccineName=etOther.getText().toString();
        }else {
            vaccineName=vaccineName;
        }
        progressDialog.show();
        AndroidNetworking.upload(api)
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("VaccinationStatus",vaccinationStatus)
                .addMultipartFile("VaccinationFile", compressVaccineFile)
                .addMultipartParameter("VaccinationDate", vaccineDate)
                .addMultipartParameter("AgeStatus", age)
                .addMultipartParameter("VaccineName", vaccineName)
                .addMultipartParameter("RelationShip", familyMember)
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

                            Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            btnSubmit.setVisibility(View.VISIBLE);

                            successAlert(vaccineName);


                        }else if (ResponseCode.equals("0"))
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();




                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("errort",error.toString());
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void successAlert(String vaccineName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SecondDoseCertificateActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        TextView tvOk=(TextView)dialogView.findViewById(R.id.tvOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("realation",familyMember);
                    jsonObject.put("vaccine",vaccineName);
                    jsonObject.put("date",vaccineDate);
                    jsonArray.put(jsonObject);
                    llFamily.setVisibility(View.VISIBLE);
                    SeconDoseMemberAdapter seconDoseMemberAdapter=new SeconDoseMemberAdapter(jsonArray,SecondDoseCertificateActivity.this);
                    rvFamilyMember.setAdapter(seconDoseMemberAdapter);
                    spFamilyMember.setSelection(0);

                    spVaccine.setSelection(0);

                    flag2=0;

                    vaccineDate="";
                    familyMember="";

                    vaccinationStatus="";

                    compressVaccineFile=new File("");
                    tvVaccineDate.setText("");

                    imgVaccinationPDF.setVisibility(View.GONE);
                    imgVaccinationPic.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(familyMember+" vaccine certificate has been  Saved Successfully.");


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void successAlertFinsl() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SecondDoseCertificateActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        TextView tvOk=(TextView)dialogView.findViewById(R.id.tvOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

               Intent intent=new Intent(SecondDoseCertificateActivity.this,EDashBoardActivity.class);
               startActivity(intent);
               finish();


            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Vaccine Second dose certificate has been Submitted Successfully.");


        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }
}