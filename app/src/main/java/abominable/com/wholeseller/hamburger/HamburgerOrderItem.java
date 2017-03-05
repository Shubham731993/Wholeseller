package abominable.com.wholeseller.hamburger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.srivastava on 22/02/17.
 */

public class HamburgerOrderItem {

  private String orderId;
  private String totalPrice;
  private String orderDisplayId;
  private String date;

  public String getOrderId() {
    return orderId;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public String getOrderDisplayId() {
    return orderDisplayId;
  }

  public String getDate() {
    return date;
  }

  public HamburgerOrderItem(JSONObject jsonObject) {
    try {
      if (jsonObject.has("date")) {
        date = jsonObject.getString("date");
      }
      if (jsonObject.has("orderId")) {
        orderId = jsonObject.getString("orderId");
      }
      if (jsonObject.has("totalPrice")) {
        totalPrice = jsonObject.getString("totalPrice");
      }
      if (jsonObject.has("order_nbr")) {
        orderDisplayId = jsonObject.getString("order_nbr");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

}
