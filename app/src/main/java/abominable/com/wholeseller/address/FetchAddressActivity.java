package abominable.com.wholeseller.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.WholeMartApplication;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;

/**
 * Created by shubham.srivastava on 27/12/16.
 */
public class FetchAddressActivity extends BaseActivity implements View.OnClickListener {
  private EditText city, flat, apartment, company;
  private String flow;
  private String orderId;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_delivery_address);
    TextView done = (TextView) findViewById(R.id.button_done);
    city = (EditText) findViewById(R.id.edt_city);
    flat = (EditText) findViewById(R.id.edt_flat);
    apartment = (EditText) findViewById(R.id.edt_apartment);
    company = (EditText) findViewById(R.id.edt_company);
    flow = getIntent().getStringExtra(Constants.FETCH_ADDRESS_FLOW);
    if(getIntent().hasExtra(Constants.ORDER_ID)) {
      orderId = getIntent().getStringExtra(Constants.ORDER_ID);
    }
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    getSupportActionBar().setTitle("Address");
    done.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_done:
        if (validateData()) {
          StringBuilder builder = new StringBuilder();
          builder.append(flat.getText().toString())
              .append(",")
              .append(apartment.getText().toString())
              .append(",")
              .append(city.getText().toString());
          WholeMartApplication.setValue(Constants.UserConstants.USER_LOCATION, builder.toString());
          WholeMartApplication.setValue(Constants.AddressConstants.CITY, city.getText().toString());
          WholeMartApplication.setValue(Constants.AddressConstants.COMPANY, company.getText().toString());
          WholeMartApplication.setValue(Constants.AddressConstants.ADDRESS_LINE_ONE, flat.getText().toString());
          WholeMartApplication.setValue(Constants.AddressConstants.ADDRESS_LINE_TWO, apartment.getText().toString());
          switch (flow) {
            case Constants.AddressFlow.CHECKOUT_ADDRESS:
              Intent data = new Intent();
              data.putExtra(Constants.AddressConstants.ADDRESS, builder.toString());
              setResult(RESULT_OK, data);
              finish();
              break;
            case Constants.AddressFlow.CHECKOUT_NO_ADDRESS:
              Intent intent = new Intent(FetchAddressActivity.this, FinalAddressActivity.class);
              intent.putExtra(Constants.AddressConstants.ADDRESS, builder.toString());
              intent.putExtra(Constants.ORDER_ID, orderId);
              intent.putExtra(Constants.DETAIL_OBJECT,getIntent().getParcelableArrayExtra(Constants.DETAIL_OBJECT));
              intent.putExtra(Constants.TOTAL_PRICE,getIntent().getStringExtra(Constants.TOTAL_PRICE));
              startActivity(intent);
              break;
          }
        } else {
          Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }

    }
  }

  private boolean validateData() {
    if (TextUtils.isEmpty(city.getText().toString()) || TextUtils.isEmpty(flat.getText().toString()) || TextUtils.isEmpty(apartment.getText().toString()) || TextUtils.isEmpty(company.getText().toString()))
      return false;
    else return true;
  }
}
