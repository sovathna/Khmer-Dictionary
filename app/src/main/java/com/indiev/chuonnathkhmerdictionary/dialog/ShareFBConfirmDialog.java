package com.indiev.chuonnathkhmerdictionary.dialog;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.activity.MainActivity;

/**
 * Created by sovathna on 12/13/14.
 */
public class ShareFBConfirmDialog extends DialogFragment implements View.OnClickListener {
    private Button buttonRetry;
    private Button buttonCancel;
    private Typeface typeface;
    private OnOptionClick onOptionClick;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        View view = inflater.inflate(R.layout.dialog_connection, container, false);

        buttonRetry = (Button) view.findViewById(R.id.buttonRetry);
        buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        buttonRetry.setText(getResources().getString(R.string.share_confirm));
        buttonCancel.setText(getResources().getString(R.string.share_cancel));

        buttonRetry.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        typeface = Typeface.createFromAsset(
                getActivity().getAssets(),
                getActivity().getResources().getString(R.string.font_path_main)
        );

        TextView textView = (TextView) view.findViewById(R.id.text1);
        textView.setText(getResources().getString(R.string.share_title));
        textView.setTypeface(typeface);
        textView = (TextView) view.findViewById(R.id.text2);
        textView.setText(getResources().getString(R.string.share_text));

        textView.setTypeface(typeface);
        buttonRetry.setTypeface(typeface);
        buttonCancel.setTypeface(typeface);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments().getBoolean("ISMAIN",false))
            onOptionClick = (OnOptionClick) getActivity().getSupportFragmentManager().findFragmentByTag("MAIN");
        else
            onOptionClick = (OnOptionClick) getActivity();
    }

    @Override
    public void onClick(View v) {
        if (onOptionClick != null) {
            onOptionClick.onClick(v);
        }
    }

    public interface OnOptionClick {
        public void onClick(View v);
    }
}
