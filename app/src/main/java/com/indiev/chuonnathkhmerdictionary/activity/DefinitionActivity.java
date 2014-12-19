package com.indiev.chuonnathkhmerdictionary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.dialog.ShareFBConfirmDialog;
import com.indiev.chuonnathkhmerdictionary.fb.Facebook;
import com.indiev.chuonnathkhmerdictionary.sqlitehelper.MyDB;

/**
 * Created by sovathna on 12/7/14.
 */
public class DefinitionActivity extends ActionBarActivity  implements ShareFBConfirmDialog.OnOptionClick{

    private TextView textViewDef;
    private MyDB db;
    private String word;
    private String def;
    private Facebook fb;

    ShareFBConfirmDialog shareFBConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        word = getIntent().getStringExtra("WORD");
        setTitle(word);
        setContentView(R.layout.activity_definition);

        fb = new Facebook(this,savedInstanceState);
        textViewDef = (TextView) findViewById(R.id.textViewDef);

        textViewDef.setTypeface(SplashActivity.typeface);

        db = new MyDB(this);
        def = db.getDeftByWord(word);
        def = def.replace("/a", "");
        def = def.replace("\\n","<br/>");

        textViewDef.setText(Html.fromHtml(def));

    }

    private void setTitle(String word) {

        TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.title, null);
        tv.setText(word);
        tv.setTypeface(SplashActivity.typeface);
        getSupportActionBar().setCustomView(tv);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fbshare, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_fbshare:
                showConfirmDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fb.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        fb.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fb.onResume();
    }

    @Override
    protected void onDestroy() {
        fb.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fb.onSaveInstanceState(outState);
    }

    private void showConfirmDialog(){
        shareFBConfirmDialog = new ShareFBConfirmDialog();
        Bundle args = new Bundle();
        args.putBoolean("ISMAIN",false);
        shareFBConfirmDialog.setArguments(args);
        shareFBConfirmDialog.show(getSupportFragmentManager(),"FBCONFIRM");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonRetry){
            fb.getSession(word,Html.fromHtml(def).toString());
        }
        shareFBConfirmDialog.dismiss();
    }
}
