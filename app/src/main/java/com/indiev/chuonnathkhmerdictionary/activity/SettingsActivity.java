package com.indiev.chuonnathkhmerdictionary.activity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.notification.TimeReceiver;
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

    public static class SettingFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.setting);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            Log.i("Vathna", "PrefKey: "+preference.getKey());
            if (preference.getKey().equals("notification")) {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                if(checkBoxPreference.isChecked()){
                   Log.i("Vathna","True");
                    PackageManager pm  = getActivity().getPackageManager();
                    ComponentName componentName = new ComponentName(getActivity(), TimeReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }else {
                    Log.i("Vathna", "False");
                    PackageManager pm  = getActivity().getPackageManager();
                    ComponentName componentName = new ComponentName(getActivity(), TimeReceiver.class);
                    pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

    }

}