package abominable.com.wholeseller;

import android.app.Application;
import android.content.Context;

import android.content.SharedPreferences;

import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


/**
 * Created by shubham.srivastava on 06/08/16.
 */
public class WholeMartApplication extends Application {
  private static Context context;
  private static SharedPreferences prefs;
  public static WholeMartApplication instance;

  public static WholeMartApplication getInstance() {
    return instance;
  }

  public static Context getAppContext() {
    return WholeMartApplication.context;
  }

  /**
   * @param k
   * @param v
   * @return
   */
  public static boolean setValue(final String k, final String v) {
    final SharedPreferences.Editor editor = WholeMartApplication.prefs.edit();
    editor.putString(k, v);
    editor.apply();
    return true;
  }

  public static boolean setValueWithCommit(final String k, final String v) {
    final SharedPreferences.Editor editor = WholeMartApplication.prefs.edit();
    editor.putString(k, v);
    return editor.commit();
  }

  public static boolean setValueWithCommit(final String k, final boolean v) {
    final SharedPreferences.Editor editor = WholeMartApplication.prefs.edit();
    editor.putBoolean(k, v);
    return editor.commit();
  }

  public static String getValue(final String k, final String d) {
    return WholeMartApplication.prefs.getString(k, d);
  }


  /**
   * @param k
   * @param v
   * @return
   */
  public static boolean setValue(final String k, final boolean v) {
    final SharedPreferences.Editor editor = WholeMartApplication.prefs.edit();
    editor.putBoolean(k, v);
    editor.apply();
    return true;
  }

  /**
   * @param k
   * @param v
   */
  public static void setValue(final String k, final long v) {
    final SharedPreferences.Editor editor = WholeMartApplication.prefs.edit();
    editor.putLong(k, v);
    editor.apply();
  }

  public static boolean removeKey(final String k) {
    return WholeMartApplication.prefs.edit().remove(k).commit();
  }

  /**
   * @param k
   * @param v
   * @return
   */
  public static long getValue(final String k, final long v) {
    return WholeMartApplication.prefs.getLong(k, v);
  }

  public static boolean getValue(final String k, final boolean d) {
    return WholeMartApplication.prefs.getBoolean(k, d);
  }

  /**
   * @param k
   * @param v
   * @return
   */
  public static boolean setValue(final String k, final int v) {
    final SharedPreferences.Editor editor = WholeMartApplication.prefs.edit();
    editor.putInt(k, v);
    editor.apply();
    return true;
  }


  /**
   * @param k
   * @param d
   * @return
   */
  public static int getValue(final String k, final int d) {
    return WholeMartApplication.prefs.getInt(k, d);
  }

  public static SharedPreferences getSharedPref() {
    return WholeMartApplication.prefs;
  }


  @Override
  public void onCreate() {
    super.onCreate();
    WholeMartApplication.context = getApplicationContext();
    WholeMartApplication.prefs = PreferenceManager.getDefaultSharedPreferences(WholeMartApplication.context.getApplicationContext());
    instance = this;
    Fabric.with(this, new Crashlytics());
  }
}