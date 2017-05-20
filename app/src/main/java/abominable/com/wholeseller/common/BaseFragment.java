package abominable.com.wholeseller.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import abominable.com.wholeseller.R;

/**
 * Created by shubham.srivastava on 29/04/17.
 */

public abstract class BaseFragment extends Fragment  {

  protected Context mContext;
  private ProgressDialog progress;
  protected BaseActivity mActivity;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
    mActivity = (BaseActivity) context;
  }

  public void showErrorDialog(String title, String message) {
    if (isAdded()) {
      AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
      if (title != null && !title.isEmpty())
        alertDialog.setTitle(title);
      alertDialog.setMessage(message);
      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.close), new AlertDialog.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          mActivity.onBackPressed();
        }
      });
      alertDialog.setCancelable(false);
      alertDialog.show();
    }
  }

  public void showErrorDialog(String title, String message, AlertDialog.OnClickListener dialogListener) {
    if (isAdded()) {
      AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
      if (title != null && !title.isEmpty())
        alertDialog.setTitle(title);
      alertDialog.setMessage(message);
      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.close), dialogListener);
      alertDialog.show();
    }
  }

  public void showErrorDialog(String title, String message, boolean isCancellable, AlertDialog.OnClickListener dialogListener) {
    if (isAdded()) {
      AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
      if (title != null && !title.isEmpty())
        alertDialog.setTitle(title);
      alertDialog.setMessage(message);
      alertDialog.setCancelable(isCancellable);
      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), dialogListener);
      alertDialog.show();
    }
  }

  public void showInfoDialog(String title, String message) {
    if (isAdded()) {
      AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
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

  public void showProgress(String message, boolean isCancelable) {
    if (isAdded()) {
      if (progress != null && progress.isShowing()) {
        progress.dismiss();
      }
      progress = new ProgressDialog(mContext);
      progress.setMessage(message);
      progress.show();
      progress.setCanceledOnTouchOutside(false);
      progress.setCancelable(isCancelable);
      progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
          mActivity.onBackPressed();
        }
      });
    }
  }

  public void showProgress(String message, boolean isCancelable, DialogInterface.OnCancelListener cancelListener) {
    if (isAdded()) {
      if (progress != null && progress.isShowing()) {
        progress.dismiss();
      }
      progress = new ProgressDialog(mContext);
      progress.setMessage(message);
      progress.show();
      progress.setCanceledOnTouchOutside(false);
      progress.setCancelable(isCancelable);
      progress.setOnCancelListener(cancelListener);
    }
  }


  public void hideBlockingProgress() {
    try {
      if (isAdded()) {
        if (progress != null && progress.isShowing()) {
          progress.dismiss();
        }
      }
    } catch (Exception e) {
      Utility.reportException(e);
    }
  }

  public void showSnackBar(Context context, CoordinatorLayout coordinatorLayout, String message) {
    if (!isAdded()) {
      Snackbar snackbar = Snackbar
          .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
          .setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              retry();
            }
          });
      snackbar.setActionTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
      snackbar.show();
    }
  }

  public void retry() {
    //Do nothing
  }
}

