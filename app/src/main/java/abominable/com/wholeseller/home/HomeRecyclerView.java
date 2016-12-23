package abominable.com.wholeseller.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.detail.DetailActivity;

/**
 * Created by shubham.srivastava on 24/09/16.
 */
public class HomeRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private JSONArray genresList;
  private Context context;

  public HomeRecyclerView(Context context, JSONArray genresList) {
    this.genresList = genresList;
    this.context = context;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemLayoutView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.home_card_view, null);
    return new HomeItemHolder(itemLayoutView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    HomeItemHolder homeItemHolder = (HomeItemHolder) holder;
   // homeItemHolder.price.setText(context.getString(R.string.rupee_string, detailObjects.get(position).getPrice()));
    try {
      homeItemHolder.itemName.setText(genresList.getString(0));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    homeItemHolder.view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Intent homeIntent = new Intent(context, DetailActivity.class);
        context.startActivity(homeIntent);
        ((HomeActivity) context).overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      }
    });
  }

  @Override
  public int getItemCount() {
    return genresList.length();
  }

  private class HomeItemHolder extends RecyclerView.ViewHolder  {
    TextView itemName;
    TextView price;
    View view;

    public HomeItemHolder(View itemLayoutView) {
      super(itemLayoutView);
      view=itemLayoutView;
      itemName = (TextView) itemLayoutView.findViewById(R.id.item_name);
      price = (TextView) itemLayoutView.findViewById(R.id.price);
    }
  }
}
