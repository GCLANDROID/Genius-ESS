package com.genius.employee.activity.clientcall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.activity.clientcall.adapter.PublicDocListAdapter;
import com.genius.employee.utility.APi;

import java.util.ArrayList;

public class ReachedOfficeActivity extends AppCompatActivity {
    LinearLayout llPublic,llPrivate,llPublicSelected,llPrivateSelected,llPublicTransport,llPrivateTransport;
    Spinner spPrivate;
    ArrayList<String> privateList=new ArrayList<>();
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
    RecyclerView rvDocument;
    LinearLayout llPublicReim,llPrivateReim,llTollDoc,llTollParking;
    Switch swToll,swParking;
    int tarvelMode=0;
    TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reached_office);
        initView();
        onClick();
    }

    private void initView(){
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
                (ReachedOfficeActivity.this, android.R.layout.simple_spinner_item,
                        privateList); //selected item will look like a spinner set from XML
        spinnerMonthArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrivate.setAdapter(spinnerMonthArrayAdapter);

        llOWNShare=findViewById(R.id.llOWNShare);
        llOWNShareSelected=findViewById(R.id.llOWNShareSelected);

        llOtherSharing=findViewById(R.id.llOtherSharing);
        llOtherSharingSelected=findViewById(R.id.llOtherSharingSelected);
        llChipContainer = findViewById(R.id.llChipContainer);
        spPublic=findViewById(R.id.spPublic);


        llPublicReim=findViewById(R.id.llPublicReim);
        llPrivateReim=findViewById(R.id.llPrivateReim);
        llTollDoc=findViewById(R.id.llTollDoc);
        llTollParking=findViewById(R.id.llTollParking);
        rvDocument=findViewById(R.id.rvDocument);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ReachedOfficeActivity.this, LinearLayoutManager.VERTICAL, false);
        rvDocument.setLayoutManager(layoutManager);
        PublicDocListAdapter adapter=new PublicDocListAdapter(ReachedOfficeActivity.this);
        rvDocument.setAdapter(adapter);

        swParking=findViewById(R.id.swParking);
        swToll=findViewById(R.id.swToll);
        tvSave=findViewById(R.id.tvSave);

        showSelectedChips();
    }

    private void onClick(){
        swToll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    llTollDoc.setVisibility(View.VISIBLE);
                }else {
                    llTollDoc.setVisibility(View.GONE);
                }
            }
        });

        swParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    llTollParking.setVisibility(View.VISIBLE);
                }else {
                    llTollParking.setVisibility(View.GONE);
                }
            }
        });
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

        llPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llPrivateSelected.getVisibility()==View.GONE){
                    llPrivateSelected.setVisibility(View.VISIBLE);
                    llPublicSelected.setVisibility(View.GONE);
                    llPrivateTransport.setVisibility(View.VISIBLE);
                    llPublicTransport.setVisibility(View.GONE);

                    if (llPublicReim.getVisibility()==View.VISIBLE){
                        llPublicReim.setVisibility(View.GONE);
                    }
                    //APi.transportMode=1;
                    tarvelMode=1;

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
                    tarvelMode=0;

                    if (llPrivateReim.getVisibility()==View.VISIBLE){
                        llPrivateReim.setVisibility(View.GONE);
                    }
                    //APi.transportMode=2;
                }
            }
        });


        llOWNShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llOWNShareSelected.getVisibility()==View.GONE){
                    llOtherSharingSelected.setVisibility(View.GONE);
                    llOWNShareSelected.setVisibility(View.VISIBLE);
                    llPrivateReim.setVisibility(View.VISIBLE);
                    //APi.SharingFlag=1;


                }
            }
        });

        llOtherSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llOtherSharingSelected.getVisibility()==View.GONE){
                    llOtherSharingSelected.setVisibility(View.VISIBLE);
                    llOWNShareSelected.setVisibility(View.GONE);
                    llPrivateReim.setVisibility(View.GONE);
                    llPublicReim.setVisibility(View.GONE);

                    //APi.SharingFlag=2;

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
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReachedOfficeActivity.this,ClientCallReportActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                    llPublicReim.setVisibility(View.VISIBLE);

                } else {

                    llSharingOption.setVisibility(View.GONE);

                    llOWNShareSelected.setVisibility(View.GONE);
                    llOtherSharingSelected.setVisibility(View.GONE);
                    llPublicReim.setVisibility(View.VISIBLE);


                   // APi.SharingFlag = 0;
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
            llPublicReim.setVisibility(View.GONE);
            llPrivateReim.setVisibility(View.GONE);

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
                        llPublicReim.setVisibility(View.VISIBLE);

                    } else {

                        llSharingOption.setVisibility(View.GONE);

                        llOWNShareSelected.setVisibility(View.GONE);
                        llOtherSharingSelected.setVisibility(View.GONE);
                        llPublicReim.setVisibility(View.VISIBLE);

                       // APi.SharingFlag = 0;
                    }

                    showSelectedChips();
                }
            });



            llChipContainer.addView(tvChip);
        }
    }
}