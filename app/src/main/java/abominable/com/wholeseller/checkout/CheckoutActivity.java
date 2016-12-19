package abominable.com.wholeseller.checkout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
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

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.checkout_activity);
    String orderId=getIntent().getStringExtra(Constants.ORDER_ID);
    recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
    callCheckoutApi(orderId);
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(mLayoutManager);
  }

  private void callCheckoutApi(String orderId) {
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/add_to_user_cart/"+orderId,RequestMethod.GET);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            JSONObject jsonObject = new JSONObject(response);
            prepareCheckOutItems(jsonObject);
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

  private void prepareCheckOutItems(JSONObject jsonObject) {
    ArrayList<CheckOutItem> checkOutItemArrayList=new ArrayList<>();
    try {
        JSONArray jsonArray=jsonObject.getJSONArray("itemsInOrder");
        for(int i=0;i<jsonArray.length();i++) {
          JSONObject jsonObject1=jsonArray.getJSONObject(i);
          CheckOutItem checkOutItem = new CheckOutItem(jsonObject1);
          checkOutItemArrayList.add(checkOutItem);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }

    hideBlockingProgress();
    CheckOutAdapter checkOutAdapter=new CheckOutAdapter(checkOutItemArrayList);
    recyclerView.setAdapter(checkOutAdapter);
  }
}
