package abominable.com.wholeseller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.location.WholeMartFetchLocationActivity;
import abominable.com.wholeseller.login.WholeMartLoginActivity;

/**
 * Created by shubham.srivastava on 24/07/16.
 */
public class SplashScreen extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_screen);

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    final ImageView logo = (ImageView) findViewById(R.id.logo);
    final TextView wholeMart = (TextView) findViewById(R.id.wholemart);
    final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
    logo.clearAnimation();
    TranslateAnimation transAnim = new TranslateAnimation(0, 0, 0, getDisplayHeight() / 3);
    transAnim.setStartOffset(500);
    transAnim.setDuration(2000);
    transAnim.setFillAfter(true);
    transAnim.setInterpolator(new BounceInterpolator());
    transAnim.setAnimationListener(new Animation.AnimationListener() {

      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            startHomeActivity();
          }
        }, 1000);
      }
    });

    slideUp.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {

      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });
    logo.startAnimation(transAnim);
    wholeMart.setAnimation(slideUp);
  }

  private void startHomeActivity() {
    String location = WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION, "");
    String userId = WholeMartApplication.getValue(Constants.UserConstants.AUTH_KEY, "");
    if (TextUtils.isEmpty(location)) {
      final Intent homeIntent = new Intent(SplashScreen.this, WholeMartFetchLocationActivity.class);
      homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(homeIntent);
      overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      finish();
    } else if (TextUtils.isEmpty(userId)) {
      final Intent homeIntent = new Intent(SplashScreen.this, WholeMartLoginActivity.class);
      homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(homeIntent);
      overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      finish();
    } else {
      final Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
      homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(homeIntent);
      overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      finish();
    }
  }

  private int getDisplayHeight() {
    return this.getResources().getDisplayMetrics().heightPixels;
  }
}
