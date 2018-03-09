package glass.brew.napkin.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import glass.brew.napkin.R;
import glass.brew.napkin.utils.Application;
import glass.brew.napkin.utils.Argument;
import glass.brew.napkin.utils.PreferenceManager;

public class SplashActivity extends ActionBarActivity {
    private static final String TAG = "Splash Activity";

    /**
     *
     */

    LoginApiTask mLoginApiTask = null;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        /**
         *
         */
        Tracker t = ((Application) getApplication()).getTracker(Application.TrackerName.APP_TRACKER);
        t.setScreenName("SplashActivity");
        t.send(new HitBuilders.AppViewBuilder().build());


        /**
         *
         */
        mLoginApiTask = new LoginApiTask();
        mLoginApiTask.execute();

    }

    @Override
    public void onDestroy() {
        if (mLoginApiTask != null) {
            mLoginApiTask.cancel(true);
        }

        super.onDestroy();
    }


    /**
     *
     */
    public class LoginApiTask extends AsyncTask<Void, Void, String> {

        /**
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(Void... params) {

            /**
             *
             */
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String server_status_message) {

            String session_key = PreferenceManager.get(getApplication(), Argument.PREFS_SESSION_KEY, null);
            if (session_key == null) {
                LoginActivity.startActivity(SplashActivity.this);
            } else {
                MainActivity.startActivity(SplashActivity.this);
            }
            finish();

            mLoginApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mLoginApiTask = null;
        }

    }

}
