package abominable.com.wholeseller.hamburger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.ticket.DisplayOrder;
import abominable.com.wholeseller.ticket.OrderTicketActivity;

/**
 * Created by shubham.srivastava on 22/02/17.
 */

public class YourOrderAdapter extends RecyclerView.Adapter {

  private ArrayList<DisplayOrder> displayOrderItems;
  private Context context;

  public YourOrderAdapter(Context context,ArrayList<DisplayOrder> displayOrderItems) {
    this.displayOrderItems = displayOrderItems;
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
    itemHolder.orderId.setText(displayOrderItems.get(position).getOrderId());
    itemHolder.price.setText(context.getString(R.string.detail_price,String.valueOf(displayOrderItems.get(position).getTotalPrice())));
    itemHolder.date.setText(displayOrderItems.get(position).getDate());
    itemHolder.view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(context, OrderTicketActivity.class);
        intent.putExtra(Constants.ORDER,displayOrderItems.get(position));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return displayOrderItems.size();
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
