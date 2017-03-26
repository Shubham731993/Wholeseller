package abominable.com.wholeseller.address;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.checkout.CheckOutItem;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.SpacesItemDecoration;
import abominable.com.wholeseller.common.WholeSellerHttpClient;
import abominable.com.wholeseller.login.EnterMobileNumberPage;
import abominable.com.wholeseller.ticket.DisplayOrder;
import abominable.com.wholeseller.ticket.OrderTicketActivity;

/**
 * Created by shubham.srivastava on 26/12/16.
 */

public class FinalAddressActivity extends BaseActivity implements View.OnClickListener {

  private int step = 1;
  private TextView address, phoneNumber;
  private String orderId, addressValue, totalPrice;
  private boolean isAddressSet = false;
  private boolean isPhoneSet = false;
  private RelativeLayout addressLayout, confirmOrder, placeOrder;
  private Button continueButton;
  private ArrayList<CheckOutItem> checkOutItemArrayList;
  private RecyclerView recyclerView;
  private RadioGroup paymentGroup;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.final_address_layout);
    continueButton = (Button) findViewById(R.id.continueButton);
    ImageView addressEditImage = (ImageView) findViewById(R.id.edit_image);
    ImageView phoneEditImage = (ImageView) findViewById(R.id.edit_image_phone);
    addressLayout = (RelativeLayout) findViewById(R.id.delivery_address);
    confirmOrder = (RelativeLayout) findViewById(R.id.confirm_order);
    placeOrder = (RelativeLayout) findViewById(R.id.place_order);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    paymentGroup = (RadioGroup) findViewById(R.id.payment_group);
    address = (TextView) findViewById(R.id.address);
    TextView price = (TextView) findViewById(R.id.total_price);
    checkOutItemArrayList = getIntent().getParcelableArrayListExtra(Constants.DETAIL_OBJECT);
    totalPrice = getIntent().getStringExtra(Constants.TOTAL_PRICE);
    price.setText(totalPrice);
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

    if (!TextUtils.isEmpty(WholeMartApplication.getValue(Constants.UserConstants.PHONE, ""))) {
      phoneNumber.setText(WholeMartApplication.getValue(Constants.UserConstants.PHONE, ""));
      isPhoneSet = true;
    } else {
      isPhoneSet = false;
      phoneNumber.setText("No phone number found.Please tap on edit button to fill phone number");
    }
    continueButton.setOnClickListener(this);
    addressEditImage.setOnClickListener(this);
    phoneEditImage.setOnClickListener(this);
    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.order_spacing);
    recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    resetStepsLayout();
  }

  private void resetStepsLayout() {
    RelativeLayout stepsLayout = (RelativeLayout) findViewById(R.id.stepsLayout);
    TextView step1 = (TextView) stepsLayout.findViewById(R.id.step1);
    TextView step2 = (TextView) stepsLayout.findViewById(R.id.step2);
    TextView step3 = (TextView) stepsLayout.findViewById(R.id.step3);

    if (step == 1) {
      addressLayout.setVisibility(View.VISIBLE);
      confirmOrder.setVisibility(View.GONE);
      placeOrder.setVisibility(View.GONE);
      continueButton.setText("Continue");
      ((GradientDrawable) step1.getBackground()).setColor(ContextCompat.getColor(this, R.color.logo_orange));
      step1.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step2.getBackground()).setColor(ContextCompat.getColor(this, R.color.step_disabled));
      step2.setTextColor(ContextCompat.getColor(this, R.color.grey));
      ((GradientDrawable) step3.getBackground()).setColor(ContextCompat.getColor(this, R.color.step_disabled));
      step3.setTextColor(ContextCompat.getColor(this, R.color.grey));
    } else if (step == 2) {
      addressLayout.setVisibility(View.GONE);
      confirmOrder.setVisibility(View.VISIBLE);
      placeOrder.setVisibility(View.GONE);
      recyclerView.setAdapter(new FinalOrderAdapter(this, checkOutItemArrayList));
      continueButton.setText("Confirm Order");
      ((GradientDrawable) step1.getBackground()).setColor(ContextCompat.getColor(this, R.color.logo_orange));
      step1.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step2.getBackground()).setColor(ContextCompat.getColor(this, R.color.logo_orange));
      step2.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step3.getBackground()).setColor(ContextCompat.getColor(this, R.color.step_disabled));
      step3.setTextColor(ContextCompat.getColor(this, R.color.grey));
    } else if (step == 3) {
      addressLayout.setVisibility(View.GONE);
      confirmOrder.setVisibility(View.GONE);
      placeOrder.setVisibility(View.VISIBLE);
      continueButton.setText("Place Order");
      ((GradientDrawable) step1.getBackground()).setColor(ContextCompat.getColor(this, R.color.logo_orange));
      step1.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step2.getBackground()).setColor(ContextCompat.getColor(this, R.color.logo_orange));
      step2.setTextColor(ContextCompat.getColor(this, R.color.white));
      ((GradientDrawable) step3.getBackground()).setColor(ContextCompat.getColor(this, R.color.logo_orange));
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
          resetStepsLayout();
        } else if (step == 3) {
          JSONObject jsonObject = new JSONObject();
          try {
            jsonObject.put(Constants.AddressConstants.ADDRESS, WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION, ""));
            jsonObject.put(Constants.AddressConstants.DELIVERY_TYPE, "single");
            jsonObject.put(Constants.AddressConstants.ADDRESS_LINE_ONE, WholeMartApplication.getValue(Constants.AddressConstants.ADDRESS_LINE_ONE, ""));
            jsonObject.put(Constants.AddressConstants.ADDRESS_LINE_TWO, WholeMartApplication.getValue(Constants.AddressConstants.ADDRESS_LINE_TWO, ""));
            jsonObject.put(Constants.AddressConstants.CITY, WholeMartApplication.getValue(Constants.AddressConstants.CITY, ""));
            jsonObject.put(Constants.AddressConstants.COMPANY, WholeMartApplication.getValue(Constants.AddressConstants.COMPANY, ""));
            jsonObject.put(Constants.AddressConstants.PHONE, WholeMartApplication.getValue(Constants.UserConstants.PHONE, ""));
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
            String regId = pref.getString("regId", null);
            jsonObject.put(Constants.PARAMS_TOKEN, regId);
            switch (paymentGroup.getCheckedRadioButtonId()) {
              case R.id.account:
                jsonObject.put(Constants.AddressConstants.PAYMENT_TYPE, "ACCOUNT TRANSFER ON DELIVERY");
                break;
              case R.id.cash:
                jsonObject.put(Constants.AddressConstants.PAYMENT_TYPE, "CASH ON DELIVERY");
                break;
              case R.id.cheque:
                jsonObject.put(Constants.AddressConstants.PAYMENT_TYPE, "CHEQUE ON DELIVERY");
                break;

            }
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
        intent.putExtra(Constants.FETCH_ADDRESS_FLOW, Constants.AddressFlow.CHECKOUT_ADDRESS);
        startActivityForResult(intent, Constants.REQUEST_ADDRESS);
        break;

      case R.id.edit_image_phone:
        Intent intent1 = new Intent(FinalAddressActivity.this, EnterMobileNumberPage.class);
        intent1.putExtra(Constants.ENTER_NUMBER_FLOW, Constants.EnterNumberFlow.CHANGE_NUMBER_FLOW);
        startActivityForResult(intent1, Constants.REQUEST_NUMBER);
        break;

    }
  }

  private void callAddressPostApi(JSONObject jsonObject) {
    showProgress("Please Wait", false);
    WholeSellerHttpClient wholeSellerHttpClient = new WholeSellerHttpClient("/post_user_details", jsonObject.toString(), RequestMethod.POST);
    wholeSellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          hideBlockingProgress();
          try {
            DisplayOrder displayOrder = new DisplayOrder(new JSONObject(response));
            Intent intent=new Intent(FinalAddressActivity.this, OrderTicketActivity.class);
            intent.putExtra(Constants.ORDER,displayOrder);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(FinalAddressActivity.this, "Your order has been generated", Toast.LENGTH_SHORT).show();
          } catch (JSONException e) {
            e.printStackTrace();
          }
        } else {
          hideBlockingProgress();
          showErrorDialog(null, getResources().getString(R.string.error));
        }
      }
    });
    wholeSellerHttpClient.setmHttpProtocol(Constants.HTTP);
    wholeSellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    wholeSellerHttpClient.executeAsync();
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

  @Override
  public void onBackPressed() {
    if (step == 1) {
      super.onBackPressed();
    } else if (step == 2) {
      step = 1;
      resetStepsLayout();
    } else if (step == 3) {
      step = 2;
      resetStepsLayout();
    }
  }
}
