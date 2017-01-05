package abominable.com.wholeseller.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.home.HomeActivity;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholesellerHttpClient;

/**
 * Created by shubham.srivastava on 06/08/16.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener {
  private TextView buttonSignUp;
  private EditText fullName;
  private EditText phoneNumber;
  private EditText email;
  private EditText password;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup_activity);
    buttonSignUp = (TextView) findViewById(R.id.button_create_account);
    fullName = (EditText) findViewById(R.id.first_name);
    phoneNumber = (EditText) findViewById(R.id.phone);
    email = (EditText) findViewById(R.id.email_id);
    password = (EditText) findViewById(R.id.password);
    buttonSignUp.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_create_account:
        if (Utility.internetConnected()) {
          JSONObject jsonObject = new JSONObject();
          if (addSignUpParams(jsonObject)) {
            callSignUpApi(jsonObject);
          }
        } else {
          showErrorDialog("No Internet Connection", "Please connect to the internet");
        }

        break;
    }
  }

  private void callSignUpApi(JSONObject jsonObject) {
    showProgress("Please Wait", false);
    WholeMartApplication.setValue(Constants.UserConstants.PHONE,phoneNumber.getText().toString());
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/signup", jsonObject.toString(), RequestMethod.POST);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            hideBlockingProgress();
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has(Constants.AUTH_KEY)){
              WholeMartApplication.setValue(Constants.UserConstants.AUTH_KEY, jsonObject.get(Constants.AUTH_KEY).toString());
              Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
              startActivity(intent);
              finish();
              overridePendingTransition(R.anim.show_info, R.anim.fade_out);
            }else {
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

  private boolean isEmailValid(final EditText emailEditText) {
    final boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches();
    if (!isEmailValid) {
      showInputFieldError(emailEditText, null);
    }
    return isEmailValid;
  }

  private boolean isPhoneNumberValid(final CharSequence phone) {
    final boolean isPhoneNumberValid = Patterns.PHONE.matcher(phone).matches();
    if (!isPhoneNumberValid || (phone.length() < 10)) {
      showInputFieldError(phoneNumber, null);
      return false;
    }
    return isPhoneNumberValid;
  }

  private void showInputFieldError(final EditText editText, final String error) {
    if (error == null) {
      switch (editText.getId()) {
        case R.id.first_name:
          editText.setError(getString(R.string.login_invalid_FirstName));
          break;
        case R.id.phone:
          editText.setError(getString(R.string.login_invalid_PhoneNumber));
          break;
        case R.id.password:
          editText.setError(getString(R.string.login_invalid_Password));
          break;
        case R.id.email_id:
          editText.setError(getString(R.string.login_invalid_emailid));
          break;
      }
    } else {
      editText.setError(error);

    }
    editText.requestFocus();
  }

  private boolean addSignUpParams(final JSONObject params) {
    final String name = fullName.getText().toString().trim();

    if (name.split(" ").length > 2) {
      showInputFieldError(fullName, getString(R.string.name_validation_error_can_enter_only_first_and_last_name));
      return false;
    }
    if (name.isEmpty()) {
      showInputFieldError(this.fullName, null);
      return false;
    }
    final String firstName = name.split(" ")[0];
    String lastName = "";
    if (name.split(" ").length > 1) {
      lastName = name.split(" ")[1];
    }
    if (firstName.isEmpty()) {
      showInputFieldError(this.fullName, null);
      return false;
    }

    if (firstName.length() < 2) {
      showInputFieldError(this.fullName, null);
      showInputFieldError(this.fullName, getString(R.string.name_validation_error_firstname_min_2_chars));
      return false;
    }
    if (!firstName.matches("[a-zA-Z]+")) {
      showInputFieldError(this.fullName, getString(R.string.name_validation_error_firstName_should_have_only_alphabets));
      return false;

    }
    if (!lastName.isEmpty()) {
      if (lastName.length() < 2) {
        showInputFieldError(this.fullName, getString(R.string.name_validation_error_lastname_min_2_chars));
        return false;

      }

      if (!lastName.matches("[a-zA-Z]+")) {
        showInputFieldError(this.fullName, getString(R.string.name_validation_error_lastName_should_have_only_alphabets));
        return false;

      }
      if (firstName.equals(lastName)) {
        showInputFieldError(this.fullName, getString(R.string.name_validation_error_firstname_and_lastName_shouldnt_be_same));
        return false;
      }
    }
    this.fullName.setError(null);
    this.fullName.requestFocus();

    if (!isPhoneNumberValid(phoneNumber.getText())) {
      return false;
    }

    if (!isEmailValid(email)) {
      showInputFieldError(email, null);
      return false;
    }
    if (password.getText().toString().trim().isEmpty() || (password.getText().length() < Constants.MIN_PASSWORD_LENGTH)) {
      showInputFieldError(password, null);
      return false;
    }


    try {
      params.put(Constants.PARAMS_FULLNAME, String.valueOf(fullName.getText()));
      params.put(Constants.PARAMS_PASSWORD, String.valueOf(password.getText()));
      params.put(Constants.PARAMS_EMAIL, String.valueOf(email.getText()));
      params.put(Constants.PARAMS_PHONE, String.valueOf(phoneNumber.getText()));
      params.put(Constants.PARAMS_TYPE, "NORMAL ACCOUNT");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return true;
  }
}
