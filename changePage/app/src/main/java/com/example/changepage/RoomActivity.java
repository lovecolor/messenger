package com.example.changepage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RoomActivity extends AppCompatActivity {
    private TextView txtName,txtAge,txtGender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        txtName=(TextView) findViewById(R.id.txt_name);
        txtAge=(TextView) findViewById(R.id.txt_age);
        txtGender=(TextView) findViewById(R.id.txt_gender);

        setDataByBundle();
    }

    public void setDataByBundle(){
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra(InputActivity.BUNDLE);
        String name=bundle.getString(InputActivity.NAME);
        String age=bundle.getString(InputActivity.AGE);
        String gender=bundle.getString(InputActivity.GENDER);

        txtName.setText(name);
        txtAge.setText(age);
        txtGender.setText(gender);
    }
}
