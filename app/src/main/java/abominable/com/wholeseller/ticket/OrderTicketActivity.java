package abominable.com.wholeseller.ticket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import abominable.com.wholeseller.MainActivity;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.SpacesItemDecoration;
import abominable.com.wholeseller.common.WMTextView;

/**
 * Created by shubham.srivastava on 17/03/17.
 */

public class OrderTicketActivity extends BaseActivity {

  private DisplayOrder displayOrder;
  private RecyclerView recyclerView;
  private String orderFlow;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.final_order);
    if (getIntent() != null) {
      displayOrder = getIntent().getParcelableExtra(Constants.ORDER);
      orderFlow = getIntent().getStringExtra(Constants.ORDER_FLOW);
    } else {
      showErrorDialog(null, getString(R.string.error));
    }

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(OrderTicketActivity.this, MainActivity.class);
        startActivity(intent);
      }
    });
    getSupportActionBar().setTitle("Thank You !!");

    WMTextView status = (WMTextView) findViewById(R.id.status);
    WMTextView orderId = (WMTextView) findViewById(R.id.order_id);
    WMTextView date = (WMTextView) findViewById(R.id.booked_on);
    WMTextView email = (WMTextView) findViewById(R.id.email);
    WMTextView paid = (WMTextView) findViewById(R.id.you_paid);
    WMTextView callItem = (WMTextView) findViewById(R.id.call);
    recyclerView = (RecyclerView) findViewById(R.id.order_list);
    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.order_spacing);
    recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(new DisplayAdapter(this, displayOrder.getCheckOutItemArrayList()));
    orderId.setText(displayOrder.getOrderId());
    date.setText(displayOrder.getDate());
    email.setText(displayOrder.getEmail());
    paid.setText(getString(R.string.rupee_string, displayOrder.getTotalPrice()));


    callItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String number = "09654887711";
        Uri call = Uri.parse("tel:" + number);
        Intent surf = new Intent(Intent.ACTION_DIAL, call);
        startActivity(surf);
      }
    });

    switch (displayOrder.getStatus()) {
      case "tbd":
        status.setText("TO BE DELIVERED");
        status.setTextColor(ContextCompat.getColor(this, R.color.red));
        break;
      case "dispatched":
        status.setText("DISPATCHED");
        status.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        break;
      case "delivered":
        status.setText("DELIVERED");
        status.setTextColor(ContextCompat.getColor(this, R.color.confirm));
        break;
    }


  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if(orderFlow.equalsIgnoreCase(Constants.OrderFlow.ORDER_POST)) {
      Intent intent = new Intent(OrderTicketActivity.this, MainActivity.class);
      startActivity(intent);
    }
  }
}
