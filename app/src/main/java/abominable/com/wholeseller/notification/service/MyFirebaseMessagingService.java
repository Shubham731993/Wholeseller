package abominable.com.wholeseller.notification.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());

                handleDataMessage(remoteMessage.getData());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }


    }

    private void handleDataMessage(Map<String, String> data) {

        try {
            // JSONObject data = json.getJSONObject("default");


            JSONObject notificationObject = new JSONObject();
            notificationObject.put("tagid", data.get("tagid"));
            notificationObject.put("message", data.get("message"));
            JSONObject jsonObject = new JSONObject(data.get("dataobj"));
            notificationObject.put("dataobj", jsonObject);
            Gson gson = new Gson();
            NotificationModel notificationModel = gson.fromJson(String.valueOf(notificationObject), NotificationModel.class);
//            if (notificationModel != null) {
//                ShrofileDataSource dataSource = new ShrofileDataSource(getApplicationContext());
//                dataSource.insertNotificationItem(notificationModel);
//            }
            //String title = data.getString("title");
            String message = data.get("message");
            //boolean isBackground = data.getBoolean("is_background");

            //Log.e(TAG, "imageUrl: " + imageUrl);
            //Log.e(TAG, "timestamp: " + timestamp);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
//                if (data.get("tagid").equals(String.valueOf(NotificationTags.SHOW_VIDEO_ACTIVITY))) {
                Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
                pushNotification.putExtra(Constants.PAYLOAD, notificationModel);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
//                } else {
//                    setUpNotification(notificationModel, message);
//                }
            } else {

                // app is in background, show the notification in notification tray
       /* Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.setData(Uri.parse(payload.getString("url")));*/

                setUpNotification(notificationModel, message);
                // check for image attachment
        /*if (TextUtils.isEmpty(imageUrl)) {
          showNotificationMessage(getApplicationContext(), message, resultIntent);
        } else {
          // image is present, show notification with image
          showNotificationMessageWithBigImage(getApplicationContext(),  message,  resultIntent, imageUrl);
        }*/
            }

            // }
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
