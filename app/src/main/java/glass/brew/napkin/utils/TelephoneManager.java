package glass.brew.napkin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by yearnning on 15. 10. 4..
 */
public class TelephoneManager {

    private Activity mActivity = null;
    private String mTel = null;

    /**
     *
     * @param activity
     * @param tel
     */
    public TelephoneManager(Activity activity, String tel) {
        mActivity = activity;
        mTel = tel;
    }

    /**
     *
     */
    public void call(){
        if (!isTablet()) {
            startCallIntent();
        } else {
            Toast.makeText(mActivity, "이 기기는 전화기능을 지원하지 않습니다", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @return
     */
    private boolean isTablet() {
        return ((TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     *
     */
    private void startCallIntent() {

        /**
         *
         */
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mTel));
        mActivity.startActivity(intent);

        /**
         *
         */
        Tracker t = ((Application) mActivity.getApplication()).getTracker(Application.TrackerName.APP_TRACKER);
        t.send(new HitBuilders.EventBuilder()
                .setCategory("TelManager")
                .setAction("PressButton")
                .setLabel("TelBtn")
                .build());
    }
}
