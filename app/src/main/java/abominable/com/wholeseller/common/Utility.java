package abominable.com.wholeseller.common;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;

import com.crashlytics.android.Crashlytics;

import abominable.com.wholeseller.WholeMartApplication;

/**
 * Created by shubham.srivastava on 10/07/16.
 */
public class Utility {

  public static void reportException(Throwable e) {
    try {
      Crashlytics.logException(e);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static boolean internetConnected() {
    final ConnectivityManager connectivity = (ConnectivityManager) WholeMartApplication
        .getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean internetConnected = false;
    if (connectivity != null) {
      final NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (final NetworkInfo anInfo : info) {
          if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
            internetConnected = true;
          }
        }
      }
    }
    return internetConnected;
  }

  public static void showConnectDialogWithoutFinish(final Context ctx) {

    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
    alertDialogBuilder.setTitle("No Internet Connection");
    alertDialogBuilder.setMessage("Please connect to the internet")
        .setCancelable(false)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(final DialogInterface dialog, final int id) {
            dialog.cancel();
          }
        });
    final AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }

  public static Location getLocation() {
    final LocationManager locationManager = (LocationManager) WholeMartApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
    Location lastKnownLocation = null;
    try {
      if (ActivityCompat.checkSelfPermission(WholeMartApplication.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(WholeMartApplication.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation == null) {
          lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return lastKnownLocation;
      }

    } catch (final Exception e) {
      Utility.reportException(e);
    }
    return lastKnownLocation;
  }
}
