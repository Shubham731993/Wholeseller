package abominable.com.wholeseller.common;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.WholeMartApplication;

/**
 * Created by shubham.srivastava on 10/07/16.
 */
public class WholeSellerHttpClient extends AsyncTask<Void,Void,String> {


  private static final String TAG = WholeSellerHttpClient.class.getSimpleName();
  private final String mBaseUrl;
  private final RequestMethod mRequestMethod;
  private String mData;
  private WholeSellerHttpClient mlastRequest;
  private String mHttpProtocol= Constants.HTTP;
  private String mHttpHost= BuildConfig.APP_ENGINE_HOST;


  public static final int HTTP_OK = 200;
  private static final int HTTP_ERROR = 500;
  private static final String NETWORK_ERROR_MESSAGE = "Wholemart was unable to establish a network connection to fulfil your request. Please check your network connection and try again.";
  private static final int HTTP_AUTHENTICATE = 401;
  private static final int LOGIN_RETRY_LIMIT = 2;
  private static final String AUTH_HEADER = "Authorization";
  private static final String ACCEPT_HEADER = "Accept";
  private static final String ACCEPT_ENCODING = "Accept-Encoding";
  private static int loginRetryCount;
  private ResponseListener mListner;
  private String mEmail;
  private int mStatus;

  public WholeSellerHttpClient(final String url, final RequestMethod rm) {
    super();
    mBaseUrl = url;
    mRequestMethod = rm;
  }

  /**
   * @param url
   * @param params
   * @param rm
   */
  public WholeSellerHttpClient(final String url, final String params, final RequestMethod rm) {
    super();
    mBaseUrl = url;
    mData = params;
    mRequestMethod = rm;
  }
  @Override
  protected String doInBackground(Void... params) {
    mlastRequest = this;
      Log.i(WholeSellerHttpClient.TAG, "Request URL: " + mHttpProtocol + mHttpHost + mBaseUrl);
      Log.i(WholeSellerHttpClient.TAG, "Request Method: " + mRequestMethod);
      Log.i(WholeSellerHttpClient.TAG, "Request Data: " + mData);

    String result = "";
    switch (mRequestMethod) {
      case GET:
        result = getHttp();
        break;
      case POST:
        result = postHttp();
        break;
      case PUT:
        result = putHttp();
        break;
      case DELETE:
        result = deleteHttp();
        break;
      default:
        break;
    }
    return result;
  }



  @Override
  protected void onPostExecute(final String result) {
    super.onPostExecute(result);
    if (mStatus == WholeSellerHttpClient.HTTP_AUTHENTICATE) {
      if (WholeSellerHttpClient.loginRetryCount <= WholeSellerHttpClient.LOGIN_RETRY_LIMIT) {
        //loginIntoReviewsSystem();
      } else {
        if (mListner != null) {
          mListner.onResponse(mStatus, result);
        }
      }
    } else {
      if (mListner != null) {
        mListner.onResponse(mStatus, result);
      }
    }
  }


  private String getHttp() {
    try {
      URL url = new URL(mHttpProtocol + mHttpHost + mBaseUrl);
      HttpURLConnection getConn = (HttpURLConnection) url.openConnection();
      getConn.setRequestMethod("GET");
      setHeaders(getConn);
      final int status = getConn.getResponseCode();
      InputStream in = null;
      if (getConn.getErrorStream() != null) {
        in = getConn.getErrorStream();
      }

      if (status == HttpsURLConnection.HTTP_OK) {
        in = getConn.getInputStream();
        final String contentEncoding = getConn.getHeaderField("Content-Encoding");
        if ((contentEncoding != null) && "gzip".equalsIgnoreCase(contentEncoding)) {
          in = new GZIPInputStream(in);
        }
      }

      return processAndSendResponse(status,in);
    } catch (final Exception e) {
      mStatus = WholeSellerHttpClient.HTTP_ERROR;
      return WholeSellerHttpClient.NETWORK_ERROR_MESSAGE;
    }
  }

  /**
   * @return
   */
  private String postHttp() {
    try {
      URL url = new URL(mHttpProtocol + mHttpHost + mBaseUrl);
      HttpURLConnection postConn = (HttpURLConnection) url.openConnection();
      postConn.setRequestMethod("POST");
      setHeaders(postConn);
      if (mData != null) {
        postConn.setDoOutput(true);
        postConn.setRequestProperty("Content-Length", Integer.toString(mData.length()));
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(postConn.getOutputStream(), "UTF-8");
        final PrintWriter out = new PrintWriter(outputStreamWriter, true);
        out.print(mData);
        out.close();
      }
      final int status = postConn.getResponseCode();
      InputStream in = null;
      if (postConn.getErrorStream() != null) {
        in = postConn.getErrorStream();
      }

      if (status == HttpsURLConnection.HTTP_OK) {
        in = postConn.getInputStream();
        final String contentEncoding = postConn.getHeaderField("Content-Encoding");
        if ((contentEncoding != null) && "gzip".equalsIgnoreCase(contentEncoding)) {
          in = new GZIPInputStream(in);
        }
      }

      return processAndSendResponse(status,in);

    } catch (final Exception e) {
      mStatus = WholeSellerHttpClient.HTTP_ERROR;
      return WholeSellerHttpClient.NETWORK_ERROR_MESSAGE;
    }
  }

