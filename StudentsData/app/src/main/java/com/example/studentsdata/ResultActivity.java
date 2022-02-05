package com.example.studentsdata;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studentsdata.Utility.DefaultResponse;
import com.example.studentsdata.Utility.Network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {


    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        t1 = findViewById(R.id.inst);
        t2 = findViewById(R.id.course);
        t3 = findViewById(R.id.name);
        t4= findViewById(R.id.roll);
        t5 = findViewById(R.id.branch);
        t6 = findViewById(R.id.enroll);
        t7 = findViewById(R.id.fatherName);
        t8 = findViewById(R.id.gender);
        t9 = findViewById(R.id.hindiName);

        fetchStudent();

    }

    public void fetchStudent()
    {
        ProgressDialog progressdialog = new ProgressDialog(ResultActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setTitle("Fetching Details");
        progressdialog.show();


        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getId(getIntent().getStringExtra("serial"));
        call.enqueue(new Callback<DefaultResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201) {

                    String data = dr.getMessage();
                    String[] st = data.split("#");

                    try {
                        t1.setText(st[0]);
                        t2.setText(st[1]);
                        t3.setText(st[2]);
                        t4.setText(st[3]);
                        t5.setText(st[4]);
                        t6.setText(st[5]);
                        t7.setText(st[6]);
                        t8.setText(st[7]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Student find with this RollNo.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResultActivity.this,SearchActivity.class));
                    finish();
                }

                progressdialog.dismiss();

            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressdialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server Down!", Toast.LENGTH_SHORT).show();

            }

        });
    }





}