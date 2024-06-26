package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
/*import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.fragment.AttendanceViewFragment;
import com.genius.employee.fragment.CurrentCTCFragment;
import com.genius.employee.fragment.OnlineAttendanceReportFragment;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MarkInAttendanceDashboardActivity extends AppCompatActivity {
    LinearLayout llManage, llReport,llAddFace;
    ImageView imgBack, imgHome;
    String lebelId;

    Pref pref;
    TextView tvManage,tvReport;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markin_attendance_dashboard);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(MarkInAttendanceDashboardActivity.this);
        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llAddFace = (LinearLayout) findViewById(R.id.llAddFace);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvReport=(TextView) findViewById(R.id.tvReport);
        tvManage=(TextView) findViewById(R.id.tvManage);
        loadAttendanceViewFragment();
    }

    private void onClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAttendanceViewFragment();
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAttendanceReportFragment();
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
                Intent intent = new Intent(MarkInAttendanceDashboardActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    /*private void getLebelId() {
        final ProgressDialog pd = new ProgressDialog(MarkInAttendanceDashboardActivity.this);
        pd.setMessage("loading...");
        pd.setCancelable(false);
        pd.show();

        final String surl = APi.sUrl+"ManageEmployee/Hierarchy?EmployeeID=" + pref.getEmpId();
        Log.d("inputLogin", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    lebelId = obj.optString("LevelID");
                                    pref.saveLebelId(lebelId);
                                }
                                pd.dismiss();
                                if (lebelId.equals("2060000003")||lebelId.equals("2060000005")||lebelId.equals("2060000010")||lebelId.equals("2060000012")||pref.getEmpId().equals("2070002087")) {
                                    Intent intent = new Intent(MarkInAttendanceDashboardActivity.this, WFHReportDashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    customType(MarkInAttendanceDashboardActivity.this, "left-to-right");
                                }else {
                                    Intent intent = new Intent(MarkInAttendanceDashboardActivity.this, MarkInReportActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    customType(MarkInAttendanceDashboardActivity.this, "left-to-right");
                                }

                            } else {
                                pd.dismiss();
                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MarkInAttendanceDashboardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {};
        RequestQueue requestQueue = Volley.newRequestQueue(MarkInAttendanceDashboardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }*/

    private void showFaceAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please train your face first");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                       /* Intent intent = new Intent(MarkInAttendanceDashboardActivity.this, AddPerson.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MarkInAttendanceDashboardActivity.this,EDashBoardActivity.class);
        startActivity(intent);
        finish();
    }


    public void loadAttendanceViewFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AttendanceViewFragment pfragment=new AttendanceViewFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        llManage.setBackgroundColor(Color.parseColor("#006CFF"));
        llReport.setBackgroundColor(Color.parseColor("#FFFFFF"));

        tvManage.setTextColor(Color.parseColor("#FFFFFF"));
        tvReport.setTextColor(Color.parseColor("#006CFF"));
        //tvHeader.setText("Personal");
    }


    public void loadAttendanceReportFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        OnlineAttendanceReportFragment pfragment=new OnlineAttendanceReportFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        llManage.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llReport.setBackgroundColor(Color.parseColor("#006CFF"));

        tvManage.setTextColor(Color.parseColor("#006CFF"));
        tvReport.setTextColor(Color.parseColor("#FFFFFF"));
        //tvHeader.setText("Personal");
    }

}
