package abominable.com.wholeseller.checkout;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abominable.com.wholeseller.R;

/**
 * Created by shubham.srivastava on 14/10/16.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

  private ArrayList<CheckOutItem> checkOutItems;
  private Typeface fontAwesomeFont;
  private Context context;

  public CheckOutAdapter(Context context,ArrayList<CheckOutItem> checkOutItemArrayList, Typeface fontAwesomeFont){
    this.checkOutItems=checkOutItemArrayList;
    this.fontAwesomeFont=fontAwesomeFont;
    this.context=context;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemLayoutView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.checkout_item, null);
    return new ViewHolder(itemLayoutView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    ViewHolder viewHolder=(ViewHolder) holder;
    viewHolder.itemQuantity.setText("Quantity :" +checkOutItems.get(position).getQuantity() + " kgs");
    viewHolder.crossIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((CheckoutActivity)context).callDeleteApi(checkOutItems.get(position),position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return checkOutItems.size();
  }

  private class ViewHolder extends RecyclerView.ViewHolder {
    TextView itemQuantity;
    TextView itemName;
    ImageView crossIcon;
    public ViewHolder(View itemLayoutView) {
      super(itemLayoutView);
      itemQuantity= (TextView) itemLayoutView.findViewById(R.id.item_number);
      itemName= (TextView) itemLayoutView.findViewById(R.id.item_name);
      crossIcon= (ImageView) itemLayoutView.findViewById(R.id.close_icon);
    }
  }

}
