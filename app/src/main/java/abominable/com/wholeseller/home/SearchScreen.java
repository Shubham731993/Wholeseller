package abominable.com.wholeseller.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import abominable.com.wholeseller.common.WholeSellerHttpClient;

/**
 * Created by shubham.srivastava on 10/04/17.
 */

public class SearchScreen extends BaseActivity {
  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private SearchAdapter searchAdapter;
  private ArrayList<SearchObject> searchObjects;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.search_screen);
    recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
    progressBar= (ProgressBar) findViewById(R.id.progress_bar);
    searchObjects = new ArrayList<>();
    callSearchApi();
    final SearchView searchView= (SearchView) findViewById(R.id.search_view);
    searchView.setQueryHint("Search");
    searchView.setFocusable(true);
    searchView.setIconified(true);
    searchView.clearFocus();
    searchView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchView.setIconified(false);
      }
    });
    searchAdapter=new SearchAdapter(this,searchObjects);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(searchAdapter);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(final String query) {
        searchView.clearFocus();
        return false;
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        searchAdapter.getFilter().filter(newText);
        return false;
      }
    });
  }

  private void callSearchApi() {
    showProgress();
    WholeSellerHttpClient wholeSellerHttpClient = new WholeSellerHttpClient("/custom_homepage_items", RequestMethod.GET);
    wholeSellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if (status == 200) {
          try {
            hideProgress();
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0;i<jsonArray.length();i++){
              SearchObject searchObject=new SearchObject(jsonArray.getJSONObject(i));
              searchObjects.add(searchObject);
            }
            searchAdapter.notifyDataSetChanged();

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
    wholeSellerHttpClient.setmHttpProtocol(Constants.HTTP);
    wholeSellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    wholeSellerHttpClient.executeAsync();
  }

  private void hideProgress() {
    recyclerView.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  private void showProgress() {
    recyclerView.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }
}
