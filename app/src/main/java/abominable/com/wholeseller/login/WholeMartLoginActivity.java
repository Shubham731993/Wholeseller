package abominable.com.wholeseller.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

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
 * Created by shubham.srivastava on 06/08/16.
 */
public class WholeMartLoginActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

  private GoogleApiClient mGoogleApiClient;
  private int RC_SIGN_IN = 100;
  private EditText email;
  private EditText password;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_layout);
    TextView loginButton = (TextView) findViewById(R.id.login_button);
    TextView signUpButton = (TextView) findViewById(R.id.sign_up);
    email = (EditText) findViewById(R.id.email_id);
    password = (EditText) findViewById(R.id.old_pass);
    loginButton.setOnClickListener(this);
    signUpButton.setOnClickListener(this);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestScopes(new Scope(Scopes.PLUS_LOGIN))
        .requestEmail()
        .build();

    SignInButton signInButton = (SignInButton) findViewById(R.id.gmail_login);
    signInButton.setSize(SignInButton.SIZE_WIDE);
    signInButton.setScopes(gso.getScopeArray());
    setGooglePlusButtonText(signInButton, "LOGIN WITH GOOGLE");

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();

    signInButton.setOnClickListener(this);


  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.sign_up:
        Intent intent = new Intent(WholeMartLoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        break;
      case R.id.gmail_login:
        signIn();
        break;
      case R.id.login_button:
        if (isEmailValid(email)) {
          if (password.getText().toString().trim().isEmpty() || (password.getText().length() < Constants.MIN_PASSWORD_LENGTH)) {
            showInputFieldError(password, null);
          } else {
            signInThroughWholeApi();
          }
        }
        break;
    }
  }

  private boolean isEmailValid(final EditText emailEditText) {
    final boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches();
    if (!isEmailValid) {
      showInputFieldError(emailEditText, null);
    }
    return isEmailValid;
  }


  private void showInputFieldError(final EditText editText, final String error) {
    if (error == null) {
      switch (editText.getId()) {
        case R.id.old_pass:
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

  private void signInThroughWholeApi() {
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(Constants.PARAMS_EMAIL, email.getText().toString());
      jsonObject.put(Constants.PARAMS_PASSWORD, password.getText().toString());
      showProgress("Please Wait", false);
      WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/login", jsonObject.toString(), RequestMethod.POST);
      wholesellerHttpClient.setResponseListner(new ResponseListener() {
        @Override
        public void onResponse(int status, String response) {
          if (status == 200) {
            try {
              hideBlockingProgress();
              JSONObject jsonObject = new JSONObject(response);
              if (jsonObject.has(Constants.AUTH_KEY)) {
                WholeMartApplication.setValue(Constants.UserConstants.AUTH_KEY, jsonObject.get(Constants.AUTH_KEY).toString());
                Intent intent = new Intent(WholeMartLoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
              } else {
                showInfoDialog(null, getString(R.string.error));
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
    } catch (JSONException e) {
      Utility.reportException(e);
      showInfoDialog(null, getString(R.string.error));
    }

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
  }


  private void signIn() {
    showProgress("Please Wait", false);
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }

  private void handleSignInResult(GoogleSignInResult result) {
    if (result.isSuccess()) {
      hideBlockingProgress();
      GoogleSignInAccount acct = result.getSignInAccount();
      if (acct != null) {
        Intent intent = new Intent(WholeMartLoginActivity.this, EnterMobileNumberPage.class);
        WholeMartApplication.setValue(Constants.UserConstants.USERNAME, acct.getDisplayName());
        WholeMartApplication.setValue(Constants.UserConstants.USER_EMAIL, acct.getEmail());
        if (acct.getPhotoUrl() != null) {
          WholeMartApplication.setValue(Constants.UserConstants.USER_PHOTO_URL, acct.getPhotoUrl().toString());
        }
        startActivity(intent);
        finish();
      }
    } else {
      hideBlockingProgress();
      Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
    }
  }

  protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {

    for (int i = 0; i < signInButton.getChildCount(); i++) {
      View v = signInButton.getChildAt(i);

      if (v instanceof TextView) {
        TextView tv = (TextView) v;
        tv.setText(buttonText);
        tv.setTypeface(Typeface.MONOSPACE);
        return;
      }
    }
  }
}
