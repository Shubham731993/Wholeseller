package abominable.com.wholeseller.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.address.FetchAddressActivity;
import abominable.com.wholeseller.address.FinalAddressActivity;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholesellerHttpClient;

/**
 * Created by shubham.srivastava on 14/10/16.
 */

public class CheckoutActivity extends BaseActivity {

  RecyclerView recyclerView;
  private ArrayList<CheckOutItem> checkOutItemArrayList;
  private CheckOutAdapter checkOutAdapter;
  private String orderId;
  private Button checkOut;
  private String flow;
  private String totalPrice;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.checkout_activity);
    orderId = getIntent().getStringExtra(Constants.ORDER_ID);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    checkOut = (Button) findViewById(R.id.checkout_button);
    checkOut.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!TextUtils.isEmpty(WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION, ""))) {
          flow = Constants.AddressFlow.CHECKOUT_ADDRESS;
          callCheckoutApi(true);
        } else {
          flow = Constants.AddressFlow.CHECKOUT_NO_ADDRESS;
          callCheckoutApi(true);
        }
      }
    });
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    getSupportActionBar().setTitle("Checkout");
    callCheckoutApi(false);
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(mLayoutManager);
  }

  private void callCheckoutApi(final boolean isAddressActivityCalled) {
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/add_to_user_cart/" + orderId, RequestMethod.GET);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            JSONObject jsonObject = new JSONObject(response);
            prepareCheckOutItems(jsonObject, isAddressActivityCalled);
          } catch (JSONException e) {
            Utility.reportException(e);
            hideBlockingProgress();
            showErrorDialog(null, getResources().getString(R.string.error));
          }
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

  private void prepareCheckOutItems(JSONObject jsonObject, boolean isActivityCalled) throws JSONException {
    checkOutItemArrayList = new ArrayList<>();
    totalPrice=jsonObject.getString(Constants.TOTAL_PRICE);
    try {
      JSONArray jsonArray = jsonObject.getJSONArray("itemsInOrder");
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
        CheckOutItem checkOutItem = new CheckOutItem(jsonObject1);
        checkOutItemArrayList.add(checkOutItem);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    hideBlockingProgress();
    if (isActivityCalled) {
      Intent intent = null;
      switch (flow) {
        case Constants.AddressFlow.CHECKOUT_ADDRESS:
          intent = new Intent(CheckoutActivity.this, FinalAddressActivity.class);
          break;
        case Constants.AddressFlow.CHECKOUT_NO_ADDRESS:
          intent = new Intent(CheckoutActivity.this, FetchAddressActivity.class);
          break;
        default:
          intent = new Intent(CheckoutActivity.this, FinalAddressActivity.class);
          break;
      }
      intent.putExtra(Constants.FETCH_ADDRESS_FLOW, flow);
      intent.putExtra(Constants.DETAIL_OBJECT, checkOutItemArrayList);
      intent.putExtra(Constants.ORDER_ID, orderId);
      intent.putExtra(Constants.TOTAL_PRICE, totalPrice);
      startActivity(intent);

    } else {
      checkOutAdapter = new CheckOutAdapter(this, checkOutItemArrayList);
      recyclerView.setAdapter(checkOutAdapter);
    }
  }


  public void onItemRemove(int position) {
    if (checkOutAdapter != null && checkOutItemArrayList != null) {
      checkOutItemArrayList.remove(position);
      checkOutAdapter.notifyItemRemoved(position);
    }
  }

  public void callDeleteApi(CheckOutItem checkOutItem, final int position) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(Constants.PARAMS_ITEM_ID, checkOutItem.getId());
      jsonObject.put(Constants.PARAMS_QUANTITY, checkOutItem.getQuantity());
      jsonObject.put(Constants.PARAMS_DAYS, "1");
      jsonObject.put(Constants.PARAMS_ITEM_NAME, checkOutItem.getName());
      jsonObject.put(Constants.PRICE, checkOutItem.getItemPrice());
    } catch (Exception e) {
      Utility.reportException(e);
    }
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/add_to_user_cart/" + orderId + "/query?itemId=" + checkOutItem.getId(), RequestMethod.DELETE);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        hideBlockingProgress();
        if (status == 200) {
          onItemRemove(position);
        } else {
          showErrorDialog(null, getResources().getString(R.string.error));
        }

      }
    });
    wholesellerHttpClient.setmHttpProtocol(Constants.HTTP);
    wholesellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    wholesellerHttpClient.executeAsync();

  }
}
