package glass.brew.napkin.api;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class ComparisonPutApi extends ApiBase {

    private static final String API_NAME = "ComparisonPutApi";

    /**
     *
     */
    private static final String PARAM_COMPARISON_ID = "comparison_id";
    private static final String PARAM_CONCLUSION = "conclusion";

    /**
     *
     */
    public ComparisonPutApi(Application application, HashMap<String, String> params) {
        super(application, params);

        String response = getResponseFromRemote();
        if (response == null) {
            return;
        }

        Log.d(API_NAME, "response -> " + response);
    }

    public void getResult() {
        return;
    }

    /**
     * @return
     */
    protected String getResponseFromRemote() {

        /**
         *
         */
        int comparison_id = 0;
        try {
            String comparison_id_str = mParams.get(PARAM_COMPARISON_ID);
            comparison_id = Integer.parseInt(comparison_id_str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         *
         */
        if (comparison_id > 0 ) {
            return super.getResponseFromRemote("/comparison/" + comparison_id , "PUT", "httpa");
        }
        return null;
    }

    public static HashMap<String, String> createParams(int comparison_id, int conclusion) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_COMPARISON_ID, "" + comparison_id);
        params.put(PARAM_CONCLUSION, "" + conclusion);
        return params;
    }


}
