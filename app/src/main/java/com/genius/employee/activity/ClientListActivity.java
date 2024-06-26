package com.genius.employee.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.adapter.ClientAdapter;
import com.genius.employee.model.ClientModule;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClientListActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<ClientModule> itemList = new ArrayList();
    ImageView imgBack;
    EditText etClientName;
    Pref pref;
    String clintName;
    String consultid;
    ImageView imgSearch;
    ImageView imgHome;
    LinearLayout llMain,llNoData,llInternet;
    NetworkConnectionCheck connectionCheck;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        initialize();
        onClick();
    }

    private void initialize() {
        pref = new Pref(ClientListActivity.this);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ClientListActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        etClientName = (EditText) findViewById(R.id.etClientName);
        consultid = pref.getEmpConId();
        Log.d("rikuuu", consultid);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        llInternet=(LinearLayout)findViewById(R.id.llInternet);
        connectionCheck=new NetworkConnectionCheck(ClientListActivity.this);




    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etClientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etClientName.getText().toString().length() > 2) {
                    clintName = etClientName.getText().toString();
                    if (pref.getDomainId().equals("ITR")||pref.getDomainId().equals("PSS")||pref.getDomainId().equals("FSSR")){
                        getClinetListForRMS();
                    }else {
                        getClinetList();
                    }

                }
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getDomainId().equals("ITR")||pref.getDomainId().equals("PSS")||pref.getDomainId().equals("FSSR")){
                    getClinetListForRMS1();
                }else {
                    getClinetList1();
                }
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClientListActivity.this,FeedBackDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void getClinetList() {
        llMain.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
        String surl = "http://111.93.182.174/GeniusiOSApi/api//gcl_CommonDDL?ddltype=12001&id1=" + consultid + "&id2=" + pref.getFeedBackMasterId() + "&id3=" + clintName + "&SecurityCode=" + pref.getDomainId();
        Log.d("clintname", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        llMain.setVisibility(View.VISIBLE);
        llInternet.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        StringRequest stringRequest = new
                StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        itemList.clear();

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
                                    String clintvalue = obj.optString("value");
                                    String id = obj.optString("id");
                                    ClientModule cmodule = new ClientModule(clintvalue, id);
                                    itemList.add(cmodule);

                                }
                                rvItem.setVisibility(View.VISIBLE);
                                ClientAdapter cAdapter = new ClientAdapter(itemList,ClientListActivity.this);
                                rvItem.setAdapter(cAdapter);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                llInternet.setVisibility(View.GONE);


                            } else {
                                rvItem.setVisibility(View.GONE);
                                itemList.clear();
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                llInternet.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ClientListActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                llMain.setVisibility(View.GONE);
                llInternet.setVisibility(View.VISIBLE);
                llNoData.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ClientListActivity.this);
        requestQueue.add(stringRequest);


    }
    private void getClinetList1() {
        String surl = "http://111.93.182.174/GeniusiOSApi/api//gcl_CommonDDL?ddltype=12001&id1=" + consultid + "&id2=" + pref.getFeedbackEmpId() + "&id3=" + etClientName.getText().toString() + "&SecurityCode=" + pref.getDomainId();
        Log.d("clintname", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        llNoData.setVisibility(View.GONE);
        llMain.setVisibility(View.VISIBLE);
        llInternet.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        itemList.clear();

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
                                    String clintvalue = obj.optString("value");
                                    String id = obj.optString("id");
                                    ClientModule cmodule = new ClientModule(clintvalue, id);
                                    itemList.add(cmodule);

                                }
                                rvItem.setVisibility(View.VISIBLE);
                                ClientAdapter cAdapter = new ClientAdapter(itemList,ClientListActivity.this);
                                rvItem.setAdapter(cAdapter);


                            } else {
                                rvItem.setVisibility(View.GONE);
                                itemList.clear();
                                llNoData.setVisibility(View.VISIBLE);
                                llMain.setVisibility(View.GONE);
                                llInternet.setVisibility(View.GONE);
                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ClientListActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                llMain.setVisibility(View.GONE);
                llInternet.setVisibility(View.VISIBLE);
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {};

        RequestQueue requestQueue = Volley.newRequestQueue(ClientListActivity.this);
        requestQueue.add(stringRequest);
    }

    //RMSCLIENT
    private void getClinetListForRMS() {
        llMain.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
        String surl = "https://cloud.geniusconsultant.com/GeniusJobsAPI/api/RMSClientFeedBack/GetClientListDe?UserName="+pref.getFeedbackEmpId()+"&ClientName="+etClientName.getText().toString()+"&SourceType="+pref.getDomainId();
        Log.d("clintname", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        llMain.setVisibility(View.VISIBLE);
        llInternet.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        StringRequest stringRequest = new
                StringRequest(Request.Method.GET, surl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("responseLogin", response);
                                progressBar.dismiss();
                                itemList.clear();

                                try {
                                    JSONObject job1 = new JSONObject(response);
                                    Log.e("response12", "@@@@@@" + job1);
                                    String responseText = job1.optString("responseText");
                                    int ResponseCode = job1.optInt("ResponseCode");
                                    if (ResponseCode==1) {
                                        //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                        JSONArray responseData = job1.optJSONArray("ResponseData");
                                        for (int i = 0; i < responseData.length(); i++) {
                                            JSONObject obj = responseData.getJSONObject(i);
                                            String clintvalue = obj.optString("RMSClientName");
                                            String id = obj.optString("RMSClientID");
                                            ClientModule cmodule = new ClientModule(clintvalue, id);
                                            itemList.add(cmodule);

                                        }
                                        rvItem.setVisibility(View.VISIBLE);
                                        ClientAdapter cAdapter = new ClientAdapter(itemList,ClientListActivity.this);
                                        rvItem.setAdapter(cAdapter);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNoData.setVisibility(View.GONE);
                                        llInternet.setVisibility(View.GONE);


                                    } else {
                                        rvItem.setVisibility(View.GONE);
                                        itemList.clear();
                                        llMain.setVisibility(View.GONE);
                                        llNoData.setVisibility(View.VISIBLE);
                                        llInternet.setVisibility(View.GONE);

                                    }

                                    // boolean _status = job1.getBoolean("status");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ClientListActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.dismiss();
                        llMain.setVisibility(View.GONE);
                        llInternet.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);

                        //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("ert", error.toString());
                    }
                }) {

                };
        RequestQueue requestQueue = Volley.newRequestQueue(ClientListActivity.this);
        requestQueue.add(stringRequest);


    }
    private void getClinetListForRMS1() {
        llMain.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
        String surl = "https://cloud.geniusconsultant.com/GeniusJobsAPI/api/RMSClientFeedBack/GetClientListDe?UserName="+pref.getFeedbackEmpId()+"&ClientName="+etClientName.getText().toString()+"&SourceType="+pref.getDomainId();
        Log.d("clintname", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        llMain.setVisibility(View.VISIBLE);
        llInternet.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        StringRequest stringRequest = new
                StringRequest(Request.Method.GET, surl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("responseLogin", response);
                                progressBar.dismiss();
                                itemList.clear();
                                try {
                                    JSONObject job1 = new JSONObject(response);
                                    Log.e("response12", "@@@@@@" + job1);
                                    String responseText = job1.optString("responseText");
                                    int ResponseCode = job1.optInt("ResponseCode");
                                    if (ResponseCode==1) {
                                        //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                        JSONArray responseData = job1.optJSONArray("ResponseData");
                                        for (int i = 0; i < responseData.length(); i++) {
                                            JSONObject obj = responseData.getJSONObject(i);
                                            String clintvalue = obj.optString("RMSClientName");
                                            String id = obj.optString("RMSClientID");
                                            ClientModule cmodule = new ClientModule(clintvalue, id);
                                            itemList.add(cmodule);
                                        }
                                        rvItem.setVisibility(View.VISIBLE);
                                        ClientAdapter cAdapter = new ClientAdapter(itemList,ClientListActivity.this);
                                        rvItem.setAdapter(cAdapter);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNoData.setVisibility(View.GONE);
                                        llInternet.setVisibility(View.GONE);
                                    } else {
                                        rvItem.setVisibility(View.GONE);
                                        itemList.clear();
                                        llMain.setVisibility(View.GONE);
                                        llNoData.setVisibility(View.VISIBLE);
                                        llInternet.setVisibility(View.GONE);
                                    }
                                    // boolean _status = job1.getBoolean("status");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ClientListActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.dismiss();
                        llMain.setVisibility(View.GONE);
                        llInternet.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                        //Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("ert", error.toString());
                    }
                }) {};
        RequestQueue requestQueue = Volley.newRequestQueue(ClientListActivity.this);
        requestQueue.add(stringRequest);
    }
}
