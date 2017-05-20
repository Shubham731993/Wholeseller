package abominable.com.wholeseller.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.MainActivity;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholeSellerHttpClient;

/**
 * Created by shubham.srivastava on 14/08/16.
 */
public class EnterMobileNumberPage extends BaseActivity {
  private EditText phoneNumber;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.enter_mobile_layout);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    final Button button = (Button) findViewById(R.id.button);
    final String flow=getIntent().getStringExtra(Constants.ENTER_NUMBER_FLOW);
    switch (flow){
      case Constants.EnterNumberFlow.CHANGE_NUMBER_FLOW:
        button.setText("Done");
        break;
      case Constants.EnterNumberFlow.CREATE_ACCOUNT_FLOW:
        button.setText("Next");
        break;
    }
    setSupportActionBar(toolbar);
    setTitle(getResources().getString(R.string.welcome));
    TextView textView = (TextView) findViewById(R.id.fragment_enter_mob_mob_Verify_mobile_number_txt);
    phoneNumber = (EditText) findViewById(R.id.fragment_enter_mob_no_edtTxt);
    textView.setText(getResources().getString(R.string.enter_mobile));
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isPhoneNumberValid(phoneNumber.getText())) {
          switch (flow){
            case Constants.EnterNumberFlow.CHANGE_NUMBER_FLOW:
              WholeMartApplication.setValue(Constants.UserConstants.PHONE, phoneNumber.getText().toString());
              finish();
              break;
            case Constants.EnterNumberFlow.CREATE_ACCOUNT_FLOW:
              callLoginApi();
              break;
          }
        }
      }
    });
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
  }

  private boolean isPhoneNumberValid(final CharSequence phone) {
    final boolean isPhoneNumberValid = Patterns.PHONE.matcher(phone).matches();
    if (!isPhoneNumberValid || (phone.length() < 10)) {
      showInputFieldError(phoneNumber);
      return false;
    }
    return isPhoneNumberValid;
  }

  private void showInputFieldError(final EditText editText) {
    editText.setError(getString(R.string.login_invalid_PhoneNumber));
    editText.requestFocus();
  }

  private void callLoginApi() {
    WholeMartApplication.setValue(Constants.UserConstants.PHONE, phoneNumber.getText().toString());
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(Constants.PARAMS_FULLNAME, WholeMartApplication.getValue(Constants.UserConstants.USERNAME, ""));
      jsonObject.put(Constants.PARAMS_EMAIL, WholeMartApplication.getValue(Constants.UserConstants.USER_EMAIL, ""));
      jsonObject.put(Constants.PARAMS_PHONE, String.valueOf(phoneNumber.getText()));
      jsonObject.put(Constants.PARAMS_TYPE, "GOOGLE ACCOUNT");
      SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
      String regId = pref.getString("regId", null);
      jsonObject.put(Constants.PARAMS_TOKEN, regId);
    } catch (JSONException e) {
      Utility.reportException(e);
    }
    showProgress("Please Wait", false);
    WholeSellerHttpClient wholeSellerHttpClient = new WholeSellerHttpClient("/signup", jsonObject.toString(), RequestMethod.POST);
    wholeSellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            hideBlockingProgress();
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has(Constants.AUTH_KEY)) {
              WholeMartApplication.setValue(Constants.UserConstants.AUTH_KEY, jsonObject.get(Constants.AUTH_KEY).toString());
              Intent intent = new Intent(EnterMobileNumberPage.this, MainActivity.class);
              startActivity(intent);
              finish();
            } else {
              showInfoDialog(null, getResources().getString(R.string.error));
            }

          } catch (Exception e) {
            Utility.reportException(e);
            hideBlockingProgress();
            showInfoDialog(null, getResources().getString(R.string.error));
          }
        } else {
          hideBlockingProgress();
          showInfoDialog(null, getResources().getString(R.string.error));
        }
      }
    });
    wholeSellerHttpClient.setmHttpProtocol(Constants.HTTP);
    wholeSellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    wholeSellerHttpClient.executeAsync();
  }
}
