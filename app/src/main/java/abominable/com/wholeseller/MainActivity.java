package abominable.com.wholeseller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONObject;

import java.util.ArrayList;

import abominable.com.wholeseller.botttomNavigation.HomeFragment;
import abominable.com.wholeseller.botttomNavigation.ProfileFragment;
import abominable.com.wholeseller.botttomNavigation.YourOrderFragment;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.WMTextView;
import abominable.com.wholeseller.ticket.DisplayOrder;

/**
 * Created by shubham.srivastava on 29/04/17.
 */

public class MainActivity extends BaseActivity implements HomeFragment.HomeData, YourOrderFragment.OrderData {

  private static int currentPosition = 0;
  private HomeFragment homeFragment;
  private YourOrderFragment yourOrderFragment;
  private ProfileFragment profileFragment;
  private JSONObject homeData;
  private ArrayList<DisplayOrder> displayOrders;
  private CoordinatorLayout coordinatorLayout;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);
    BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    final WMTextView title= (WMTextView) toolbar.findViewById(R.id.toolbar_custom_title);
    final WMTextView subtitle= (WMTextView) toolbar.findViewById(R.id.toolbar_custom_sub_title);
    bottomNavigationView.setOnNavigationItemSelectedListener
        (new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
              case R.id.home:
                if (homeFragment == null) {
                  homeFragment = HomeFragment.newInstance();
                }
                selectedFragment = homeFragment;
                currentPosition = 0;
                title.setText("Home");
                subtitle.setText(WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION,""));
                break;
              case R.id.orders:
                if (yourOrderFragment == null) {
                  yourOrderFragment = YourOrderFragment.newInstance();
                }
                selectedFragment = yourOrderFragment;
                currentPosition = 1;
                title.setText("Orders");
                if(displayOrders!=null) {
                  subtitle.setText(displayOrders.size()+" Orders");
                }else {
                  subtitle.setText("");
                }
                break;
              case R.id.profile:
                if (profileFragment == null) {
                  profileFragment = ProfileFragment.newInstance();
                }
                selectedFragment = profileFragment;
                currentPosition = 2;
                title.setText("Profile");
                subtitle.setText("");
                break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commitAllowingStateLoss();
            return true;
          }
        });
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    switch (currentPosition) {
      case 2:
        transaction.replace(R.id.frame_layout, ProfileFragment.newInstance());
        title.setText("Profile");
        subtitle.setText("");
        break;
      case 1:
        transaction.replace(R.id.frame_layout, YourOrderFragment.newInstance());
        title.setText("Orders");
        if(displayOrders!=null) {
          subtitle.setText(displayOrders.size()+" Orders");
        }else {
          subtitle.setText("");
        }
        break;
      case 0:
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        title.setText("Home");
        subtitle.setText(WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION,""));
        break;
    }
    bottomNavigationView.getMenu().getItem(currentPosition).setChecked(true);
    transaction.commitAllowingStateLoss();
  }

  @Override
  public JSONObject getHomeData() {
    return homeData;
  }

  @Override
  public void setHomeData(JSONObject jsonObject) {
    homeData = jsonObject;
  }

  @Override
  public ArrayList<DisplayOrder> getOrderData() {
    return displayOrders;
  }

  @Override
  public void setOrderData(ArrayList<DisplayOrder> displayOrders) {
    this.displayOrders = displayOrders;
  }

 public void showError(String message){
   showSnackBar(this,coordinatorLayout,message);
 }
}
