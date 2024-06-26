package com.genius.employee.activity;

import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.employee.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PDFViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        PDFView pdfView=(PDFView)findViewById(R.id.pdfView);
        pdfView.fromUri(Uri.parse("/storage/emulated/0/Download/Flexym.pdf"))
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .spacing(0)
                .load();
    }
}