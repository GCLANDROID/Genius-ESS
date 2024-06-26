package com.genius.employee.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.Color;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.adapter.CTCAdapter;
import com.genius.employee.fragment.CurrentCTCFragment;
import com.genius.employee.fragment.HistoryCTCFragment;
import com.genius.employee.fragment.PersonalFragment;
import com.genius.employee.model.CTCModel;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CuurentCTCActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<CTCModel> ctcList = new ArrayList();
    LinearLayout llLoader, llMain;
    Pref pref;
    LinearLayout llNoData;
    ImageView imgBack,imgHome;
    LinearLayout llCoonection;
    String WebURL;
    LinearLayout llCurrent,llHistory;
    ImageView imgCurrent,imgHistory;
    TextView tvCurrent,tvHistory;
    TextView tvCTC;
    private static int CODE_AUTHENTICATION_VERIFICATION=241;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctc);
        initialize();

        onClick();
    }

    private void initialize() {
       /* pref = new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(CuurentCTCActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(layoutManager);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);*/

        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        if(km.isKeyguardSecure()) {

            Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "password");
            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION);
        }
        else {
            Toast.makeText(this, "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
        }
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llCurrent=(LinearLayout)findViewById(R.id.llCurrent);
        llHistory=(LinearLayout)findViewById(R.id.llHistory);

        tvHistory=(TextView)findViewById(R.id.tvHistory);
        tvCurrent=(TextView)findViewById(R.id.tvCurrent);

        imgHistory=(ImageView)findViewById(R.id.imgHistory);
        imgCurrent=(ImageView)findViewById(R.id.imgCurrent);

        tvCTC=(TextView)findViewById(R.id.tvCTC);

        loadCurrentFragment();

    }



    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        llCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCurrentFragment();
            }
        });
        llHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHistoryFragment();
            }
        });
    }



    public void loadCurrentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CurrentCTCFragment pfragment=new CurrentCTCFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        imgCurrent.setImageResource(R.drawable.currentctc);
        imgHistory.setImageResource(R.drawable.historyctc1);
        tvCurrent.setTextColor(Color.parseColor("#085A96"));
        tvHistory.setTextColor(Color.parseColor("#30B3A3"));
        tvCTC.setText("Current CTC");
        //tvHeader.setText("Personal");
    }

    public void loadHistoryFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HistoryCTCFragment htfragment=new HistoryCTCFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();
        imgCurrent.setImageResource(R.drawable.currentctc1);
        imgHistory.setImageResource(R.drawable.historyctc);
        tvHistory.setTextColor(Color.parseColor("#085A96"));
        tvCurrent.setTextColor(Color.parseColor("#30B3A3"));
        tvCTC.setText("CTC History");
        //tvHeader.setText("Personal");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==CODE_AUTHENTICATION_VERIFICATION)
        {
            // Toast.makeText(this, "Success: Verified user's identity", Toast.LENGTH_SHORT).show();
        }
        else
        {
            onBackPressed();
        }
    }
}
