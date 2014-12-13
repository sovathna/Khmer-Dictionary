package com.indiev.chuonnathkhmerdictionary.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;

/**
 * Created by sovathna on 12/10/14.
 */
public class OtherActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        setContentView(R.layout.activity_other);

        TextView tv1 = (TextView) findViewById(R.id.text1);
        tv1.setTypeface(SplashActivity.typeface);
        tv1 = (TextView) findViewById(R.id.text2);
        tv1.setTypeface(SplashActivity.typeface);
        tv1 = (TextView) findViewById(R.id.text3);
        tv1.setTypeface(SplashActivity.typeface);

    }

    private void setTitle() {

        TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.title, null);
        tv.setText(getResources().getString(R.string.other));
        tv.setTypeface(SplashActivity.typeface);
        getSupportActionBar().setCustomView(tv);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
