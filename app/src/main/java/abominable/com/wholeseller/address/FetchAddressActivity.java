package abominable.com.wholeseller.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;

/**
 * Created by shubham.srivastava on 27/12/16.
 */
public class FetchAddressActivity extends BaseActivity implements View.OnClickListener{
  private EditText city,flat,apartment,company;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_delivery_address);
    TextView done= (TextView) findViewById(R.id.button_done);
    city= (EditText) findViewById(R.id.edt_city);
    flat= (EditText) findViewById(R.id.edt_flat);
    apartment= (EditText) findViewById(R.id.edt_apartment);
    company= (EditText) findViewById(R.id.edt_company);
    done.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_done:
        if(validateData()) {
          Intent data = new Intent();
          StringBuilder builder=new StringBuilder();
          builder.append(flat.getText().toString())
              .append(",")
              .append(apartment.getText().toString())
              .append(",")
              .append(city.getText().toString());
          data.putExtra(Constants.AddressConstants.ADDRESS, builder.toString());
          data.putExtra(Constants.AddressConstants.CITY, city.getText().toString());
          data.putExtra(Constants.AddressConstants.COMPANY, company.getText().toString());
          data.putExtra(Constants.AddressConstants.ADDRESS_LINE_ONE, flat.getText().toString());
          data.putExtra(Constants.AddressConstants.ADDRESS_LINE_TWO, apartment.getText().toString());
          setResult(RESULT_OK,data);
          finish();
          break;
        }else {
          Toast.makeText(this,"All fields are required",Toast.LENGTH_SHORT).show();
        }
    }
  }

  private boolean validateData() {
    if(TextUtils.isEmpty(city.getText().toString())  || TextUtils.isEmpty(flat.getText().toString()) || TextUtils.isEmpty(apartment.getText().toString()))
    return false;
    else return true;
  }
}
