package abominable.com.wholeseller.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.Utility;

/**
 * Created by shubham.srivastava on 25/07/16.
 */
public class BaseActivity extends AppCompatActivity{

  private ProgressDialog progress;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void showProgress(String message, boolean isCancelable) {
    if (!isFinishing()) {
      if (progress != null && progress.isShowing()) {
        progress.dismiss();
      }
      progress = new ProgressDialog(this);
      progress.setMessage(message);
      progress.show();
      progress.setCanceledOnTouchOutside(false);
      progress.setCancelable(isCancelable);
      progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
          finish();
        }
      });
    }
  }


  public void hideBlockingProgress() {
    //putting this block of code in try/catch, becuz, sometimes,method doesn't give the correct status which gives illegal state exception and
    // results crashing the app
    try {
      if (!isFinishing()) {
        if (progress != null && progress.isShowing()) {
          progress.dismiss();
        }
      }
    } catch (Exception e) {
      Utility.reportException(e);
    }

  }

  public void showErrorDialog(String title, String message) {
    if (!isFinishing()) {
      AlertDialog alertDialog = new AlertDialog.Builder(this).create();
      if (title != null && !title.isEmpty())
        alertDialog.setTitle(title);
      alertDialog.setMessage(message);
      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.close), new AlertDialog.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
          finish();
        }
      });
      alertDialog.setCancelable(false);
      alertDialog.show();
    } else {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
  }

  public void showInfoDialog(String title, String message) {
    if (!isFinishing()) {
      AlertDialog alertDialog = new AlertDialog.Builder(this).create();
      if (title != null && !title.isEmpty())
        alertDialog.setTitle(title);
      alertDialog.setMessage(message);
      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.close), new AlertDialog.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      });
      alertDialog.show();
    }
  }

}
