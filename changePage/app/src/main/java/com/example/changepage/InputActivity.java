package com.example.changepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InputActivity extends AppCompatActivity {
    private EditText ipName,ipAge,ipGender;
    private Button btnSubmit;

    public static final String NAME="NAME";
    public static final String AGE="AGE";
    public static final String GENDER="GENDER";
    public static final String BUNDLE="BUNDLE";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ipName=(EditText) findViewById(R.id.ip_name);
        ipAge=(EditText) findViewById(R.id.ip_age);
        ipGender=(EditText) findViewById(R.id.ip_gender);
        btnSubmit=(Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=ipName.getText().toString();
                String age= ipAge.getText().toString();
                String gender=ipGender.getText().toString();
                byBundle(name,age,gender);
            }
        });


        }
    public void byBundle(String name,String age,String gender){
        Intent intent=new Intent(InputActivity.this,RoomActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString(NAME,name);
        bundle.putString(AGE,age);
        bundle.putString(GENDER,gender);
        intent.putExtra(BUNDLE,bundle);
        startActivity(intent);

    }
}
