package abominable.com.wholeseller.ticket;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import abominable.com.wholeseller.checkout.CheckOutItem;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.Utility;

/**
 * Created by shubham.srivastava on 18/03/17.
 */

public class DisplayOrder implements Parcelable{

  private String email;
  private String status;
  private String orderId;
  private String totalPrice;
  private ArrayList<CheckOutItem> checkOutItemArrayList;
  private JSONArray jsonArray;
  private String date;

  public DisplayOrder(JSONObject jsonObject) {
    try {
      checkOutItemArrayList=new ArrayList<>();
      if (jsonObject.has(Constants.USER)) {
        email = jsonObject.getString(Constants.USER);
      }
      if (jsonObject.has(Constants.STATUS)) {
        status = jsonObject.getString(Constants.STATUS);
      }
      if (jsonObject.has(Constants.TOTAL_PRICE)) {
        totalPrice = jsonObject.getString(Constants.TOTAL_PRICE);
      }
      if (jsonObject.has(Constants.ORDER_NBR)) {
        orderId = jsonObject.getString(Constants.ORDER_NBR);
      }
      if (jsonObject.has(Constants.DATE)) {
        date = jsonObject.getString(Constants.DATE);
      }
      if (jsonObject.has(Constants.ITEMS_ORDER)) {
        JSONObject jsonObject2 = jsonObject.getJSONObject(Constants.ITEMS_ORDER);
        jsonArray = jsonObject2.getJSONArray(Constants.ITEMS_ORDER);
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject jsonObject1 = jsonArray.getJSONObject(i);
          CheckOutItem checkOutItem = new CheckOutItem(jsonObject1);
          checkOutItemArrayList.add(checkOutItem);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
      Utility.reportException(e);

    }

  }

  protected DisplayOrder(Parcel in) {
    email = in.readString();
    status = in.readString();
    orderId = in.readString();
    totalPrice = in.readString();
    date = in.readString();
    checkOutItemArrayList = in.createTypedArrayList(CheckOutItem.CREATOR);
  }

  public static final Creator<DisplayOrder> CREATOR = new Creator<DisplayOrder>() {
    @Override
    public DisplayOrder createFromParcel(Parcel in) {
      return new DisplayOrder(in);
    }

    @Override
    public DisplayOrder[] newArray(int size) {
      return new DisplayOrder[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  public String getDate() {
    return date;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(email);
    dest.writeString(status);
    dest.writeString(orderId);
    dest.writeString(totalPrice);
    dest.writeString(date);
    dest.writeTypedList(checkOutItemArrayList);
  }

  public String getEmail() {
    return email;
  }

  public String getStatus() {
    return status;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public ArrayList<CheckOutItem> getCheckOutItemArrayList() {
    return checkOutItemArrayList;
  }
}
