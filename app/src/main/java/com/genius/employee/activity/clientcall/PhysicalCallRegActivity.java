package com.genius.employee.activity.clientcall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.activity.clientcall.adapter.AddPhySPOCAdapter;
import com.genius.employee.activity.clientcall.model.AddSPOCModel;
import com.genius.employee.utility.APi;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;

public class PhysicalCallRegActivity extends AppCompatActivity {
    TextView tvClientTitle;
    String scheduleTypeStr,otherOption;
    EditText etClientName;
    LinearLayout llAddSPOC,llMain,llSPOCPopup,llPublic,llPrivate,llPublicSelected,llPrivateSelected,llPublicTransport,llPrivateTransport;
    RecyclerView rvSPOC;
    EditText etSPOCName,etMobile,etEmail,etDepartment,etDesignation;
    ArrayList<AddSPOCModel> spocList=new ArrayList<>();
    TextView tvSaveSPOC;
    TextView tvAddSpocTitle,tvSave;
    ImageView imgCross;
    AlertDialog alertDialog1;
    int travelFlag=0;
    Spinner spPrivate;
    ArrayList<String>privateList=new ArrayList<>();
    LinearLayout llSharingOption,llOWNShare,llOWNShareSelected,llOtherSharing,llOtherSharingSelected;
    LinearLayout llChipContainer;

    String[] publicTransportArray = {
            "Train",
            "Flight",
            "Bus",
            "Cab Service"
    };

    boolean[] selectedTransport = new boolean[publicTransportArray.length];

    ArrayList<String> selectedTransportList = new ArrayList<>();
    HorizontalScrollView spPublic;
    MaterialAutoCompleteTextView etClientAddress;

    PlacesClient placesClient;

    ArrayAdapter<String> addressAdapter;

