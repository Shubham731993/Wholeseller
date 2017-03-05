package abominable.com.wholeseller.address;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.WholesellerHttpClient;
import abominable.com.wholeseller.login.EnterMobileNumberPage;

/**
 * Created by shubham.srivastava on 26/12/16.
 */

public class FinalAddressActivity extends BaseActivity implements View.OnClickListener {

  private int step = 1;
  private TextView address,phoneNumber;
  private String orderId, addressValue;
  private boolean isAddressSet = false;
  private boolean isPhoneSet = false;
  private RelativeLayout addressLayout;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.final_address_layout);
    Button continueButton = (Button) findViewById(R.id.continueButton);
    ImageView addressEditImage = (ImageView) findViewById(R.id.edit_image);
    ImageView phoneEditImage = (ImageView) findViewById(R.id.edit_image_phone);
    addressLayout = (RelativeLayout) findViewById(R.id.delivery_address);
    address = (TextView) findViewById(R.id.address);
    phoneNumber = (TextView) findViewById(R.id.phone_number);
    orderId = getIntent().getStringExtra(Constants.ORDER_ID);
    if (getIntent().hasExtra(Constants.AddressConstants.ADDRESS)) {
      addressValue = getIntent().getStringExtra(Constants.AddressConstants.ADDRESS);
      address.setText(addressValue);
      isAddressSet = true;
    } else if (!TextUtils.isEmpty(WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION, ""))) {
      address.setText(WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION, ""));
      isAddressSet = true;
    } else {
      isAddressSet = false;
      address.setText("No address found.Please tap on edit button to fill address");
    }

    if(!TextUtils.isEmpty(WholeMartApplication.getValue(Constants.UserConstants.PHONE, ""))){
      phoneNumber.setText(WholeMartApplication.getValue(Constants.UserConstants.PHONE, ""));
      isPhoneSet=true;
    }else {
      isPhoneSet = false;
      phoneNumber.setText("No phone number found.Please tap on edit button to fill phone number");
    }
    continueButton.setOnClickListener(this);
    addressEditImage.setOnClickListener(this);
    phoneEditImage.setOnClickListener(this);
    resetStepsLayout();
  }

  private void resetStepsLayout() {
    RelativeLayout stepsLayout = (RelativeLayout) findViewById(R.id.stepsLayout);
    TextView step1 = (TextView) stepsLayout.findViewById(R.id.step1);
    TextView step2 = (TextView) stepsLayout.findViewById(R.id.step2);
    TextView step3 = (TextView) stepsLayout.findViewById(R.id.step3);

    if (step == 1) {
      addressLayout.setVisibility(View.VISIBLE);
      ((GradientDrawable) step1.getBackground()).setColor(ContextCompat.getColor(this, R.color.signup_blue));
      step1.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step2.getBackground()).setColor(ContextCompat.getColor(this, R.color.step_disabled));
      step2.setTextColor(ContextCompat.getColor(this, R.color.grey));
      ((GradientDrawable) step3.getBackground()).setColor(ContextCompat.getColor(this, R.color.step_disabled));
      step3.setTextColor(ContextCompat.getColor(this, R.color.grey));
    } else if (step == 2) {
      addressLayout.setVisibility(View.GONE);
      ((GradientDrawable) step1.getBackground()).setColor(ContextCompat.getColor(this, R.color.signup_blue));
      step1.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step2.getBackground()).setColor(ContextCompat.getColor(this, R.color.signup_blue));
      step2.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step3.getBackground()).setColor(ContextCompat.getColor(this, R.color.step_disabled));
      step3.setTextColor(ContextCompat.getColor(this, R.color.grey));
    } else if (step == 3) {
      addressLayout.setVisibility(View.GONE);
      ((GradientDrawable) step1.getBackground()).setColor(ContextCompat.getColor(this, R.color.signup_blue));
      step1.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step2.getBackground()).setColor(ContextCompat.getColor(this, R.color.signup_blue));
      step2.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step3.getBackground()).setColor(ContextCompat.getColor(this, R.color.signup_blue));
      step3.setTextColor(ContextCompat.getColor(this, R.color.white));
    }
  }


  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.continueButton:
        if (step == 1) {
          if (isAddressSet && isPhoneSet) {
            step = 2;
            resetStepsLayout();
          } else {
            Toast.makeText(FinalAddressActivity.this, "Please fill the details", Toast.LENGTH_SHORT).show();
          }
        } else if (step == 2) {
          step = 3;
          JSONObject jsonObject = new JSONObject();
          try {
            jsonObject.put(Constants.AddressConstants.ADDRESS, WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION, ""));
            jsonObject.put(Constants.AddressConstants.DELIVERY_TYPE, "single");
            jsonObject.put(Constants.AddressConstants.ADDRESS_LINE_ONE, WholeMartApplication.getValue(Constants.AddressConstants.ADDRESS_LINE_ONE, ""));
            jsonObject.put(Constants.AddressConstants.ADDRESS_LINE_TWO, WholeMartApplication.getValue(Constants.AddressConstants.ADDRESS_LINE_TWO, ""));
            jsonObject.put(Constants.AddressConstants.CITY, WholeMartApplication.getValue(Constants.AddressConstants.CITY, ""));
            jsonObject.put(Constants.AddressConstants.COMPANY, WholeMartApplication.getValue(Constants.AddressConstants.COMPANY, ""));
            jsonObject.put(Constants.AddressConstants.PHONE, WholeMartApplication.getValue(Constants.UserConstants.PHONE, ""));
            jsonObject.put(Constants.AddressConstants.PAYMENT_TYPE, "cod");
            jsonObject.put(Constants.AddressConstants.ORDER_ID, orderId);
          } catch (JSONException e) {
            e.printStackTrace();
          }
          callAddressPostApi(jsonObject);
          resetStepsLayout();
        }
        break;
      case R.id.edit_image:
        Intent intent = new Intent(FinalAddressActivity.this, FetchAddressActivity.class);
        intent.putExtra(Constants.FETCH_ADDRESS_FLOW,Constants.AddressFlow.CHECKOUT_ADDRESS);
        startActivityForResult(intent, Constants.REQUEST_ADDRESS);
        break;

      case R.id.edit_image_phone:
        Intent intent1 = new Intent(FinalAddressActivity.this, EnterMobileNumberPage.class);
        intent1.putExtra(Constants.ENTER_NUMBER_FLOW,Constants.EnterNumberFlow.CHANGE_NUMBER_FLOW);
        startActivityForResult(intent1, Constants.REQUEST_NUMBER);
        break;

    }
  }

  private void callAddressPostApi(JSONObject jsonObject) {
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/post_user_details", jsonObject.toString(), RequestMethod.POST);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          hideBlockingProgress();
          Toast.makeText(FinalAddressActivity.this, "Your order has been generated", Toast.LENGTH_SHORT).show();
        } else {
          hideBlockingProgress();
          showErrorDialog(null, getResources().getString(R.string.error));
        }
      }
    });
    wholesellerHttpClient.setmHttpProtocol(Constants.HTTP);
    wholesellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    wholesellerHttpClient.executeAsync();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Constants.REQUEST_ADDRESS && resultCode == RESULT_OK) {
      if (data != null) {
        isAddressSet = true;
        address.setText(WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION, ""));
      }
    }
    if (requestCode == Constants.REQUEST_NUMBER && resultCode == RESULT_OK) {
      if (data != null) {
        isPhoneSet = true;
        phoneNumber.setText(WholeMartApplication.getValue(Constants.UserConstants.PHONE, ""));
      }
    }
  }
}
