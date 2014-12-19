package com.indiev.chuonnathkhmerdictionary.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.activity.DefinitionActivity;
import com.indiev.chuonnathkhmerdictionary.dialog.ShareFBConfirmDialog;
import com.indiev.chuonnathkhmerdictionary.fb.Facebook;
import com.indiev.chuonnathkhmerdictionary.listview.IndexableListView;
import com.indiev.chuonnathkhmerdictionary.listview.StringMatcher;
import com.indiev.chuonnathkhmerdictionary.sqlitehelper.MyDB;
import com.indiev.chuonnathkhmerdictionary.sqlitehelper.MyLocalDB;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sovathna on 11/17/14.
 */
public class MainListFragment extends Fragment implements ShareFBConfirmDialog.OnOptionClick{

    MyLocalDB localDB;
    private IndexableListView mRecyclerView;
    private ArrayList<String> mWords;
    private ArrayList<String> origins;
    private MyDB db;
    private int pos;
    private TextView textViewWord;
    private TextView textViewDef;
    private MainListAdapter adapter;
    private MenuItem itemSearch;
    private String query;
    private boolean isRestore;

    private ImageView fbShare;
    private Facebook fb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fb = new Facebook(getActivity(),savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);

        mRecyclerView = (IndexableListView) view.findViewById(R.id.recyclerView);
        db = new MyDB(getActivity());
        if (savedInstanceState == null) {
            origins = db.getWords("dict");
            Collections.sort(origins);
            mWords = origins;
        } else {
            pos = savedInstanceState.getInt("POSITION", 0);
            origins = savedInstanceState.getStringArrayList("WORDS");
            query = savedInstanceState.getString("QUERY");
            isRestore = savedInstanceState.getBoolean("RESTORE");
            if (isRestore)
                mWords = savedInstanceState.getStringArrayList("WORD");
            else {
                mWords = origins;
            }
        }

        adapter = new MainListAdapter(getActivity());

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFastScrollEnabled(true);
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                if (getActivity().findViewById(R.id.textViewDef) == null) {
                    Intent intent = new Intent(getActivity(), DefinitionActivity.class);
                    intent.putExtra("WORD", mWords.get(position));
                    startActivity(intent);
                } else {
                    if (textViewDef != null) {
                        String def = db.getDeftByWord(mWords.get(position));
                        def = def.replace("/a", "");
                        def = def.replace("\\n","<br/>");
                        textViewDef.setText(Html.fromHtml(def));
                        textViewWord.setText(mWords.get(position));
                    }
                }
                String def = DatabaseUtils.sqlEscapeString(db.getDeftByWord(mWords.get(position)));
                if (localDB == null)
                    localDB = new MyLocalDB(getActivity());
                if (localDB.insert("history", mWords.get(position), def) <= 0) {
                    Toast.makeText(getActivity(), "An error occur!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private ShareFBConfirmDialog shareFBConfirmDialog;

    private void showFBConfirmDialog(){
        shareFBConfirmDialog = new ShareFBConfirmDialog();
        Bundle args = new Bundle();
        args.putBoolean("ISMAIN",true);
        shareFBConfirmDialog.setArguments(args);
        shareFBConfirmDialog.show(getActivity().getSupportFragmentManager(),"FBCONFIRM");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewDef = (TextView) getActivity().findViewById(R.id.textViewDef);
        textViewWord = (TextView) getActivity().findViewById(R.id.textViewWord);
        fbShare = (ImageView) getActivity().findViewById(R.id.fbShare);
        if (textViewDef != null) {
            textViewWord.setTypeface(SplashActivity.typeface);
            textViewDef.setTypeface(SplashActivity.typeface);
            if (savedInstanceState != null && mWords.size() > pos) {
                String def = db.getDeftByWord(mWords.get(pos));
                def = def.replace("/a", "");
                textViewDef.setText(Html.fromHtml(def));
                textViewWord.setText(mWords.get(pos));
                fbShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFBConfirmDialog();
                    }
                });
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fb.onSaveInstanceState(outState);
        outState.putStringArrayList("WORDS", origins);
        outState.putInt("POSITION", pos);
        if (MenuItemCompat.isActionViewExpanded(itemSearch) && query.length() > 0) {
            outState.putString("QUERY", query);
            outState.putStringArrayList("WORD", mWords);
            outState.putBoolean("RESTORE", true);
        } else {
            outState.putBoolean("RESTORE", false);
        }
    }

    @Override
    public void onDestroy() {
        if (db != null)
            db.close();
        fb.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        fb.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        fb.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fb.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
        itemSearch = menu.findItem(R.id.menu_search);

        initSearchView();
    }

    private void initSearchView() {
        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(itemSearch);
        searchView.setQueryHint(getActivity().getResources().getString(R.string.search_kh));
        // searchView.setIconifiedByDefault(false);
        TextView tv = (TextView) searchView.findViewById(R.id.search_src_text);
        tv.setTypeface(SplashActivity.typeface);
        tv.setTextSize(16);
        if (isRestore) {
            MenuItemCompat.expandActionView(itemSearch);
            searchView.setQuery(query, true);
            searchView.clearFocus();
            adapter.getFilter().filter(query);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.i("Sovathna", newText);
                adapter.getFilter().filter(newText);
                query = newText;
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonRetry){
            fb.getSession(
                    textViewWord.getText().toString(),
                    textViewDef.getText().toString()
            );
        }
        shareFBConfirmDialog.dismiss();
    }

    private class MainListAdapter extends BaseAdapter implements SectionIndexer, Filterable {


        private Context context;
        private ViewHolder holder;
        private String mSections;
        private ValueFilter valueFilter;

        public MainListAdapter(Context context) {
            this.context = context;
            mSections = context.getResources().getString(R.string.alphabets);
        }

        @Override
        public int getCount() {
            return mWords.size();
        }

        @Override
        public String getItem(int position) {
            return mWords.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.textview, parent, false);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView.findViewById(R.id.textView);
                holder.mTextView.setTypeface(SplashActivity.typeface);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTextView.setText(getItem(position));

            return convertView;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[mSections.length()];
            for (int i = 0; i < mSections.length(); i++)
                sections[i] = String.valueOf(mSections.charAt(i));
            return sections;
        }

        @Override
        public int getPositionForSection(int section) {
            for (int i = section; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        // For numeric section
                        for (int k = 0; k <= 9; k++) {
                            if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k)))
                                return j;
                        }
                    } else {
                        if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(mSections.charAt(i))))
                            return j;
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        public class ViewHolder {
            public TextView mTextView;

        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                mWords = origins;
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<String> filterList = new ArrayList<String>();
                    for (int i = 0; i < mWords.size(); i++) {
                        if ((mWords.get(i))
                                .startsWith(constraint.toString())) {
                            filterList.add(mWords.get(i));
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mWords.size();
                    results.values = mWords;
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mWords = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
