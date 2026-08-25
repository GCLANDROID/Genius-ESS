package com.genius.employee.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.genius.employee.R;


public class ShowDialog {
    private static AlertDialog errorAlertDialog,successaAlertDialog;

    public static void showAlertDialog(Context context,String text,ResultListener resultListener) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogNew2);
        dialog.setContentView(R.layout.alert_dialog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        TextView tvError = (TextView) dialog.findViewById(R.id.tvError);
        tvError.setText(text);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //errorAlertDialog.dismiss();
                dialog.cancel();
                resultListener.onSuccess();
            }
        });
        dialog.show();
    }

    public static void onDismiss(){
        if (successaAlertDialog != null  || successaAlertDialog.isShowing()){
            successaAlertDialog.dismiss();
        }
    }

    public interface ResultListener{
        void onSuccess();
    }
}
