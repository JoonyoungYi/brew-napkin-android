package glass.brew.napkin.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import glass.brew.napkin.model.Selection;
import glass.brew.napkin.model.Food;
import glass.brew.napkin.model.Store;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class SelectionDetailApi extends ApiBase {

    private static final String API_NAME = "Com.Sel.DetailApi";

    /**
     *
     */
    private static final String PARAM_COMPARISON_ID = "comparison_id";
    private static final String PARAM_SELECTION_ID = "selection_id";

    /**
     *
     */
    private Selection selection = null;

    /**
     * @param application
     * @param params
     */
    public SelectionDetailApi(Application application, HashMap<String, String> params) {
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
        selection = getComparisonFromResponse(response);

    }

    private Selection getComparisonFromResponse(String response) {

        try {

            Selection selection = new Selection();

            JSONObject jsonObject = new JSONObject(response).getJSONObject("payload");

            JSONObject choiceObject_a = jsonObject.getJSONObject("choice1");
            JSONObject choiceObject_b = jsonObject.getJSONObject("choice2");

            selection.setFood_a(getFoodFromChoiceObject(choiceObject_a));
            selection.setFood_b(getFoodFromChoiceObject(choiceObject_b));

            return selection;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Food getFoodFromChoiceObject(JSONObject choiceObject) throws JSONException {

        Food food = new Food();

        if (!choiceObject.isNull("menu")) {
            JSONObject menuObject = choiceObject.getJSONObject("menu");

            if (!menuObject.isNull("id")) {
                int id = menuObject.getInt("id");
                food.setId(id);
            }

            if (!menuObject.isNull("image_url")) {
                String image_url = menuObject.getString("image_url");
                food.setImg_url(image_url);
            }

            if (!menuObject.isNull("name")) {
                String name = menuObject.getString("name");
                food.setName(name);
            }

            if (!menuObject.isNull("price")) {
                int price = menuObject.getInt("price");
                food.setPrice(price);
            }
        }

        if (!choiceObject.isNull("store")) {

            JSONObject storeObject = choiceObject.getJSONObject("store");
            Store store = new Store();

            if (!storeObject.isNull("id")) {
                int id = storeObject.getInt("id");
                store.setId(id);
            }

            if (!storeObject.isNull("name")) {
                String name = storeObject.getString("name");
                store.setName(name);
            }

            food.setStore(store);
        }

        return food;
    }

    public Selection getResult() {
        return selection;
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
            return super.getResponseFromRemote("/comparison/" + comparison_id + "/selection/" + selection_id, "GET", "httpa");
        }
        return null;
    }


    public static HashMap<String, String> createParams(int comparison_id, int selection_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_COMPARISON_ID, "" + comparison_id);
        params.put(PARAM_SELECTION_ID, "" + selection_id);
        return params;
    }
}
