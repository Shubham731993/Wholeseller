package abominable.com.wholeseller.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;

/**
 * Created by shubham.srivastava on 29/06/16.
 */
public abstract class WholeMartLocationActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener ,ResultCallback<LocationSettingsResult> {

  Location mLastLocation;
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;
  protected static final int REQUEST_CHECK_SETTINGS = 0x1;
  protected LocationSettingsRequest mLocationSettingsRequest;
  String lat, lon;
  private AddressResultReceiver mResultReceiver;
  private static final int LOCATION_RETRIEVER_CODE = 111;
  public final String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
  private String TAG=getClass().getSimpleName();
  private boolean mRequestingLocationUpdates;

  public abstract void onLocationUpdate(boolean addressFound,String currentAddress);

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    buildGoogleApiClient();
    createLocationRequest();
    buildLocationSettingsRequest();
  }

  private void buildLocationSettingsRequest() {
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
    builder.addLocationRequest(mLocationRequest);
    builder.setAlwaysShow(true);
    mLocationSettingsRequest = builder.build();

  }

  private void createLocationRequest() {
    mLocationRequest = LocationRequest.create();
    mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    mLocationRequest.setInterval(100); // Update location every second
  }

  @Override
  public void onConnected(Bundle bundle) {
    /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      return;
    }
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
        mGoogleApiClient);
    if (mLastLocation != null) {
      lat = String.valueOf(mLastLocation.getLatitude());
      lon = String.valueOf(mLastLocation.getLongitude());
      if (!Geocoder.isPresent()) {
        Toast.makeText(this, R.string.no_geocoder_available,
            Toast.LENGTH_LONG).show();
        return;
      }
      startIntentService();

    }*/
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onLocationChanged(Location location) {
    lat = String.valueOf(location.getLatitude());
    lon = String.valueOf(location.getLongitude());
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    buildGoogleApiClient();

  }

  synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();

  }

  @Override
  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mGoogleApiClient.disconnect();
  }

  protected void startIntentService() {
    showProgress("Please Wait...",false);
    Intent intent = new Intent(this, FetchAddressIntentService.class);
    mResultReceiver = new AddressResultReceiver(null);
    intent.putExtra(Constants.RECEIVER, mResultReceiver);
    intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
    startService(intent);
  }


  @SuppressLint("ParcelCreator")
  class AddressResultReceiver extends ResultReceiver {
    public AddressResultReceiver(Handler handler) {
      super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
      final String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
      WholeMartApplication.setValue(Constants.UserConstants.USER_LOCATION, mAddressOutput);
      hideBlockingProgress();
      if (resultCode == Constants.SUCCESS_RESULT) {

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            onLocationUpdate(true,mAddressOutput);
          }
        });
      }else {
        onLocationUpdate(false,mAddressOutput);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case LOCATION_RETRIEVER_CODE: {
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          checkLocationSettings();
        } else {
          Snackbar.make(findViewById(android.R.id.content), R.string.location, Snackbar.LENGTH_SHORT)
              .setAction("ENABLE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  final Intent settingsIntent = new Intent();
                  settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                  settingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                  settingsIntent.setData(Uri.parse("package:" + getPackageName()));
                  settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                  settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                  startActivity(settingsIntent);
                }
              }).show();
        }
        return;
      }
    }
  }

  public void startLocationFinding() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      return;
    }
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
        mGoogleApiClient);
    if (mLastLocation != null) {
      lat = String.valueOf(mLastLocation.getLatitude());
      lon = String.valueOf(mLastLocation.getLongitude());
      if (!Geocoder.isPresent()) {
        Toast.makeText(this, R.string.no_geocoder_available,
            Toast.LENGTH_LONG).show();
        return;
      }
      startIntentService();

    }
  }

  public void startLocationUpdate() {
    if (isGoogleplayservices()) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        checkLocationSettings();
      } else {
        ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, LOCATION_RETRIEVER_CODE);
      }
    }
  }

  private boolean isGoogleplayservices() {
    Integer resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    if (resultCode == ConnectionResult.SUCCESS) {
      return true;
    } else {
      GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
      return false;
    }
  }

  protected void checkLocationSettings() {
    PendingResult<LocationSettingsResult> result =
        LocationServices.SettingsApi.checkLocationSettings(
            mGoogleApiClient,
            mLocationSettingsRequest
        );
    result.setResultCallback(this);
  }

  @Override
  public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
    final Status status = locationSettingsResult.getStatus();
    switch (status.getStatusCode()) {
      case LocationSettingsStatusCodes.SUCCESS:
        startLocationFinding();
        break;
      case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
        try {
          status.startResolutionForResult(WholeMartLocationActivity.this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
          Log.i(TAG, "PendingIntent unable to execute request.");
        }
        break;
      case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
            "not created.");
        break;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
      startLocationFinding();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (mGoogleApiClient.isConnected()) {
      stopLocationUpdates();
    }
  }

  private void stopLocationUpdates() {
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
      LocationServices.FusedLocationApi.removeLocationUpdates(
          mGoogleApiClient,
          this
      ).setResultCallback(new ResultCallback<Status>() {
        @Override
        public void onResult(Status status) {
          mRequestingLocationUpdates = false;
        }
      });
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    mGoogleApiClient.disconnect();
    hideBlockingProgress();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //startLocationFinding();
    switch (requestCode) {
      case REQUEST_CHECK_SETTINGS:
        switch (resultCode) {
          case Activity.RESULT_OK:
            mRequestingLocationUpdates=true;
            startLocationFinding();
            Log.i(TAG, "User agreed to make required location settings changes.");
            break;
          case Activity.RESULT_CANCELED:
            Log.i(TAG, "User chose not to make required location settings changes.");
            break;
        }
        break;
    }
  }
}
