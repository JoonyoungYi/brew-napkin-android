package glass.brew.napkin.utils;

import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import glass.brew.napkin.R;


/**
 * Created by yearnning on 2014. 8. 1..
 * This is singletone.
 */
public class Application extends android.app.Application {

    /**
     *
     */
    private static final String PROPERTY_ID = "UA-58645401-4";
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    /**
     *
     */
    private Picasso picasso = null;

    public Picasso getPicasso() {
        if (picasso == null) {
            picasso = Picasso.with(Application.this);
        }
        return picasso;
    }

    /**
     *
     */
    private HashMap<Integer, Long> mRequestCodeTimestamps = new HashMap<>();

    /**
     *
     */
    private int time_offset = 0;

    public int getTime_offset() {
        return time_offset;
    }

    public void setTime_offset(int time_offset) {
        this.time_offset = time_offset;
    }

    /**
     * @param request_code
     * @return
     */
    public boolean isTimestampExpired(int request_code) {

        try {
            long unixTime_current = System.currentTimeMillis();

            if (mRequestCodeTimestamps == null) {
                mRequestCodeTimestamps = new HashMap<>();
            }

            if (mRequestCodeTimestamps.containsKey(request_code)) {
                long unixTime_old = mRequestCodeTimestamps.get(request_code);

                if ((unixTime_current - unixTime_old) < Toast.LENGTH_LONG * 1100) {
                    return false;
                }
            }

            mRequestCodeTimestamps.put(request_code, unixTime_current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }
}
