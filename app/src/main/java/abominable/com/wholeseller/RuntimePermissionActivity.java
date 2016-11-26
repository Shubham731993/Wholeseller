package abominable.com.wholeseller;

/**
 * Created by shubham.srivastava on 07/08/16.
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import abominable.com.wholeseller.common.BaseActivity;


/**
 * Created by Mohit Gupt on 03/05/16.
 */
public abstract class RuntimePermissionActivity extends BaseActivity {
  private int mErrorString;
  private int mSnackBarLength;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    for (int permission : grantResults) {
      permissionCheck = permissionCheck + permission;
    }
    if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
      onPermissionsGranted(requestCode);
    } else {
      String errorString = "";
      try {
        errorString = getString(mErrorString);
      } catch (Exception e) {
        errorString = "Please allow us to access your contacts";
      }
      Snackbar.make(findViewById(android.R.id.content), errorString, mSnackBarLength == Snackbar.LENGTH_INDEFINITE ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG).setAction("ALLOW",
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent();
              intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
              intent.addCategory(Intent.CATEGORY_DEFAULT);
              intent.setData(Uri.parse("package:" + getPackageName()));
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
              intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
              startActivity(intent);
            }
          }).show();
      onPermissionsDenied(requestCode);
    }
  }

  public void requestAppPermissions(final String[] requestedPermissions,
                                    final int stringId, final int requestCode, final int snackBarLength) {
    mErrorString = stringId;
    mSnackBarLength = snackBarLength;
    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    boolean shouldShowRequestPermissionRationale = false;
    for (String permission : requestedPermissions) {
      permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
      shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      if (shouldShowRequestPermissionRationale) {
        Snackbar.make(findViewById(android.R.id.content), stringId,
            snackBarLength).setAction("GRANT",
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                ActivityCompat.requestPermissions(RuntimePermissionActivity.this, requestedPermissions, requestCode);
              }
            }).show();
      } else {
        ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
      }
      onPermissionsDenied(requestCode);
    } else {
      onPermissionsGranted(requestCode);
    }
  }

  public abstract void onPermissionsGranted(int requestCode);
  public abstract void onPermissionsDenied(int requestCode);
}
