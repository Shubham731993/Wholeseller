package abominable.com.wholeseller.botttomNavigation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;

import java.util.ArrayList;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.MainActivity;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.BaseFragment;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholeSellerHttpClient;
import abominable.com.wholeseller.hamburger.YourOrderAdapter;
import abominable.com.wholeseller.ticket.DisplayOrder;

/**
 * Created by shubham.srivastava on 29/04/17.
 */

public class YourOrderFragment extends BaseFragment {

  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private YourOrderAdapter yourOrderAdapter;
  private ArrayList<DisplayOrder> displayOrders;
  private OrderData orderData;

  public static YourOrderFragment newInstance() {
    YourOrderFragment fragment = new YourOrderFragment();
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext=context;
    orderData=(MainActivity)context;
  }

  public interface OrderData {
    ArrayList<DisplayOrder> getOrderData();
    void setOrderData(ArrayList<DisplayOrder> displayOrders);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.your_order, container, false);
    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    return view;
  }

  private void callOrderApi() {
    try {
      showProgress();
      WholeSellerHttpClient wholeSellerHttpClient = new WholeSellerHttpClient("/get_user_orders", RequestMethod.GET);
      wholeSellerHttpClient.setResponseListner(new ResponseListener() {
        @Override
        public void onResponse(int status, String response) {
          if (status == 200) {
            try {
              hideProgress();
              JSONArray jsonArray = new JSONArray(response);
              for (int i=0;i<jsonArray.length();i++) {
                DisplayOrder displayOrder = new DisplayOrder(jsonArray.getJSONObject(i));
                displayOrders.add(displayOrder);
              }
              orderData.setOrderData(displayOrders);
              yourOrderAdapter=new YourOrderAdapter(mContext,displayOrders);
              recyclerView.setAdapter(yourOrderAdapter);
            } catch (Exception e) {
              Utility.reportException(e);
              hideProgress();
              ((MainActivity)mContext).showError(getString(R.string.error));
            }
          } else {
            hideProgress();
            ((MainActivity)mContext).showError(getString(R.string.error));
          }
        }
      });
      wholeSellerHttpClient.setmHttpProtocol(Constants.HTTP);
      wholeSellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
      wholeSellerHttpClient.executeAsync();
    } catch (Exception e) {
      Utility.reportException(e);
      hideProgress();
      ((MainActivity)mContext).showError(getString(R.string.error));
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

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString("s","s");
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    displayOrders=orderData.getOrderData();
    if(displayOrders!=null && displayOrders.size()>0){
      yourOrderAdapter=new YourOrderAdapter(mContext,displayOrders);
      recyclerView.setAdapter(yourOrderAdapter);
    }else {
      displayOrders=new ArrayList<>();
      callOrderApi();
    }
  }
}
