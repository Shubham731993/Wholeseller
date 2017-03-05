package abominable.com.wholeseller.hamburger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import abominable.com.wholeseller.R;

/**
 * Created by shubham.srivastava on 22/02/17.
 */

public class YourOrderAdapter extends RecyclerView.Adapter {

  private ArrayList<HamburgerOrderItem> hamburgerOrderItems;
  private Context context;

  public YourOrderAdapter(Context context,ArrayList<HamburgerOrderItem> hamburgerOrderItems) {
    this.hamburgerOrderItems = hamburgerOrderItems;
    this.context=context;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemLayoutView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.order_item, null);
    return new YourItemHolder(itemLayoutView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    YourItemHolder itemHolder = (YourItemHolder) holder;
    itemHolder.orderId.setText(hamburgerOrderItems.get(position).getOrderDisplayId());
    itemHolder.price.setText(context.getString(R.string.detail_price,String.valueOf(hamburgerOrderItems.get(position).getTotalPrice())));
    itemHolder.date.setText(hamburgerOrderItems.get(position).getDate());
    itemHolder.view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    });
  }

  @Override
  public int getItemCount() {
    return hamburgerOrderItems.size();
  }

  public class YourItemHolder extends RecyclerView.ViewHolder {
    TextView orderId;
    TextView price;
    TextView date;
    View view;

    public YourItemHolder(View itemLayoutView) {
      super(itemLayoutView);
      view = itemLayoutView;
      price = (TextView) itemLayoutView.findViewById(R.id.price);
      orderId = (TextView) itemLayoutView.findViewById(R.id.order_id);
      date = (TextView) itemLayoutView.findViewById(R.id.date);
    }
  }
}
