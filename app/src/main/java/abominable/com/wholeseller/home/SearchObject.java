package abominable.com.wholeseller.home;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.srivastava on 10/04/17.
 */

public class SearchObject {


  private String name;
  private String genreName;

  public SearchObject(JSONObject jsonObject){
    super();
    try {
      name = jsonObject.getString("name");
      genreName = jsonObject.getString("genreName");

    } catch (final JSONException e) {
      e.printStackTrace();
    }
  }

  public String getName() {
    return name;
  }

  public String getGenreName() {
    return genreName;
  }


}
