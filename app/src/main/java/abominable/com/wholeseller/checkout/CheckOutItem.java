package abominable.com.wholeseller.checkout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.srivastava on 14/10/16.
 */
public class CheckOutItem {

  private double quantity;
  private String id;
  private String imagePath;

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
}
