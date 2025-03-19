package com.genius.employee.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
/*import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
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
import com.genius.employee.activity.DailyActivityDashBoardActivity;
import com.genius.employee.activity.DailyInTimeActivity;
import com.genius.employee.activity.LoginActivity;
import com.genius.employee.activity.ProfileActivity;
import com.genius.employee.adapter.FamilyAdapter;
import com.genius.employee.model.FamilyModel;
import com.genius.employee.model.SpinnerModel;
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
    ArrayList<FamilyModel> familyList = new ArrayList();
    LinearLayout llLoader, llMain, llNoData;
    Pref pref;
    Button btnNext, btnPrevious;
    int MY_SOCKET_TIMEOUT_MS = 60000;
    AlertDialog familyEditDialog;
    int dependent;
    ArrayList<String> rltnList = new ArrayList<>();
    ArrayList<SpinnerModel> modelRltnList = new ArrayList<>();
    String memberID = "";
    LinearLayout llAddNew;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_family, container, false);
        initialize();
        getProfile();
        getMemeberType();
        onClick();
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
        btnNext = (Button) v.findViewById(R.id.btnNext);
        btnPrevious = (Button) v.findViewById(R.id.btnPrevious);


    }

    private void getProfile() {
        String surl = APi.sManageEmployeeApi + "epID=" + pref.getSecureEmpId();
        Log.d("manageinput", surl);
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
                                familyList.clear();
                                //    Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    JSONArray lstFamily = obj.optJSONArray("lstFamily");
                                    if (lstFamily != null) {
                                        for (int b = 0; b < lstFamily.length(); b++) {

                                            JSONObject job = lstFamily.getJSONObject(b);
                                            String MemberName = job.optString("MemberName");
                                            String RltnName = job.optString("RltnName");
                                            String Information = job.optString("Information");
                                            boolean IsDependent = job.optBoolean("IsDependent");
                                            if (IsDependent) {
                                                dependent = 1;
                                            } else {
                                                dependent = 0;
                                            }
                                            int MemberID = job.optInt("MemberID");
                                            FamilyModel fmodel = new FamilyModel(MemberName, RltnName, Information);
                                            fmodel.setDependent(dependent);
                                            fmodel.setMemberID(MemberID);
                                            fmodel.setCount(b + 1);
                                            familyList.add(fmodel);


                                        }

                                        llLoader.setVisibility(View.GONE);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNoData.setVisibility(View.GONE);
                                        FamilyAdapter onAdapter = new FamilyAdapter(familyList, FamilyFragment.this);
                                        rvItem.setAdapter(onAdapter);


                                    } else {
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
                params.put("Authorization", "Bearer " + pref.getAccessToken());
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


    private void getMemeberType() {
        String surl = APi.srelationApi;
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
                            if (responseStatus) {
                                //    Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Relation = obj.optString("Relation");
                                    String ID = obj.optString("ID");
                                    rltnList.add(Relation);
                                    SpinnerModel model = new SpinnerModel(Relation, ID);
                                    modelRltnList.add(model);


                                }


                            } else {
                                dialog.dismiss();


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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void onClick() {
        llAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familySavePopUp();
            }
        });
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

    public void familySavePopUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_family_edit, null);
        dialogBuilder.setView(dialogView);
        Spinner spMemberType = (Spinner) dialogView.findViewById(R.id.spMemberType);
        EditText etMemeberName = (EditText) dialogView.findViewById(R.id.etMemeberName);
        EditText etOccupation = (EditText) dialogView.findViewById(R.id.etOccupation);
        CheckBox ckDependent = (CheckBox) dialogView.findViewById(R.id.ckDependent);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyEditDialog.dismiss();
            }
        });


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        rltnList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spMemberType.setAdapter(spinnerArrayAdapter);


        spMemberType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                memberID = modelRltnList.get(i).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        TextView tvUpdate = (TextView) dialogView.findViewById(R.id.tvUpdate);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!memberID.equals("")) {
                    if (etMemeberName.getText().toString().length() > 0) {
                        if (etOccupation.getText().toString().length() > 0) {
                            if (ckDependent.isChecked()) {
                                familyMemberSaveEdit(memberID, etMemeberName.getText().toString(), etOccupation.getText().toString(), true);
                            } else {
                                familyMemberSaveEdit(memberID, etMemeberName.getText().toString(), etOccupation.getText().toString(), false);
                            }
                        } else {
                            Toast.makeText(getContext(), "Please Enter Member Occupation", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getContext(), "Please Enter Member Name", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please Select Member Type", Toast.LENGTH_LONG).show();
                }


            }
        });


        familyEditDialog = dialogBuilder.create();
        familyEditDialog.setCancelable(false);
        Window window = familyEditDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        familyEditDialog.show();
    }

    public void familyEditPopUp(int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_family_edit, null);
        dialogBuilder.setView(dialogView);
        Spinner spMemberType = (Spinner) dialogView.findViewById(R.id.spMemberType);
        EditText etMemeberName = (EditText) dialogView.findViewById(R.id.etMemeberName);
        EditText etOccupation = (EditText) dialogView.findViewById(R.id.etOccupation);
        CheckBox ckDependent = (CheckBox) dialogView.findViewById(R.id.ckDependent);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyEditDialog.dismiss();
            }
        });


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        rltnList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int index = rltnList.indexOf(familyList.get(pos).getRealation());

        spMemberType.setAdapter(spinnerArrayAdapter);
        spMemberType.setSelection(index);

        spMemberType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                memberID = modelRltnList.get(i).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etMemeberName.setText(familyList.get(pos).getName());
        etOccupation.setText(familyList.get(pos).getInformation());
        if (familyList.get(pos).getDependent() == 1) {
            ckDependent.setChecked(true);
        } else {
            ckDependent.setChecked(false);
        }

        TextView tvUpdate = (TextView) dialogView.findViewById(R.id.tvUpdate);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ckDependent.isChecked()) {
                    familyMemberSaveEdit(memberID, etMemeberName.getText().toString(), etOccupation.getText().toString(), true);
                } else {
                    familyMemberSaveEdit(memberID, etMemeberName.getText().toString(), etOccupation.getText().toString(), false);
                }

            }
        });


        familyEditDialog = dialogBuilder.create();
        familyEditDialog.setCancelable(false);
        Window window = familyEditDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        familyEditDialog.show();
    }

    public void familyMemberSaveEdit(String memberID, String memberName, String occupation, boolean dependent) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(APi.sfamilyAddupdateApi)
                .addMultipartParameter("EmployeeID", pref.getSecureEmpId())
                .addMultipartParameter("MemberID", memberID)
                .addMultipartParameter("Name", memberName)
                .addMultipartParameter("Occupation", occupation)
                .addMultipartParameter("Depandent", String.valueOf(dependent))
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                //.addHeaders("Content-Type: application/x-www-form-urlencoded")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        boolean responseStatus = response.optBoolean("responseStatus");

                        familyEditDialog.dismiss();
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
        builder.setMessage("Do you want to delete this family member?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteMemeber(familyList.get(pos).getMemberID());

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


    private void deleteMemeber(int memberID) {
        String surl = APi.sdelfamilyApi + "EmployeeID=" + pref.getSecureEmpId() + "&MemberID=" + memberID;
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


}
