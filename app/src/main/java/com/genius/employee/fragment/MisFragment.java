package com.genius.employee.fragment;


import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisFragment extends Fragment {


   View v;
   TextView tvAccNo,tvEsiNo,tvPf,tvPan;
    LinearLayout llLoader;
    Pref pref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_mis, container, false);
        initialize();
        getProfile();
        return v;
    }

    private void initialize(){
        pref=new Pref(getContext());
        tvAccNo=(TextView)v.findViewById(R.id.tvAccNo);
        tvEsiNo=(TextView)v.findViewById(R.id.tvEsiNo);
        tvPf=(TextView)v.findViewById(R.id.tvPf);
        tvPan=(TextView)v.findViewById(R.id.tvPan);
        llLoader=(LinearLayout)v.findViewById(R.id.llLoader);
    }


    private void getProfile() {
        llLoader.setVisibility(View.VISIBLE);

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
                                JSONObject responseData = job1.optJSONObject("responseData");
                                String AccountNo=responseData.optString("AccountNo");
                                if (AccountNo.equals("null")||AccountNo.equals("")){
                                    tvAccNo.setText("N/A");
                                }else {
                                    tvAccNo.setText(AccountNo);
                                }
                                String PFNO=responseData.optString("PFNO");
                                if (PFNO.equals("null")||PFNO.equals("")){
                                    tvPf.setText("N/A");
                                }else {
                                    tvPf.setText(PFNO);
                                }
                                String ESINO=responseData.optString("ESINO");
                                if (ESINO.equals("null")||ESINO.equals("")){
                                    tvEsiNo.setText("N/A");
                                }else {
                                    tvEsiNo.setText(ESINO);
                                }
                                String PanNo=responseData.optString("PanNo");
                                if (PanNo.equals("null")||PanNo.equals("")){
                                    tvPan.setText("N/A");
                                }else {
                                    tvPan.setText(PanNo);
                                }


                                llLoader.setVisibility(View.GONE);



                            } else {
                                 llLoader.setVisibility(View.GONE);
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
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

}
