package abominable.com.wholeseller.ticket;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.checkout.CheckOutItem;

/**
 * Created by shubham.srivastava on 18/03/17.
 */

public class DisplayAdapter extends RecyclerView.Adapter {
  private ArrayList<CheckOutItem> checkOutItemArrayList;
  private Context mContext;
  public DisplayAdapter(Context context,ArrayList<CheckOutItem> checkOutItemArrayList) {
    this.checkOutItemArrayList=checkOutItemArrayList;
    mContext=context;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemLayoutView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.final_display_item, null);
    return new ViewHolder(itemLayoutView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ViewHolder holder1=(ViewHolder)holder;
    holder1.itemName.setText(checkOutItemArrayList.get(position).getName());
    holder1.quantity.setText(mContext.getResources().getString(R.string.quantity,String.valueOf(checkOutItemArrayList.get(position).getQuantity())));
    holder1.itemPrice.setText(mContext.getResources().getString(R.string.rupee_string,String.valueOf(checkOutItemArrayList.get(position).getItemPrice())));
  }

  @Override
  public int getItemCount() {
    return checkOutItemArrayList.size();
  }
  private class ViewHolder extends RecyclerView.ViewHolder {
    TextView itemName;
    TextView quantity;
    TextView itemPrice;

    public ViewHolder(View itemLayoutView) {
      super(itemLayoutView);
      itemName= (TextView) itemLayoutView.findViewById(R.id.item_name);
      quantity= (TextView) itemLayoutView.findViewById(R.id.item_quantity);
      itemPrice= (TextView) itemLayoutView.findViewById(R.id.item_price);
    }
  }
}
