package abominable.com.wholeseller.login;

import android.content.Intent;
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
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholesellerHttpClient;
import abominable.com.wholeseller.home.HomeActivity;

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
    setSupportActionBar(toolbar);
    setTitle(getResources().getString(R.string.welcome));
    TextView textView = (TextView) findViewById(R.id.fragment_enter_mob_mob_Verify_mobile_number_txt);
    phoneNumber = (EditText) findViewById(R.id.fragment_enter_mob_no_edtTxt);
    textView.setText(getResources().getString(R.string.enter_mobile));
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    Button callDetailButton = (Button) findViewById(R.id.fragment_enter_mob_no_verify_Btn);
    callDetailButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isPhoneNumberValid(phoneNumber.getText())) {
          callLoginApi();
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
    } catch (JSONException e) {
      Utility.reportException(e);
    }
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/signup", jsonObject.toString(), RequestMethod.POST);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            hideBlockingProgress();
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has(Constants.AUTH_KEY)) {
              WholeMartApplication.setValue(Constants.UserConstants.AUTH_KEY, jsonObject.get(Constants.AUTH_KEY).toString());
              Intent intent = new Intent(EnterMobileNumberPage.this, HomeActivity.class);
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
    wholesellerHttpClient.setmHttpProtocol(Constants.HTTP);
    wholesellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    wholesellerHttpClient.executeAsync();
  }
}