  /**
   * @return
   */
  private String putHttp() {
    try {
      URL url = new URL(mHttpProtocol + mHttpHost + mBaseUrl);
      HttpURLConnection putConn = (HttpURLConnection) url.openConnection();
      putConn.setRequestMethod("PUT");
      setHeaders(putConn);

      if (mData != null) {
        putConn.setDoOutput(true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(putConn.getOutputStream(), "UTF-8");
        PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);
        printWriter.print(mData);
        printWriter.flush();
        printWriter.close();
      }

      final int status = putConn.getResponseCode();
      InputStream in = null;
      if (putConn.getErrorStream() != null) {
        in = putConn.getErrorStream();
      }

      if (status == HttpsURLConnection.HTTP_OK) {
        in = putConn.getInputStream();
        final String contentEncoding = putConn.getHeaderField("Content-Encoding");
        if ((contentEncoding != null) && "gzip".equalsIgnoreCase(contentEncoding)) {
          in = new GZIPInputStream(in);
        }
      }

      return processAndSendResponse(status,in);
    } catch (final Exception e) {
      mStatus = WholeSellerHttpClient.HTTP_ERROR;
      return WholeSellerHttpClient.NETWORK_ERROR_MESSAGE;
    }
  }

  /**
   * @return
   */
  private String deleteHttp() {
    try {
//      final HttpClient client = new DefaultHttpClient();
//      final HttpDelete delete = new HttpDelete(mHttpProtocol + mHttpHost + mBaseUrl);
//      setHeaders(delete);

      URL url = new URL(mHttpProtocol + mHttpHost + mBaseUrl);
      HttpURLConnection deleteConn = (HttpURLConnection) url.openConnection();
      deleteConn.setRequestMethod("DELETE");
      setHeaders(deleteConn);

      final int status = deleteConn.getResponseCode();
      InputStream in = null;
      if (deleteConn.getErrorStream() != null) {
        in = deleteConn.getErrorStream();
      }

      if (status == HttpsURLConnection.HTTP_OK) {
        in = deleteConn.getInputStream();
        final String contentEncoding = deleteConn.getHeaderField("Content-Encoding");
        if ((contentEncoding != null) && "gzip".equalsIgnoreCase(contentEncoding)) {
          in = new GZIPInputStream(in);
        }
      }

      return processAndSendResponse(status,in);
    } catch (final Exception e) {
      mStatus = WholeSellerHttpClient.HTTP_ERROR;
      return WholeSellerHttpClient.NETWORK_ERROR_MESSAGE;
    }
  }

  private void setHeaders(final HttpURLConnection connection) {
    connection.addRequestProperty("Content-type", "application/json");
    connection.setRequestProperty(WholeSellerHttpClient.ACCEPT_HEADER, "application/json");
    connection.setRequestProperty(WholeSellerHttpClient.ACCEPT_ENCODING, "gzip");
    connection.setRequestProperty(WholeSellerHttpClient.AUTH_HEADER, WholeMartApplication.getValue(Constants.AUTH_KEY,""));
  }


  private String processAndSendResponse(final int statusCode, final InputStream in) {

    try {
      mStatus = statusCode;
      Log.i(WholeSellerHttpClient.TAG, "Reponse Code" + mStatus);
      return Util.getStringFromInputStream(in);
    } catch (final Exception e) {
      mStatus = WholeSellerHttpClient.HTTP_ERROR;
      return WholeSellerHttpClient.NETWORK_ERROR_MESSAGE;
    }
  }

  public void setmHttpHost(final String mHttpHost) {
    this.mHttpHost = mHttpHost;
  }

  public void setmHttpProtocol(final String mHttpProtocol) {
    this.mHttpProtocol = mHttpProtocol;
  }

  /**
   *
   */
  public void executeAsync() {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      } else {
        execute();
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void setResponseListner(final ResponseListener listner) {
    mListner = listner;
  }

  @Override
  public String toString() {
    return "WholeSellerHttpClient{" +
        "mBaseUrl='" + mBaseUrl + '\'' +
        ", mRequestMethod=" + mRequestMethod +
        ", mHttpHost='" + mHttpHost + '\'' +
        ", mHttpProtocol='" + mHttpProtocol + '\'' +
        ", mData='" + mData + '\'' +
        ", mListner=" + mListner +
        ", mEmail='" + mEmail + '\'' +
        ", mStatus=" + mStatus +
        ", mlastRequest=" + mlastRequest +
        '}';
  }
}
