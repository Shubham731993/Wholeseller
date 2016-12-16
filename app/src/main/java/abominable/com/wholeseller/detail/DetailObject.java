package abominable.com.wholeseller.detail;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import abominable.com.wholeseller.common.Utility;

/**
 * Created by shubham.srivastava on 24/07/16.
 */
public class DetailObject implements Parcelable {


  private String price;
  private String name;
  private String id;

  public DetailObject(JSONObject jsonObject){
     try {
       if(jsonObject.has("name")){
         name=jsonObject.getString("name");
       }
       if(jsonObject.has("price")){
         price=jsonObject.getString("price");
       }
       if(jsonObject.has("_id")){
         id=jsonObject.getString("_id");
       }
     }catch (JSONException e){
       Utility.reportException(e);
     }
  }

  protected DetailObject(Parcel in) {
    price = in.readString();
    name = in.readString();
    id = in.readString();
  }

  public static final Creator<DetailObject> CREATOR = new Creator<DetailObject>() {
    @Override
    public DetailObject createFromParcel(Parcel in) {
      return new DetailObject(in);
    }

    @Override
    public DetailObject[] newArray(int size) {
      return new DetailObject[size];
    }
  };

  public String getId() {
    return id;
  }

  public String getPrice() {
    return price;
  }

  public String getName() {
    return name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(price);
    dest.writeString(name);
    dest.writeString(id);
  }
}
