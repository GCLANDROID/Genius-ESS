package com.genius.employee.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.AlertDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.employee.R;
import com.genius.employee.adapter.AddExperienceAdapter;
import com.genius.employee.model.AddExperienceModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.DateConverter;
import com.genius.employee.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExperienceActivity extends AppCompatActivity {
    private static final String TAG = "ExperienceActivity";
    TextView textView2;
    AlertDialog alert3;
    private Dialog experienceAddDialog;
    Calendar fromDateCalendar,toDateCalendar;
    ArrayList<AddExperienceModel> experienceList = new ArrayList<>();
    String fromDate="",toDate="";
    AddExperienceAdapter experienceAdapter;
    RecyclerView rvExperience;
    AppCompatButton btnFinish;
    double totalExperienceMonth;
    ProgressDialog progressDialog;
    LinearLayout llNoRecordFound,llLoading;
    ConstraintLayout clMain;
    Pref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);
        initView();

    }

    private void initView() {
        pref=new Pref(ExperienceActivity.this);
        textView2 = findViewById(R.id.textView2);
        rvExperience = findViewById(R.id.rvExperience);
        clMain = findViewById(R.id.clMain);
        llNoRecordFound = findViewById(R.id.llNoRecordFound);
        llLoading = findViewById(R.id.llLoading);
        btnFinish = findViewById(R.id.btnFinish);
        rvExperience.setLayoutManager(new LinearLayoutManager(ExperienceActivity.this));
        experienceAdapter = new AddExperienceAdapter(ExperienceActivity.this,experienceList);
        rvExperience.setAdapter(experienceAdapter);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddExperiencePopup(-1);
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (experienceList.size() == 0)
                    Toast.makeText(ExperienceActivity.this, "Please add your experience", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        submitOperation();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        getExperienceData();
    }

    private void getExperienceData() {
        llLoading.setVisibility(View.VISIBLE);
        clMain.setVisibility(View.GONE);
        AndroidNetworking.get(APi.GET_EXPERIENCE)
                .addQueryParameter("id",pref.getSecureEmpId())
                .setTag("uploadTest")
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_EXPERIENCE: "+response.toString(4));
                            clMain.setVisibility(View.VISIBLE);
                            llLoading.setVisibility(View.GONE);
                            JSONObject job1 = response;
                            experienceList.clear();
                            if (job1.optBoolean("responseStatus")){
                                JSONArray jsonArray=job1.getJSONArray("responseData");
                                Log.e(TAG, "jsonArray: "+jsonArray.length());
                                if(jsonArray.length() != 0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String JobRecordid = obj.getString("JobRecordid");
                                        Log.e(TAG, "JobRecordid: "+JobRecordid);
                                        String EmployeeID = obj.getString("EmployeeID");
                                        String FromDate = obj.getString("FromDate");
                                        String Todate = obj.getString("Todate");
                                        String OrganisationName = obj.getString("OrganisationName");
                                        String Designation = obj.getString("Designation");
                                        String GrossSal = obj.getString("GrossSal");
                                        String Experience = obj.getString("Experience");
                                        String JobLocation = obj.getString("JobLocation");
                                        AddExperienceModel experienceModel = new AddExperienceModel(OrganisationName,FromDate,Todate,Designation,
                                                (Experience.isEmpty())? 0 : Double.parseDouble(Experience)
                                                ,GrossSal,JobLocation);
                                        experienceModel.setJobRecordId(JobRecordid);
                                        Log.e(TAG, "JobRecordid: ===="+JobRecordid);
                                        experienceList.add(experienceModel);
                                    }
                                    experienceAdapter.notifyDataSetChanged();
                                    rvExperience.setVisibility(View.VISIBLE);
                                    llNoRecordFound.setVisibility(View.GONE);
                                }
                            } else {
                                rvExperience.setVisibility(View.VISIBLE);
                                llNoRecordFound.setVisibility(View.GONE);
                                openAddExperiencePopup(-1);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_EXPERIENCE_error: "+anError.getErrorBody());
                        clMain.setVisibility(View.VISIBLE);
                        llNoRecordFound.setVisibility(View.VISIBLE);
                        llLoading.setVisibility(View.GONE);

                    }
                });
    }

    private void submitOperation() throws JSONException {
        progressDialog=new ProgressDialog(ExperienceActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JSONArray jobRecords = new JSONArray();
        for (int i = 0; i < experienceList.size(); i++) {
            JSONObject job1 = new JSONObject();
            Log.e(TAG, "JobRecordId: "+experienceList.get(i).getJobRecordId());
            job1.put("JobRecordid", (experienceList.get(i).getJobRecordId().isEmpty())?JSONObject.NULL:experienceList.get(i).getJobRecordId());
            job1.put("EmployeeID", pref.getEmpId());
            job1.put("FromDate", DateConverter.convert_Date_dd_MMM_yyyy_To_yyyy_MM_dd(experienceList.get(i).getFromDate()));
            job1.put("ToDate", DateConverter.convert_Date_dd_MMM_yyyy_To_yyyy_MM_dd(experienceList.get(i).getToDate()));
            job1.put("OrganisationName", experienceList.get(i).getOrganizationName());
            job1.put("Designation", experienceList.get(i).getDesignation());
            job1.put("GrossSal", experienceList.get(i).getCtc());
            job1.put("JobLocation", experienceList.get(i).getLocation());
            job1.put("Experience", experienceList.get(i).getExperience());

            jobRecords.put(job1);
        }
        JSONObject rootObj = new JSONObject();
        rootObj.put("jobrecords", jobRecords);
        Log.e(TAG, "submitOperation: "+rootObj.toString(4));
        experienceAddAPICall(rootObj);
    }

    private void experienceAddAPICall(JSONObject jsonObject) {
        AndroidNetworking.post(APi.ADD_EXPERIENCE)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "ADD_EXPERIENCE: "+response.toString(4));
                            progressDialog.dismiss();
                            JSONObject job1 = response;
                            if (job1.optBoolean("responseStatus")){
                                Toast.makeText(ExperienceActivity.this, "Experience successfully added", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ExperienceActivity.this,EDashBoardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ExperienceActivity.this, job1.optString("responseText"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: "+anError.getErrorBody());
                        progressDialog.dismiss();
                    }
                });

    }

    public void openAddExperiencePopup(int position) {
        experienceAddDialog = new Dialog(ExperienceActivity.this,R.style.CustomDialogNew);
        experienceAddDialog.setContentView(R.layout.experience_input_layout);
        experienceAddDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        experienceAddDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        experienceAddDialog.setCancelable(false);
        experienceAddDialog.show();

        TextView txtFromDate = experienceAddDialog.findViewById(R.id.txtFromDate);
        TextView txtToDate = experienceAddDialog.findViewById(R.id.txtToDate);
        TextView txtExperience = experienceAddDialog.findViewById(R.id.txtExperience);
        EditText edtOrgName = experienceAddDialog.findViewById(R.id.edtOrgName);
        EditText edtDesignation = experienceAddDialog.findViewById(R.id.edtDesignation);
        EditText edtCTC = experienceAddDialog.findViewById(R.id.edtCTC);
        EditText edtLocation = experienceAddDialog.findViewById(R.id.edtLocation);
        AppCompatButton btnSave = experienceAddDialog.findViewById(R.id.btnSave);
        ImageView imgClose = experienceAddDialog.findViewById(R.id.imgClose);
        if (position != -1){
            edtOrgName.setText(experienceList.get(position).getOrganizationName());
            txtFromDate.setText(experienceList.get(position).getFromDate());
            txtToDate.setText(experienceList.get(position).getToDate());
            txtExperience.setText(experienceList.get(position).getExperience() +(experienceList.get(position).getExperience() == 1 ? " month" : " months"));
            edtDesignation.setText(experienceList.get(position).getDesignation());
            edtCTC.setText(experienceList.get(position).getCtc());
            edtLocation.setText(experienceList.get(position).getLocation());
            fromDateCalendar  = getCalendarFromDate(experienceList.get(position).getFromDate());
            toDateCalendar = getCalendarFromDate(experienceList.get(position).getToDate());
        }
        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromDate(txtFromDate,txtExperience);
            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectToDate(txtToDate,txtExperience);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtOrgName.getText().toString().isEmpty())
                    Toast.makeText(ExperienceActivity.this, "please enter organization name", Toast.LENGTH_SHORT).show();
                else if (txtFromDate.getText().toString().isEmpty())
                    Toast.makeText(ExperienceActivity.this, "please enter from date", Toast.LENGTH_SHORT).show();
                else if(txtToDate.getText().toString().isEmpty())
                    Toast.makeText(ExperienceActivity.this, "please enter to date", Toast.LENGTH_SHORT).show();
                else if(edtDesignation.getText().toString().isEmpty())
                    Toast.makeText(ExperienceActivity.this, "please enter designation", Toast.LENGTH_SHORT).show();
                else if(edtCTC.getText().toString().isEmpty())
                    Toast.makeText(ExperienceActivity.this, "please enter CTC", Toast.LENGTH_SHORT).show();
                else if (edtLocation.getText().toString().isEmpty())
                    Toast.makeText(ExperienceActivity.this, "please enter location", Toast.LENGTH_SHORT).show();
                else {
                    experienceAddDialog.dismiss();

                    if (position != -1){
                        totalExperienceMonth = calculateMonthDifference(txtExperience);
                        AddExperienceModel experienceModel = new AddExperienceModel(
                                edtOrgName.getText().toString().trim(),
                                txtFromDate.getText().toString(),
                                txtToDate.getText().toString(),
                                edtDesignation.getText().toString(),
                                totalExperienceMonth,
                                edtCTC.getText().toString().trim(),
                                edtLocation.getText().toString().trim());
                        experienceModel.setJobRecordId(experienceList.get(position).getJobRecordId());
                        experienceList.set(position,experienceModel);
                        experienceAdapter.notifyItemChanged(position);
                    } else {
                        experienceList.add(new AddExperienceModel(edtOrgName.getText().toString().trim(),
                                txtFromDate.getText().toString(),
                                txtToDate.getText().toString(),
                                edtDesignation.getText().toString(),
                                totalExperienceMonth,
                                edtCTC.getText().toString().trim(),
                                edtLocation.getText().toString().trim()));
                        experienceAdapter.notifyDataSetChanged();
                    }

                    fromDateCalendar = null;
                    toDateCalendar = null;
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDateCalendar = null;
                toDateCalendar = null;
                experienceAddDialog.dismiss();
            }
        });
    }

    private void selectToDate(TextView txtToDate, TextView txtExperience) {
        // Get Current Date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(ExperienceActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                toDateCalendar = Calendar.getInstance();
                toDate = (selectedMonth+1)+" "+selectedDay+" "+selectedYear;
                toDateCalendar.set(selectedYear, selectedMonth, selectedDay);
                Log.e(TAG, "onDateSet: "+toDate);
                toDate = DateConverter.convert_Date_MM_DD_YYYY_To_yyyy_MM_dd(toDate);
                txtToDate.setText(DateConverter.convert_Date_yyyy_MM_dd_To_dd_MMM_yyyy(toDate));

                if (fromDateCalendar != null) {
                    totalExperienceMonth = calculateMonthDifference(txtExperience);
                }
            }
        }, year, month, day);
        if (fromDateCalendar != null) {
            datePickerDialog.getDatePicker().setMinDate(fromDateCalendar.getTimeInMillis());
        } else {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void selectFromDate(TextView txtFromDate,TextView txtExperience) {
        // Get Current Date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(ExperienceActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                fromDateCalendar = Calendar.getInstance();

                fromDate = (selectedMonth+1)+" "+selectedDay+" "+selectedYear;
                fromDateCalendar.set(selectedYear, selectedMonth, selectedDay);
                Log.e(TAG, "onDateSet: "+fromDate);
                fromDate = DateConverter.convert_Date_MM_DD_YYYY_To_yyyy_MM_dd(fromDate);
                txtFromDate.setText(DateConverter.convert_Date_yyyy_MM_dd_To_dd_MMM_yyyy(fromDate));
                if (toDateCalendar != null) {
                    totalExperienceMonth = calculateMonthDifference(txtExperience);
                }
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void calculateYearMonthDifference(TextView txtExperience) {
        if (fromDateCalendar == null || toDateCalendar == null) return;

        int fromYear = fromDateCalendar.get(Calendar.YEAR);
        int fromMonth = fromDateCalendar.get(Calendar.MONTH);
        int fromDay = fromDateCalendar.get(Calendar.DAY_OF_MONTH);

        int toYear = toDateCalendar.get(Calendar.YEAR);
        int toMonth = toDateCalendar.get(Calendar.MONTH);
        int toDay = toDateCalendar.get(Calendar.DAY_OF_MONTH);

        int yearDiff = toYear - fromYear;
        int monthDiff = toMonth - fromMonth;
        int dayDiff = toDay - fromDay;

        // Adjust days
        if (dayDiff < 0) {
            monthDiff--;
            Calendar tempCal = (Calendar) toDateCalendar.clone();
            tempCal.add(Calendar.MONTH, -1);
            dayDiff += tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        // Adjust months
        if (monthDiff < 0) {
            yearDiff--;
            monthDiff += 12;
        }

        // Build result string dynamically
        StringBuilder result = new StringBuilder();
        if (yearDiff > 0) {
            result.append(yearDiff).append(yearDiff == 1 ? " year, " : " years, ");
        }
        if (monthDiff > 0) {
            result.append(monthDiff).append(monthDiff == 1 ? " month, " : " months, ");
        }
        if (dayDiff > 0) {
            result.append(dayDiff+1).append(dayDiff == 1 ? " day" : " days");
        }

        // Remove trailing comma if exists
        String finalResult = result.toString().trim();
        if (finalResult.endsWith(",")) {
            finalResult = finalResult.substring(0, finalResult.length() - 1);
        }

        //txtExperience.setText(finalResult);
        txtExperience.setText(finalResult);
    }
    private int calculateMonthDifference(TextView txtExperience) {
        if (fromDateCalendar == null || toDateCalendar == null) return 0;

        int fromYear = fromDateCalendar.get(Calendar.YEAR);
        int fromMonth = fromDateCalendar.get(Calendar.MONTH);

        int toYear = toDateCalendar.get(Calendar.YEAR);
        int toMonth = toDateCalendar.get(Calendar.MONTH);

        // Calculate total months
        int totalMonths = (toYear - fromYear) * 12 + (toMonth - fromMonth);

        // Add 1 if you want inclusive difference
        // totalMonths += 1;

        txtExperience.setText(totalMonths + (totalMonths == 1 ? " month" : " months"));
        return totalMonths;
    }

    public static Calendar getCalendarFromDate(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        try {
            // Define the format of input date
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

            // Parse String into Date
            Date date = sdf.parse(dateStr);

            // Set into Calendar
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }
}