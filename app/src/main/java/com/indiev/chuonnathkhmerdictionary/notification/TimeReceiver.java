package com.indiev.chuonnathkhmerdictionary.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;

import com.indiev.chuonnathkhmerdictionary.R;
import com.indiev.chuonnathkhmerdictionary.activity.MainActivity;
import com.indiev.chuonnathkhmerdictionary.sqlitehelper.MyDB;

import java.util.Calendar;

/**
 * Created by sovathna on 12/19/14.
 */
public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar now = Calendar.getInstance();
        int dayOfWeek = now.get(Calendar.DATE);
        MyDB db = new MyDB(context);
        String res = Html.fromHtml(db.getWordOfTheDay()).toString();
        res = res.replace("/a", "");
        res = res.replace("\\n","<br/>");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getResources().getString(R.string.app_name_kh))
                        .setContentText(res)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(res))
                        .setDefaults(NotificationCompat.DEFAULT_ALL);
        db.close();
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
