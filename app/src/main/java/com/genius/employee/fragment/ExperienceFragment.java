package com.genius.employee.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import com.genius.employee.R;
import com.genius.employee.adapter.ExperienceAdapter;
import com.genius.employee.adapter.FamilyAdapter;
import com.genius.employee.model.ExperienceModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExperienceFragment extends Fragment {


    View v;
    LinearLayout llLoader, llMain, llNoData, llAddNew;
    Pref pref;
    RecyclerView rvItem;
    ArrayList<ExperienceModel> expList = new ArrayList<>();
    AlertDialog expDialog;
    String dated;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_experience, container, false);
        initialize();
        getProfile();
        return v;
    }

    private void initialize() {
        pref = new Pref(getContext());
        llAddNew = (LinearLayout) v.findViewById(R.id.llAddNew);
        rvItem = (RecyclerView) v.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llMain = (LinearLayout) v.findViewById(R.id.llMain);
        llLoader = (LinearLayout) v.findViewById(R.id.llLoader);
        llNoData = (LinearLayout) v.findViewById(R.id.llNoData);

        llAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expSavePopUp();
            }
        });


    }

    private void getProfile() {
        String surl = APi.sexperienceApi + pref.getEmpId();
        Log.d("manageinput", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);


        Log.d("inputleave", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);

                        expList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {


                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject object = responseData.optJSONObject(i);
                                    String FromDate = object.optString("FromDate").replaceAll("00:00:00", "").replaceAll(" ", "");
                                    String Todate = object.optString("Todate").replaceAll("00:00:00", "").replaceAll(" ", "");
                                    String OrganisationName = object.optString("OrganisationName");
                                    String Designation = object.optString("Designation");
                                    String Experience = object.optString("Experience");
                                    String JobRecordid = object.optString("JobRecordid");
                                    String JobLocation = object.optString("JobLocation");
                                    ExperienceModel model = new ExperienceModel();
                                    model.setExperience(Experience);
                                    model.setCount(i + 1);
                                    model.setDesignation(Designation);
                                    model.setFromDate(FromDate);
                                    model.setJobLocation(JobLocation);
                                    model.setJobRecordid(JobRecordid);
                                    model.setTodate(Todate);
                                    model.setOrganisationName(OrganisationName);
                                    expList.add(model);
                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                ExperienceAdapter onAdapter = new ExperienceAdapter(expList, ExperienceFragment.this);
                                rvItem.setAdapter(onAdapter);

                            } else {
                                llLoader.setVisibility(View.GONE);
                                rvItem.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + pref.getAccessToken());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void expEditPopUp(int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_experience_edit, null);
        dialogBuilder.setView(dialogView);

        EditText etOrganisationName = (EditText) dialogView.findViewById(R.id.etOrganisationName);
        EditText etDesignation = (EditText) dialogView.findViewById(R.id.etDesignation);
        EditText etExperience = (EditText) dialogView.findViewById(R.id.etExperience);
        EditText etJobLocation = (EditText) dialogView.findViewById(R.id.etJobLocation);
        TextView tvFormDate = (TextView) dialogView.findViewById(R.id.tvFormDate);
        TextView tvToDate = (TextView) dialogView.findViewById(R.id.tvToDate);

        etOrganisationName.setText(expList.get(pos).getOrganisationName());
        etDesignation.setText(expList.get(pos).getDesignation());
        etExperience.setText(expList.get(pos).getExperience());
        etJobLocation.setText(expList.get(pos).getJobLocation());
        tvFormDate.setText(Util.changeAnyDateFormat(expList.get(pos).getFromDate(),"dd-MM-yyyy","dd MMM,yyyy"));
        tvToDate.setText(Util.changeAnyDateFormat(expList.get(pos).getTodate(),"dd-MM-yyyy","dd MMM,yyyy"));

        tvFormDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(tvFormDate);

            }
        });


        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(tvToDate);
            }
        });


        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expDialog.dismiss();
            }
        });


        TextView tvUpdate = (TextView) dialogView.findViewById(R.id.tvUpdate);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                expSaveEdit(Util.changeAnyDateFormat(tvFormDate.getText().toString(),"dd MMM,yyyy","dd-MM-yyyy"),Util.changeAnyDateFormat(tvToDate.getText().toString(),"dd MMM,yyyy","dd-MM-yyyy"),etOrganisationName.getText().toString().trim(),etDesignation.getText().toString().trim(),etJobLocation.getText().toString().trim(),expList.get(pos).getJobRecordid());


            }
        });


        expDialog = dialogBuilder.create();
        expDialog.setCancelable(false);
        Window window = expDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        expDialog.show();
    }

    public void expSavePopUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_experience_edit, null);
        dialogBuilder.setView(dialogView);

        EditText etOrganisationName = (EditText) dialogView.findViewById(R.id.etOrganisationName);
        EditText etDesignation = (EditText) dialogView.findViewById(R.id.etDesignation);
        EditText etExperience = (EditText) dialogView.findViewById(R.id.etExperience);
        EditText etJobLocation = (EditText) dialogView.findViewById(R.id.etJobLocation);
        TextView tvFormDate = (TextView) dialogView.findViewById(R.id.tvFormDate);
        TextView tvToDate = (TextView) dialogView.findViewById(R.id.tvToDate);


        tvFormDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(tvFormDate);

            }
        });


        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(tvToDate);
            }
        });


        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expDialog.dismiss();
            }
        });


        TextView tvUpdate = (TextView) dialogView.findViewById(R.id.tvUpdate);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                expSaveEdit(Util.changeAnyDateFormat(tvFormDate.getText().toString(),"dd MMM,yyyy","dd-MM-yyyy"),Util.changeAnyDateFormat(tvToDate.getText().toString(),"dd MMM,yyyy","dd-MM-yyyy"),etOrganisationName.getText().toString().trim(),etDesignation.getText().toString().trim(),etJobLocation.getText().toString().trim(),"0");


            }
        });


        expDialog = dialogBuilder.create();
        expDialog.setCancelable(false);
        Window window = expDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        expDialog.show();
    }


    private void datePicker(TextView tv) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        String dated=dayOfMonth+"-"+(monthOfYear + 1)+"-"+year;
                        tv.setText(Util.changeAnyDateFormat(dated,"dd-MM-yyyy","dd MMM,yyyy"));



                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }


    public void expSaveEdit(String fromDate, String toDate, String organisationName, String designation,String location,String jobRId ) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(APi.sexperienceAddApi)
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("fromDate", fromDate)
                .addMultipartParameter("toDate", toDate)
                .addMultipartParameter("organisationName", organisationName)
                .addMultipartParameter("designation", designation)
                .addMultipartParameter("location", location)
                .addMultipartParameter("jobRId", jobRId)
                //.addHeaders("Content-Type: application/x-www-form-urlencoded")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        boolean responseStatus = response.optBoolean("responseStatus");

                        expDialog.dismiss();
                        Toast.makeText(getContext(), response.optString("responseText"), Toast.LENGTH_LONG).show();
                        getProfile();


                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void deleteAlert(int pos) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to delete this ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteExp(expList.get(pos).getJobRecordid());

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        android.app.AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert");
        alert.show();
    }


    private void deleteExp(String memberID) {
        String surl = APi.sdelexperienceApi + "EmployeeID=" + pref.getEmpId() + "&jobRId=" + memberID;
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseRelation", response);
                        dialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responsedocumentreport", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");

                            Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
                            getProfile();
                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            // Toast.makeText(DocumentReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();


                //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

}
