package com.genius.employee.fragment;


import android.os.Bundle;
/*import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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
import com.genius.employee.R;
import com.genius.employee.activity.ProfileActivity;
import com.genius.employee.adapter.FamilyAdapter;
import com.genius.employee.model.FamilyModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {

   View v;
   RecyclerView rvItem;
   ArrayList<FamilyModel>familyList=new ArrayList();
   LinearLayout llLoader,llMain,llNoData;
   Pref pref;
   Button btnNext,btnPrevious;
    int MY_SOCKET_TIMEOUT_MS = 60000;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_family, container, false);
        initialize();
        getProfile();
        onClick();
        return v;
    }

    private void initialize(){
        pref=new Pref(getContext());
        rvItem=(RecyclerView)v.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llMain=(LinearLayout)v.findViewById(R.id.llMain);
        llLoader=(LinearLayout)v.findViewById(R.id.llLoader);
        llNoData=(LinearLayout)v.findViewById(R.id.llNoData);
        btnNext=(Button)v.findViewById(R.id.btnNext);
        btnPrevious=(Button)v.findViewById(R.id.btnPrevious);


    }

    private void getProfile() {
        String surl = APi.sManageEmployeeApi+"epID=" + pref.getSecureEmpId();
        Log.d("manageinput",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responsedocumentreport", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //    Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    JSONArray lstFamily=obj.optJSONArray("lstFamily");
                                    if (lstFamily!=null) {
                                        for (int b = 0; b < lstFamily.length(); b++) {

                                            JSONObject job = lstFamily.getJSONObject(b);
                                            String MemberName = job.optString("MemberName");
                                            String RltnName = job.optString("RltnName");
                                            String Information = job.optString("Information");
                                            FamilyModel fmodel = new FamilyModel(MemberName, RltnName, Information);
                                            familyList.add(fmodel);


                                        }

                                        llLoader.setVisibility(View.GONE);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNoData.setVisibility(View.GONE);
                                        FamilyAdapter onAdapter = new FamilyAdapter(familyList);
                                        rvItem.setAdapter(onAdapter);


                                    }
                                    else {
                                        llLoader.setVisibility(View.GONE);
                                        rvItem.setVisibility(View.GONE);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNoData.setVisibility(View.VISIBLE);
                                    }


                                }





                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            // Toast.makeText(DocumentReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);


                //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));




    }

    private void onClick(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
