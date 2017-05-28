package abominable.com.wholeseller.botttomNavigation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.MainActivity;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.BaseFragment;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholeSellerHttpClient;
import abominable.com.wholeseller.detail.DetailActivity;
import abominable.com.wholeseller.home.CarousalImageAdapter;
import abominable.com.wholeseller.home.SearchScreen;

/**
 * Created by shubham.srivastava on 29/04/17.
 */

public class HomeFragment extends BaseFragment {

  //private RecyclerView recyclerView;
  private ViewPager mVwPager;
  private CoordinatorLayout coordinatorLayout;
  private HomeData homeData;
  private ProgressBar progressBar;
  private JSONArray homeItems;

  ExpandableListAdapter listAdapter;
  ExpandableListView expListView;
  List<String> listDataHeader;
  HashMap<String, List<ChildData>> listDataChild;

  public static HomeFragment newInstance() {
    HomeFragment fragment = new HomeFragment();
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    homeData = (MainActivity) context;
  }

  public interface HomeData {
    JSONObject getHomeData();

    void setHomeData(JSONObject jsonObject);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.app_bar_main, container, false);
  }

  private void callHomeApi() {
    if (Utility.internetConnected()) {
      showProgress();
      WholeSellerHttpClient wholeSellerHttpClient = new WholeSellerHttpClient("/custom_homepage_items/homepage_vers2_00", RequestMethod.GET);
      wholeSellerHttpClient.setResponseListner(new ResponseListener() {
        @Override
        public void onResponse(int status, String response) {
          if (status == 200) {
            hideProgress();
            try {
              JSONObject jsonObject = new JSONObject(response);
              homeData.setHomeData(jsonObject);
              if (jsonObject.has("homeItems")) {
                homeItems=jsonObject.getJSONArray("homeItems");
                prepareListData(homeItems);
              }
            } catch (JSONException e) {
              e.printStackTrace();
              hideProgress();
              showSnackBar(mContext, coordinatorLayout, getString(R.string.error));
            }

          } else {
            hideProgress();
            showSnackBar(mContext, coordinatorLayout, getString(R.string.error));
          }
        }
      });
      wholeSellerHttpClient.setmHttpProtocol(Constants.HTTP);
      wholeSellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
      wholeSellerHttpClient.executeAsync();
    } else {
      showSnackBar(mContext, coordinatorLayout, getString(R.string.internet_connected));
    }
  }

  private void callSearchScreen() {
    Intent intent = new Intent(mContext, SearchScreen.class);
    startActivity(intent);
  }

  @Override
  public void retry() {
    super.retry();
    //callHomeApi();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
    expListView = (ExpandableListView) view.findViewById(R.id.item_list);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      expListView.setNestedScrollingEnabled(true);
    }
    expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

      @Override
      public boolean onGroupClick(ExpandableListView parent, View v,
                                  int groupPosition, long id) {
        return false;
      }
    });

    expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

      @Override
      public boolean onChildClick(ExpandableListView parent, View v,
                                  int groupPosition, int childPosition, long id) {
        callDetailActivity(groupPosition);
        return false;
      }
    });


    // Listview Group expanded listener
    expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

      @Override
      public void onGroupExpand(int groupPosition) {
       // view1.setBackgroundColor(ContextCompat.getColor(mContext,R.color.yellow));

      }
    });

    // Listview Group collasped listener
    expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

      @Override
      public void onGroupCollapse(int groupPosition) {

      }
    });


    progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
/*    GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
    layoutManager.setOrientation(GridLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);*/
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
    JSONObject jsonObject = homeData.getHomeData();
    try {
      if (jsonObject != null && jsonObject.has("homeItems")) {
        homeItems=jsonObject.getJSONArray("homeItems");
        prepareListData(homeItems);
      } else {
        callHomeApi();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void callDetailActivity(int groupPosition) {
    try {
      final Intent intent = new Intent(mContext, DetailActivity.class);
      if(homeItems!=null && homeItems.getJSONObject(groupPosition)!=null && homeItems.getJSONObject(groupPosition).has("title"))
        intent.putExtra(Constants.GENRE_NAME,homeItems.getJSONObject(groupPosition).getString("title"));
      mContext.startActivity(intent);
      ((MainActivity) mContext).overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void showProgress() {
    progressBar.setVisibility(View.VISIBLE);
    expListView.setVisibility(View.GONE);
  }

  private void hideProgress() {
    progressBar.setVisibility(View.GONE);
    expListView.setVisibility(View.VISIBLE);
  }


  private void prepareListData(JSONArray jsonArray) {
    try {
      listDataHeader = new ArrayList<>();
      listDataChild = new HashMap<>();
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        listDataHeader.add(jsonObject.getString("title"));
        JSONArray childData = jsonObject.getJSONArray("item");
        List<ChildData> childDataList = new ArrayList<>();
        for (int j = 0; j < childData.length(); j++) {
          ChildData childData1 = new ChildData(childData.getJSONObject(j));
          childDataList.add(childData1);
        }
        listDataChild.put(listDataHeader.get(i), childDataList);
      }
      listAdapter = new ExpandableListAdapter(mContext, listDataHeader, listDataChild);
      expListView.setAdapter(listAdapter);

    } catch (JSONException e) {
      e.printStackTrace();
      showSnackBar(mContext, coordinatorLayout, getString(R.string.error));
    }

  }

}
