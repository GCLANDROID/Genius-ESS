package com.genius.employee.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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
import com.developers.imagezipper.ImageZipper;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.GPSTracker;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.RetrofitService;
import com.genius.employee.utility.UploadObject;
import com.google.gson.JsonObject;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DailyOutTimeActivity extends AppCompatActivity {
    Button btnOut;
    Pref pref;
    String MARK_LOCATION_ID, ID;
    TextView tvClintName, tvRegTime, tvInTime, tvOutTime;
    EditText etContactPerson;

    private View signview;

    private Bitmap bitmap;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";
    private SignatureView canvasLL;
    String path;
    AlertDialog alerDialog1;
    ImageView imgSign;
    File f;
    File wallpaperDirectory;;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    File file,seccondcompress,thirdcompress;
    File imageZipperFile;
    ImageView imgCamera;
    GPSTracker gps;
    String sLat,sLong;
    double latitude,longitude;
    String cuuaddress="";
    TextView tvAddress;
    EditText etRemarks,etContactNumber,etEmail;
    int camFlag=0;
    int signFlag=0;
    ImageView imgBack,imgHome;
    LinearLayout llLoader,llRetry,llPD;
    ProgressDialog pd;
    TextView tvText;
    LinearLayout llLoad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_out_time);
        init();
        getvalue();
        onClick();
    }

    private void init() {
        pref = new Pref(getApplicationContext());
        btnOut = (Button) findViewById(R.id.btnOut);
        tvClintName = (TextView) findViewById(R.id.tvClintName);
        tvInTime = (TextView) findViewById(R.id.tvInTime);
        tvRegTime = (TextView) findViewById(R.id.tvRegTime);
        etContactPerson = (EditText) findViewById(R.id.etContactPerson);
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        etContactNumber = (EditText) findViewById(R.id.etContactNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        tvOutTime = (TextView) findViewById(R.id.tvOutTime);
        String dateTime = formattedDate + " " + str;
        tvOutTime.setText(dateTime);

        imgSign=(ImageView)findViewById(R.id.imgSign);
        imgCamera=(ImageView)findViewById(R.id.imgCamera);

        gps = new GPSTracker(this);

// check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            sLat= String.valueOf(latitude);
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
            sLong= String.valueOf(longitude);
        } else {

            gps.showSettingsAlert();
        }

        cuuaddress = getAddress(latitude, longitude);
        tvAddress=(TextView)findViewById(R.id.tvAddress);
        tvAddress.setText(cuuaddress);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();
        // Change base URL to your upload server URL.

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llRetry=(LinearLayout)findViewById(R.id.llRetry);
        llPD=(LinearLayout)findViewById(R.id.llPD);
        pd=new ProgressDialog(DailyOutTimeActivity.this);
        pd.setMessage("Loadingg...");
        tvText=(TextView)findViewById(R.id.tvText);
        llLoad=(LinearLayout)findViewById(R.id.llLoad);



    }

    private void onClick() {
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cuuaddress.equals("")||cuuaddress!=null){
                if (etContactPerson.getText().toString().length()>0){
                    if (etContactNumber.getText().toString().length()>9){
                        if (etEmail.getText().toString().length()>0){
                            if (etEmail.getText().toString().contains("@") && etEmail.getText().toString().contains(".")) {
                                if (camFlag==1){
                                    if (camFlag == 1 && signFlag == 0) {
                                        postFunctionForCamera();
                                    } else if (camFlag == 0 && signFlag == 1) {
                                        postFunctionForSign();
                                    } else if (camFlag == 1 && signFlag == 1) {
                                        postFunctionForTwo();
                                    } else {
                                        postFunctionForNone();
                                    }
                                }else {
                                    Toast.makeText(DailyOutTimeActivity.this,"Image is mandatory",Toast.LENGTH_LONG).show();
                                }


                            }else {
                                etEmail.setError("Please provide visiting person's valid email");
                                etEmail.requestFocus();
                            }

                        }else {
                            etEmail.setError("Please provide visiting person's email");
                            etEmail.requestFocus();
                        }

                    }else {
                        etContactNumber.setError("Please provide visiting person's contact number");
                        etContactNumber.requestFocus();
                    }

                }else {
                    etContactPerson.setError("Please provide visiting person's name");
                    etContactPerson.requestFocus();
                }
                }else {
                    Toast.makeText(getApplicationContext(),"Sorry!Your current address not found.Please check your google map",Toast.LENGTH_LONG).show();
                }


