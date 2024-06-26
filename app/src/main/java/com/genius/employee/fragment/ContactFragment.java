package com.genius.employee.fragment;


import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.genius.employee.activity.ProfileActivity;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {


    View view;
    LinearLayout llMain, llLoader;
    Pref pref;
    TextView tvPresentAddr, tvPresentState, tvPresentCity, tvPresentPin, tvPresentCountry, tvPreAddr, tvPreState, tvPreCity, tvPrePin, tvPreCountry, tvDistance, tvPerEmail, tvOffEmail, tvMob, tvlandline;
    Button btnNext,btnPrevoius;
    int MY_SOCKET_TIMEOUT_MS = 60000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        initialize();
        getProfile();
        onClick();
        return view;
    }

    private void initialize() {
        pref = new Pref(getContext());
        llLoader = (LinearLayout) view.findViewById(R.id.llLoader);
        llMain = (LinearLayout) view.findViewById(R.id.llMain);

        tvPresentAddr = (TextView) view.findViewById(R.id.tvPresentAddr);
        tvPresentState = (TextView) view.findViewById(R.id.tvPresentState);
        tvPresentCity = (TextView) view.findViewById(R.id.tvPresentCity);
        tvPresentPin = (TextView) view.findViewById(R.id.tvPresentPin);
        tvPresentCountry = (TextView) view.findViewById(R.id.tvPresentCountry);
        tvPreAddr = (TextView) view.findViewById(R.id.tvPreAddr);
        tvPreState = (TextView) view.findViewById(R.id.tvPreState);
        tvPreCity = (TextView) view.findViewById(R.id.tvPreCity);
        tvPrePin = (TextView) view.findViewById(R.id.tvPrePin);
        tvPreCountry = (TextView) view.findViewById(R.id.tvPreCountry);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvPerEmail = (TextView) view.findViewById(R.id.tvPerEmail);
        tvOffEmail = (TextView) view.findViewById(R.id.tvOffEmail);
        tvMob = (TextView) view.findViewById(R.id.tvMob);
        tvlandline = (TextView) view.findViewById(R.id.tvlandline);

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
                                    String PermanentAddress = obj.optString("PermanentAddress");
                                    tvPreAddr.setText(PermanentAddress);
                                    String PermanentStateID = obj.optString("PermanentStateID");
                                    tvPreState.setText(PermanentStateID);
                                    String PermanentCityID = obj.optString("PermanentCityID");
                                    tvPreCity.setText(PermanentCityID);
                                    String PermanentPinCode = obj.optString("PermanentPinCode");
                                    tvPrePin.setText(PermanentPinCode);
                                    String PermanentCountryID = obj.optString("PermanentCountryID");
                                    tvPreCountry.setText(PermanentCountryID);


                                    String PresentAddress = obj.optString("PresentAddress");
                                    tvPresentAddr.setText(PresentAddress);
                                    String PresentStateID = obj.optString("PresentStateID");
                                    tvPresentState.setText(PresentStateID);
                                    String PresentCityID = obj.optString("PresentCityID");
                                    tvPresentCity.setText(PresentCityID);
                                    String PresentPincode = obj.optString("PresentPincode");
                                    tvPresentPin.setText(PresentPincode);
                                    String PresentCountryID = obj.optString("PresentCountryID");
                                    tvPresentCountry.setText(PresentCountryID);


                                    String Phone = obj.optString("Phone");
                                    if (Phone.equals("null")||Phone.equals("")){
                                        tvlandline.setText("N/A");
                                    }else {
                                        tvlandline.setText(Phone);
                                    }
                                    String Mobile = obj.optString("Mobile");
                                    tvMob.setText(Mobile);
                                    String EmailID = obj.optString("EmailID");
                                    tvOffEmail.setText(EmailID);
                                    String PersonalEmail = obj.optString("PersonalEmail");
                                    if (PersonalEmail.equals("")||PersonalEmail.equals("null")) {
                                        tvPerEmail.setText("N/A");
                                    }else {
                                        tvPerEmail.setText(PersonalEmail);
                                    }
                                    String DistanceFromOffice = obj.optString("DistanceFromOffice");
                                    tvDistance.setText(DistanceFromOffice);


                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);


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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void onClick(){



    }



}
