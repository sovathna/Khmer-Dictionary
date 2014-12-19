package com.indiev.chuonnathkhmerdictionary.fb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.indiev.chuonnathkhmerdictionary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sovathna on 12/13/14.
 */
public class Facebook {

    private static final List<String> PERMISSIONS = new ArrayList<String>() {
        {
            add("public_profile");
        }
    };
    private static final String PUBLISH_PERMISSION = "publish_actions";
    public Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {

        }
    };
    private Context context;
    private UiLifecycleHelper uiHelper;
    private Bundle savedInstanceState;

    public Facebook(Context context, Bundle savedInstanceState) {

        this.context = context;
        this.savedInstanceState = savedInstanceState;

        init();

    }

    private Activity getActivity() {
        return (Activity) context;
    }

    private void init() {
        uiHelper = new UiLifecycleHelper(getActivity(),
                callback);
        uiHelper.onCreate(savedInstanceState);
    }


    public void shareDialog() {

        FacebookDialog.ShareDialogBuilder
                shareDialogBuilder = new FacebookDialog.ShareDialogBuilder(getActivity());

        shareDialogBuilder.setDescription("This is a sample description!");
        shareDialogBuilder.setCaption("This is a sample caption");

        FacebookDialog facebookDialog = shareDialogBuilder.build();
        uiHelper.trackPendingDialogCall(facebookDialog.present());
    }

    public void getSession(final String word, final String def) {
        Session.openActiveSession(getActivity(), true, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                shareWord(word, def);
            }
        });
    }

    public void shareWord(String word, String def) {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (session.getPermissions().contains(PUBLISH_PERMISSION)) {
                prepareShareWord(word, def);
            } else if (session.isOpened()) {
                session.requestNewPublishPermissions(
                        new Session.NewPermissionsRequest(
                                getActivity(),
                                PUBLISH_PERMISSION
                        )
                );
            }
        }
    }

    private void prepareShareWord(String word, String def) {
        Bundle params = new Bundle();
        params.putString("name",
                getActivity().getResources().getString(R.string.app_name_kh));
        params.putString("caption",
                getActivity().getResources().getString(R.string.caption));
        params.putString("description",
                getActivity().getResources().getString(R.string.description));
        params.putString("link",
                "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
        //params.putString("picture", Constant.PICTURE_URL);
        params.putString("message", word + ": " + def);
        Request.Callback callback = new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                FacebookRequestError error = response.getError();
                if(error==null) {
                    JSONObject graphResponse = response.getGraphObject()
                            .getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {
                        Log.i("Vathna", "JSON error " + e.getMessage());
                    }
                    if (postId != null) {
                        Toast.makeText(getActivity(), "Share completed!", Toast.LENGTH_LONG).show();
                        Log.d("Sovathna", "Post Successful!");
                    } else {
                        Toast.makeText(getActivity(), "Share failed! An error has occurred!", Toast.LENGTH_LONG).show();
                        Log.d("Sovathna", "Post Fail!");
                    }
                }else{
                    Toast.makeText(getActivity(), "Share failed! An error has occurred!", Toast.LENGTH_LONG).show();
                }



            }
        };
        Request request = new Request(Session.getActiveSession(), "me/feed",
                params, HttpMethod.POST, callback);

        RequestAsyncTask task = new RequestAsyncTask(request);
        task.execute();

    }

    public void getKeyHash() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.indiev.chuonnathkhmerdictionary",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void onResume() {

        uiHelper.onResume();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        uiHelper.onActivityResult(requestCode, resultCode, data);
    }


    public void onPause() {

        uiHelper.onPause();
    }


    public void onDestroy() {

        uiHelper.onDestroy();
    }


    public void onSaveInstanceState(Bundle outState) {

        uiHelper.onSaveInstanceState(outState);
    }

}
