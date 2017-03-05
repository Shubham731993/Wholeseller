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
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

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
  private TabLayout tabLayout;
  private String orderId="";
  private Button checkOut;
  private int selectedTabPosition;
  private JSONArray tabsList;
  private int currentItemPosition;
  private int checkOutCounterValue=0;
  private HashMap<String,Boolean> itemIds;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.detail_activity);
    viewPager= (ViewPager) findViewById(R.id.view_pager);
    tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    checkOut = (Button) findViewById(R.id.checkout_button);
    orderId=WholeMartApplication.getValue(Constants.CURRENT_ORDER_ID,"");
    itemIds=new HashMap<>();
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
    getSupportActionBar().setTitle("Add to cart");
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
    DetailPagerAdapter detailPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(detailPagerAdapter);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        selectedTabPosition=tab.getPosition();
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
  public void passNoOfKgs(String val, String id,int itemPosition,String itemName,double price,String imagePath) {
    currentItemPosition=itemPosition;
    if(TextUtils.isEmpty(orderId)) {
      getOrderId(val,id,itemName,price,imagePath);
    }else {
      addToCart(val,id,itemName,price,imagePath);
    }
  }

  private void addToCart(String val,final String id,String itemName,double price,String imagePath) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(Constants.PARAMS_ITEM_ID, id);
      jsonObject.put(Constants.PARAMS_ITEM_NAME, itemName);
      jsonObject.put(Constants.PARAMS_QUANTITY, val);
      jsonObject.put(Constants.PARAMS_DAYS, "1");
      jsonObject.put(Constants.PRICE,price);
      jsonObject.put(Constants.IMAGE_PATH,imagePath);
    } catch (Exception e) {
      Utility.reportException(e);
    }
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/add_to_user_cart/"+ orderId+"/query?itemId="+id,jsonObject.toString(), RequestMethod.PUT);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            if(itemIds.get(id)==null) {
              itemIds.put(id,true);
              checkOutCounterValue++;
              checkOut.setText(getString(R.string.checkout, checkOutCounterValue));
            }else if((itemIds.get(id)!=null && !itemIds.get(id))){
              itemIds.put(id, true);
              checkOutCounterValue++;
              checkOut.setText(getString(R.string.checkout, checkOutCounterValue));
            }
            hideBlockingProgress();
            JSONObject jsonObject = new JSONObject(response);
            updateData(jsonObject.getInt("quantity"));
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

  private void updateData(int quantity) {
    Fragment fragment=((DetailPagerAdapter)viewPager.getAdapter()).getRegisteredFragment(selectedTabPosition);
    if(fragment instanceof DetailFragment){
      ((DetailFragment)fragment).updateItem(currentItemPosition,quantity);
    }
  }

  public void getOrderId(String val,final String id,String itemName,double price,String imagePath){
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(Constants.PARAMS_ITEM_ID, id);
      jsonObject.put(Constants.PARAMS_QUANTITY, val);
      jsonObject.put(Constants.PARAMS_DAYS, "1");
      jsonObject.put(Constants.PARAMS_ITEM_NAME,itemName);
      jsonObject.put(Constants.PRICE,price);
      jsonObject.put(Constants.IMAGE_PATH,imagePath);
    } catch (JSONException e) {
      Utility.reportException(e);
    }
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/add_to_user_cart",jsonObject.toString(), RequestMethod.POST);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            if(itemIds.get(id)==null) {
              itemIds.put(id, true);
              checkOutCounterValue++;
              checkOut.setText(getString(R.string.checkout, checkOutCounterValue));
            }else if((itemIds.get(id)!=null && !itemIds.get(id))){
              itemIds.put(id, true);
              checkOutCounterValue++;
              checkOut.setText(getString(R.string.checkout, checkOutCounterValue));
            }
            hideBlockingProgress();
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("_id")){
              orderId=jsonObject.getString("_id");
              WholeMartApplication.setValue(Constants.CURRENT_ORDER_ID,"");
            }
            if(jsonObject.has("itemsInOrder")){
              JSONArray jsonArray=jsonObject.getJSONArray("itemsInOrder");
              updateData(jsonArray.getJSONObject(0).getInt("quantity"));
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
    private final SparseArray<WeakReference<Fragment>> registeredFragments = new SparseArray<>();

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

    @Nullable
    public Fragment getRegisteredFragment(final int position) {
      final WeakReference<Fragment> wr = registeredFragments.get(position);
      if (wr != null) {
        return wr.get();
      } else {
        return null;
      }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
      final Fragment fragment = (Fragment) super.instantiateItem(container, position);
      registeredFragments.put(position, new WeakReference<>(fragment));
      return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
      registeredFragments.remove(position);
      super.destroyItem(container, position, object);
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.add_to_cart) {
      if(!TextUtils.isEmpty(orderId)) {
      Intent intent = new Intent(DetailActivity.this, CheckoutActivity.class);
      intent.putExtra(Constants.ORDER_ID, orderId);
      startActivity(intent);
      }else {
        Toast.makeText(DetailActivity.this,"Please add items to cart",Toast.LENGTH_SHORT).show();
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.detail_menu, menu);
    return true;
  }
}
