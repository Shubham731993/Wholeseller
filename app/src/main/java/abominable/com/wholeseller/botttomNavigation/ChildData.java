package abominable.com.wholeseller.botttomNavigation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.srivastava on 28/05/17.
 */

class ChildData {

  private String name;
  private String imagePath;

  public String getPrice() {
    return price;
  }

  private String price;

  public String getName() {
    return name;
  }

  public String getImagePath() {
    return imagePath;
  }

  public ChildData(JSONObject jsonObject) {
    try {
      if (jsonObject.has("name")) {
        name = jsonObject.getString("name");
      }
      if (jsonObject.has("imagePath")) {
        imagePath = jsonObject.getString("imagePath");
      }
      if (jsonObject.has("price")) {
        price = jsonObject.getString("price");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

}
