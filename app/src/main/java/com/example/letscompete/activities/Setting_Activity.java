package com.example.letscompete.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.adapters.CustomAdapter;
import com.example.letscompete.R;

public class Setting_Activity extends AppCompatActivity {

    private TextView mTextView;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView content = findViewById(R.id.listSettings);
        String[] main = {getString(R.string.settings0),
                getString(R.string.settings1),
                getString(R.string.settings2),
                getString(R.string.settings3),
                getString(R.string.settings4)
        };
        String[] sub = {"",
                getString(R.string.settings1des),
                getString(R.string.settings2des),
                getString(R.string.settings3des),
                ""
        };

        CustomAdapter ad = new CustomAdapter(main,sub);
        content.setAdapter(ad);
        content.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}