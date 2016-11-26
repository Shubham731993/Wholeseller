package abominable.com.wholeseller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.home.WholeSellerHomeActivity;
import abominable.com.wholeseller.location.WholeMartFetchLocationActivity;
import abominable.com.wholeseller.login.WholeMartLoginActivity;

/**
 * Created by shubham.srivastava on 24/07/16.
 */
public class SplashScreen extends Activity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_screen);


    final Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
    final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

    final TextView lowerText= (TextView) findViewById(R.id.mart);
    new Handler().postDelayed(new Runnable() {
     @Override
     public void run() {
     /*  lowerText.startAnimation(slideUp);
       upperText.startAnimation(slideDown);*/
       startHomeActivity();
     }
   }, 500);
  }


  private void launchHomeScreen() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (!isFinishing())
          startHomeActivity();
      }
    }, 2500);
  }

  private void startHomeActivity() {
    String location=WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION,"");
    String userId=WholeMartApplication.getValue(Constants.UserConstants.AUTH_KEY,"");
    if(TextUtils.isEmpty(location)){
      final Intent homeIntent = new Intent(SplashScreen.this, WholeMartFetchLocationActivity.class);
      homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(homeIntent);
      overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      finish();
    }else if(TextUtils.isEmpty(userId)){
      final Intent homeIntent = new Intent(SplashScreen.this, WholeMartLoginActivity.class);
      homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(homeIntent);
      overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      finish();
    }else {
      final Intent homeIntent = new Intent(SplashScreen.this, WholeSellerHomeActivity.class);
      homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(homeIntent);
      overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      finish();
    }
  }
}
