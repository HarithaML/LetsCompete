package com.example.letscompete.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.letscompete.R;

public class MainActivity extends AppCompatActivity {
    Button mRegisterBtn,mLoginBtn;
    ViewFlipper v_flipper;


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

        mRegisterBtn.setOnClickListener(v -> {
                // start RegisterActivity
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
        mLoginBtn.setOnClickListener(v -> {
                // start LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });


        int []images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4,R.drawable.slide5,R.drawable.slide6};

        v_flipper = findViewById(R.id.logo);


        for (int image:images){
            filpperImages(image);
        }
    }

    public void filpperImages (int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(3000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }


}







