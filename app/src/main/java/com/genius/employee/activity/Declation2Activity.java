package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
/*import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.employee.R;
import com.genius.employee.adapter.DeclartionQuestionAdapter;
import com.genius.employee.adapter.DeclartionQuestionAdapter1;
import com.genius.employee.model.Declarition2Model;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Declation2Activity extends AppCompatActivity {
    RecyclerView rvItem,rvItem1;
    ArrayList<Declarition2Model>itemList=new ArrayList<Declarition2Model>();
    ArrayList<Declarition2Model>itemList1=new ArrayList<Declarition2Model>();
    DeclartionQuestionAdapter decAdapter;
    DeclartionQuestionAdapter1 dec1Adapter;
    ArrayList<String>item=new ArrayList<String>();
    ArrayList<String>item1=new ArrayList<String>();
    String ans,ans1;

    private Bitmap bitmap;
    int signFlag;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";
    private SignatureView canvasLL;
    String path;
    AlertDialog alerDialog1,alert;
    File f;
    File wallpaperDirectory;;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    private Uri imageUri;
    ImageView imgSign;
    Button btnSubmit;
    LinearLayout llAgree,llAgreeTick;
    LinearLayout llLoader;
    ScrollView scMain;
    Pref pref;
    ImageView imgBack,imgHome;
    String childbelow,noChild,leave,leaveMonth;
    String count,monthLeave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declation2);
        initView();
        onClick();

    }

    private void initView(){
        pref=new Pref(Declation2Activity.this);
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Declation2Activity.this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        rvItem.setLayoutManager(layoutManager);


        rvItem1=(RecyclerView)findViewById(R.id.rvItem1);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(Declation2Activity.this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        rvItem1.setLayoutManager(layoutManager1);
        imgSign=(ImageView)findViewById(R.id.imgSign);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);


        llAgreeTick=(LinearLayout)findViewById(R.id.llAgreeTick);
        llAgree=(LinearLayout)findViewById(R.id.llAgree);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);

        scMain=(ScrollView)findViewById(R.id.scMain);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        childbelow=getIntent().getStringExtra("childbelow");
        noChild=getIntent().getStringExtra("noChild");
        leave=getIntent().getStringExtra("leave");
        leaveMonth=getIntent().getStringExtra("leaveMonth");




    }






    private void setAdapter(){
        decAdapter=new DeclartionQuestionAdapter(itemList,Declation2Activity.this);
        rvItem.setAdapter(decAdapter);
    }

    private void setAdapter1(){
        dec1Adapter=new DeclartionQuestionAdapter1(itemList1,Declation2Activity.this);
        rvItem1.setAdapter(dec1Adapter);
    }


    public void updateStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            item.add(itemList.get(position).getQuestionid() + "-" + itemList.get(position).getAnswervalue());
        } else {
            item.remove(position);
        }
        Log.d("arpan", item.toString());
        String i = item.toString();
        String d = i.replace("[", "").replace("]", "");
        ans = d.replaceAll("\\s+", "");

        decAdapter.notifyDataSetChanged();


    }


    public void updateStatus1(int position, boolean status) {
        itemList1.get(position).setSelected(status);
        if (itemList1.get(position).isSelected() == true) {
            item1.add(itemList1.get(position).getQuestionid() + "-" + itemList1.get(position).getAnswervalue());
        } else {
            item1.remove(position);
        }
        Log.d("arpan", item1.toString());
        String i = item1.toString();
        String d = i.replace("[", "").replace("]", "");
        ans1 = d.replaceAll("\\s+", "");

        dec1Adapter.notifyDataSetChanged();


    }


    private void signatureAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Declation2Activity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) Declation2Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.signtaure_lay, null);
        dialogBuilder.setView(dialogView);

        canvasLL = (SignatureView) dialogView.findViewById(R.id.canvasLL);
        Button btnclear = (Button) dialogView.findViewById(R.id.btnclear);
        Button btnsave = (Button) dialogView.findViewById(R.id.btnsave);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasLL.clearCanvas();
                alerDialog1.dismiss();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = canvasLL.getSignatureBitmap();
                path = saveImage(bitmap);
                signFlag=1;
                alerDialog1.dismiss();

                imgSign.setImageBitmap(bitmap);
                btnSubmit.setVisibility(View.VISIBLE);

            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY /*iDyme folder*/);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh", wallpaperDirectory.toString());
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(Declation2Activity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private void onClick(){
        llAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAgreeTick.setVisibility(View.VISIBLE);
                signatureAlert();
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
                Intent intent=new Intent(Declation2Activity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.size()+itemList1.size()==item.size()+item1.size()) {

                }else {
                    Toast.makeText(Declation2Activity.this,"All Questions are mandatory",Toast.LENGTH_LONG).show();
                }
            }
        });
    }





    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Declation2Activity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk=(LinearLayout)dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();

                Intent intent=new Intent(getApplicationContext(),EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView tvSuccess=(TextView)dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);


        alert = dialogBuilder.create();
        alert.setCancelable(false);
        Window window = alert.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert.show();
    }
}
