package abominable.com.wholeseller.detail;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;


/**
 * Created by shubham.srivastava on 17/07/16.
 */
public class DetailFragmentAdapter extends RecyclerView.Adapter {
  private ArrayList<DetailObject> detailItemList;
  private Context context;
  public DetailFragmentAdapter(Context context,ArrayList<DetailObject> detailItemList) {
    this.detailItemList=detailItemList;
    this.context=context;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemLayoutView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.detail_item, null);
    return new DetailItemHolder(itemLayoutView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    DetailItemHolder detailItemHolder=(DetailItemHolder)holder;
    detailItemHolder.itemName.setText(detailItemList.get(position).getName());
    detailItemHolder.price.setText(context.getResources().getString(R.string.detail_price,String.valueOf(detailItemList.get(position).getPrice())));
    detailItemHolder.marketPrice.setText(context.getResources().getString(R.string.detail_price,String.valueOf(detailItemList.get(position).getMarketPrice())));
    detailItemHolder.marketPrice.setPaintFlags(detailItemHolder.marketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    detailItemHolder.cardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager manager = ((DetailActivity)context).getFragmentManager();
        DialogFragment dialogFragment=AddToCartFragment.newInstance(detailItemList.get(position),position);
        dialogFragment.show(manager,"dialog");

      }
    });
    String imagePath= BuildConfig.IMAGE_BUCKET_HOST+detailItemList.get(position).getImagePath()+".jpg";
    Picasso.with(context).load(imagePath).into(detailItemHolder.itemImage);
  }

  @Override
  public int getItemCount() {
    return detailItemList.size();
  }

  public class DetailItemHolder extends RecyclerView.ViewHolder {
    public TextView itemName;
    public TextView price;
    public TextView marketPrice;
    public TextView itemNumber;
    public ImageView itemImage;
    public CardView cardView;
    public DetailItemHolder(View itemLayoutView) {
      super(itemLayoutView);
      itemName = (TextView) itemLayoutView.findViewById(R.id.item_name);
      price = (TextView) itemLayoutView.findViewById(R.id.scratched_price);
      marketPrice = (TextView) itemLayoutView.findViewById(R.id.market_price);
      cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
      itemNumber = (TextView) itemLayoutView.findViewById(R.id.item_number);
      itemImage = (ImageView) itemLayoutView.findViewById(R.id.image_view);
    }
  }
}
