package abominable.com.wholeseller.db;

import android.provider.BaseColumns;

/**
 * Created by shubham.srivastava on 20/08/16.
 */
public final class DBHelperContract {

  public static final String DATABASE_NAME = "wholemart.db";
  public static final int DATABASE_VERSION = 1;

  DBHelperContract(){}

  public static abstract class WholeSellerDatabase implements BaseColumns{
    public static final String TABLE_NAME = "orders";
    public static final String COLUMN_NAME_ENTRY_ID = "entryid";
    public static final String COLUMN_NAME_TITLE = "title";
  }

}
