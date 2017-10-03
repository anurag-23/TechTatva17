package in.tt.tt17.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.tt.tt17.R;
import in.tt.tt17.activities.SplashActivity;

/**
 * Created by anurag on 3/10/17.
 */
public class FCMMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null){
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData().get("url"));
        }
    }

    private void sendNotification(String title, String body, String url) {
        if (title == null) title = "TechTatva '17";
        if (body == null) body = "";

        Intent notificationIntent;

        if (url != null){
            notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }else{
            notificationIntent = new Intent(this, SplashActivity.class);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.tt17_white)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentIntent)
                .setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notifBuilder.build());
    }
}
