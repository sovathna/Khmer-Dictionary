package com.indiev.chuonnathkhmerdictionary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.indiev.chuonnathkhmerdictionary.activity.MainActivity;
import com.indiev.chuonnathkhmerdictionary.dialog.ConnectionDialog;
import com.indiev.chuonnathkhmerdictionary.utils.DownloadDict;
import com.indiev.chuonnathkhmerdictionary.utils.ExtractDict;

import java.io.File;


public class SplashActivity extends ActionBarActivity implements ConnectionDialog.OnOptionClick {
    public static Typeface typeface;
    private ConnectionDialog connectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(
                getAssets(),
                getResources().getString(R.string.font_path_main)
        );
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        if (!isExist(Constant.DB_PATH)) {
            if (!isExist(Constant.ZIP_PATH)) {
                if (haveNetworkConnection()) {
                    new DownloadDict(this).execute();
                } else {
                    if (connectionDialog == null)
                        connectionDialog = new ConnectionDialog();
                    connectionDialog.show(getSupportFragmentManager(), "CONNECTION");
                }
            } else {
                new ExtractDict(this).execute();
            }

        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    public boolean isExist(String filePath) {
        File f = new File(filePath);
        return f.exists();
    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRetry:
                connectionDialog.dismiss();
                init();
                break;
            case R.id.buttonCancel:
                connectionDialog.dismiss();
                finish();
                break;
            default:
                break;
        }
    }
}
