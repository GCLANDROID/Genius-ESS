package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
/*import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.genius.employee.R;
import com.genius.employee.adapter.AttendanceAdapter;
import com.genius.employee.adapter.CTCAdapter;
import com.genius.employee.adapter.HolidayAdapter;
import com.genius.employee.adapter.LeaveAdapter;
import com.genius.employee.adapter.SalaryAdapter;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.CTCModel;
import com.genius.employee.model.HolidayModel;
import com.genius.employee.model.LeaveModel;
import com.genius.employee.model.SalaryModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.Util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class VoiceAssistantActivity extends AppCompatActivity {
    ImageView imgMic;
    EditText etText;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    LinearLayout llVoice,llMain,llNodata;
    RecyclerView rvSalary;

    ImageView imgMicSearch;
    TextToSpeech t1;
    String mId,yID;
    String apiYID;
    Pref pref;

    TextView tvSearch;

    String cuyear;
    int y;
    String year;
    ImageView imgBack,imgHome;
    ArrayList<HolidayModel> holidayitemList = new ArrayList();
    ArrayList<LeaveModel>leaveList=new ArrayList();
    ArrayList<CTCModel> ctcList = new ArrayList();
    ArrayList<SalaryModel> salaryList = new ArrayList();
    ArrayList<AttendanceModel> attendanceList = new ArrayList();
    String HolidayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_assistant);
        initView();
        setYearItem();
        onCLick();
    }

    private void initView(){
        pref=new Pref(VoiceAssistantActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        tvSearch=(TextView)findViewById(R.id.tvSearch);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llVoice=(LinearLayout)findViewById(R.id.llVoice);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        rvSalary = (RecyclerView) findViewById(R.id.rvSalary);
       /* LinearLayoutManager layoutManager = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);*/
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        imgMic=(ImageView)findViewById(R.id.imgMic);
        imgMicSearch=(ImageView)findViewById(R.id.imgMicSearch);
        etText=(EditText)findViewById(R.id.etText);
        etText.setEnabled(false);
        y= Calendar.getInstance().get(Calendar.YEAR);
        cuyear=String.valueOf(y);

        imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast
                            .makeText(VoiceAssistantActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                etText.setText(
                        Objects.requireNonNull(result).get(0));
                String text=etText.getText().toString().toLowerCase();

                if (text.contains("salary") ||text.contains("payslip") ||text.contains("pay slip")){
                    tvSearch.setText("Your Salary List is Here:");


                    if (text.contains("january")){
                        mId="1";

                    }else if (text.contains("february")){

                        mId="2";
                    }else if (text.contains("march")){

                        mId="3";
                    }else if (text.contains("april")){

                        mId="4";
                    }else if (text.contains("may")){

                        mId="5";
                    }else if (text.contains("june")){

                        mId="6";
                    }else if (text.contains("july")){

                        mId="7";
                    }else if (text.contains("august")){

                        mId="8";
                    }else if (text.contains("september")){

                        mId="9";
                    }else if (text.contains("october")){

                        mId="10";
                    }else if (text.contains("november")){

                        mId="11";
                    }else if (text.contains("december")){

                        mId="12";
                    }else {

                        mId="0";
                    }

                    if (text.contains("january") && text.contains("2022")){
                        yID="17";
                    }else if (text.contains("february") && text.contains("2022")){
                        yID="17";
                    }else if (text.contains("march") && text.contains("2022")){
                        yID="17";
                    }else if (text.contains("2022")){
                        yID="18";
                    }else if (text.contains("january") && text.contains("2021")){
                        yID="16";
                    }else if (text.contains("february") && text.contains("2021")){
                        yID="16";
                    }else if (text.contains("march") && text.contains("2021")){
                        yID="16";
                    }else if (text.contains("2021")){
                        yID="17";
                    }else if (text.contains("january") && text.contains("2023")){
                        yID="18";
                    }else if (text.contains("february") && text.contains("2023")){
                        yID="18";
                    }else if (text.contains("march") && text.contains("2023")){
                        yID="18";
                    }else if ( text.contains("2023")){
                        yID="19";
                    }else if (text.contains("january") && text.contains("2024")){
                        yID="19";
                    }else if (text.contains("february") && text.contains("2024")){
                        yID="19";
                    }else if (text.contains("march") && text.contains("2024")){
                        yID="19";
                    }else if ( text.contains("2024")){
                        yID="20";
                    }else if (text.contains("january") && text.contains("2025")){
                        yID="20";
                    }else if (text.contains("february") && text.contains("2025")){
                        yID="20";
                    }else if (text.contains("march") && text.contains("2025")){
                        yID="20";
                    }else if ( text.contains("2025")){
                        yID="21";
                    }else if (text.contains("january") && text.contains("2026")){
                        yID="21";
                    }else if (text.contains("february") && text.contains("2026")){
                        yID="21";
                    }else if (text.contains("march") && text.contains("2026")){
                        yID="21";
                    }else if ( text.contains("2026")){
                        yID="22";
                    }else {
                        yID=apiYID;
                    }
                    //Salary
                    t1.speak("Here is your salary list", TextToSpeech.QUEUE_FLUSH, null);
                    getSalaryList(mId,yID);


                }else  if (text.contains("report") || text.contains("monthly")) {
                    tvSearch.setText("Your Attendance Report is Here:");


                    if (text.contains("january")) {
                        mId = "1";

                    } else if (text.contains("february")) {

                        mId = "2";
                    } else if (text.contains("march")) {

                        mId = "3";
                    } else if (text.contains("april")) {

                        mId = "4";
                    } else if (text.contains("may")) {

                        mId = "5";
                    } else if (text.contains("june")) {

                        mId = "6";
                    } else if (text.contains("july")) {

                        mId = "7";
                    } else if (text.contains("august")) {

                        mId = "8";
                    } else if (text.contains("september")) {

                        mId = "9";
                    } else if (text.contains("october")) {

                        mId = "10";
                    } else if (text.contains("november")) {

                        mId = "11";
                    } else if (text.contains("december")) {

                        mId = "12";
                    } else {

                        mId = "0";
                    }

                    if (text.contains("january") && text.contains("2022")) {
                        yID = "17";
                    } else if (text.contains("february") && text.contains("2022")) {
                        yID = "17";
                    } else if (text.contains("march") && text.contains("2022")) {
                        yID = "17";
                    } else if (text.contains("2022")) {
                        yID = "18";
                    } else if (text.contains("january") && text.contains("2021")) {
                        yID = "16";
                    } else if (text.contains("february") && text.contains("2021")) {
                        yID = "16";
                    } else if (text.contains("march") && text.contains("2021")) {
                        yID = "16";
                    } else if (text.contains("2021")) {
                        yID = "17";
                    } else if (text.contains("january") && text.contains("2023")) {
                        yID = "18";
                    } else if (text.contains("february") && text.contains("2023")) {
                        yID = "18";
                    } else if (text.contains("march") && text.contains("2023")) {
                        yID = "18";
                    } else if (text.contains("2023")) {
                        yID = "19";
                    } else if (text.contains("january") && text.contains("2024")) {
                        yID = "19";
                    } else if (text.contains("february") && text.contains("2024")) {
                        yID = "19";
                    } else if (text.contains("march") && text.contains("2024")) {
                        yID = "19";
                    } else if (text.contains("2024")) {
                        yID = "20";
                    } else if (text.contains("january") && text.contains("2025")) {
                        yID = "20";
                    } else if (text.contains("february") && text.contains("2025")) {
                        yID = "20";
                    } else if (text.contains("march") && text.contains("2025")) {
                        yID = "20";
                    } else if (text.contains("2025")) {
                        yID = "21";
                    } else if (text.contains("january") && text.contains("2026")) {
                        yID = "21";
                    } else if (text.contains("february") && text.contains("2026")) {
                        yID = "21";
                    } else if (text.contains("march") && text.contains("2026")) {
                        yID = "21";
                    } else if (text.contains("2026")) {
                        yID = "22";
                    } else {
                        yID = apiYID;
                    }
                    //Salary
                    t1.speak("Here is your attendance report ", TextToSpeech.QUEUE_FLUSH, null);
                    getAttendanceReport(mId, yID);
                }

                else if (text.contains("name") || text.contains("hi")||text.contains("hello")){
                    t1.speak("Hi I am Your Genius Voice Assistant.You may ask me about your leave balance,PF Balance,Salary,CTC,Holiday , monthly attendance report and you can navigate to online attendance from here", TextToSpeech.QUEUE_FLUSH, null);
                }else if (text.contains("leave balance") || text.contains("leavebalance") ||text.contains("life")){
                    tvSearch.setText("Your Leave Balance is Here:");
                    t1.speak("Here is your Leave Balance", TextToSpeech.QUEUE_FLUSH, null);
                    getLeaveBalance();
                    //leave balance
                }else if (text.contains("current")){
                    t1.speak("Here is Current CTC", TextToSpeech.QUEUE_FLUSH, null);
                    tvSearch.setText("Current CTC is Here:");
                    getCurrentCTC();
                    //CTC
                }else if (text.contains("ctc")){
                    tvSearch.setText("CTC list is Here:");
                    t1.speak("Here is CTC List", TextToSpeech.QUEUE_FLUSH, null);
                    getpreviousCTC();
                    //CTC
                }else if (text.contains("holiday")){
                    tvSearch.setText("Holiday list is Here:");
                    if (text.contains("2021")){
                        year="2021";
                    }else if (text.contains("2022")){
                        year="2022";
                    }else if (text.contains("2023")){
                        year="2023";
                    }else if (text.contains("2024")){
                        year="2024";
                    }else if (text.contains("2025")){
                        year="2025";
                    }else if (text.contains("2026")){
                        year="2026";
                    }else if (text.contains("2027")){
                        year="2027";
                    }else if (text.contains("2028")){
                        year="2028";
                    }else {
                        year=cuyear;
                    }

                    t1.speak("Here is your Holidaylist", TextToSpeech.QUEUE_FLUSH, null);

                    //Holidaylist
                    getHolidayList(year);
                }else if (text.contains("online attendance") || text.contains("attendance")||text.contains("mark attendance")|| text.contains("online")){
                    t1.speak("Here is your Online Attendance", TextToSpeech.QUEUE_FLUSH, null);
                    Intent intent = new Intent(VoiceAssistantActivity.this, MarkInManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (text.contains("pf") || text.contains("pf balance")){
                    t1.speak("Here is your PF balance", TextToSpeech.QUEUE_FLUSH, null);
                    getPFLInk();
                }
                else {
                    t1.speak("Sorry! I don't have any training regarding this", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }
    }


    private void onCLick(){
        imgMicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                etText.setText("");
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VoiceAssistantActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getHolidayList(String y){
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        rvSalary.setLayoutManager(new GridLayoutManager(this, 3));

        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String surl = APi.sGetHolidayApi+"CompanyID=1090000001&BranchID="+pref.getBranchId()+"&Year="+year;
        Log.d("inputholiday",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        progressDialog.dismiss();
                       holidayitemList.clear();
                        leaveList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String HolidayName = obj.optString("HolidayName");
                                    String HolidayDayName = obj.optString("HolidayDayName");
                                    try {
                                        HolidayDate = Util.changeAnyDateFormat(obj.optString("HolidayDate"),"dd-MMM-yyyy","dd \nMMM");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String IsColor=obj.optString("IsColor");
                                    HolidayModel aModel = new HolidayModel(HolidayName, HolidayDate, HolidayDayName);
                                    aModel.setIsColor(IsColor);
                                    holidayitemList.add(aModel);



                                }
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                HolidayAdapter hAdapter=new HolidayAdapter(holidayitemList);
                                rvSalary.setAdapter(hAdapter);

                            }

                            else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                Toast.makeText(VoiceAssistantActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getLeaveBalance(){
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String surl = APi.sManageLeaveBalanceApi+"epID="+pref.getSecureEmpId();
        Log.d("inputholiday",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        progressDialog.dismiss();
                        holidayitemList.clear();
                        leaveList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String LeaveType=obj.optString("LeaveType");
                                    String Closing_Balance=obj.optString("Closing_Balance");

                                    LeaveModel obj2 = new LeaveModel(LeaveType,Closing_Balance);
                                    leaveList.add(obj2);



                                }
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                LeaveAdapter lAdapter=new LeaveAdapter(leaveList);
                                rvSalary.setAdapter(lAdapter);

                            }

                            else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                Toast.makeText(VoiceAssistantActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getPFLInk(){
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String surl = APi.sGetPFManagementURLApi + "EmployeeId=" + pref.getSecureEmpId();
        Log.d("inputholiday",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        progressDialog.dismiss();
                        holidayitemList.clear();
                        leaveList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                           boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                String url = job1.optString("responseData");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);


                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                Toast.makeText(VoiceAssistantActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getCurrentCTC() {
        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        byte[] data = new byte[0];
        try {
            data = pref.getEmpId().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        String surl = APi.sEMPCTCHistoryApi+"EmployeeId="+pref.getSecureEmpId();
        Log.d("compurl", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();
                        ctcList.clear();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");

                                JSONObject obj = responseData.getJSONObject(0);
                                String EffectiveDate = obj.optString("EffectiveDate");
                                String MonthlyGross = obj.optString("MonthlyGross");
                                String YearlyGross = obj.optString("YearlyGross");
                                String EmployeeID = obj.optString("EmployeeID");
                                String MonthlyCTC=obj.optString("MonthlyCTC");
                                String YearlyCTC=obj.optString("YearlyCTC");


                                CTCModel ctcModel = new CTCModel(EffectiveDate,MonthlyGross,YearlyGross,EmployeeID,YearlyCTC,MonthlyCTC);
                                ctcList.add(ctcModel);
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                CTCAdapter lAdapter = new CTCAdapter(ctcList, VoiceAssistantActivity.this);
                                rvSalary.setAdapter(lAdapter);

                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
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
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void getpreviousCTC() {
        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        byte[] data = new byte[0];
        try {
            data = pref.getEmpId().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        String surl = APi.sEMPCTCHistoryApi+"EmployeeId="+pref.getSecureEmpId();
        Log.d("compurl", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        ctcList.clear();
                        progressDialog.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String EffectiveDate = obj.optString("EffectiveDate");
                                    String MonthlyGross = obj.optString("MonthlyGross");
                                    String YearlyGross = obj.optString("YearlyGross");
                                    String EmployeeID = obj.optString("EmployeeID");
                                    String MonthlyCTC=obj.optString("MonthlyCTC");
                                    String YearlyCTC=obj.optString("YearlyCTC");


                                    CTCModel ctcModel = new CTCModel(EffectiveDate,MonthlyGross,YearlyGross,EmployeeID,YearlyCTC,MonthlyCTC);
                                    ctcList.add(ctcModel);


                                }
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                CTCAdapter lAdapter = new CTCAdapter(ctcList, VoiceAssistantActivity.this);
                                rvSalary.setAdapter(lAdapter);

                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
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
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    private void setYearItem() {
        String surl = APi.sGetFinancialYearApi;
      ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
      progressDialog.setMessage("Loading");
      progressDialog.setCancelable(false);
      progressDialog.show();
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
                            String responseID = job1.optString("responseID");
                            if (responseID.equalsIgnoreCase("2022-2023")){
                                apiYID="18";
                            }else if (responseID.equalsIgnoreCase("2023-2024")){
                                apiYID="19";
                            }else if (responseID.equalsIgnoreCase("2024-2025")){
                                apiYID="20";
                            }else if (responseID.equalsIgnoreCase("2025-2026")){
                                apiYID="21";
                            }else if (responseID.equalsIgnoreCase("2026-2027")){
                                apiYID="22";
                            }else if (responseID.equalsIgnoreCase("2027-2028")){
                                apiYID="23";
                            }else if (responseID.equalsIgnoreCase("2028-2029")){
                                apiYID="24";
                            }








                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VoiceAssistantActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              progressDialog.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
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
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

    private void getSalaryList(String monthid,String yearid) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        ProgressDialog pd=new ProgressDialog(VoiceAssistantActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        String surl = APi.sSalaryHistoryApi+"EmployeeId="+pref.getSecureEmpId()+"&FinYearID="+yearid+"&Month="+monthid;

        Log.d("reporturl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        salaryList.clear();
                        pd.dismiss();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {




                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String MonthName = obj.optString("MonthName");
                                    String FinancialYear = obj.optString("FinancialYear");
                                    Log.d("FinancialYear",FinancialYear);
                                    String MonthlyGross = obj.optString("MonthlyGross");
                                    String ActualGross = obj.optString("ActualGross");
                                    String Deduction = obj.optString("Deduction");
                                    String MonthlyNet = obj.optString("MonthlyNet");
                                    String EsiEmployeeCon = obj.optString("EsiEmployeeCon");
                                    String PTValue = obj.optString("PTValue");
                                    String WebURL=obj.optString("WebURL");
                                    SalaryModel salModel = new SalaryModel(FinancialYear,MonthlyGross,ActualGross,Deduction,MonthlyNet,EsiEmployeeCon,PTValue,MonthName,WebURL);
                                    salaryList.add(salModel);


                                }
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);

                                SalaryAdapter salAdapter = new SalaryAdapter(salaryList,VoiceAssistantActivity.this);
                                rvSalary.setAdapter(salAdapter);


                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                llVoice.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.VISIBLE);
                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getAttendanceReport(String monthid,String yearid) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        ProgressDialog pd=new ProgressDialog(VoiceAssistantActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        String surl = APi.sGetMonthWiseAttendanceApi+"YearID=" + yearid + "&DepartmentID=" + pref.getDeptId() + "&EmployeeID=" + pref.getSecureEmpId() + "&MonthID=" + monthid;
        Log.d("reporturl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        attendanceList.clear();
                        pd.dismiss();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {




                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String AWDays = obj.optString("AWDays");
                                    String AttDays = obj.optString("AttDays");
                                    String Abs = obj.optString("Abs");
                                    String OtherAbs = obj.optString("OtherAbs");
                                    String LS = obj.optString("LS");
                                    String OtherLS = obj.optString("OtherLS");
                                    String LWP = obj.optString("LWP");
                                    String MLwp = obj.optString("MLwp");
                                    String PaybleDays = obj.optString("PaybleDays");
                                    String MonthName = obj.optString("MonthName");
                                    String MonthView = obj.optString("MonthView");


                                    AttendanceModel aModel = new AttendanceModel(AWDays, AttDays, PaybleDays, Abs, OtherAbs, LS, OtherLS, LWP, MLwp, MonthName, MonthView);
                                    attendanceList.add(aModel);



                                }
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);

                                AttendanceAdapter lAdapter = new AttendanceAdapter(attendanceList, VoiceAssistantActivity.this);
                                rvSalary.setAdapter(lAdapter);


                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                llVoice.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.VISIBLE);
                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }



}