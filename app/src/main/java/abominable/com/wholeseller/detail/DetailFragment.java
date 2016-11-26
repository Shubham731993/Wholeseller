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

import java.util.ArrayList;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.Utility;

/**
 * Created by shubham.srivastava on 17/07/16.
 */
public class DetailFragment extends Fragment {

  private Context context;
  private LinearLayoutManager mLayoutManager;
  private DetailFragmentAdapter qnaCityAdapter;
  private DataInterface mCallback;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.context=context;
    try {
      mCallback= (DataInterface) context;
    }catch (Exception e){
      Utility.reportException(e);
    }
  }

  public interface DataInterface{
    ArrayList<DetailObject> getDetailItems(String id);
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
    String id=getArguments().getString("id");
    ArrayList<DetailObject> detailObject=mCallback.getDetailItems(id);
    mLayoutManager = new LinearLayoutManager(context);
    detailListView.setLayoutManager(mLayoutManager);
  /*  final RecyclerView.ItemDecoration itemDecoration =
        new LoaderItemDecorater(getActivity(), LinearLayoutManager.VERTICAL);
    detailListView.addItemDecoration(itemDecoration);*/
    qnaCityAdapter = new DetailFragmentAdapter(context,detailObject);
    detailListView.setAdapter(qnaCityAdapter);
    return view;

  }
}
