package com.genius.employee.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.genius.employee.R;
import com.genius.employee.adapter.AttendanceAdapter;
import com.genius.employee.adapter.WFHAddressDetailsAdapter;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.model.WFHAddressDetailsModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WFHAddressDetailsActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ImageView imgBack, imgHome;
    String empID, empName;
    TextView tvToolBar;
    ArrayList<WFHAddressDetailsModel> itemList = new ArrayList<>();
    AlertDialog addressDialog;
    Pref pref;
    AlertDialog alerDialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wfhaddress_details);
        initialize();
        onClick();

        // setYearItem();
    }


    private void initialize() {
        pref = new Pref(WFHAddressDetailsActivity.this);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);

        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(WFHAddressDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(layoutManager);
        empID = getIntent().getStringExtra("empID");
        empName = getIntent().getStringExtra("empName");
        tvToolBar.setText("Address Approval of \n" + empName);
        getAddressSavedOrNot();


    }

    private void getAddressSavedOrNot() {
        String surl = "http://171.16.1.150/GeniusESSMobile/api/WFHAddress/getaddress?EmployeeID=" + empID;
        // String surl = "http://172.16.1.184/GeniusESSMobile/API/Utility/GetLocationKey";
        Log.d("residancelist", surl);
        final ProgressDialog progressDialog = new ProgressDialog(WFHAddressDetailsActivity.this);
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
                        itemList.clear();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject object = responseData.optJSONObject(i);
                                    String Latitude = object.optString("Latitude");
                                    String Longitude = object.optString("Longitude");
                                    String AddressType = object.optString("AddressType");
                                    String ApprovedBy = object.optString("ApprovedBy");
                                    String address = getCompleteAddressString(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
                                    if (ApprovedBy.equalsIgnoreCase("Pending")) {
                                        WFHAddressDetailsModel model = new WFHAddressDetailsModel();
                                        model.setAddress(address);
                                        model.setAddressType(AddressType);
                                        model.setLatitude(Latitude);
                                        model.setLongitude(Longitude);
                                        itemList.add(model);
                                    }


                                }
                                if (itemList.size() > 0) {
                                    WFHAddressDetailsAdapter detailsAdapter = new WFHAddressDetailsAdapter(itemList, WFHAddressDetailsActivity.this);
                                    rvItem.setAdapter(detailsAdapter);
                                } else {
                                    nodatadialog();
                                }


                            } else {
                                nodatadialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(WFHAddressDetailsActivity.this);
        requestQueue.add(stringRequest);


    }


    private void onClick() {


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WFHAddressDetailsActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void nodatadialog() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WFHAddressDetailsActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_nodata, null);
        dialogBuilder.setView(dialogView);
        TextView tvOK = (TextView) dialogView.findViewById(R.id.tvOK);
        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        addressDialog = dialogBuilder.create();
        addressDialog.setCancelable(false);
        Window window = addressDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        addressDialog.show();
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


    public void WFHAddressApproval(String code, String AddressType) {
        final ProgressDialog progressDialog = new ProgressDialog(WFHAddressDetailsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        AndroidNetworking.upload("http://171.16.1.150/GeniusESSMobile/api/WFHAddress/approveaddress")
                .addMultipartParameter("EmployeeID", empID)
                .addMultipartParameter("AddressType", AddressType)
                .addMultipartParameter("ApprovedBy", pref.getEmpId())
                .addMultipartParameter("ApprovalStatus", code)
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
                            if (code.equals("1")) {
                                successAlert("Address has been approved successfully");
                            } else if (code.equals("0")) {
                                successAlert("Address has been rejected successfully");
                            }


                        } else {
                            Toast.makeText(WFHAddressDetailsActivity.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error

                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WFHAddressDetailsActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                getAddressSavedOrNot();

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

    public void rejectDialog(String code, String AddressType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WFHAddressDetailsActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to reject ?");

        // Set Alert Title
        builder.setTitle("Rejection Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
            WFHAddressApproval(code,AddressType);

        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }


}
