package com.indiev.chuonnathkhmerdictionary.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.Constant;
import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.activity.MainActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by sovathna on 12/8/14.
 */
public class ExtractDict extends AsyncTask<Void, Integer, Boolean> {

    private ProgressDialog myProgressDialog;
    private Context context;

    public ExtractDict(Context context) {
        this.context = context;
        prepareDialog();
    }

    private void prepareDialog() {
        TextView title = (TextView) LayoutInflater.from(context).inflate(R.layout.progress_title, null, false);
        title.setText(context.getResources().getString(R.string.extracting));
        title.setTypeface(SplashActivity.typeface);
        myProgressDialog = new ProgressDialog(this.context);
        myProgressDialog.setCustomTitle(title);
        myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        myProgressDialog.setProgress(0);
        myProgressDialog.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Toast.makeText(context, "Extract start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result = unzip();
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        myProgressDialog.dismiss();
        if (result) {
            //Toast.makeText(context, "Extract Finished", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, MainActivity.class));
            ((SplashActivity) context).finish();
            File f = new File(Constant.ZIP_PATH);
            if (f.exists())
                f.delete();
        }
    }

    private boolean unzip() {
        OutputStream out = null;
        FileInputStream fin = null;
        try {
            out = new FileOutputStream(Constant.DB_PATH);
            fin = new FileInputStream(Constant.ZIP_PATH);
            File f = new File(Constant.ZIP_PATH);
            if (myProgressDialog != null)
                myProgressDialog.setMax((int) f.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedInputStream bin = new BufferedInputStream(fin);
        ZipInputStream zin = new ZipInputStream(bin);
        ZipEntry ze = null;
        try {

            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals("khtokh")) {
                    byte[] buffer = new byte[8192];
                    int len;
                    int extracted = 0;
                    while ((len = zin.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        extracted = len + extracted;
                        if (myProgressDialog != null)
                            myProgressDialog.setProgress(extracted);
                    }
                    out.close();
                    return true;
                }
            }
        } catch (IOException exception) {

        }
        return false;
    }
}
