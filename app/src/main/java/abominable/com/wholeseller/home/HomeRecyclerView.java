package abominable.com.wholeseller.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import abominable.com.wholeseller.MainActivity;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.Utility;
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
  public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
    HomeItemHolder homeItemHolder = (HomeItemHolder) holder;
    try {
      homeItemHolder.itemName.setText(genresList.getString(position));
      switch (genresList.getString(position)){
        case "Dal":
          homeItemHolder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.daal));
          break;
        case "Rice":
          homeItemHolder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.rice));
          break;
        case "Dry Fruits":
          homeItemHolder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.dry_fruits));
          break;
      }
    } catch (JSONException e) {
      e.printStackTrace();
      Utility.reportException(e);
    }

    homeItemHolder.view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Intent homeIntent = new Intent(context, DetailActivity.class);
        try {
          homeIntent.putExtra(Constants.GENRE_NAME,genresList.getString(position));
        } catch (JSONException e) {
          e.printStackTrace();
        }
        context.startActivity(homeIntent);
        ((MainActivity) context).overridePendingTransition(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left);
      }
    });
  }

  @Override
  public int getItemCount() {
    return genresList.length();
  }

  private class HomeItemHolder extends RecyclerView.ViewHolder  {
    TextView itemName;
    View view;
    ImageView imageView;

    public HomeItemHolder(View itemLayoutView) {
      super(itemLayoutView);
      view=itemLayoutView;
      itemName = (TextView) itemLayoutView.findViewById(R.id.item_name);
      imageView = (ImageView) itemLayoutView.findViewById(R.id.image_view);
    }
  }
}
