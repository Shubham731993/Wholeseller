package abominable.com.wholeseller.notification.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shubham.srivastava on 19/03/17.
 */

public class NotificationModel implements Parcelable {
  private int tagid;
  private String message;

  public NotificationModel(){

  }

  protected NotificationModel(Parcel in) {
    tagid = in.readInt();
    message = in.readString();
  }

  public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
    @Override
    public NotificationModel createFromParcel(Parcel in) {
      return new NotificationModel(in);
    }

    @Override
    public NotificationModel[] newArray(int size) {
      return new NotificationModel[size];
    }
  };

  public int getTagid() {
    return tagid;
  }

  public String getMessage() {
    return message;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(tagid);
    dest.writeString(message);
  }

  public void setTagid(int tagid) {
    this.tagid = tagid;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}