    ArrayList<String> addressList = new ArrayList<>();
    ImageView imgBack,imgHome;
    LinearLayout llState,llStateSelected,llOutside,llOutsideSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_call_reg);
        initView();
        onClick();
    }

    private void initView(){
        if (!Places.isInitialized()) {

            Places.initialize(
                    getApplicationContext(),
                    "AIzaSyBuxUn1s4S2yv8fqwd0wGUTFegxNyASL1g"
            );
        }
        scheduleTypeStr=getIntent().getStringExtra("scheduleTypeStr");
        tvClientTitle=findViewById(R.id.tvClientTitle);
        tvClientTitle.setText("Name of the "+scheduleTypeStr);

        etClientName=findViewById(R.id.etClientName);
        otherOption=getIntent().getStringExtra("otherOption");
        if (otherOption.equalsIgnoreCase("Others.")){
            etClientName.setHint("Please specify the name of the "+scheduleTypeStr+"");
        }else {
            etClientName.setText(otherOption);
        }

        llAddSPOC=findViewById(R.id.llAddSPOC);
        llMain=findViewById(R.id.llMain);
        llSPOCPopup=findViewById(R.id.llSPOCPopup);

        rvSPOC=findViewById(R.id.rvSPOC);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(PhysicalCallRegActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSPOC.setLayoutManager(layoutManager);

        etSPOCName=findViewById(R.id.etSPOCName);
        etMobile=findViewById(R.id.etMobile);
        etEmail=findViewById(R.id.etEmail);
        etDepartment=findViewById(R.id.etDepartment);
        etDesignation=findViewById(R.id.etDesignation);

        tvAddSpocTitle=findViewById(R.id.tvAddSpocTitle);

        tvSaveSPOC=findViewById(R.id.tvSaveSPOC);

        imgCross=findViewById(R.id.imgCross);

        llPublic=findViewById(R.id.llPublic);
        llPrivate=findViewById(R.id.llPrivate);
        llSharingOption=findViewById(R.id.llSharingOption);

        llPublicSelected=findViewById(R.id.llPublicSelected);
        llPrivateSelected=findViewById(R.id.llPrivateSelected);

        llPublicTransport=findViewById(R.id.llPublicTransport);
        llPrivateTransport=findViewById(R.id.llPrivateTransport);
        spPrivate=findViewById(R.id.spPrivate);
        privateList.add("Please Select");
        privateList.add("Two Wheeler");
        privateList.add("Four Wheeler");
        ArrayAdapter<String> spinnerMonthArrayAdapter = new ArrayAdapter<String>
                (PhysicalCallRegActivity.this, android.R.layout.simple_spinner_item,
                        privateList); //selected item will look like a spinner set from XML
        spinnerMonthArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrivate.setAdapter(spinnerMonthArrayAdapter);

        llOWNShare=findViewById(R.id.llOWNShare);
        llOWNShareSelected=findViewById(R.id.llOWNShareSelected);

        llOtherSharing=findViewById(R.id.llOtherSharing);
        llOtherSharingSelected=findViewById(R.id.llOtherSharingSelected);
        llChipContainer = findViewById(R.id.llChipContainer);
        spPublic=findViewById(R.id.spPublic);
        showSelectedChips();
        etClientAddress = findViewById(R.id.etClientAddress);

        placesClient = Places.createClient(this);

        addressAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                addressList
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(android.R.id.text1);

                textView.setTextColor(getResources().getColor(android.R.color.black));

                textView.setTextSize(16);

                return view;
            }
        };

        etClientAddress.setAdapter(addressAdapter);
        etClientAddress.setDropDownBackgroundResource(android.R.color.white);

        etClientAddress.setThreshold(1);
        imgHome=findViewById(R.id.imgHome);
        imgBack=findViewById(R.id.imgBack);
        tvSave=findViewById(R.id.tvSave);

        llState=findViewById(R.id.llState);
        llStateSelected=findViewById(R.id.llStateSelected);

        llOutside=findViewById(R.id.llOutside);
        llOutsideSelected=findViewById(R.id.llOutsideSelected);

    }

    private void onClick(){
        spPrivate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    llSharingOption.setVisibility(View.VISIBLE);
                }else {
                    llSharingOption.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSPOCPopup.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);
            }
        });
        llAddSPOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSPOCPopup.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
            }
        });

        tvSaveSPOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSPOCName.getText().toString().length()>0){
                    if (etMobile.getText().toString().length()>0){
                        if (etEmail.getText().toString().length()>0){
                            AddSPOCModel model=new AddSPOCModel();
                            model.setSpocName(etSPOCName.getText().toString());
                            model.setSpocMob(etMobile.getText().toString());
                            model.setSpocEmail(etEmail.getText().toString());
                            model.setSpocDept(etDepartment.getText().toString());
                            model.setSpocDesignation(etDesignation.getText().toString());

                            spocList.add(model);

                            llSPOCPopup.setVisibility(View.GONE);
                            llMain.setVisibility(View.VISIBLE);
                            rvSPOC.setAdapter(new AddPhySPOCAdapter( spocList,PhysicalCallRegActivity.this));
                            tvAddSpocTitle.setText("Add Another SPOC Information");
                            etSPOCName.setText("");
                            etMobile.setText("");
                            etEmail.setText("");
                            etDepartment.setText("");
                            etDesignation.setText("");

                        }else {
                            etEmail.setError("Please enter SPOC Email");
                            etEmail.requestFocus();
                        }

                    }else {
                        etMobile.setError("Please enter SPOC Mobile Number");
                        etMobile.requestFocus();
                    }

                }else {
                    etSPOCName.setError("Please enter SPOC Name");
                    etSPOCName.requestFocus();
                }
            }
        });

        llPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llPrivateSelected.getVisibility()==View.GONE){
                    llPrivateSelected.setVisibility(View.VISIBLE);
                    llPublicSelected.setVisibility(View.GONE);
                    llPrivateTransport.setVisibility(View.VISIBLE);
                    llPublicTransport.setVisibility(View.GONE);
                    APi.transportMode=1;

                }
            }
        });

        llPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llPublicSelected.getVisibility()==View.GONE){
                    llPrivateSelected.setVisibility(View.GONE);
                    llPublicSelected.setVisibility(View.VISIBLE);
                    llPrivateTransport.setVisibility(View.GONE);
                    llPublicTransport.setVisibility(View.VISIBLE);
                    llSharingOption.setVisibility(View.GONE);
                    APi.transportMode=2;
                }
            }
        });


        llOWNShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llOWNShareSelected.getVisibility()==View.GONE){
                    llOtherSharingSelected.setVisibility(View.GONE);
                    llOWNShareSelected.setVisibility(View.VISIBLE);
                    APi.SharingFlag=1;

                }
            }
        });

        llOtherSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llOtherSharingSelected.getVisibility()==View.GONE){
                    llOtherSharingSelected.setVisibility(View.VISIBLE);
                    llOWNShareSelected.setVisibility(View.GONE);
                    APi.SharingFlag=2;

                }
            }
        });

        llState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llStateSelected.getVisibility()==View.GONE){
                    llStateSelected.setVisibility(View.VISIBLE);
                    llOutsideSelected.setVisibility(View.GONE);
                }
            }
        });

        llOutside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llOutsideSelected.getVisibility()==View.GONE){
                    llStateSelected.setVisibility(View.GONE);
                    llOutsideSelected.setVisibility(View.VISIBLE);
                }
            }
        });
        llPublicTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTransportPopup();
            }
        });

        spPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTransportPopup();
            }
        });
        etClientAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    etClientAddress.showDropDown();
                }
            }
        });
        etClientAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    searchAddress(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                onBackPressed();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PhysicalCallRegActivity.this, PhysicalCallVisitLogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void showSPOCDetails(int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PhysicalCallRegActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.added_spoc_details_popup, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCross=dialogView.findViewById(R.id.imgCross);
        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();

            }
        });
        TextView tvName = (TextView) dialogView.findViewById(R.id.tvName);
        tvName.setText(spocList.get(pos).getSpocName());

        TextView tvMob = (TextView) dialogView.findViewById(R.id.tvMob);
        tvMob.setText(spocList.get(pos).getSpocMob());

        TextView tvEmail = (TextView) dialogView.findViewById(R.id.tvEmail);
        tvEmail.setText(spocList.get(pos).getSpocEmail());

        TextView tvDept = (TextView) dialogView.findViewById(R.id.tvDept);
        tvDept.setText(spocList.get(pos).getSpocDept());


        TextView tvDesignation = (TextView) dialogView.findViewById(R.id.tvDesignation);
        tvDesignation.setText(spocList.get(pos).getSpocDesignation());

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();


    }

    private void showTransportPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Transport");

        builder.setMultiChoiceItems(publicTransportArray, selectedTransport,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if (isChecked) {

                            if (!selectedTransportList.contains(publicTransportArray[position])) {
                                selectedTransportList.add(publicTransportArray[position]);
                            }

                        } else {

                            selectedTransportList.remove(publicTransportArray[position]);
                        }
                    }
                });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                showSelectedChips();
                if (selectedTransportList.contains("Cab Service")) {

                    llSharingOption.setVisibility(View.VISIBLE);

                } else {

                    llSharingOption.setVisibility(View.GONE);

                    llOWNShareSelected.setVisibility(View.GONE);
                    llOtherSharingSelected.setVisibility(View.GONE);

                    APi.SharingFlag = 0;
                }
            }
        });

        builder.show();
    }

    private void showSelectedChips() {

        llChipContainer.removeAllViews();

        // If no option selected
        if (selectedTransportList.size() == 0) {

            TextView tvSelect = new TextView(this);

            tvSelect.setText("Please Select Transport");

            tvSelect.setTextSize(14);

            tvSelect.setPadding(30,20,30,20);

            tvSelect.setTextColor(getResources().getColor(android.R.color.black));

            llChipContainer.addView(tvSelect);

            return;
        }

        // Selected chips
        for (int i = 0; i < selectedTransportList.size(); i++) {

            final String value = selectedTransportList.get(i);

            TextView tvChip = new TextView(this);

            tvChip.setText(value + "  ✕");

            tvChip.setTextSize(14);

            tvChip.setPadding(35,15,35,15);

            tvChip.setTextColor(getResources().getColor(android.R.color.white));

            tvChip.setBackgroundResource(R.drawable.chip_bg);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(10,10,10,10);

            tvChip.setLayoutParams(params);

            tvChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectedTransportList.remove(value);

                    for (int j = 0; j < publicTransportArray.length; j++) {

                        if (publicTransportArray[j].equals(value)) {
                            selectedTransport[j] = false;
                        }
                    }

                    if (selectedTransportList.contains("Cab Service")) {

                        llSharingOption.setVisibility(View.VISIBLE);

                    } else {

                        llSharingOption.setVisibility(View.GONE);

                        llOWNShareSelected.setVisibility(View.GONE);
                        llOtherSharingSelected.setVisibility(View.GONE);

                        APi.SharingFlag = 0;
                    }

                    showSelectedChips();
                }
            });



            llChipContainer.addView(tvChip);
        }
    }
    private void searchAddress(String query) {

        FindAutocompletePredictionsRequest request =
                FindAutocompletePredictionsRequest.builder()
                        .setQuery(query)
                        .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {

                    addressList.clear();

                    for (AutocompletePrediction prediction :
                            response.getAutocompletePredictions()) {

                        addressList.add(
                                prediction.getFullText(null).toString()
                        );
                    }

                    addressAdapter.notifyDataSetChanged();

                    etClientAddress.post(new Runnable() {
                        @Override
                        public void run() {
                            etClientAddress.showDropDown();
                        }
                    });

                });
    }
}