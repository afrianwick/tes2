package com.pertamina.portal.firebaseHandler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pertamina.portal.MainActivity;
import com.pertamina.portal.R;
import com.pertamina.portal.activity.LoginActivity;
import com.pertamina.portal.activity.OTPInputActivity;
import com.pertamina.portal.activity.OTPRequestActivity;
import com.pertamina.portal.activity.PinInputActivity;
import com.pertamina.portal.activity.PortalHomeActivity;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.iam.activity.MyWorklistActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sendMyNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), "", false);
    }

    private void sendMyNotification(String title, String message, String userId, boolean isChat) {

        //On click of notification it redirect to this Activity
        Intent intent;
        PendingIntent pendingIntent;

        NotificationCompat.Builder notificationBuilder;

        NotificationManager notifManager = null;

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel("PertaminaIAM");
            if (mChannel == null) {
                mChannel = new NotificationChannel("PertaminaIAM", "Test", importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            notificationBuilder = new NotificationCompat.Builder(this, "PertaminaIAM");
            intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentTitle(title)  // required
                    .setContentText(message)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notif_recieve)
                    .setTicker("PertaminaIAM")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
            intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentTitle(title)  // required
                    .setContentText(message)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notif_recieve)
                    .setTicker("PertaminaIAM")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = notificationBuilder.build();
        notifManager.notify(0, notification);
    }

    private Intent getIntent() {
        SharedPreferences pref = PrefUtils.Build(this).getPref();
        String accsTkn = pref
                .getString(Constants.KEY_ACCES_TOKEN, null);
        int authStep = pref.getInt(Constants.KEY_AUTH_STEP, 0);

        Intent intent = null;

        if (accsTkn != null) {
            switch (authStep) {
                case 2:
                    intent = new Intent(getApplicationContext(), OTPRequestActivity.class);
                    break;
                case 3:
                    intent = new Intent(getApplicationContext(), OTPInputActivity.class);
                    break;
                case 4:
                    intent = new Intent(getApplicationContext(), PinInputActivity.class);
                    break;
                default:
                    intent = new Intent(getApplicationContext(), MyWorklistActivity.class);
                    break;
            }
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }

        return intent;
    }
}
