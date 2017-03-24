package abominable.com.wholeseller.notification.service;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import abominable.com.wholeseller.common.Constants;

/**
 * Created by shubham.srivastava on 21/10/16.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
  private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
  private StringBuilder builder=new StringBuilder();

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    storeRegIdInPref(refreshedToken);
  /*  Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
    registrationComplete.putExtra("token", refreshedToken);
    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
  }

  private void storeRegIdInPref(String token) {
    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString("regId", token);
    editor.commit();
  }
}
