package abominable.com.wholeseller.checkout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import abominable.com.wholeseller.R;

/**
 * Created by shubham.srivastava on 14/10/16.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

  private ArrayList<CheckOutItem> checkOutItems;

  public CheckOutAdapter(ArrayList<CheckOutItem> checkOutItemArrayList){
    this.checkOutItems=checkOutItemArrayList;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemLayoutView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.checkout_item, null);
    return new ViewHolder(itemLayoutView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ViewHolder viewHolder=(ViewHolder) holder;
    viewHolder.itemQuantity.setText("Quantity :" +checkOutItems.get(position).getQuantity() + " kgs");
  }

  @Override
  public int getItemCount() {
    return checkOutItems.size();
  }

  private class ViewHolder extends RecyclerView.ViewHolder {
    TextView itemQuantity;
    TextView itemName;
    public ViewHolder(View itemLayoutView) {
      super(itemLayoutView);
      itemQuantity= (TextView) itemLayoutView.findViewById(R.id.item_number);
      itemName= (TextView) itemLayoutView.findViewById(R.id.item_name);
    }
  }
}
