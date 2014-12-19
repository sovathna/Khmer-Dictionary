package com.indiev.chuonnathkhmerdictionary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.adapter.MainListAdapter;
import com.indiev.chuonnathkhmerdictionary.sqlitehelper.MyLocalDB;

import java.util.ArrayList;

public class HistoryActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> mWords;
    private MyLocalDB localDB;
    private MainListAdapter adapter;
    private int pos;
    private TextView textViewEmpty;
    private TextView textViewDef;
    private TextView textViewWord;

    private OnClearListener onClearListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.history));
        setContentView(R.layout.activity_history);

        listView = (ListView) findViewById(R.id.listView);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        textViewEmpty.setTypeface(SplashActivity.typeface);
        localDB = new MyLocalDB(this);
        if (savedInstanceState == null) {
            mWords = localDB.getWords("history");
        } else {
            mWords = savedInstanceState.getStringArrayList("WORDS");
            pos = savedInstanceState.getInt("POSITION", pos);
        }

        adapter = new MainListAdapter(this, mWords);

        onClearListener = adapter;

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        textViewDef = (TextView) findViewById(R.id.textViewDef);
        textViewWord = (TextView) findViewById(R.id.textViewWord);
        if (mWords.size() > 0) {
            if (textViewDef != null) {
                textViewWord.setTypeface(SplashActivity.typeface);
                textViewDef.setTypeface(SplashActivity.typeface);
                if (savedInstanceState != null) {
                    String def = localDB.getDefByWord("history", mWords.get(pos));
                    def = def.replace("/a", "");
                    def = def.replace("\\n","<br/>");
                    textViewDef.setText(Html.fromHtml(def));
                    textViewWord.setText(mWords.get(pos));
                }
            }
        } else {
            textViewEmpty.setVisibility(View.VISIBLE);
        }

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mWords != null)
            outState.putStringArrayList("WORDS", mWords);
        outState.putInt("POSITION", pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_clear) {
            if (mWords.size() > 0)
                if (localDB.clear()) {
                    textViewEmpty.setVisibility(View.VISIBLE);

                    if (onClearListener != null)
                        onClearListener.onClick();
                    mWords = null;
                    mWords = new ArrayList<>();
                }
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pos = position;
        if (findViewById(R.id.textViewDef) == null) {
            Intent intent = new Intent(HistoryActivity.this, DefinitionActivity.class);
            intent.putExtra("WORD", mWords.get(position));
            startActivity(intent);
        } else {
            if (textViewDef != null) {
                String def = localDB.getDefByWord("history", mWords.get(position));
                def = def.replace("/a", "");
                textViewDef.setText(Html.fromHtml(def));
                textViewWord.setText(mWords.get(position));
            }
        }
    }

    @Override
    protected void onDestroy() {
        localDB.close();
        super.onDestroy();
    }

    public interface OnClearListener {
        public void onClick();
    }
}
