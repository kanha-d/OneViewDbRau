package com.example.studentsdata;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URLEncoder;

public class ViewPDFActivity extends AppCompatActivity {

    WebView pdfView;

    WebView printWeb;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdfactivity);

        pdfView = findViewById(R.id.viewPdf);
        pdfView.getSettings().setJavaScriptEnabled(true);

        String sName = getIntent().getStringExtra("sName");
        String sRollNo = getIntent().getStringExtra("sRollNo");
        String fileName = getIntent().getStringExtra("fileName");

        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle(sName+" "+sRollNo);
        pd.setMessage("Opening...");

        pdfView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        String url = "";
        try{
            url = URLEncoder.encode(fileName,"UTF-8");
        }catch (Exception ex){}

        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);

        printWeb = pdfView;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.download_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.download:
                if (printWeb != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        PrintTheWebPage(printWeb);
                    } else {
                        Toast.makeText(getApplicationContext(), "Not available for device below Android LOLLIPOP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
                }


                break;
            default:
                Toast.makeText(getApplicationContext(),"No option",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);

    }

    PrintJob printJob;

    boolean printBtnPressed = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void PrintTheWebPage(WebView webView) {
        printBtnPressed = true;
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " webpage" + webView.getUrl();

        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (printJob != null && printBtnPressed) {
            if (printJob.isCompleted()) {
                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
            } else if (printJob.isStarted()) {
                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show();

            } else if (printJob.isBlocked()) {
                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show();

            } else if (printJob.isCancelled()) {
                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show();

            } else if (printJob.isFailed()) {
                Toast.makeText(this, "isFailed", Toast.LENGTH_SHORT).show();

            } else if (printJob.isQueued()) {
                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show();

            }
            printBtnPressed = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}