package abominable.com.wholeseller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.common.RequestMethod;
import abominable.com.wholeseller.common.ResponseListener;
import abominable.com.wholeseller.common.WholesellerHttpClient;

/**
 * Created by shubham.srivastava on 10/07/16.
 */
public class CheckApiCall extends AppCompatActivity{
  private TextView textView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.check_api);
    textView = (TextView) findViewById(R.id.dummy);
    callApi();
  }

  private void callApi() {
    final WholesellerHttpClient client = new WholesellerHttpClient("/wholemart", RequestMethod.GET);

    client.setResponseListner(new ResponseListener() {

      @Override
      public void onResponse(final int status, final String response) {
        if (status == WholesellerHttpClient.HTTP_OK) {
          try {
            textView.setText(response);
          } catch (final Exception e) {
            e.printStackTrace();
          }
        }
      }
    });
    client.setmHttpProtocol(Constants.HTTP);
    client.setmHttpHost(BuildConfig.APP_ENGINE_HOST);
    client.executeAsync();
  }
}
