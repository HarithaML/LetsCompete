package com.example.letscompete;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Setting_Activity extends AppCompatActivity {

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);
        RecyclerView content = findViewById(R.id.listSettings);
        String[] main = {getString(R.string.settings0).toString(),
                getString(R.string.settings1).toString(),
                getString(R.string.settings2).toString(),
                getString(R.string.settings3).toString(),
                getString(R.string.settings4).toString()
        };
        String[] sub = {"",
                getString(R.string.settings1des).toString(),
                getString(R.string.settings2des).toString(),
                getString(R.string.settings3des).toString(),
                ""
        };

        CustomAdapter ad = new CustomAdapter(main,sub);
        content.setAdapter(ad);
        content.setLayoutManager(new LinearLayoutManager(this));

    }
}