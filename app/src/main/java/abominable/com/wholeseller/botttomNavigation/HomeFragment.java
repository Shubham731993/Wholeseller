package abominable.com.wholeseller.botttomNavigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.MainActivity;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.BaseFragment;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholeSellerHttpClient;
import abominable.com.wholeseller.home.CarousalImageAdapter;
import abominable.com.wholeseller.home.HomeRecyclerView;
import abominable.com.wholeseller.home.SearchScreen;

/**
 * Created by shubham.srivastava on 29/04/17.
 */

public class HomeFragment extends BaseFragment {

  private RecyclerView recyclerView;
  private ViewPager mVwPager;
  private CoordinatorLayout coordinatorLayout;
  private HomeData homeData;
  private ProgressBar progressBar;

  public static HomeFragment newInstance() {
    HomeFragment fragment = new HomeFragment();
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext=context;
    homeData=(MainActivity)context;
  }

  public interface HomeData {
    JSONArray getHomeData();
    void setHomeData(JSONArray jsonArray);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.app_bar_main, container, false);
  }

  private void callHomeApi() {
    if(Utility.internetConnected()) {
      showProgress();
      WholeSellerHttpClient wholeSellerHttpClient = new WholeSellerHttpClient("/get_all_genres", RequestMethod.GET);
      wholeSellerHttpClient.setResponseListner(new ResponseListener() {
        @Override
        public void onResponse(int status, String response) {
          if (status == 200) {
            hideProgress();
            try {
              JSONArray jsonArray = new JSONArray(response);
              homeData.setHomeData(jsonArray);
              HomeRecyclerView homeRecyclerView = new HomeRecyclerView(mContext, jsonArray);
              recyclerView.setAdapter(homeRecyclerView);
            } catch (JSONException e) {
              e.printStackTrace();
              hideProgress();
              showSnackBar(mContext,coordinatorLayout,getString(R.string.error));
            }

          } else {
            hideProgress();
            showSnackBar(mContext,coordinatorLayout,getString(R.string.error));
          }
        }
      });
      wholeSellerHttpClient.setmHttpProtocol(Constants.HTTP);
      wholeSellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
      wholeSellerHttpClient.executeAsync();
    }else {
      showSnackBar(mContext,coordinatorLayout,getString(R.string.internet_connected));
    }
  }

  private void callSearchScreen() {
    Intent intent = new Intent(mContext, SearchScreen.class);
    startActivity(intent);
  }

  @Override
  public void retry() {
    super.retry();
    callHomeApi();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    coordinatorLayout= (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
    recyclerView = (RecyclerView) view.findViewById(R.id.item_list);
    progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
    layoutManager.setOrientation(GridLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    mVwPager = (ViewPager) view.findViewById(R.id.view_pager);
    CirclePageIndicator mPageIndicator = (CirclePageIndicator) view.findViewById(R.id.pager_indicator);
    final CardView searchCard = (CardView) view.findViewById(R.id.search_card);
    searchCard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        callSearchScreen();
      }
    });
    mVwPager.setAdapter(new CarousalImageAdapter(mContext));
    mPageIndicator.setViewPager(mVwPager);
    JSONArray jsonArray=homeData.getHomeData();
    if(jsonArray!=null && jsonArray.length()>0){
      HomeRecyclerView homeRecyclerView = new HomeRecyclerView(mContext, jsonArray);
      recyclerView.setAdapter(homeRecyclerView);
    }else {
      callHomeApi();
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
