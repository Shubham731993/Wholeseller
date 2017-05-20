package abominable.com.wholeseller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;

import java.util.ArrayList;

import abominable.com.wholeseller.botttomNavigation.HomeFragment;
import abominable.com.wholeseller.botttomNavigation.ProfileFragment;
import abominable.com.wholeseller.botttomNavigation.YourOrderFragment;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.ticket.DisplayOrder;

/**
 * Created by shubham.srivastava on 29/04/17.
 */

public class MainActivity extends BaseActivity implements HomeFragment.HomeData, YourOrderFragment.OrderData {

  private static int currentPosition = 0;
  private HomeFragment homeFragment;
  private YourOrderFragment yourOrderFragment;
  private ProfileFragment profileFragment;
  private JSONArray homeData;
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
    getSupportActionBar().setTitle(R.string.home);
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
                toolbar.setTitle("Home");
                break;
              case R.id.orders:
                if (yourOrderFragment == null) {
                  yourOrderFragment = YourOrderFragment.newInstance();
                }
                selectedFragment = yourOrderFragment;
                currentPosition = 1;
                toolbar.setTitle("Orders");
                break;
              case R.id.profile:
                if (profileFragment == null) {
                  profileFragment = ProfileFragment.newInstance();
                }
                selectedFragment = profileFragment;
                currentPosition = 2;
                toolbar.setTitle("Profile");
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
        break;
      case 1:
        transaction.replace(R.id.frame_layout, YourOrderFragment.newInstance());
        break;
      case 0:
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        break;
    }
    bottomNavigationView.getMenu().getItem(currentPosition).setChecked(true);
    transaction.commitAllowingStateLoss();
  }

  @Override
  public JSONArray getHomeData() {
    return homeData;
  }

  @Override
  public void setHomeData(JSONArray jsonArray) {
    homeData = jsonArray;
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
