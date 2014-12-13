package com.indiev.chuonnathkhmerdictionary.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.Constant;
import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sovathna on 12/8/14.
 */
public class DownloadDict extends AsyncTask<Void, Integer, Boolean> {

    private Context context;
    private ProgressDialog myProgressDialog;

    public DownloadDict(Context context) {
        this.context = context;
        prepareDialog();

    }

    private void prepareDialog() {
        TextView title = (TextView) LayoutInflater.from(context).inflate(R.layout.progress_title, null, false);
        title.setText(context.getResources().getString(R.string.downloading));
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
        //Toast.makeText(context, "Download Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (myProgressDialog != null)
            myProgressDialog.setProgress(values[0]);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result = download();
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        myProgressDialog.dismiss();
        if (result) {
            //Toast.makeText(context, "Download Finished", Toast.LENGTH_SHORT).show();
            new ExtractDict(context).execute();
        }
    }

    private boolean download() {
        try {

            URL url = new URL(Constant.DOWNLOAD_PATH);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();


            File db = new File(Constant.DB_DIR);
            if (!db.exists())
                db.mkdirs();

            File file = new File(Constant.ZIP_PATH);


            FileOutputStream fileOutput = new FileOutputStream(file);


            InputStream inputStream = urlConnection.getInputStream();


            int totalSize = urlConnection.getContentLength();

            if (myProgressDialog != null)
                myProgressDialog.setMax(totalSize);

            int downloadedSize = 0;

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //now, read through the input buffer and write the contents to the file
            while ((bufferLength = inputStream.read(buffer)) > 0) {

                fileOutput.write(buffer, 0, bufferLength);

                downloadedSize += bufferLength;

                publishProgress(downloadedSize);
            }

            fileOutput.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}