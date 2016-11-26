package abominable.com.wholeseller.common;

/**
 * Created by shubham.srivastava on 10/07/16.
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

  /**
   * @param params
   * @return
   */
  public static String addDataToJsonArray(final String params) {
    String newData = "";
    try {
      final JSONArray ja = new JSONArray();
      final JSONObject jo = new JSONObject(params);
      ja.put(jo);
      newData = ja.toString();
    } catch (final JSONException e) {
      return "";
    }
    return newData;
  }

  /**
   * @param d
   * @return
   */
  public static String removeDataFromJsonArray(final String d) {
    String newData = null;
    try {
      final JSONArray ja = new JSONArray(d);
      newData = ja.getJSONObject(0).toString();
    } catch (final JSONException e) {
      e.printStackTrace();
    }
    return newData;
  }

  /**
   * @param ctx
   * @return
   */
  public static boolean isInternetConnected(final Context ctx) {
    final ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean internetConnected = false;
    if (connectivity != null) {
      final NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (final NetworkInfo element : info) {
          if (element.getState() == State.CONNECTED) {
            internetConnected = true;
          }
        }
      }
    }
    return internetConnected;
  }

  /**
   * @param message
   * @return
   */
  public static String getMD5hash(final String message) {

    try {
      final MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(message.getBytes());
      final byte[] mb = md.digest();
      String out = "";
      for (final byte temp : mb) {
        String s = Integer.toHexString(temp);
        while (s.length() < 2) {
          s = "0" + s;
        }
        s = s.substring(s.length() - 2);
        out += s;
      }
      return out;
    } catch (final NoSuchAlgorithmException e) {
      return "";
    }
  }

  /**
   * @param is
   * @return
   */
  public static String getStringFromInputStream(final InputStream is) {
    if (is == null) {
      return null;
    }
    BufferedReader br = null;
    final StringBuilder sb = new StringBuilder();
    try {
      br = new BufferedReader(new InputStreamReader(is));
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }

    } catch (final IOException e) {
     e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }
    return sb.toString();
  }
}
