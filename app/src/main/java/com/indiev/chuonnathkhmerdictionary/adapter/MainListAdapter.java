package com.indiev.chuonnathkhmerdictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;
import com.indiev.chuonnathkhmerdictionary.activity.HistoryActivity;
import com.indiev.chuonnathkhmerdictionary.listview.StringMatcher;

import java.util.ArrayList;

/**
 * Created by sovathna on 11/17/14.
 */
public class MainListAdapter extends BaseAdapter implements SectionIndexer, Filterable, HistoryActivity.OnClearListener {

    private ArrayList<String> words;
    private ArrayList<String> origins;
    private Context context;
    private ViewHolder holder;
    private String mSections;
    private ValueFilter valueFilter;

    public MainListAdapter(Context context, ArrayList<String> words) {
        this.words = words;
        this.origins = words;
        this.context = context;
        mSections = context.getResources().getString(R.string.alphabets);
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public String getItem(int position) {
        return words.get(position);
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

    @Override
    public void onClick() {
        //Log.i("Sovathna", "Click");
        words.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView mTextView;

    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            words = origins;
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filterList = new ArrayList<String>();
                for (int i = 0; i < words.size(); i++) {
                    if ((words.get(i))
                            .startsWith(constraint.toString())) {
                        filterList.add(words.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = words.size();
                results.values = words;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            words = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }

}
