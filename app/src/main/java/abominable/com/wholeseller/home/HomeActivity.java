package abominable.com.wholeseller.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.SpacesItemDecoration;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholesellerHttpClient;
import abominable.com.wholeseller.login.WholeMartLoginActivity;

public class HomeActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private RecyclerView recyclerView;
  private ViewPager mVwPager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    recyclerView = (RecyclerView) findViewById(R.id.item_list);
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
    layoutManager.setOrientation(GridLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
    recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    mVwPager = (ViewPager) findViewById(R.id.view_pager);
    CirclePageIndicator mPageIndicator = (CirclePageIndicator) findViewById(R.id.pager_indicator);
    mVwPager.setAdapter(new CarousalImageAdapter(this));
    mPageIndicator.setViewPager(mVwPager);
    callHomeApi();
  }

  private void callHomeApi() {
    showProgress("Please Wait", false);
    WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/get_all_genres", RequestMethod.GET);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          hideBlockingProgress();
          try {
            JSONArray jsonArray = new JSONArray(response);
            HomeRecyclerView homeRecyclerView = new HomeRecyclerView(HomeActivity.this, jsonArray);
            recyclerView.setAdapter(homeRecyclerView);
          } catch (JSONException e) {
            e.printStackTrace();
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

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.logout) {
      callLogoutApi();

    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void callLogoutApi() {
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(Constants.AUTH_KEY, WholeMartApplication.getValue(Constants.UserConstants.AUTH_KEY, ""));
      showProgress("Please Wait", false);
      WholesellerHttpClient wholesellerHttpClient = new WholesellerHttpClient("/login/logout", jsonObject.toString(), RequestMethod.POST);
      wholesellerHttpClient.setResponseListner(new ResponseListener() {
        @Override
        public void onResponse(int status, String response) {
          if (status == 200) {
            try {
              hideBlockingProgress();
              JSONObject jsonObject = new JSONObject(response);
              String returnValue = jsonObject.getString(Constants.MESSAGE);
              if (returnValue.equalsIgnoreCase("LOGOUT SUCCESSFUL") || returnValue.equalsIgnoreCase("ALREADY LOGGED OUT")) {
                WholeMartApplication.setValue(Constants.UserConstants.AUTH_KEY, "");
                Toast.makeText(HomeActivity.this, "You have been logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, WholeMartLoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.show_info, R.anim.fade_out);
              } else {
                showInfoDialog(null, getResources().getString(R.string.error));
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
      showErrorDialog("", getString(R.string.error));
    }
  }

}
