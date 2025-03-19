package com.genius.employee.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.genius.employee.activity.ProfileActivity;
import com.genius.employee.adapter.EducationAdapter;
import com.genius.employee.adapter.FamilyAdapter;
import com.genius.employee.model.EducationModel;
import com.genius.employee.model.FamilyModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EducationFragment extends Fragment {


    View v;
    Pref pref;
    RecyclerView rvItem;
    LinearLayout llMain, llLoader, llNoData;
    ArrayList<EducationModel> educationList = new ArrayList();
    Button btnPrevious;
    int MY_SOCKET_TIMEOUT_MS = 60000;
    AlertDialog eduEditDialog;
    String eduID="";
    ArrayList<String>eduList=new ArrayList<>();
    ArrayList<SpinnerModel>modelEduList=new ArrayList<>();
    LinearLayout llAddNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_education, container, false);
        initialize();
        getProfile();
        getEduType();
        onClick();
        return v;
    }

    private void initialize() {
        pref = new Pref(getContext());
        rvItem = (RecyclerView) v.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llMain = (LinearLayout) v.findViewById(R.id.llMain);
        llLoader = (LinearLayout) v.findViewById(R.id.llLoader);
        llNoData = (LinearLayout) v.findViewById(R.id.llNoData);
        btnPrevious = (Button) v.findViewById(R.id.btnPrevious);

        llAddNew=(LinearLayout) v.findViewById(R.id.llAddNew);
    }

    private void getProfile() {
        String surl = APi.sManageEmployeeApi+"epID=" + pref.getSecureEmpId();
        Log.d("manageinput", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        educationList.clear();


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
                                    JSONArray lstQualification = obj.optJSONArray("lstQualification");
                                    if (lstQualification != null) {
                                        for (int b = 0; b < lstQualification.length(); b++) {

                                            JSONObject job = lstQualification.getJSONObject(b);
                                            String QualificationName = job.optString("QualificationName");
                                            String Inst = job.optString("Inst");
                                            String Specification = job.optString("Specification");
                                            int QualificationID=job.optInt("QualificationID");
                                            String Year=job.optString("Year");
                                            String marks=job.optString("marks");
                                            EducationModel fmodel = new EducationModel(QualificationName, Specification, Inst);
                                            fmodel.setMarks(marks);
                                            fmodel.setYear(Year);
                                            fmodel.setCount(b+1);
                                            fmodel.setQualificationID(QualificationID);
                                            educationList.add(fmodel);


                                        }

                                        llLoader.setVisibility(View.GONE);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNoData.setVisibility(View.GONE);
                                        EducationAdapter onAdapter = new EducationAdapter(educationList,EducationFragment.this);
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

    private void onClick() {
        llAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eduSavePopUp();
            }
        });
    }


    public void eduEditPopUp(int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_education_edit, null);
        dialogBuilder.setView(dialogView);
        Spinner spEduType = (Spinner) dialogView.findViewById(R.id.spEduType);
        EditText etYear = (EditText) dialogView.findViewById(R.id.etYear);
        EditText etMarks = (EditText) dialogView.findViewById(R.id.etMarks);
        EditText etSpecification = (EditText) dialogView.findViewById(R.id.etSpecification);
        EditText etInst = (EditText) dialogView.findViewById(R.id.etInst);

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eduEditDialog.dismiss();
            }
        });


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        eduList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int index = eduList.indexOf(educationList.get(pos).getQualification());

        spEduType.setAdapter(spinnerArrayAdapter);
        spEduType.setSelection(index);

        spEduType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eduID = modelEduList.get(i).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etMarks.setText(educationList.get(pos).getMarks());
        etYear.setText(educationList.get(pos).getYear());
        etSpecification.setText(educationList.get(pos).getSpecification());
        etInst.setText(educationList.get(pos).getInstitute());



        TextView tvUpdate = (TextView) dialogView.findViewById(R.id.tvUpdate);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(etMarks.getText().toString().replace(".",""))<=100) {

                    eduSaveEdit(eduID, etSpecification.getText().toString(), etInst.getText().toString(), etMarks.getText().toString(), etYear.getText().toString());
                }else {
                    Toast.makeText(getContext(),"Marks can not be greater than 100",Toast.LENGTH_LONG).show();
                }

            }
        });


        eduEditDialog = dialogBuilder.create();
        eduEditDialog.setCancelable(false);
        Window window = eduEditDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        eduEditDialog.show();
    }

    public void eduSavePopUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_education_edit, null);
        dialogBuilder.setView(dialogView);
        Spinner spEduType = (Spinner) dialogView.findViewById(R.id.spEduType);
        EditText etYear = (EditText) dialogView.findViewById(R.id.etYear);
        EditText etMarks = (EditText) dialogView.findViewById(R.id.etMarks);
        EditText etSpecification = (EditText) dialogView.findViewById(R.id.etSpecification);
        EditText etInst = (EditText) dialogView.findViewById(R.id.etInst);

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eduEditDialog.dismiss();
            }
        });


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        eduList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spEduType.setAdapter(spinnerArrayAdapter);


        spEduType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eduID = modelEduList.get(i).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        TextView tvUpdate = (TextView) dialogView.findViewById(R.id.tvUpdate);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMarks.getText().toString().length()>0){
                    if (Integer.parseInt(etMarks.getText().toString().replace(".",""))<=100){
                        if (etSpecification.getText().toString().length()>0){
                            if (etInst.getText().toString().length()>0){

                                eduSaveEdit(eduID,etSpecification.getText().toString(),etInst.getText().toString(),etMarks.getText().toString(),etYear.getText().toString());


                            }else {
                                Toast.makeText(getContext(),"Please enter Institute details",Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(getContext(),"Please enter your Specification details",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(getContext(),"Marks can not be greater than 100",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getContext(),"Please Enter Marks",Toast.LENGTH_LONG).show();
                }



            }
        });


        eduEditDialog = dialogBuilder.create();
        eduEditDialog.setCancelable(false);
        Window window = eduEditDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        eduEditDialog.show();
    }



    private void getEduType() {
        String surl = APi.squalificationApi;
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
                                    String Qualification = obj.optString("Qualification");
                                    String ID = obj.optString("ID");
                                    eduList.add(Qualification);
                                    SpinnerModel model = new SpinnerModel(Qualification, ID);
                                    modelEduList.add(model);


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


    public void eduSaveEdit(String qualificationId, String specification, String institute, String marks,String passingYear) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(APi.seducationAddupdateApi)
                .addMultipartParameter("EmployeeID", pref.getSecureEmpId())
                .addMultipartParameter("qualificationId", qualificationId)
                .addMultipartParameter("specification", specification)
                .addMultipartParameter("institute", institute)
                .addMultipartParameter("marks", marks)
                .addMultipartParameter("passingYear", passingYear)
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

                        eduEditDialog.dismiss();
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
        builder.setMessage("Do you want to delete this ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteEdu(educationList.get(pos).getQualificationID());

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


    private void deleteEdu(int id) {
        String surl = APi.sdeleducationApi + "EmployeeID=" + pref.getSecureEmpId() + "&qualificationId=" + id;
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
