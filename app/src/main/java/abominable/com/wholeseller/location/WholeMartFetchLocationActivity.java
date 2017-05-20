package abominable.com.wholeseller.location;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import abominable.com.wholeseller.MainActivity;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.login.WholeMartLoginActivity;


/**
 * Created by shubham.srivastava on 07/08/16.
 */
public class WholeMartFetchLocationActivity extends WholeMartLocationActivity implements View.OnClickListener {
  private TextView currentLocation;
  private TextView useCurrentLocationButton;
  private TextView enterLocationManually;
  private TextView skipText;

  @Override
  public void onLocationUpdate(boolean locationFound, String currentAddress) {
    if (locationFound) {
      currentLocation.setText(currentAddress);
      WholeMartApplication.setValue(Constants.UserConstants.USER_LOCATION, currentAddress);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          callAppropriateScreen();
        }
      }, 2000);
    } else {
      Toast.makeText(WholeMartFetchLocationActivity.this, currentAddress, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.location_retreiver);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle(getResources().getString(R.string.delivery_text));
    currentLocation = (TextView) findViewById(R.id.current_location);
    useCurrentLocationButton = (TextView) findViewById(R.id.use_current_location);
    enterLocationManually = (TextView) findViewById(R.id.enter_manual_location);
    skipText = (TextView) findViewById(R.id.skip);
    skipText.setOnClickListener(this);
    useCurrentLocationButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.use_current_location:
        startLocationUpdate();
        break;
      case R.id.skip:
        callAppropriateScreen();
        break;
    }
  }

  private void callAppropriateScreen() {
    String userId = WholeMartApplication.getValue(Constants.UserConstants.AUTH_KEY, "");
    if (TextUtils.isEmpty(userId)) {
      final Intent homeIntent = new Intent(WholeMartFetchLocationActivity.this, WholeMartLoginActivity.class);
      homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(homeIntent);
      overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      finish();
    } else {
      Intent intent = new Intent(WholeMartFetchLocationActivity.this, MainActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      finish();
      overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    startLocationFinding();
  }
}
