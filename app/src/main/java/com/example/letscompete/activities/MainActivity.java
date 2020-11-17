package com.example.letscompete.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.letscompete.R;

public class MainActivity extends AppCompatActivity {
    Button mRegisterBtn,mLoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                something();
            }
        });
*/
        //init views
        mRegisterBtn = findViewById(R.id.register_btn);
        mLoginBtn = findViewById(R.id.login_btn);

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // start RegisterActivity
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
    public void something() {
        Intent intent = new Intent(this, Setting_Activity.class);
        startActivity(intent);
    }
}







