package com.indiev.chuonnathkhmerdictionary.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.preferences.PreferenceFragment;

public class SettingsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        FrameLayout parent = new FrameLayout(this);
        parent.setId(getResources().getInteger(R.integer.frameId));
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        parent.setLayoutParams(params);
        int padding = getResources().getInteger(R.integer.padding);
        parent.setPadding(padding, padding, padding, padding);
        setContentView(parent);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(getResources().getInteger(R.integer.frameId), new SettingFragment(), "TAG")
                    .commit();
        else
            getSupportFragmentManager().findFragmentByTag("TAG");

    }

    private void setTitle() {

        TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.title, null);
        tv.setText(getResources().getString(R.string.settings));
        tv.setTypeface(SplashActivity.typeface);
        getSupportActionBar().setCustomView(tv);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public static class SettingFragment extends PreferenceFragment {

        public SettingFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.setting);
        }

    }
}