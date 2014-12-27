package com.indiev.chuonnathkhmerdictionary.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.Constant;
import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.fragment.MainListFragment;
import com.indiev.chuonnathkhmerdictionary.fragment.NavigationDrawerFragment;
import com.indiev.chuonnathkhmerdictionary.notification.TimeReceiver;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainListFragment(), "MAIN").commit();
        else
            getSupportFragmentManager().findFragmentByTag("MAIN");
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if(savedInstanceState==null) {
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            boolean isFistTime = sp.getBoolean(Constant.ISFIRSTTIME, false);
            if (!isFistTime) {
                handleNotification();
                sp.edit().putBoolean(Constant.ISFIRSTTIME, true).commit();
            }
        }
    }

    private void handleNotification() {
        Intent alarmIntent = new Intent(this, TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 11);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, HistoryActivity.class);
                break;
            case 1:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case 2:
                intent = new Intent(this, OtherActivity.class);
                break;
            default:
                break;

        }
        if (intent != null)
            startActivity(intent);
    }

    private void setTitle() {

        TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.title, null);
        tv.setText(getResources().getString(R.string.app_name_kh));
        tv.setTypeface(SplashActivity.typeface);

        getSupportActionBar().setCustomView(tv);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else
            super.onBackPressed();
    }

}
