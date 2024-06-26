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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
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

import com.developers.imagezipper.ImageZipper;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.GPSTracker;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.RetrofitService;
import com.genius.employee.utility.UploadObject;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

public class MarkInManageWithFaceActivity extends AppCompatActivity {
    TextView tvAddress, tvTime;
    LinearLayout llRefresh;
    ImageView imgCamera, imgImage;
    GPSTracker gps;
    ;
    double latitude, longitude;
    String address;
    Pref pref;
    String currentDateTimeString;
    private String encodedImage;
    private Uri imageUri, uri;
    private static final int CAMERA_REQUEST = 1;
    int flag=1;
    File compressedImageFile, file;
    Button btnSubmit;
    String currentlat, currentlong;

    ;

    TextView tvName;
    EditText etRemarks;
    AlertDialog alerDialog1;
    ImageView imgBack, imgHome;
    TextView tvClick,tvClickHere;

    private static final int REQUEST_GALLERY_CODE = 200;
    Bitmap bitmap;
    LinearLayout llImage;
    TextView tvNote,tvCapture;
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_in_manage);
        initview();
        onClick();
    }

    private void initview() {
        pref = new Pref(MarkInManageWithFaceActivity.this);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvTime = (TextView) findViewById(R.id.tvTime);

        llRefresh = (LinearLayout) findViewById(R.id.llRefresh);

        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgImage = (ImageView) findViewById(R.id.imgImage);

        gps = new GPSTracker(MarkInManageWithFaceActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
        } else {
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings

        }
        address = getCompleteAddressString(latitude, longitude).toUpperCase();
        tvAddress.setText("YOU ARE AT: " + address);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentDateTimeString = sdf.format(d);
        tvTime.setText("Current time is : " + currentDateTimeString);
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
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvNote.setText("Face recognized successfully");
        tvName.setText("Hi! " + pref.getEmpName());
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvClick = (TextView) findViewById(R.id.tvClick);

            final byte[] byteArray = getIntent().getByteArrayExtra("image");
             bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imgImage.setImageBitmap(bitmap);



        tvClickHere=(TextView)findViewById(R.id.tvClickHere);
        tvClickHere.setVisibility(View.GONE);
        file = new File(pref.getMasterId()+".jpg");
        try {
            compressedImageFile = new ImageZipper(MarkInManageWithFaceActivity.this)
                    .setQuality(80)
                    .setMaxWidth(250)
                    .setMaxHeight(250)
                    .compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        llImage=(LinearLayout)findViewById(R.id.llImage);
        tvCapture=(TextView)findViewById(R.id.tvCapture);
        tvCapture.setVisibility(View.GONE);
        imgCamera.setVisibility(View.GONE);
        persistImage(bitmap,"text");

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

    private void onClick() {
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getEmpId().equals("2070002087")) {
                    /*Intent intent = new Intent(MarkInManageWithFaceActivity.this, FaceRecognitation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                } else {
                    cameraIntent();
                }

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    if (!tvAddress.getText().toString().equals("YOU ARE AT: null") || tvAddress.getText().toString().equals("YOU ARE AT: ")) {
                        if (etRemarks.getText().toString().length() > 0) {

                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter remarks", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry! Your address not found.Please click on Refresh button", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please attach Image", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MarkInManageWithFaceActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                finish();
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
                /*Intent intent = new Intent(MarkInManageWithFaceActivity.this, AddPerson.class);
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
                            compressedImageFile = new ImageZipper(MarkInManageWithFaceActivity.this)
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
                            flag = 1;


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
                            String filePath = getRealPathFromURIPath(uri, MarkInManageWithFaceActivity.this);
                            file = new File(filePath);
                            compressedImageFile = new ImageZipper(MarkInManageWithFaceActivity.this)
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

    /*private void attendancePostWithImage() {
        final ProgressDialog pd=new ProgressDialog(MarkInManageActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload( "http://172.16.0.145/GeniusESSMobile/API/post_DailyLogTatGY")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("ApprovalStatus", "0")
                .addMultipartParameter("Remarks", "ok")
                .addMultipartParameter("Longitude", currentlong)
                .addMultipartParameter("Latitude", currentlat)
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Year", "2020")
                .addMultipartParameter("Month", "March")
                .addMultipartParameter("FName", "0")
                .addMultipartFile("Image", compressedImageFile)
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
                        String responseText=job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }*/



    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MarkInManageWithFaceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                Intent intent = new Intent(getApplicationContext(), MarkInViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your Attendance saved successfully");


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

    private void persistImage(Bitmap bitmap, String name) {
        File filesDir = getFilesDir();
         imageFile = new File(filesDir, name + ".png");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
    }


}
