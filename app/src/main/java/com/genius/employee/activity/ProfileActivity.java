package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
/*import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.fragment.ContactFragment;
import com.genius.employee.fragment.EducationFragment;
import com.genius.employee.fragment.FamilyFragment;
import com.genius.employee.fragment.MisFragment;
import com.genius.employee.fragment.OfficialFragment;
import com.genius.employee.fragment.PersonalFragment;
import com.genius.employee.model.LeaveModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity {

    int flag;
    TextView tvProfile;
    LinearLayout llMain, llLoader;
    Pref pref;
    String phy;
    TextView tvHeader;
    ImageView imgBack, imgHome;
    NetworkConnectionCheck networkConnectionCheck;
    LinearLayout llOfficial, llPersonal, llContact, llFamily, llEducation;
    ImageView imgOfficial, imgPersonal, imgContact, imgFamily, imgEducation;
    TextView tvOfficial, tvPersonal, tvContact, tvFamily, tvEducation;
    TextView tvToolBar;
    ImageView imgMis;
    TextView tvMis;
    LinearLayout llMis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
        getProfile();
        onclick();
    }

    private void initialize() {
        networkConnectionCheck = new NetworkConnectionCheck(ProfileActivity.this);
        pref = new Pref(getApplicationContext());
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llOfficial = (LinearLayout) findViewById(R.id.llOfficial);
        llPersonal = (LinearLayout) findViewById(R.id.llPersonal);
        llContact = (LinearLayout) findViewById(R.id.llContact);
        llFamily = (LinearLayout) findViewById(R.id.llFamily);
        llEducation = (LinearLayout) findViewById(R.id.llEducation);
        llMis = (LinearLayout) findViewById(R.id.llMis);

        imgOfficial = (ImageView) findViewById(R.id.imgOfficial);
        imgPersonal = (ImageView) findViewById(R.id.imgPersonal);
        imgContact = (ImageView) findViewById(R.id.imgContact);
        imgFamily = (ImageView) findViewById(R.id.imgFamily);
        imgEducation = (ImageView) findViewById(R.id.imgEducation);
        imgMis = (ImageView) findViewById(R.id.imgMis);

        tvOfficial = (TextView) findViewById(R.id.tvOfficail);
        tvPersonal = (TextView) findViewById(R.id.tvPersonal);
        tvContact = (TextView) findViewById(R.id.tvContact);
        tvFamily = (TextView) findViewById(R.id.tvFamily);
        tvEducation = (TextView) findViewById(R.id.tvEducation);
        tvMis = (TextView) findViewById(R.id.tvMis);

        tvToolBar = (TextView) findViewById(R.id.tvToolBar);

    }

    private void onclick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llOfficial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOfficialFragment();

            }
        });
        llPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPersonalFragment();
            }
        });
        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadContactFragment();
            }
        });
        llFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFamilyFragment();
            }
        });
        llEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEducationFragment();
            }
        });
        llMis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMisFragment();
            }
        });
    }


    public void loadOfficialFragment() {
        flag = 1;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        OfficialFragment fragment = new OfficialFragment();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
        imgOfficial.setImageResource(R.drawable.official);
        imgPersonal.setImageResource(R.drawable.employee1);
        imgContact.setImageResource(R.drawable.contact1);
        imgFamily.setImageResource(R.drawable.family1);
        imgEducation.setImageResource(R.drawable.education1);
        imgEducation.setImageResource(R.drawable.education1);
        imgMis.setImageResource(R.drawable.ic_charity);

        tvOfficial.setTextColor(Color.parseColor("#085A96"));
        tvPersonal.setTextColor(Color.parseColor("#30B3A3"));
        tvContact.setTextColor(Color.parseColor("#30B3A3"));
        tvFamily.setTextColor(Color.parseColor("#30B3A3"));
        tvEducation.setTextColor(Color.parseColor("#30B3A3"));
        tvMis.setTextColor(Color.parseColor("#30B3A3"));

        tvToolBar.setText("Official");


        //tvHeader.setText("Official");


    }

    public void loadPersonalFragment() {
        flag = 1;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PersonalFragment pfragment = new PersonalFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        imgOfficial.setImageResource(R.drawable.official1);
        imgPersonal.setImageResource(R.drawable.employee);
        imgContact.setImageResource(R.drawable.contact1);
        imgFamily.setImageResource(R.drawable.family1);
        imgEducation.setImageResource(R.drawable.education1);
        imgMis.setImageResource(R.drawable.ic_charity);

        tvPersonal.setTextColor(Color.parseColor("#085A96"));
        tvOfficial.setTextColor(Color.parseColor("#30B3A3"));
        tvContact.setTextColor(Color.parseColor("#30B3A3"));
        tvFamily.setTextColor(Color.parseColor("#30B3A3"));
        tvEducation.setTextColor(Color.parseColor("#30B3A3"));
        tvMis.setTextColor(Color.parseColor("#30B3A3"));

        tvToolBar.setText("Personal");

        //tvHeader.setText("Personal");


    }

    public void loadContactFragment() {
        flag = 1;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ContactFragment cfragment = new ContactFragment();
        transaction.replace(R.id.frameLayout, cfragment);
        transaction.commit();
        imgOfficial.setImageResource(R.drawable.official1);
        imgPersonal.setImageResource(R.drawable.employee1);
        imgContact.setImageResource(R.drawable.contact);
        imgFamily.setImageResource(R.drawable.family1);
        imgEducation.setImageResource(R.drawable.education1);
        imgMis.setImageResource(R.drawable.ic_charity);

        tvContact.setTextColor(Color.parseColor("#085A96"));
        tvPersonal.setTextColor(Color.parseColor("#30B3A3"));
        tvOfficial.setTextColor(Color.parseColor("#30B3A3"));
        tvFamily.setTextColor(Color.parseColor("#30B3A3"));
        tvEducation.setTextColor(Color.parseColor("#30B3A3"));
        tvMis.setTextColor(Color.parseColor("#30B3A3"));

        tvToolBar.setText("Contact");

        //tvHeader.setText("Contact");
    }

    public void loadFamilyFragment() {
        flag = 1;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        FamilyFragment ffragment = new FamilyFragment();
        transaction.replace(R.id.frameLayout, ffragment);
        transaction.commit();
        imgOfficial.setImageResource(R.drawable.official1);
        imgPersonal.setImageResource(R.drawable.employee1);
        imgContact.setImageResource(R.drawable.contact1);
        imgFamily.setImageResource(R.drawable.family);
        imgEducation.setImageResource(R.drawable.education1);
        imgMis.setImageResource(R.drawable.ic_charity);


        tvFamily.setTextColor(Color.parseColor("#085A96"));
        tvPersonal.setTextColor(Color.parseColor("#30B3A3"));
        tvContact.setTextColor(Color.parseColor("#30B3A3"));
        tvOfficial.setTextColor(Color.parseColor("#30B3A3"));
        tvEducation.setTextColor(Color.parseColor("#30B3A3"));
        tvMis.setTextColor(Color.parseColor("#30B3A3"));

        tvToolBar.setText("Family");

        //tvHeader.setText("Family");
    }


    public void loadEducationFragment() {
        flag = 1;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EducationFragment efragment = new EducationFragment();
        transaction.replace(R.id.frameLayout, efragment);
        transaction.commit();
        imgOfficial.setImageResource(R.drawable.official1);
        imgPersonal.setImageResource(R.drawable.employee1);
        imgContact.setImageResource(R.drawable.contact1);
        imgFamily.setImageResource(R.drawable.family1);
        imgEducation.setImageResource(R.drawable.education);
        tvEducation.setTextColor(Color.parseColor("#30B3A3"));

        tvEducation.setTextColor(Color.parseColor("#085A96"));
        tvPersonal.setTextColor(Color.parseColor("#30B3A3"));
        tvContact.setTextColor(Color.parseColor("#30B3A3"));
        tvOfficial.setTextColor(Color.parseColor("#30B3A3"));
        tvFamily.setTextColor(Color.parseColor("#30B3A3"));
        tvMis.setTextColor(Color.parseColor("#30B3A3"));

        tvToolBar.setText("Education");
        //tvHeader.setText("Education");
    }


    public void loadMisFragment() {
        flag = 1;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MisFragment efragment = new MisFragment();
        transaction.replace(R.id.frameLayout, efragment);
        transaction.commit();
        imgOfficial.setImageResource(R.drawable.official1);
        imgPersonal.setImageResource(R.drawable.employee1);
        imgContact.setImageResource(R.drawable.contact1);
        imgFamily.setImageResource(R.drawable.family1);
        imgEducation.setImageResource(R.drawable.education1);
        imgMis.setImageResource(R.drawable.ic_charity1);


        tvEducation.setTextColor(Color.parseColor("#30B3A3"));
        tvPersonal.setTextColor(Color.parseColor("#30B3A3"));
        tvContact.setTextColor(Color.parseColor("#30B3A3"));
        tvOfficial.setTextColor(Color.parseColor("#30B3A3"));
        tvFamily.setTextColor(Color.parseColor("#30B3A3"));
        tvMis.setTextColor(Color.parseColor("#085A96"));

        tvToolBar.setText("Miscellaneous");
        //tvHeader.setText("Education");
    }

    private void getProfile() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        String surl = APi.sManageEmployeeApi+"epID=" + pref.getSecureEmpId();
        Log.d("inputleave", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);

                        // attendabceInfiList.clear();

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
                                    String FirstName = obj.optString("FirstName");
                                    pref.saveFName(FirstName);
                                    String LastName = obj.optString("LastName");
                                    pref.saveLName(LastName);
                                    String DepartmentID = obj.optString("DepartmentID");
                                    pref.saveDept(DepartmentID);
                                    String Branch = obj.optString("Branch");
                                    pref.saveBranch(Branch);
                                    String DesignationID = obj.optString("DesignationID");
                                    pref.saveDes(DesignationID);
                                    String FunctionName = obj.optString("FunctionName");
                                    if (FunctionName.equals("") || FunctionName.equals("0") || FunctionName.equals("null")) {
                                        pref.saveFunction("N/A");
                                    } else {
                                        pref.saveFunction(FunctionName);
                                    }
                                    String DateOfJoining = obj.optString("DateOfJoining");
                                    pref.saveDOJ(DateOfJoining);
                                    String EmploymentType = obj.optString("EmploymentType");
                                    pref.saveEmpType(EmploymentType);
                                    String Salutation = obj.optString("Salutation");
                                    pref.saveSalutation(Salutation);
                                    Boolean PhysicallyChallenged = obj.optBoolean("PhysicallyChallenged");
                                    if (PhysicallyChallenged) {
                                        phy = "YES";
                                    } else {
                                        phy = "NO";
                                    }

                                    pref.savePhy(phy);
                                    String DateOfBirth = obj.optString("DateOfBirth");
                                    if (DateOfBirth.equals("") || DateOfBirth.equals("0") || DateOfBirth.equals("null")) {
                                        pref.saveDOB("N/A");
                                    } else {
                                        pref.saveDOB(DateOfBirth);
                                    }
                                    String MaritalStatus = obj.optString("MaritalStatus");
                                    if (MaritalStatus.equals("") || MaritalStatus.equals("0") || MaritalStatus.equals("null")) {
                                        pref.saveMartial("N/A");
                                    } else {
                                        pref.saveMartial(MaritalStatus);
                                    }
                                    String Sex = obj.optString("Sex");
                                    if (Sex.equals("") || Sex.equals("0") || Sex.equals("null")) {
                                        pref.saveGender("N/A");
                                    } else {
                                        pref.saveGender(Sex);
                                    }

                                    String BloodGroup = obj.optString("BloodGroup");
                                    if (BloodGroup.equals("null") || BloodGroup.equals("") || BloodGroup.equals("0")) {
                                        pref.saveBlood("N/A");
                                    } else {
                                        pref.saveBlood(BloodGroup);
                                    }

                                    String GuardianName = obj.optString("GuardianName");
                                    if (GuardianName.equals("") || GuardianName.equals("null") || GuardianName.equals("0")) {
                                        pref.saveGurdian("N/A");
                                    } else {
                                        pref.saveGurdian(GuardianName);
                                    }
                                    String RelationShip = obj.optString("RelationShip");
                                    if (RelationShip.equals("null") || RelationShip.equals("0") || RelationShip.equals("")) {
                                        pref.saveRelation("N/A");
                                    } else {
                                        pref.saveRelation(RelationShip);
                                    }


                                }

                                llLoader.setVisibility(View.VISIBLE);
                                llMain.setVisibility(View.GONE);
                                getMisProfile();


                            } else {

                                /*llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
*/
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
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


    private void getMisProfile() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);


        String surl = APi.sBankAccountApi + "EmployeeId=" + pref.getSecureEmpId();
        Log.d("inputleave", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {


                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONObject responseData = job1.optJSONObject("responseData");
                                String AccountNo = responseData.optString("AccountNo");
                                if (AccountNo.equals("null") || AccountNo.equals("")) {
                                    pref.saveAccNo("N/A");
                                } else {
                                    pref.saveAccNo(AccountNo);
                                }
                                String PFNO = responseData.optString("PFNO");
                                if (PFNO.equals("null") || PFNO.equals("")) {
                                    pref.savePFNO("N/A");
                                } else {
                                    pref.savePFNO(PFNO);
                                }
                                String ESINO = responseData.optString("ESINO");
                                if (ESINO.equals("null") || ESINO.equals("")) {
                                    pref.saveESICNO("N/A");
                                } else {
                                    pref.saveESICNO(ESINO);
                                }
                                String PanNo = responseData.optString("PanNo");
                                if (PanNo.equals("null") || PanNo.equals("")) {
                                    pref.savePANO("N/A");
                                } else {
                                    pref.savePANO(PanNo);
                                }

                                String AadharCardNo = responseData.optString("AadharCardNo");
                                pref.saveAadhar(AadharCardNo);


                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                loadOfficialFragment();


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                loadOfficialFragment();
                                /*llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
*/
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(ProfileActivity.this, "volly 2" + error.toString() + "Contact to administrator", Toast.LENGTH_LONG).show();
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
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


}
