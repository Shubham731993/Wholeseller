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
  public static final int REQUEST_ADDRESS = 101;
  public static final String FETCH_ADDRESS_FLOW = "fetchAddressFlow";

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

}
