package com.genius.employee.activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Collections;

public class CovidNegativeReportActivity extends AppCompatActivity {
    Spinner spFamilyMember;
    ArrayList<String> familyMemberList = new ArrayList<String>();
    ArrayList<SpinnerModel> mFamilyMember = new ArrayList<SpinnerModel>();

    Button btnCovidFile;
    AlertDialog al2, al3;
    ImageView imgPic, imgPDF;
    File file, compressedImageFile;
    int flag, flag2;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int REQUEST_GALLERY_CODE_VACCINE = 201;
    Uri uri;
    Spinner spCovid;
    ArrayList<String> covidList = new ArrayList<String>();
    LinearLayout llCovid;
    String covidStatus = "";
    TextView tvCovidDate;
    Button btnSubmit;
    String covidDate = "";
    String vaccineDate = "";
    TextView tvDate;
    ImageView imgCovidDate;
    Pref pref;
    AlertDialog alerDialog1, alert;
    String familyMember = "";
    String postiveDate;
    Button btnSave;
    ArrayList<String>submitList=new ArrayList<String>();
    TextView tvPostiveDate;
    AlertDialog al4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_negative_report);
        initView();
        onClick();
    }


    private void initView() {
        pref = new Pref(CovidNegativeReportActivity.this);


        spFamilyMember = (Spinner) findViewById(R.id.spFamilyMember);

        llCovid = (LinearLayout) findViewById(R.id.llCovid);
        btnCovidFile = (Button) findViewById(R.id.btnCovidFile);


        imgPic = (ImageView) findViewById(R.id.imgPic);
        imgPDF = (ImageView) findViewById(R.id.imgPDF);

        spCovid = (Spinner) findViewById(R.id.spCovid);

        covidList.add("Recovered");


        ArrayAdapter<String> covidspinnerArrayAdapter = new ArrayAdapter<String>
                (CovidNegativeReportActivity.this, android.R.layout.simple_spinner_item,
                        covidList); //selected item will look like a spinner set from XML
        covidspinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCovid.setAdapter(covidspinnerArrayAdapter);

        tvCovidDate = (TextView) findViewById(R.id.tvCovidDate);


        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        tvDate = (TextView) findViewById(R.id.tvDate);

        imgCovidDate = (ImageView) findViewById(R.id.imgCovidDate);

        btnSave=(Button)findViewById(R.id.btnSave);
        tvPostiveDate=(TextView)findViewById(R.id.tvPostiveDate);




    }



    private void onClick() {


        spFamilyMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    familyMember = mFamilyMember.get(position).getItemName();
                    postiveDate =mFamilyMember.get(position).getItemId();
                    tvPostiveDate.setText("Covid Positive Report Date: "+postiveDate);
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


        btnCovidFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileOpenDilog();
            }
        });

        spCovid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    covidStatus = covidList.get(position);
                    tvCovidDate.setText("Negative Report Date:");
                    btnCovidFile.setText("Upload Negative Report");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finalSubmit();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlert();
            }
        });
    }

    private void fileOpenDilog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CovidNegativeReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_file, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llCamera = (LinearLayout) dialogView.findViewById(R.id.llCamera);
        LinearLayout llGallery = (LinearLayout) dialogView.findViewById(R.id.llGallery);
        LinearLayout llPDF = (LinearLayout) dialogView.findViewById(R.id.llPDF);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageBackCameraActivity.launch(CovidNegativeReportActivity.this);

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

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
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
                            String filePath = getRealPathFromURIPath(uri, CovidNegativeReportActivity.this);
                            file = new File(filePath);
                            compressedImageFile = new ImageZipper(CovidNegativeReportActivity.this)
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
                        compressedImageFile = new ImageZipper(CovidNegativeReportActivity.this)
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


        }
    }

    private void galleryIntent() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
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
                    flag = 1;
                    imgPDF.setVisibility(View.VISIBLE);
                    compressedImageFile = new File(files[0]);
                    al2.dismiss();
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(CovidNegativeReportActivity.this,
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


    private void finalSubmit() {
        if (!familyMember.equals("")) {

            covidPostiveorNegativeCheck();
        }else {
            Toast.makeText(CovidNegativeReportActivity.this,"Please Select Relationship",Toast.LENGTH_LONG).show();
        }


    }

    private void covidPostiveorNegativeCheck() {

            if (!covidDate.equals("")) {
                if (flag == 1) {





                } else {
                    Toast.makeText(CovidNegativeReportActivity.this, "Please Upload Report", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(CovidNegativeReportActivity.this, "Please Select Recovery Date", Toast.LENGTH_LONG).show();
            }



    }



    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CovidNegativeReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();


            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(familyMember+ " details has been submitted successfully");


        alert = dialogBuilder.create();
        alert.setCancelable(false);
        Window window = alert.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert.show();
    }

    private void finalAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CovidNegativeReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                Intent intent=new Intent(CovidNegativeReportActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();


            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Thank You for Your Declaration");


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }



    @Override
    public void onBackPressed() {
       // super.onBackPressed();

    }

    public  boolean equalLists(ArrayList<String> one, ArrayList<String> two){
        if (one == null && two == null){
            return true;
        }

        if((one == null && two != null)
                || one != null && two == null
                || one.size() != two.size()){
            return false;
        }

        //to avoid messing the order of the lists we will use a copy
        //as noted in comments by A. R. S.
        one = new ArrayList<String>(one);
        two = new ArrayList<String>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

    private void showErrorDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CovidNegativeReportActivity.this, R.style.CustomDialogNew);
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