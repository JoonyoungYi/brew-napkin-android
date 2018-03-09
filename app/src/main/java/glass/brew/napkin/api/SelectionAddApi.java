package glass.brew.napkin.api;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

import glass.brew.napkin.model.Selection;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class SelectionAddApi extends ApiBase {

    private static final String API_NAME = "SelectionAddApi";

    /**
     *
     */
    private static final String PARAM_COMPARISON_ID = "comparison_id";
    private static final String PARAM_SELECTION_ID = "selection_id";
    private static final String PARAM_SELECTED = "selected";

    /**
     *
     */
    public SelectionAddApi(Application application, HashMap<String, String> params) {
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
        int selection_id = 0;
        try {
            String comparison_id_str = mParams.get(PARAM_COMPARISON_ID);
            comparison_id = Integer.parseInt(comparison_id_str);
            String selection_id_str = mParams.get(PARAM_SELECTION_ID);
            selection_id = Integer.parseInt(selection_id_str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         *
         */
        if (comparison_id > 0 && selection_id > 0) {
            return super.getResponseFromRemote("/comparison/" + comparison_id + "/selection/" + selection_id, "PUT", "httpa");
        }
        return null;
    }

    public static HashMap<String, String> createParams(int selection_id, int selected, int comparison_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_COMPARISON_ID, "" + comparison_id);
        params.put(PARAM_SELECTED, "" + selected);
        params.put(PARAM_SELECTION_ID, "" + selection_id);
        return params;
    }


}
