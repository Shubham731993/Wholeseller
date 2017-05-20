package abominable.com.wholeseller.botttomNavigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.MainActivity;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.common.BaseFragment;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WMTextView;
import abominable.com.wholeseller.common.WholeSellerHttpClient;
import abominable.com.wholeseller.login.WholeMartLoginActivity;

/**
 * Created by shubham.srivastava on 29/04/17.
 */

public class ProfileFragment extends BaseFragment {
  public static ProfileFragment newInstance() {
    ProfileFragment fragment = new ProfileFragment();
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.profile_layout, container, false);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    RelativeLayout logout= (RelativeLayout) view.findViewById(R.id.logout);
    WMTextView email= (WMTextView) view.findViewById(R.id.email);
    WMTextView phone= (WMTextView) view.findViewById(R.id.phone);
    WMTextView name= (WMTextView) view.findViewById(R.id.name);
    WMTextView address= (WMTextView) view.findViewById(R.id.address);
    email.setText(WholeMartApplication.getValue(Constants.UserConstants.USER_EMAIL,""));
    phone.setText(WholeMartApplication.getValue(Constants.UserConstants.PHONE,""));
    name.setText(WholeMartApplication.getValue(Constants.UserConstants.USERNAME,""));
    address.setText(WholeMartApplication.getValue(Constants.UserConstants.USER_LOCATION,""));
    logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        callLogoutApi();
      }
    });
  }

  private void callLogoutApi() {
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(Constants.AUTH_KEY, WholeMartApplication.getValue(Constants.UserConstants.AUTH_KEY, ""));
      showProgress("Please Wait", false);
      WholeSellerHttpClient wholeSellerHttpClient = new WholeSellerHttpClient("/login/logout", jsonObject.toString(), RequestMethod.POST);
      wholeSellerHttpClient.setResponseListner(new ResponseListener() {
        @Override
        public void onResponse(int status, String response) {
          if (status == 200) {
            try {
              hideBlockingProgress();
              JSONObject jsonObject = new JSONObject(response);
              String returnValue = jsonObject.getString(Constants.MESSAGE);
              if (returnValue.equalsIgnoreCase("LOGOUT SUCCESSFUL") || returnValue.equalsIgnoreCase("ALREADY LOGGED OUT")) {
                WholeMartApplication.setValue(Constants.UserConstants.AUTH_KEY, "");
                Toast.makeText(mContext, "You have been logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, WholeMartLoginActivity.class);
                startActivity(intent);
                ((MainActivity)mContext).finish();
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
      wholeSellerHttpClient.setmHttpProtocol(Constants.HTTP);
      wholeSellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
      wholeSellerHttpClient.executeAsync();
    } catch (JSONException e) {
      Utility.reportException(e);
      showErrorDialog("", getString(R.string.error));
    }
  }
}
