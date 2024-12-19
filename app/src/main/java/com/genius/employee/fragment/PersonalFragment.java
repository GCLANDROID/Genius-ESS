package com.genius.employee.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.genius.employee.R;
import com.genius.employee.activity.ProfileActivity;
import com.genius.employee.utility.Pref;


import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {


    View view;
    TextView tvDOB,tvMartial,tvgender,tvBlood,tvGurdian,tvRelationship,tvIndentification;
    Pref pref;
    ImageView imgProfile;
    Button btnNext,btnPrevious;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_personal, container, false);
        initialize();
        onClick();
        return  view;
    }

    private void initialize(){
        pref=new Pref(getContext());

        tvDOB=(TextView)view.findViewById(R.id.tvDOB);
        tvDOB.setText(pref.getDOB());

        tvMartial=(TextView)view.findViewById(R.id.tvMartial);
        tvMartial.setText(pref.getMartial());

        tvgender=(TextView)view.findViewById(R.id.tvgender);
        tvgender.setText(pref.getGender());

        tvBlood=(TextView)view.findViewById(R.id.tvBlood);
        tvBlood.setText(pref.getBlood());

        tvGurdian=(TextView)view.findViewById(R.id.tvGurdian);
        tvGurdian.setText(pref.getGurdian());

        tvRelationship=(TextView)view.findViewById(R.id.tvRelationship);
        tvRelationship.setText(pref.getRelation());

        tvIndentification=(TextView)view.findViewById(R.id.tvIndentification);
        tvIndentification.setText(pref.getEmpId());

        imgProfile=(ImageView) view.findViewById(R.id.circleImage);

        String profileimage=pref.getProfileImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(profileimage, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        if (pref.getProfileImage().equals("null")) {
            imgProfile.setImageResource(R.drawable.user);
        } else {
            imgProfile.setImageBitmap(decodedImage);
        }

        btnNext=(Button)view.findViewById(R.id.btnNext);
        btnPrevious=(Button)view.findViewById(R.id.btnPrevious);
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
