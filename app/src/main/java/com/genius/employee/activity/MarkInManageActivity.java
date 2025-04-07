package com.genius.employee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.developers.imagezipper.ImageZipper;
import com.dhims.timerview.TimerTextView;
import com.genius.employee.R;
import com.genius.employee.adapter.MarkViewAdapter;
import com.genius.employee.model.AddFaceModel;
import com.genius.employee.model.MarkInViewModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.GPSTracker;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.RetrofitService;
import com.genius.employee.utility.UploadObject;
import com.genius.employee.utility.Util;
import com.google.android.cameraview.LongImageBackCameraActivity;
import com.google.android.cameraview.LongImageCameraActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipul.hp_hp.library.Layout_to_Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MarkInManageActivity extends AppCompatActivity {
    TextView tvAddress, tvTime;
    LinearLayout llRefresh;
    ImageView imgCamera;
    CircleImageView imgImage;
    GPSTracker gps;
    ;
    double latitude = 0.0, longitude = 0.0;
    String address = "--";
    Pref pref;
    String currentDateTimeString;
    private String encodedImage;
    private Uri imageUri, uri;
    private static final int CAMERA_REQUEST = 1;
    int flag;
    File compressedImageFile, file;
    Button btnSubmit;
    String currentlat, currentlong;
    private RetrofitService uploadService;
    ;
    // private static String SERVER_PATH = APi.sUrl+"DailyLogTatGY/";

    TextView tvName;
    EditText etRemarks;
    AlertDialog alerDialog1;
    ImageView imgBack, imgHome;
    TextView tvClick, tvClickHere;

    private static final int REQUEST_GALLERY_CODE = 200;
    Bitmap bitmap;
    LinearLayout llImage, llNote;
    TextView tvCapture;
    public static final String TAG = MarkInManageActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //  private MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    LatLng latLng;
    AlertDialog al2,addressDialog,addressDetailsDialog;
    TextView tvCustom;
    ImageView imgUser;
    String time;
    private final static int INTERVAL = 1000 * 60 * 2;
    Spinner spWorkFrom;
    ArrayList<String> wrkHomeList = new ArrayList<>();
    String wrkFrm="";
    FrameLayout flImage;
    TextView tvDate, tvInTime, tvOutTime;
    String cuDate;
    int co;
    String intime;
    boolean status;
    String mode;
    ArrayList<String>approvalList=new ArrayList<>();
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_in_manage);
        initview();
        onClick();
    }

    @SuppressLint("RestrictedApi")
    private void initview() {
        pref = new Pref(MarkInManageActivity.this);
        if (pref.getBranchId().equals("1100000001")){
            wrkHomeList.add("Please Select");
            wrkHomeList.add("Home");
            wrkHomeList.add("Client");
            wrkHomeList.add("Vendor");
            wrkHomeList.add("Others");
        }else {
            wrkHomeList.add("Office");
            wrkHomeList.add("Home");
            wrkHomeList.add("Client");
            wrkHomeList.add("Vendor");
            wrkHomeList.add("Others");
        }

        intime = getIntent().getStringExtra("time");
        status = getIntent().getBooleanExtra("status", false);

        spWorkFrom = (Spinner) findViewById(R.id.spWorkFrom);

        ArrayAdapter<String> spinnerMonthArrayAdapter = new ArrayAdapter<String>
                (MarkInManageActivity.this, android.R.layout.simple_spinner_item,
                        wrkHomeList); //selected item will look like a spinner set from XML
        spinnerMonthArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWorkFrom.setAdapter(spinnerMonthArrayAdapter);

        imgUser = (ImageView) findViewById(R.id.imgUser);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvTime = (TextView) findViewById(R.id.tvTime);

        llRefresh = (LinearLayout) findViewById(R.id.llRefresh);

        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgImage = (CircleImageView) findViewById(R.id.imgImage);

        gps = new GPSTracker(MarkInManageActivity.this);
        if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                Log.d("saikatdas", String.valueOf(latitude));
                longitude = gps.getLongitude();
                Log.d("saikatdas", String.valueOf(longitude));


        } else {


        }

        long futureTimestamp = System.currentTimeMillis() + (2 * 60 * 1000);
        TimerTextView timerText = (TimerTextView) findViewById(R.id.timerText);
        timerText.setEndTime(futureTimestamp);

        getAPIKey();
        flImage = (FrameLayout) findViewById(R.id.flImage);


        // tvAddress.setText("YOU ARE AT: " + address);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentDateTimeString = sdf.format(d);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        currentlat = String.valueOf(latitude);
        currentlong = String.valueOf(longitude);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(360, TimeUnit.SECONDS)
                .connectTimeout(360, TimeUnit.SECONDS)
                .build();

        // Change base URL to your upload server URL.

        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText("Hi! " + pref.getEmpName());
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvClick = (TextView) findViewById(R.id.tvClick);
        tvClickHere = (TextView) findViewById(R.id.tvClickHere);
        tvCapture = (TextView) findViewById(R.id.tvCapture);
        llImage = (LinearLayout) findViewById(R.id.llImage);
        llNote = (LinearLayout) findViewById(R.id.llNote);
        if (pref.getDeptId().equals("60") && pref.getBranchId().equals("1100000001") && !pref.getEmpId().equals("2070001652")) {
            llNote.setVisibility(View.VISIBLE);
            tvCapture.setText("Recognize your Face");

        } else {
            llNote.setVisibility(View.GONE);
            tvCapture.setText("Capture Image");
        }
        tvCustom = (TextView) findViewById(R.id.tvCustom);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getEmpId().equals("2070002087") || pref.getEmpId().equals("2070000922")) {

                } else {
                    finish();
                }
            }
        }, INTERVAL);


        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                tvTime.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
            }

            public void onFinish() {

            }
        };
        newtimer.start();
        tvDate = (TextView) findViewById(R.id.tvDate);
        Date cd = Calendar.getInstance().getTime();
        System.out.println("Current time => " + cd);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String formattedDate = df.format(cd);
        tvDate.setText(formattedDate);
        tvInTime = (TextView) findViewById(R.id.tvInTime);
        tvOutTime = (TextView) findViewById(R.id.tvOutTime);

        if (status) {
            tvInTime.setText(intime);
            CountDownTimer newtimerr = new CountDownTimer(1000000000, 1000) {

                public void onTick(long millisUntilFinished) {
                    Calendar c = Calendar.getInstance();
                    tvOutTime.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
                }

                public void onFinish() {

                }
            };
            newtimerr.start();
            btnSubmit.setText("Check Out");
        } else {
            CountDownTimer outtimer = new CountDownTimer(1000000000, 1000) {

                public void onTick(long millisUntilFinished) {
                    Calendar c = Calendar.getInstance();
                    tvInTime.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
                }

                public void onFinish() {

                }
            };
            outtimer.start();
            btnSubmit.setText("Check In");
        }
    }


    private void onClick() {
        spWorkFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0) {
                    mode = wrkHomeList.get(i);
                    if (mode.equalsIgnoreCase("Office")) {
                        wrkFrm = "0";
                    } else if (mode.equalsIgnoreCase("Home")) {
                        wrkFrm = "1";
                        getAddressSavedOrNot();
                    } else if (mode.equalsIgnoreCase("Client")) {
                        wrkFrm = "2";
                    } else if (mode.equalsIgnoreCase("Vendor")) {
                        wrkFrm = "3";
                    } else if (mode.equalsIgnoreCase("Others")) {
                        wrkFrm = "4";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        llImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileOpenDilog();
            }
        });
        flImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getEmpId().equals("2070002087")) {
                    galleryIntent();
                }
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getEmpId().equalsIgnoreCase("2070002087") || pref.getEmpId().equalsIgnoreCase("2070002668")) {
                    galleryIntent();
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    if (!tvAddress.getText().toString().equals("YOU ARE AT: null") || tvAddress.getText().toString().equals("YOU ARE AT: ")) {
                        if (!wrkFrm.equals("")){
                            if (wrkFrm.equals("1")){
                                if (approvalList.contains("Approved")){
                                    checkDistance();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Sorry! Your address has not been approved by your supervisor.", Toast.LENGTH_LONG).show();

                                }
                            }else {
                                dailyActivity();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Please select your punch type", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry! Your address not found.Please click on Refresh button", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please attach Image", Toast.LENGTH_LONG).show();
                }
            }


        });


     /*   btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    if (!tvAddress.getText().toString().equals("YOU ARE AT: null") || tvAddress.getText().toString().equals("YOU ARE AT: ")) {

                            dailyActivity();



                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry! Your address not found.Please click on Refresh button", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please attach Image", Toast.LENGTH_LONG).show();
                }
            }


        });*/

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageCameraActivity.launch(MarkInManageActivity.this);
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getEmpId().equals("2070002087")) {
                    galleryIntent();
                } else {
                    Intent intent = new Intent(MarkInManageActivity.this, EDashBoardActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        tvClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MarkInManageActivity.this, AddPerson.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });
    }

    private void cameraIntent() {
        flag = 1;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            //messageAlert();
                            String imageurl = /*"file://" +*/ getRealPathFromURIPath(imageUri);
                            Log.e(TAG, "onActivityResult: "+imageurl);
                            file = new File(imageurl);
                            compressedImageFile = new ImageZipper(MarkInManageActivity.this)
                                    .setQuality(80)
                                    .setMaxWidth(250)
                                    .setMaxHeight(250)
                                    .compressToFile(file);
                            // Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            Bitmap bo = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            Bitmap bm = new ImageZipper(getApplicationContext()).compressToBitmap(file);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bo.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            Log.d("encodedimage", encodedImage);
                            imgImage.setImageBitmap(bm);
                            al2.dismiss();
                            flag = 1;
                            // al2.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_GALLERY_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        try {
                            uri = data.getData();
                            String filePath = getRealPathFromURIPath(uri, MarkInManageActivity.this);
                            file = new File(filePath);
                            compressedImageFile = new ImageZipper(MarkInManageActivity.this)
                                    .setQuality(80)
                                    .setMaxWidth(250)
                                    .setMaxHeight(250)
                                    .compressToFile(file);
                            imageStream = getContentResolver().openInputStream(uri);
                            Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgImage.setImageBitmap(bm);
                            flag = 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                }
                break;
            case LongImageCameraActivity.LONG_IMAGE_RESULT_CODE:
                Log.e(TAG, "onActivityResult: ======: requestCode: "+requestCode);
                Log.e(TAG, "onActivityResult: ======: requestCode: "+LongImageCameraActivity.LONG_IMAGE_RESULT_CODE);
                Log.e(TAG, "onActivityResult: ======: resultCode "+resultCode);
                Log.e(TAG, "onActivityResult: ======: RESULT_OK "+Activity.RESULT_OK);

                if (resultCode == RESULT_OK && requestCode == LongImageCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    file = (File) data.getExtras().get("picture");
                    try {
                        compressedImageFile = new ImageZipper(MarkInManageActivity.this)
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
                    imgImage.setImageBitmap(putImage);
                    flag = 1;
                    al2.dismiss();
                    // al2.dismiss();
                }
                break;
            case AndroidXCameraActivity.LONG_IMAGE_RESULT_CODE:
                Log.e(TAG, "onActivityResult: resultCode: "+resultCode);
                Log.e(TAG, "onActivityResult: requestCode: "+requestCode);
                Log.e(TAG, "onActivityResult: LONG_IMAGE_RESULT_CODE: "+AndroidXCameraActivity.LONG_IMAGE_RESULT_CODE);
                if (resultCode == 100 && requestCode ==  AndroidXCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    Log.e(TAG, "onActivityResult: called");
                    String link = data.getStringExtra("image").toString();
                    Log.e(TAG, "onActivityResult: LINK: "+link);
                    imageUri = Uri.parse(link);
                    Log.e(TAG, "onActivityResult: URI: "+imageUri.getPath());
                    //String imageurl = "file://" + getRealPathFromURIPath(imageUri);
                    file = new File(link);
                    try {
                        compressedImageFile = new ImageZipper(MarkInManageActivity.this)
                                .setQuality(80)
                                .setMaxWidth(250)
                                .setMaxHeight(250)
                                .compressToFile(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inSampleSize = 6;
                    Bitmap bo = cropToSquare(BitmapFactory.decodeFile(link, o));
                    Bitmap bm = null;
                    try {
                        bm = new ImageZipper(getApplicationContext()).compressToBitmap(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bo.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    Log.d("encodedimage", encodedImage);
                    imgImage.setImageBitmap(bm);
                    al2.dismiss();
                    flag = 1;
                }
                break;
        }
    }

    private String getRealPathFromURIPath(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

   /* private void dailyActivity() {
        String aemid = pref.getEmpId();
        String security = pref.getSecurityCode();
        String remarks = etRemarks.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(MarkInManageActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        progressDialog.show();
        RequestBody mFile = RequestBody.create(MediaType.parse(".png"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        Call<UploadObject> fileUpload = uploadService.dailyactivityTATAGYV2(
                fileToUpload,
                aemid,
                "0",
                remarks,
                currentlong,
                currentlat,
                address, "0", "0", "0", wrkFrm);

        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    successAlert("Your attendance has been saved successfully");
                } else {
                    Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();
                dailyWithoutImageActivity();
                Log.e("error", "Error " + t.getMessage());
                //   Toast.makeText(AttendanceManageActivity.this,"attendance saved without image",Toast.LENGTH_LONG).show();
            }

        });

    }*/


    private void dailyActivity() {
        ProgressDialog pd=new ProgressDialog(MarkInManageActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(APi.sAddDailyLogv2Api)
                .addMultipartParameter("Longitude", currentlong)
                .addMultipartParameter("Latitude", currentlat)
                .addMultipartParameter("Address", address)
                .addMultipartParameter("AEMEmployeeID", pref.getSecureEmpId())
                 .addMultipartParameter("ApprovalStatus", "0")
                .addMultipartParameter("Remarks",etRemarks.getText().toString())
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("FName", "0")
                .addMultipartParameter("SecurityCode", wrkFrm)
                .addMultipartFile("image",compressedImageFile)
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())

                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        String responseText=job.optString("responseText");
                        if (responseStatus) {
                            successAlert("Your attendance has been saved successfully");
                        } else {
                            onBackPressed();
                            Toast.makeText(MarkInManageActivity.this,responseText,Toast.LENGTH_LONG).show();


                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(MarkInManageActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }




    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MarkInManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                onBackPressed();

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






    private void fileOpenDilog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MarkInManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_camera, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llCamera = (LinearLayout) dialogView.findViewById(R.id.llCamera);
        LinearLayout llCustom = (LinearLayout) dialogView.findViewById(R.id.llCustom);


        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cameraIntent();
                AndroidXCameraActivity.launch(MarkInManageActivity.this);
            }
        });

        llCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageCameraActivity.launch(MarkInManageActivity.this);
                //AndroidXCameraActivity.launch(MarkInManageActivity.this);
                //al2.dismiss();
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

    private void getAPIKey() {
        String surl = "https://cloud.geniusconsultant.com/GeniusESS/API/Utility/GetLocationKey";
        // String surl = "http://172.16.1.184/GeniusESSMobile/API/Utility/GetLocationKey";
        Log.d("residancelist", surl);
        final ProgressDialog progressDialog = new ProgressDialog(MarkInManageActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("clint", "1");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            time = job1.optString("responseData");


                            getaddressFromAPI(responseText);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                address = getCompleteAddressString(latitude, longitude);
                tvAddress.setText(address);
                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Date d = new Date();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                time = df.format(d);
                Log.e("time", time);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                currentDateTimeString = sdf.format(d);


            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(MarkInManageActivity.this);
        requestQueue.add(stringRequest);


    }


    private void getAddressSavedOrNot() {
        String surl = "http://171.16.1.150/GeniusESSMobile/api/WFHAddress/getaddress?EmployeeID=2070003154"/*+pref.getEmpId()*/;
        // String surl = "http://172.16.1.184/GeniusESSMobile/API/Utility/GetLocationKey";
        Log.d("residancelist", surl);
        final ProgressDialog progressDialog = new ProgressDialog(MarkInManageActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("clint", "1");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                JSONArray responseData=job1.optJSONArray("responseData");
                                JSONObject object=responseData.optJSONObject(responseData.length() - 1);
                                String responseCode=object.optString("AddressType");
                                if (responseCode.equals("1")){
                                    addressOpenDilog("1");
                                }else if (responseCode.equals("2")){

                                }else if (responseCode.equals("3")){

                                }else {

                                }

                                for (int i=0;i<responseData.length();i++){
                                    JSONObject obj=responseData.optJSONObject(i);
                                    String ApprovedBy = obj.optString("ApprovedBy");
                                    approvalList.add(ApprovedBy);

                                }
                            }else {
                                addressOpenDilog("0");
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();



            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(MarkInManageActivity.this);
        requestQueue.add(stringRequest);


    }

    private void getaddressFromAPI(String apikey) {
        String testUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=13.08,80.25&key=" + apikey;
        String surl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + apikey;
        Log.d("residancelist", surl);
        ProgressDialog pd = new ProgressDialog(MarkInManageActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        Log.d("clint", "1");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String status = job1.optString("status");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (status.equalsIgnoreCase("OK")) {
                                JSONArray results = job1.optJSONArray("results");
                                //for (int i = 0; i < results.length(); i++) {
                                JSONObject object = results.optJSONObject(0);
                                String formatted_address = object.optString("formatted_address");
                                address = formatted_address.replaceAll("Unnamed Road,", "");
                                tvAddress.setText("You are at:- " + address);


                                //}


                            } else {
                                if (pref.getEmpId().equalsIgnoreCase("2070002087")) {
                                    address = getCompleteAddressString(22.6001413, 88.4726306);
                                    tvAddress.setText(address);
                                } else {
                                    address = getCompleteAddressString(latitude, longitude);
                                    tvAddress.setText(address);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                if (pref.getEmpId().equalsIgnoreCase("2070002087")) {
                    address = getCompleteAddressString(22.6001413, 88.4726306);
                    tvAddress.setText(address);
                } else {
                    address = getCompleteAddressString(latitude, longitude);
                    tvAddress.setText(address);
                }

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(MarkInManageActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }


    @Override
    protected void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();
        if (isScreenOn) {

        } else {

            if (pref.getEmpId().equals("2070002087")) {

            } else {
                finish();
            }


        }
    }





    private void addressOpenDilog(String code) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MarkInManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_address, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llPresent = (LinearLayout) dialogView.findViewById(R.id.llPresent);
        LinearLayout llPermanent = (LinearLayout) dialogView.findViewById(R.id.llPermanent);
        LinearLayout llBoth = (LinearLayout) dialogView.findViewById(R.id.llBoth);
        if (code.equals("1")){
            llPresent.setVisibility(View.GONE);
        }else if (code.equals("2")){
            llPermanent.setVisibility(View.GONE);
            llPresent.setVisibility(View.GONE);
        }
        llPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressDetailsOpenDilog("Present Address","1");
            }
        });

        llPermanent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressDetailsOpenDilog("Permanent Address","2");
            }
        });

        llBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressDetailsOpenDilog("Both Address","3");
            }
        });





        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressDialog.dismiss();
            }
        });


        addressDialog = dialogBuilder.create();
        addressDialog.setCancelable(false);
        Window window = addressDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        addressDialog.show();
    }


    private void addressDetailsOpenDilog(String add,String code) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MarkInManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_address_details, null);
        dialogBuilder.setView(dialogView);
        TextView tvAddressDetails = (TextView) dialogView.findViewById(R.id.tvAddressDetails);
        TextView tvCoordinates = (TextView) dialogView.findViewById(R.id.tvCoordinates);
        TextView tvAddress = (TextView) dialogView.findViewById(R.id.tvAddress);
        tvAddressDetails.setText(add+" Details");
        tvCoordinates.setText(currentlat+" , "+currentlong);
        tvAddress.setText(address);

        TextView tvSave = (TextView) dialogView.findViewById(R.id.tvSave);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postWFHAddress(code);
            }
        });





        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressDetailsDialog.dismiss();
            }
        });


        addressDetailsDialog = dialogBuilder.create();
        addressDetailsDialog.setCancelable(false);
        Window window = addressDetailsDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        addressDetailsDialog.show();
    }


    private void postWFHAddress(String code) {
        final ProgressDialog progressDialog=new ProgressDialog(MarkInManageActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        AndroidNetworking.upload("http://171.16.1.150/GeniusESSMobile/api/WFHAddress/saveaddress")
                .addMultipartParameter("EmployeeID", "2070003154")
                .addMultipartParameter("Latitude", currentlat)
                .addMultipartParameter("Longitude", currentlong)
                .addMultipartParameter("AddressType", code)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();




                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseCode = job1.optString("responseCode");
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        if (responseCode.equals("1")) {
                            successAlert("Your address saved successfully");


                        }else
                        {
                            Toast.makeText(MarkInManageActivity.this,responseText,Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
                    }
                });
    }


    private void checkDistance() {
        final ProgressDialog progressDialog=new ProgressDialog(MarkInManageActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        AndroidNetworking.upload("http://171.16.1.150/GeniusESSMobile/api/WFHAddress/checkdistance")
                .addMultipartParameter("EmployeeID", "2070003154")
                .addMultipartParameter("Latitude", currentlat)
                .addMultipartParameter("Longitude", currentlong)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                        progressDialog.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseCode = job1.optString("responseCode");
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        if (responseCode.equals("1")) {

                            dailyActivity();


                        }else

                        {
                            shoeDialog(responseText);
                            Toast.makeText(MarkInManageActivity.this,responseText,Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
                    }
                });
    }


    private void shoeDialog(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MarkInManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        TextView tvText=(TextView)dialogView.findViewById(R.id.tvText);
        tvText.setText(text);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }
}



