package com.genius.employee.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.FamilyModel;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {
    ArrayList<AttendanceModel>attendanceList=new ArrayList();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

       myViewHolder.tvWorkingDays.setText(attendanceList.get(i).getWorkingdays());
        myViewHolder.tvAttendance.setText(attendanceList.get(i).getAttendance());
        myViewHolder.tvPayable.setText(attendanceList.get(i).getPayable());
        myViewHolder.tvAbsent.setText(attendanceList.get(i).getAbsent());
        myViewHolder.tvOtherAbsent.setText(attendanceList.get(i).getOtherAbsent());
        myViewHolder.tvLS.setText(attendanceList.get(i).getLeaveSanction());
        myViewHolder.tvOLS.setText(attendanceList.get(i).getOtherLaveSanction());
        myViewHolder.tvLWP.setText(attendanceList.get(i).getLwp());
        myViewHolder.tvOLWP.setText(attendanceList.get(i).getOtherLWP());
        myViewHolder.tvMonth.setText(attendanceList.get(i).getMonthName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Uri uri = Uri.parse(attendanceList.get(i).getMonthView()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/

            }
        });


    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvWorkingDays,tvAttendance,tvPayable,tvAbsent,tvOtherAbsent,tvLS,tvOLS,tvLWP,tvOLWP,tvMonth;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWorkingDays=(TextView)itemView.findViewById(R.id.tvWorkingDays);
            tvAttendance=(TextView)itemView.findViewById(R.id.tvAttendance);
            tvPayable=(TextView)itemView.findViewById(R.id.tvPayable);
            tvAbsent=(TextView)itemView.findViewById(R.id.tvAbsent);
            tvOtherAbsent=(TextView)itemView.findViewById(R.id.tvOtherAbsent);
            tvLS=(TextView)itemView.findViewById(R.id.tvLS);
            tvOLS=(TextView)itemView.findViewById(R.id.tvOLS);
            tvLWP=(TextView)itemView.findViewById(R.id.tvLWP);
            tvOLWP=(TextView)itemView.findViewById(R.id.tvOLWP);
            tvMonth=(TextView)itemView.findViewById(R.id.tvMonth);
        }
    }

    public AttendanceAdapter(ArrayList<AttendanceModel> attendanceList, Context context) {
        this.attendanceList = attendanceList;
        this.context = context;
    }
}
