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


  private String imagePath;
  private Double price;
  private String name;
  private String id;
  private Double marketPrice;

  public DetailObject(JSONObject jsonObject){
     try {
       if(jsonObject.has("name")){
         name=jsonObject.getString("name");
       }
       if(jsonObject.has("price")){
         price=jsonObject.getDouble("price");
       }
       if(jsonObject.has("_id")){
         id=jsonObject.getString("_id");
       }
       if(jsonObject.has("imagePath")){
         imagePath=jsonObject.getString("imagePath");
       }
       if(jsonObject.has("marketPrice")){
         marketPrice=jsonObject.getDouble("marketPrice");
       }
     }catch (JSONException e){
       Utility.reportException(e);
     }
  }

  protected DetailObject(Parcel in) {
    name = in.readString();
    price = in.readDouble();
    id = in.readString();
    imagePath=in.readString();
    marketPrice = in.readDouble();
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

  public String getImagePath() {
    return imagePath;
  }

  public String getId() {
    return id;

  }

  public Double getPrice() {
    return price;
  }

  public String getName() {
    return name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public Double getMarketPrice() {
    return marketPrice;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeDouble(price);
    dest.writeString(id);
    dest.writeString(imagePath);
    dest.writeDouble(marketPrice);
  }
}
