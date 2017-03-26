package abominable.com.wholeseller.notification.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.notification.utility.NotificationModel;
import abominable.com.wholeseller.notification.utility.NotificationParser;
import abominable.com.wholeseller.notification.utility.NotificationUtils;

/**
 * Created by shubham.srivastava on 21/10/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getData().toString());
//        }

        // Check if message contains a data payload.
        if (remoteMessage.getNotification() !=null) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getNotification().toString());

            try {
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());

                handleDataMessage(remoteMessage.getNotification());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }


    }

    private void handleDataMessage(RemoteMessage.Notification data) {

        try {
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("message", data.getBody());
            Gson gson = new Gson();
            NotificationModel notificationModel = gson.fromJson(String.valueOf(notificationObject), NotificationModel.class);
            String message = data.getBody();
            setUpNotification(notificationModel, message);
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        notificationUtils.playNotificationSound();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(message, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(message, intent, imageUrl);
    }

    public void setUpNotification(NotificationModel upNotification, String message) {
        Intent intent = new Intent(getApplicationContext(), NotificationParser.class);
        intent.putExtra(Constants.PAYLOAD, upNotification);
        showNotificationMessage(getApplicationContext(), message, intent);
    }
}
