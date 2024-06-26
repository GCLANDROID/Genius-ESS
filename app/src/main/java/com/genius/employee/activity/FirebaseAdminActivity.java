package com.genius.employee.activity;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.employee.R;



public class FirebaseAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_admin);
        Button btnShow=(Button)findViewById(R.id.btnShow);
        Button btnBlock=(Button)findViewById(R.id.btnBlock);


    }



}