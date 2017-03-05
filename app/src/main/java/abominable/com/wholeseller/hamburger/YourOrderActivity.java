package abominable.com.wholeseller.hamburger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;

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
 * Created by shubham.srivastava on 22/02/17.
 */

public class YourOrderActivity extends BaseActivity {

  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private YourOrderAdapter yourOrderAdapter;
  private ArrayList<HamburgerOrderItem> hamburgerOrderItems;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.your_order);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    hamburgerOrderItems=new ArrayList<>();
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    yourOrderAdapter=new YourOrderAdapter(YourOrderActivity.this,hamburgerOrderItems);
    recyclerView.setAdapter(yourOrderAdapter);
    showProgress();
    callOrderApi();

  }

  private void callOrderApi() {
    try {
      showProgress();
      WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/get_user_orders", RequestMethod.GET);
      wholesellerHttpClient.setResponseListner(new ResponseListener() {
        @Override
        public void onResponse(int status, String response) {
          if (status == 200) {
            try {
              hideProgress();
              JSONArray jsonArray = new JSONArray(response);
              for (int i=0;i<jsonArray.length();i++) {
                HamburgerOrderItem hamburgerOrderItem = new HamburgerOrderItem(jsonArray.getJSONObject(i));
                hamburgerOrderItems.add(hamburgerOrderItem);
              }
              yourOrderAdapter.notifyDataSetChanged();

            } catch (Exception e) {
              Utility.reportException(e);
              hideProgress();
              showInfoDialog(null, getResources().getString(R.string.error));
            }
          } else {
            hideProgress();
            showInfoDialog(null, getResources().getString(R.string.error));
          }
        }
      });
      wholesellerHttpClient.setmHttpProtocol(Constants.HTTP);
      wholesellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
      wholesellerHttpClient.executeAsync();
    } catch (Exception e) {
      Utility.reportException(e);
      hideProgress();
      showErrorDialog("", getString(R.string.error));
    }
  }


  private void showProgress(){
    progressBar.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(View.GONE);
  }

  private void hideProgress(){
    progressBar.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }
}
