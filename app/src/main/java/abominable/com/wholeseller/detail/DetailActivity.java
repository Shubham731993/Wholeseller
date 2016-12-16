package abominable.com.wholeseller.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.checkout.CheckoutActivity;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholesellerHttpClient;

/**
 * Created by shubham.srivastava on 15/07/16.
 */
public class DetailActivity extends BaseActivity implements AddToCartFragment.PassData{
  private ViewPager viewPager;
  private DetailPagerAdapter detailPagerAdapter;
  private TabLayout tabLayout;
  private String orderId="";
  private Button checkOut;
  private JSONArray tabsList;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.detail_activity);
    viewPager= (ViewPager) findViewById(R.id.view_pager);
    tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    checkOut = (Button) findViewById(R.id.checkout_button);
    checkOut.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!TextUtils.isEmpty(orderId)) {
          Intent intent = new Intent(DetailActivity.this, CheckoutActivity.class);
          intent.putExtra(Constants.ORDER_ID, orderId);
          startActivity(intent);
        }else {
          Toast.makeText(DetailActivity.this,"Please add items to cart",Toast.LENGTH_SHORT).show();
        }
      }
    });
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    getSupportActionBar().setTitle("Items");
    callGenresApi();
  }

  private void callGenresApi() {
    showProgress("Please Wait",false);
    WholesellerHttpClient wholesellerHttpClient=new WholesellerHttpClient("/get_all_genres", RequestMethod.GET);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if(status==200){
          try {
            hideBlockingProgress();
            tabsList = new JSONArray(response);
            /*ArrayList<String> tabNamesList=new ArrayList<>();
            JSONObject jsonObject=new JSONObject(response);
            JSONArray genresArray=jsonObject.getJSONArray(Constants.DetailContants.GENRES);
            for (int i=0;i<genresArray.length();i++){
              ArrayList<DetailObject> detailObjects=new ArrayList<>();
              JSONObject jsonObject1=genresArray.getJSONObject(i);
              String id=jsonObject1.getString("id");
              genresIdList.add(id);
              tabNamesList.add(jsonObject1.getString("name"));
              JSONArray detailArray=jsonObject1.getJSONArray("itemsInGenre");
              for(int j=0;j<detailArray.length();j++){
                DetailObject detailObject=new DetailObject(detailArray.getJSONObject(j));
                detailObjects.add(detailObject);
              }
              mapOfObjects.put(genresIdList.get(i),detailObjects);
            }*/
            setTabNames(tabsList);
            viewPager.setOffscreenPageLimit(tabsList.length());
          } catch (JSONException e) {
            Utility.reportException(e);
            hideBlockingProgress();
            showErrorDialog(null,getResources().getString(R.string.error));
          }
        }else {
          hideBlockingProgress();
          showErrorDialog(null,getResources().getString(R.string.error));
        }

      }
    });
    wholesellerHttpClient.setmHttpProtocol(Constants.HTTP);
    wholesellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    wholesellerHttpClient.executeAsync();
  }

  private void setTabNames(JSONArray tabsList) {
    for(int i=0;i<tabsList.length();i++){
      try {
        tabLayout.addTab(tabLayout.newTab().setText(tabsList.getString(i)));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    detailPagerAdapter=new DetailPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(detailPagerAdapter);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });
  }

  @Override
  public void passNoOfKgs(String val, String id) {
    if(TextUtils.isEmpty(orderId)) {
      getOrderId(val,id);
    }else {
      addToCart(val,id);
    }
  }

  private void addToCart(String val,String id) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(Constants.PARAMS_ITEM_ID, id);
      jsonObject.put(Constants.PARAMS_QUANTITY, val);
      jsonObject.put(Constants.PARAMS_DAYS, "");
      jsonObject.put(Constants.PARAMS_SHAKEY, WholeMartApplication.getValue(Constants.UserConstants.AUTH_KEY,""));
    } catch (Exception e) {
      Utility.reportException(e);
    }
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/add_items_in_cart/"+ orderId,jsonObject.toString(), RequestMethod.PUT);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            hideBlockingProgress();
            JSONObject jsonObject = new JSONObject(response);

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

  public void getOrderId(String val,String id){
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(Constants.PARAMS_ITEM_ID, id);
      jsonObject.put(Constants.PARAMS_QUANTITY, val);
      jsonObject.put(Constants.PARAMS_DAYS, "");
      jsonObject.put(Constants.PARAMS_SHAKEY, WholeMartApplication.getValue(Constants.UserConstants.AUTH_KEY,""));
    } catch (JSONException e) {
      Utility.reportException(e);
    }
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/add_items_in_cart",jsonObject.toString(), RequestMethod.POST);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            hideBlockingProgress();
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("orderId")){
              orderId=jsonObject.getString("orderId");
            }

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

  public class DetailPagerAdapter extends FragmentStatePagerAdapter{

    public DetailPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      try {
        return DetailFragment.newInstance((String) tabsList.get(position));
      } catch (JSONException e) {
        Utility.reportException(e);
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public int getCount() {
      return tabLayout.getTabCount();
    }
  }

}
