package abominable.com.wholeseller.checkout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.srivastava on 14/10/16.
 */
public class CheckOutItem {

  private String quantity;

  public String getQuantity() {
    return quantity;
  }

  public CheckOutItem(JSONObject jsonObject) {
    try {
      if (jsonObject.has("quantity")) {
        quantity = jsonObject.getString("quantity");
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

  }
}
