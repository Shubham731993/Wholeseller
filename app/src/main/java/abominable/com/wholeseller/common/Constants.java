package abominable.com.wholeseller.common;

/**
 * Created by shubham.srivastava on 29/06/16.
 */
public class Constants {
  public static final int SUCCESS_RESULT = 0;
  public static final int FAILURE_RESULT = 1;
  public static final int MIN_PASSWORD_LENGTH = 8;
  public static final String PARAMS_FULLNAME = "fullName";
  public static final String PARAMS_PASSWORD = "pwd";
  public static final String PARAMS_EMAIL = "email";
  public static final String PARAMS_PHONE = "number";
  public static final String PARAMS_TYPE = "type";
  public static final String PARAMS_TOKEN = "deviceToken";
  public static final String PARAMS_ITEM_ID = "itemId";
  public static final String PARAMS_ITEM_NAME = "itemName";
  public static final String PARAMS_QUANTITY = "quantity";
  public static final String PARAMS_DAYS = "numOfdays";
  public static final String PARAMS_USERNAME = "username";
  public static final String PARAMS_SHAKEY = "shaKey";
  public static final String PACKAGE_NAME =
      "com.google.android.gms.location.sample.locationaddress";
  public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
  public static final String RESULT_DATA_KEY = PACKAGE_NAME +
      ".RESULT_DATA_KEY";
  public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
      ".LOCATION_DATA_EXTRA";
  public static final String HTTPS = "https://";
  public static final String HTTP = "http://";
  public static final String ORDER_ID = "orderId";
  public static final String AUTH_KEY = "authKey";
  public static final String MESSAGE="message";
  public static final String CURRENT_ORDER_ID="currentOrderId";
  public static final String PRICE = "itemPrice";
  public static final String IMAGE_PATH = "imagePath";
  public static final int REQUEST_ADDRESS = 101;
  public static final int REQUEST_NUMBER = 102;
  public static final String FETCH_ADDRESS_FLOW = "fetchAddressFlow";
  public static final String TOTAL_PRICE = "totalPrice";
  public static final String ENTER_NUMBER_FLOW = "numberFlow";
  public static final String DETAIL_OBJECT = "detailObject";
  public static final String FONT_ROBOTO_LIGHT_TTF = "font/Roboto-Light.ttf";
  public static final String FONT_ROBOTO_REGULAR_TTF = "font/Roboto-Regular.ttf";
  public static final String USER = "user";
  public static final String STATUS = "status";
  public static final String ORDER_NBR = "order_nbr";
  public static final String ITEMS_ORDER = "itemsInOrder";
  public static final String ORDER = "order";
  public static final String DATE = "date";
  public static final String PAYLOAD = "payload";
  public static final int NOTIFICATION_ID = 101;
  public static final int NOTIFICATION_ID_BIG_IMAGE = 102;
  public static final String REGISTRATION_COMPLETE = "reg_complete" ;
  public static final String SHARED_PREF = "shared_pref";
  public static final String PUSH_NOTIFICATION = "push";
  public static final String GENRE_NAME = "genre_name";
  public static final String ORDER_FLOW = "orderFlow";

  public static class DetailContants{
    public static final String GENRES="genres";
  }

  public static class UserConstants{
    public static final String USERNAME="username";
    public static final String USER_EMAIL="userEmail";
    public static final String USER_PHOTO_URL="photoUrl";
    public static final String USER_LOCATION="userLocation";
    public static final String AUTH_KEY="authKey";
    public static final String PHONE="phone";
  }

  public static class AddressConstants{
    public static final String DELIVERY_TYPE="deliveryType";
    public static final String ADDRESS="address";
    public static final String ADDRESS_LINE_ONE="line1";
    public static final String ADDRESS_LINE_TWO="line2";
    public static final String CITY="city";
    public static final String COMPANY="company";
    public static final String PHONE="phNum";
    public static final String PAYMENT_TYPE="paymentType";
    public static final String ORDER_ID="orderId";
  }

  public static class AddressFlow{
    public static final String CHECKOUT_ADDRESS="checkOutAddress";
    public static final String CHECKOUT_NO_ADDRESS="checkOutNoAddress";

  }

  public static class EnterNumberFlow{
    public static final String CREATE_ACCOUNT_FLOW="createAccountFlow";
    public static final String CHANGE_NUMBER_FLOW="changeNumberFlow";

  }

  public static class OrderFlow{
    public static final String ORDER_POST="post";
    public static final String ORDER_SHOW="show";

  }


  public interface HomeTyoe{
    String HOME_API="home_api";
    String LOGOUT_API="logout_api";
  }

}
