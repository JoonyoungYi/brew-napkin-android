package glass.brew.napkin.api;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;

import glass.brew.napkin.model.Selection;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class SelectionApi extends ApiBase {

    private static final String API_NAME = "ComparisonListApi";

    /**
     *
     */
    private static final String PARAM_COMPARISON_ID = "comparison_id";

    /**
     *
     */
    private Selection selection;

    /**
     * @param application
     * @param params
     */
    public SelectionApi(Application application, HashMap<String, String> params) {
        super(application, params);
        Log.d(API_NAME, "api_started!");

        int comparison_id = -1;
        try {
            String comparison_id_str = mParams.get(PARAM_COMPARISON_ID);
            comparison_id = Integer.parseInt(comparison_id_str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         *
         */
        if (comparison_id > 0) {
            ComparisonSelectionApi comparisonSelectionApi =
                    new ComparisonSelectionApi(application, ComparisonSelectionApi.createParams(comparison_id));
            int selection_id = comparisonSelectionApi.getResult();
            Log.d(API_NAME, "selection_id -> " + selection_id);

            SelectionDetailApi selectionDetailApi =
                    new SelectionDetailApi(application,
                            SelectionDetailApi.createParams(comparison_id, selection_id));
            selection = selectionDetailApi.getResult();
            selection.setId(selection_id);
            Log.d(API_NAME, "selection is Null? " + (selection == null));
        }
    }

    public static HashMap<String, String> createParams(int comparison_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_COMPARISON_ID, "" + comparison_id);
        return params;
    }

    public Selection getResult() {
        return selection;
    }
}
