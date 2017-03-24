package abominable.com.wholeseller.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.srivastava on 14/10/16.
 */
public class CheckOutItem implements Parcelable {

  private double quantity;
  private String id;
  private String imagePath;

  protected CheckOutItem(Parcel in) {
    quantity = in.readDouble();
    id = in.readString();
    imagePath = in.readString();
    itemPrice = in.readDouble();
    name = in.readString();
  }

  public static final Creator<CheckOutItem> CREATOR = new Creator<CheckOutItem>() {
    @Override
    public CheckOutItem createFromParcel(Parcel in) {
      return new CheckOutItem(in);
    }

    @Override
    public CheckOutItem[] newArray(int size) {
      return new CheckOutItem[size];
    }
  };

  public double getItemPrice() {
    return itemPrice;
  }

  private double itemPrice;

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  private String name;

  public double getQuantity() {
    return quantity;
  }

  public CheckOutItem(JSONObject jsonObject) {
    try {
      if (jsonObject.has("quantity")) {
        quantity = jsonObject.getDouble("quantity");
      }
      if (jsonObject.has("itemId")) {
        id = jsonObject.getString("itemId");
      }
      if (jsonObject.has("itemName")) {
        name = jsonObject.getString("itemName");
      }
      if (jsonObject.has("itemPrice")) {
        itemPrice = jsonObject.getDouble("itemPrice");
      }
      if(jsonObject.has("imagePath")){
        imagePath=jsonObject.getString("imagePath");
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  public String getImagePath() {
    return imagePath;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(quantity);
    dest.writeString(id);
    dest.writeString(imagePath);
    dest.writeDouble(itemPrice);
    dest.writeString(name);
  }
}
