package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.adapter.TourEmployeeListAdapter;
import com.genius.employee.adapter.VisitEmployeeListAdapter;
import com.genius.employee.model.EmployeeListModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TourEmployeeTreeListActivity extends AppCompatActivity {
    ArrayList<EmployeeListModel>itemList=new ArrayList();
    RecyclerView rvItem;
    LinearLayout llLoader,llMain;
    String lebelId,deptId;
    Pref pref;
    ImageView imgBack,imgHome;
    Spinner spBranch;
    ArrayList<SpinnerModel>mBranchList=new ArrayList();
    ArrayList<String>branchList=new ArrayList();
    LinearLayout llDept;
    Spinner spDept;
    ArrayList<SpinnerModel>mDeptList=new ArrayList();
    ArrayList<String>deptList=new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_tree_list);
        initView();
        setBranch();
        onClick();

    }
    private void initView(){
        pref=new Pref(getApplicationContext());
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(TourEmployeeTreeListActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        lebelId=getIntent().getStringExtra("lebelId");
        if (pref.getArraySize()>1) {
            deptId="0";
        }else {
            deptId = getIntent().getStringExtra("deptId");
        }
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        spBranch=(Spinner)findViewById(R.id.spBranch);
        llDept=(LinearLayout)findViewById(R.id.llDept);
        if (pref.getArraySize()>1){
            llDept.setVisibility(View.VISIBLE);
        }else {
            llDept.setVisibility(View.GONE);
        }
        spDept=(Spinner)findViewById(R.id.spDept);
    }

    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    String branchId = mBranchList.get(position).getItemId();
                    getItem(branchId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    deptId = mDeptList.get(position).getItemId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDept() {
        String surl = APi.sManageEmployeeHierarchyApi+"EmployeeID="+pref.getSecureEmpId();
        Log.d("compurl", surl);
        final ProgressDialog pd=new ProgressDialog(TourEmployeeTreeListActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        deptList.clear();
                        mDeptList.clear();
                        deptList.add("Please select branch");
                        mDeptList.add(new SpinnerModel("0","0"));


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i =0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String DepartmentID = obj.optString("DepartmentID");
                                    String Employee = obj.optString("Employee");
                                    deptList.add(Employee);
                                    SpinnerModel mainDocModule = new SpinnerModel(Employee, DepartmentID);
                                    mDeptList.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TourEmployeeTreeListActivity.this, android.R.layout.simple_spinner_item,
                                                deptList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spDept.setAdapter(spinnerArrayAdapter);



                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TourEmployeeTreeListActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+
                Log.e("ert", error.toString());
                showAlert();
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


    }
    private void setBranch() {
        String surl = APi.sUrl+"Utility/GetAllBranch";
        Log.d("compurl", surl);
        final ProgressDialog pd=new ProgressDialog(TourEmployeeTreeListActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        branchList.add("Please select Department");
                        mBranchList.add(new SpinnerModel("0","0"));


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i =0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String BranchName = obj.optString("BranchName");
                                    String BranchID = obj.optString("BranchID");
                                    branchList.add(BranchName);
                                    SpinnerModel mainDocModule = new SpinnerModel(BranchName, BranchID);
                                    mBranchList.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TourEmployeeTreeListActivity.this, android.R.layout.simple_spinner_item,
                                                branchList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spBranch.setAdapter(spinnerArrayAdapter);
                                setDept();



                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TourEmployeeTreeListActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void getItem(String branchId) {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        String st_url=APi.sHierarchyListApi+"EmployeeID="+pref.getSecureEmpId()+"&LevelID="+lebelId+"&DeptID="+pref.getDeptId()+"&BrID="+branchId;
       // String surl = "https://cloud.geniusconsultant.com/GeniusESS/API/ManageEmployee/HierarchyList?EmployeeID="+pref.getEmpId()+"&LevelID="+lebelId+"&DeptID="+pref.getDeptId();
        Log.d("reporturl", st_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, st_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        itemList.clear();

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
                                    String Employee=obj.optString("Employee");
                                    String EmployeeID=obj.optString("EmployeeID");
                                    EmployeeListModel aModel = new EmployeeListModel(Employee,EmployeeID);
                                    itemList.add(aModel);


                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);


                                setAdapter();


                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);


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

                Toast.makeText(TourEmployeeTreeListActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setAdapter(){
        TourEmployeeListAdapter eAdapter=new TourEmployeeListAdapter(itemList, TourEmployeeTreeListActivity.this);
        rvItem.setAdapter(eAdapter);
    }


    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("somthing went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        setBranch();
                    }
                });


    }
}
