package com.indiev.chuonnathkhmerdictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.SplashActivity;

/**
 * Created by sovathna on 12/8/14.
 */
public class NavigationDrawerAdapter extends BaseAdapter {
    private String[] navigations;
    private int[] icons = {R.drawable.history, R.drawable.settings, R.drawable.info};
    private int[] colors = {R.color.blue, R.color.light_blue, R.color.cyan};
    private Context context;
    private ViewHolder viewHolder;

    public NavigationDrawerAdapter(Context context) {
        this.context = context;
        navigations = context.getResources().getStringArray(R.array.navigation);
    }

    @Override
    public int getCount() {
        return navigations.length;
    }

    @Override
    public String getItem(int position) {
        return navigations[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.navigation_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.icon = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.icon.setBackgroundColor(context.getResources().getColor(colors[position]));
        if (icons[position] > 0)
            viewHolder.icon.setImageResource(icons[position]);
        else
            viewHolder.icon.setVisibility(View.INVISIBLE);

        viewHolder.textView.setTypeface(SplashActivity.typeface);
        viewHolder.textView.setText(getItem(position));


        return convertView;
    }

    private static final class ViewHolder {
        public ImageView icon;
        public TextView textView;
    }
}
