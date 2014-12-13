package com.indiev.chuonnathkhmerdictionary.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.sqlitehelper.MyDB;

/**
 * Created by sovathna on 12/7/14.
 */
public class DefinitionActivity extends ActionBarActivity {

    private TextView textViewDef;
    private MyDB db;
    private String word;
    private String def;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        word = getIntent().getStringExtra("WORD");
        setTitle(word);
        setContentView(R.layout.activity_definition);


        textViewDef = (TextView) findViewById(R.id.textViewDef);

        textViewDef.setTypeface(SplashActivity.typeface);

        db = new MyDB(this);
        def = db.getDeftByWord(word);
        def = def.replace("/a", "");

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
