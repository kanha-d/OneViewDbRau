package com.example.studentsdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsdata.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {


    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.search.setOnClickListener(v ->{

            Intent intent = new Intent(SearchActivity.this,ResultActivity.class);
            String number = binding.number.getText().toString();

            if(number.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please Enter The Roll Number!",Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("serial",number);
            startActivity(intent);
            binding.number.setText("");

        });
    }
}