// VisitLocationID

            }
        });
        imgSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureAlert();
            }
        });
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DailyOutTimeActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalue();
            }
        });
    }

    private void getvalue() {

        String surl = APi.sGetVisitLocationMasterApi+"EmployeeID=" + pref.getSecureEmpId();
        Log.d("compurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llRetry.setVisibility(View.GONE);
        llPD.setVisibility(View.VISIBLE);
        btnOut.setEnabled(false);
        imgHome.setEnabled(false);
        imgBack.setEnabled(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        llLoader.setVisibility(View.GONE);
                        btnOut.setEnabled(true);
                        imgHome.setEnabled(true);
                        imgBack.setEnabled(true);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);


                                    MARK_LOCATION_ID = obj.optString("MARK_LOCATION_ID");
                                    String Entry_Marked_On = obj.optString("Entry_Marked_On");
                                    tvInTime.setText(Entry_Marked_On);

                                    JSONObject _ClientVisitMasterID = obj.optJSONObject("_ClientVisitMasterID");
                                    ID = _ClientVisitMasterID.optString("ID");
                                    String ClientName = _ClientVisitMasterID.optString("ClientName");
                                    tvClintName.setText(ClientName);
                                    String ClientContactPerson = _ClientVisitMasterID.optString("ClientContactPerson");
                                    etContactPerson.setText(ClientContactPerson);
                                    String CreatedOn = _ClientVisitMasterID.optString("CreatedOn");
                                    tvRegTime.setText(CreatedOn);

                                }
                                btnOut.setEnabled(true);
                                imgHome.setEnabled(true);
                                imgBack.setEnabled(true);


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DailyOutTimeActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llRetry.setVisibility(View.VISIBLE);
                llPD.setVisibility(View.GONE);
                btnOut.setEnabled(false);
                imgHome.setEnabled(false);
                imgBack.setEnabled(false);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

    private void signatureAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyOutTimeActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) DailyOutTimeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.signtaure_lay, null);
        dialogBuilder.setView(dialogView);

        canvasLL = (SignatureView) dialogView.findViewById(R.id.canvasLL);
        Button btnclear = (Button) dialogView.findViewById(R.id.btnclear);
        Button btnsave = (Button) dialogView.findViewById(R.id.btnsave);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasLL.clearCanvas();
                alerDialog1.dismiss();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = canvasLL.getSignatureBitmap();
                path = saveImage(bitmap);
                signFlag=1;
                alerDialog1.dismiss();
                imgSign.setVisibility(View.VISIBLE);
                imgSign.setImageBitmap(bitmap);

            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY /*iDyme folder*/);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh", wallpaperDirectory.toString());
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(DailyOutTimeActivity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private void cameraIntent() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
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
                            file = new File(imageurl);
                            // thirdcompress = new Compressor(this).compressToFile(file);
                            imageZipperFile=new ImageZipper(DailyOutTimeActivity.this)
                                    .setQuality(80)
                                    .setMaxWidth(250)
                                    .setMaxHeight(250)
                                    .compressToFile(file);

                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            //Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                           Bitmap bm=new ImageZipper(DailyOutTimeActivity.this).compressToBitmap(file);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgCamera.setImageBitmap(bm);
                            camFlag=1;



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
    public String getAddress(double lat, double lng) {
        String address = null;
        Geocoder geocoder = new Geocoder(DailyOutTimeActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            address=add;;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return address;
    }

    private void postFunctionForTwo() {
        AndroidNetworking.upload(APi.sAddVisitOutMarkMasterApi)
                .addMultipartParameter("VisitLocationID", MARK_LOCATION_ID)
                .addMultipartParameter("ClientVisitMasterID", ID)
                .addMultipartParameter("OutMarkAddress", cuuaddress)
                .addMultipartParameter("OutMarkLat", sLat)
                .addMultipartParameter("OutMarkLon", sLong)
                .addMultipartParameter("ContactName", etContactPerson.getText().toString())
                .addMultipartParameter("ContactNumber", etContactNumber.getText().toString())
                .addMultipartParameter("ContactEmail",  etEmail.getText().toString())
                .addMultipartParameter("ContactRemark", etRemarks.getText().toString())
                .addMultipartParameter("CreatedBy", pref.getSecureEmpId())
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .addMultipartParameter("fileFlag", "2")
                .addMultipartFile("image",imageZipperFile)
                .addMultipartFile("image1",f)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        llLoad.setVisibility(View.VISIBLE);
                        btnOut.setVisibility(View.GONE);
                        tvText.setVisibility(View.GONE);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {





                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        if (responseText.equals("Success")) {
                            successAlert();
                            llLoad.setVisibility(View.VISIBLE);
                            btnOut.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);
                        }else
                        {
                            llLoad.setVisibility(View.GONE);
                            btnOut.setVisibility(View.VISIBLE);
                            tvText.setVisibility(View.GONE);

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        llLoad.setVisibility(View.GONE);
                        btnOut.setVisibility(View.VISIBLE);
                        tvText.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Something went wrong,Please try again",Toast.LENGTH_LONG);
                    }
                });

    }

    private void postFunctionForCamera() {

        AndroidNetworking.upload(APi.sAddVisitOutMarkMasterApi)
                .addMultipartParameter("VisitLocationID", MARK_LOCATION_ID)
                .addMultipartParameter("ClientVisitMasterID", ID)
                .addMultipartParameter("OutMarkAddress", cuuaddress)
                .addMultipartParameter("OutMarkLat", sLat)
                .addMultipartParameter("OutMarkLon", sLong)
                .addMultipartParameter("ContactName", etContactPerson.getText().toString())
                .addMultipartParameter("ContactNumber", etContactNumber.getText().toString())
                .addMultipartParameter("ContactEmail",  etEmail.getText().toString())
                .addMultipartParameter("ContactRemark", etRemarks.getText().toString())
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartParameter("fileFlag", "1")
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .addMultipartFile("image",imageZipperFile)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        llLoad.setVisibility(View.VISIBLE);
                        btnOut.setVisibility(View.GONE);
                        tvText.setVisibility(View.GONE);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {





                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        if (responseText.equals("Success")) {
                            successAlert();
                            llLoad.setVisibility(View.VISIBLE);
                            btnOut.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);
                        }else
                        {
                            llLoad.setVisibility(View.GONE);
                            btnOut.setVisibility(View.VISIBLE);
                            tvText.setVisibility(View.GONE);

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        llLoad.setVisibility(View.GONE);
                        btnOut.setVisibility(View.VISIBLE);
                        tvText.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Something went wrong,Please try again",Toast.LENGTH_LONG);
                    }
                });



    }

    private void postFunctionForSign() {
        AndroidNetworking.upload(APi.sAddVisitOutMarkMasterApi)
                .addMultipartParameter("VisitLocationID", MARK_LOCATION_ID)
                .addMultipartParameter("ClientVisitMasterID", ID)
                .addMultipartParameter("OutMarkAddress", cuuaddress)
                .addMultipartParameter("OutMarkLat", sLat)
                .addMultipartParameter("OutMarkLon", sLong)
                .addMultipartParameter("ContactName", etContactPerson.getText().toString())
                .addMultipartParameter("ContactNumber", etContactNumber.getText().toString())
                .addMultipartParameter("ContactEmail",  etEmail.getText().toString())
                .addMultipartParameter("ContactRemark", etRemarks.getText().toString())
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartParameter("fileFlag", "1")
                .addMultipartFile("image",f)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        llLoad.setVisibility(View.VISIBLE);
                        btnOut.setVisibility(View.GONE);
                        tvText.setVisibility(View.GONE);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {





                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        if (responseText.equals("Success")) {
                            successAlert();
                            llLoad.setVisibility(View.VISIBLE);
                            btnOut.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);
                        }else
                        {
                            llLoad.setVisibility(View.GONE);
                            btnOut.setVisibility(View.VISIBLE);
                            tvText.setVisibility(View.GONE);
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        llLoad.setVisibility(View.GONE);
                        btnOut.setVisibility(View.VISIBLE);
                        tvText.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Something went wrong,Please try again",Toast.LENGTH_LONG);
                    }
                });




    }

    private void postFunctionForNone() {

        AndroidNetworking.upload(APi.sAddVisitOutMarkMasterApi)
                .addMultipartParameter("VisitLocationID", MARK_LOCATION_ID)
                .addMultipartParameter("ClientVisitMasterID", ID)
                .addMultipartParameter("OutMarkAddress", cuuaddress)
                .addMultipartParameter("OutMarkLat", sLat)
                .addMultipartParameter("OutMarkLon", sLong)
                .addMultipartParameter("ContactName", etContactPerson.getText().toString())
                .addMultipartParameter("ContactNumber", etContactNumber.getText().toString())
                .addMultipartParameter("ContactEmail",  etEmail.getText().toString())
                .addMultipartParameter("ContactRemark", etRemarks.getText().toString())
                .addMultipartParameter("CreatedBy", pref.getSecureEmpId())
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .addMultipartParameter("fileFlag", "0")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        llLoad.setVisibility(View.VISIBLE);
                        btnOut.setVisibility(View.GONE);
                        tvText.setVisibility(View.GONE);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {





                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        if (responseText.equals("Success")) {
                            successAlert();
                            llLoad.setVisibility(View.VISIBLE);
                            btnOut.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);
                        }else
                        {
                            llLoad.setVisibility(View.GONE);
                            btnOut.setVisibility(View.VISIBLE);
                            tvText.setVisibility(View.GONE);

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        llLoad.setVisibility(View.GONE);
                        btnOut.setVisibility(View.VISIBLE);
                        tvText.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Something went wrong,Please try again",Toast.LENGTH_LONG);
                    }
                });






    }
    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyOutTimeActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk=(LinearLayout)dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                pref.saveInFlag("2");
                Intent intent=new Intent(getApplicationContext(),DailyActivityDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView tvSuccess=(TextView)dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your Out-Time has been marked successfully");


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
}

