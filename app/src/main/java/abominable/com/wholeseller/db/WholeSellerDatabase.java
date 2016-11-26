package abominable.com.wholeseller.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shubham.srivastava on 20/08/16.
 */
public class WholeSellerDatabase extends SQLiteOpenHelper {


  public WholeSellerDatabase(Context context) {
    super(context, DBHelperContract.DATABASE_NAME, null, DBHelperContract.DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String TEXT_TYPE = " TEXT";
    final String COMMA_SEP = ",";
    final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + DBHelperContract.WholeSellerDatabase.TABLE_NAME + " (" +
            DBHelperContract.WholeSellerDatabase._ID + " INTEGER PRIMARY KEY," +
            DBHelperContract.WholeSellerDatabase.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
            DBHelperContract.WholeSellerDatabase.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            " )";

    db.execSQL(SQL_CREATE_ENTRIES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + DBHelperContract.WholeSellerDatabase.TABLE_NAME;
    db.execSQL(SQL_DELETE_ENTRIES);
    onCreate(db);
  }
}
