package glass.brew.napkin.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import glass.brew.napkin.model.Comparison;
import glass.brew.napkin.utils.Argument;
import glass.brew.napkin.utils.PreferenceManager;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class ComparisonApi extends ApiBase {

    private static final String API_NAME = "ComparisonApi";


    /**
     *
     */
    private Comparison comparison = new Comparison();

    /**
     * @param application
     * @param params
     */
    public ComparisonApi(Application application, HashMap<String, String> params) {
        super(application, params);
        Log.d(API_NAME, "api_started!");

        /**
         * Handle Response
         */
        String response = getResponseFromRemote();
        if (response == null) {
            return;
        }

        Log.d(API_NAME, "response -> " + response);
        int comparison_id = getComparisonIdFromResponse(response);
        Log.d(API_NAME, "comparison_id -> " + comparison_id);

        /**
         *
         */
        ComparisonThresholdApi comparisonThresholdApi =
                new ComparisonThresholdApi(application, ComparisonThresholdApi.createParams());
        int threshold = comparisonThresholdApi.getResult();
        Log.d(API_NAME, "threshold -> " + threshold);

        /**
         *
         */
        comparison.setId(comparison_id);
        comparison.setThreshold(threshold);
    }

    private int getComparisonIdFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int comparison_id = jsonObject.getJSONObject("payload").getInt("id");
            Log.d(API_NAME, "comparison_id -> " + comparison_id);
            return comparison_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Comparison getResult() {
        return comparison;
    }

    /**
     * @return
     */
    protected String getResponseFromRemote() {
        return super.getResponseFromRemote("/comparison", "POST", "httpa");
    }


    public static HashMap<String, String> createParams() {
        HashMap<String, String> params = new HashMap<>();
        return params;
    }
}
