package com.genius.employee.fragment;


import android.os.Bundle;
/*import android.support.v4.app.Fragment;
import android.text.Html;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.genius.employee.R;
import com.genius.employee.activity.ProfileActivity;
import com.genius.employee.utility.Pref;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfficialFragment extends Fragment {


    View view;
    TextView tvSal,tvFName,tvLName,tvDept,tvBranch,tvDes,tvFun,tvDOJ,tvEmpType,tvSalutation,tvPhy,tvAadhar;
    Pref pref;

    TextView tvEmpCode;
    TextView tvAccNo,tvEsiNo,tvPf,tvPan;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_official, container, false);
        initialize();
        onClick();
        return view;
    }

    private void initialize(){
         pref=new Pref(getContext());
        String next = "<font color='#EE0000'>*</font>";


        tvFName=(TextView)view.findViewById(R.id.tvFName);
        tvFName.setText(pref.getFName());

        tvLName=(TextView)view.findViewById(R.id.tvLName);
        tvLName.setText(pref.getLName());


        tvEmpCode=(TextView)view.findViewById(R.id.tvEmpCode);

        tvEmpCode.setText(pref.getEmpId());

        tvDept=(TextView)view.findViewById(R.id.tvDept);
        tvDept.setText(pref.getDept());

        tvBranch=(TextView)view.findViewById(R.id.tvBranch);
        tvBranch.setText(pref.getBranch());

        tvDes=(TextView)view.findViewById(R.id.tvDes);
        tvDes.setText(pref.getDes());

        tvFun=(TextView)view.findViewById(R.id.tvFun);
        tvFun.setText(pref.getFunction());

        tvDOJ=(TextView)view.findViewById(R.id.tvDOJ);
        tvDOJ.setText(pref.getDOJ());

        tvEmpType=(TextView)view.findViewById(R.id.tvEmpType);
        tvEmpType.setText(pref.getEmpType());

        tvSalutation=(TextView)view.findViewById(R.id.tvSalutation);
        tvSalutation.setText(pref.getSalutation());

        tvPhy=(TextView)view.findViewById(R.id.tvPhy);
        tvPhy.setText(pref.getPhy());

        tvAccNo=(TextView)view.findViewById(R.id.tvAccNo);
        tvAccNo.setText(pref.getAccNo());
        tvEsiNo=(TextView)view.findViewById(R.id.tvEsiNo);
        tvEsiNo.setText(pref.getESICNO());
        tvPf=(TextView)view.findViewById(R.id.tvPf);
        tvPf.setText(pref.getPFNO());
        tvPan=(TextView)view.findViewById(R.id.tvPan);
        tvPan.setText(pref.getPANO());
        tvAadhar=(TextView)view.findViewById(R.id.tvAadhar);
        tvAadhar.setText(pref.getAadhar());




    }

    private void onClick(){

    }

}
