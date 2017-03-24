package abominable.com.wholeseller.notification.utility;

import android.content.Intent;
import android.os.Bundle;

import abominable.com.wholeseller.common.BaseActivity;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.detail.DetailActivity;
import abominable.com.wholeseller.home.HomeActivity;


/**
 * Created by shubham.srivastava on 21/10/16.
 */

public class NotificationParser extends BaseActivity {
    private NotificationModel notificationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.PAYLOAD)) {
                notificationModel = (NotificationModel) getIntent().getParcelableExtra(Constants.PAYLOAD);
            }
            if (notificationModel != null) {
                Intent resultIntent = getNotificationScreenIntent(notificationModel);
                startActivity(resultIntent);
                finish();
            } else {
                callHomeActivity();
            }

        } else {
            callHomeActivity();
        }
    }

    private void callHomeActivity() {
        Intent intent = new Intent(NotificationParser.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public Intent getNotificationScreenIntent(NotificationModel notificationModel) {
        try {
            Intent intent = null;
            switch (notificationModel.getTagid()) {
                case NotificationTags.SHOW_ITEMS:
                    intent = new Intent(NotificationParser.this, DetailActivity.class);
                    startActivity(intent);
                    break;
            }
            if (intent != null) {
                return intent;
            } else {
                return new Intent(NotificationParser.this, HomeActivity.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Intent(NotificationParser.this, HomeActivity.class);
        }
    }

}
