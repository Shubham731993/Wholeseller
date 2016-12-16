package abominable.com.wholeseller.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.Utility;
import abominable.com.wholeseller.common.WholesellerHttpClient;

/**
 * Created by shubham.srivastava on 17/07/16.
 */
public class DetailFragment extends Fragment {

  private Context context;
  private LinearLayoutManager mLayoutManager;
  private DetailFragmentAdapter qnaCityAdapter;
  private ArrayList<DetailObject> genresList;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.context=context;
  }

  public static DetailFragment newInstance(String id) {
    DetailFragment detailFragment = new DetailFragment();
    Bundle args = new Bundle();
    args.putString("id",id);
    detailFragment.setArguments(args);
    return detailFragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.detail_fragment, container, false);
    RecyclerView detailListView = (RecyclerView) view.findViewById(R.id.recycler_view);
    genresList=new ArrayList<>();
    mLayoutManager = new LinearLayoutManager(context);
    detailListView.setLayoutManager(mLayoutManager);

    String id=getArguments().getString("id");
    try {
      String encodedId= URLEncoder.encode(id,"UTF-8");
      callGenresApi(encodedId);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    qnaCityAdapter = new DetailFragmentAdapter(context,genresList);
    detailListView.setAdapter(qnaCityAdapter);
    return view;

  }

  private void callGenresApi(String id) {
    ((DetailActivity)context).showProgress("Please Wait",false);
    WholesellerHttpClient wholesellerHttpClient=new WholesellerHttpClient("/get_all_genres/"+id, RequestMethod.GET);
    wholesellerHttpClient.setResponseListner(new ResponseListener() {
      @Override
      public void onResponse(int status, String response) {
        if(status==200){
          try {
            ((DetailActivity)context).hideBlockingProgress();
            JSONArray genresArray=new JSONArray(response);
            for (int i=0;i<genresArray.length();i++){
              DetailObject jsonObject1=new DetailObject(genresArray.getJSONObject(i));
              genresList.add(jsonObject1);
            }
            qnaCityAdapter.notifyDataSetChanged();
          } catch (JSONException e) {
            Utility.reportException(e);
            ((DetailActivity)context).hideBlockingProgress();
            ((DetailActivity)context).showErrorDialog(null,getResources().getString(R.string.error));
          }
        }else {
          ((DetailActivity)context).hideBlockingProgress();
          ((DetailActivity)context).showErrorDialog(null,getResources().getString(R.string.error));
        }

      }
    });
    wholesellerHttpClient.setmHttpProtocol(Constants.HTTP);
    wholesellerHttpClient.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    wholesellerHttpClient.executeAsync();
  }
}
