package glass.brew.napkin.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import glass.brew.napkin.model.Food;
import glass.brew.napkin.model.Selection;
import glass.brew.napkin.model.Store;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class ComparisonRecommendationsApi extends ApiBase {

    private static final String API_NAME = "Com.Recom.Api";

    /**
     *
     */
    private static final String PARAM_COMPARISON_ID = "comparison_id";

    /**
     *
     */
    private ArrayList<Food> foods = null;

    /**
     * @param application
     * @param params
     */
    public ComparisonRecommendationsApi(Application application, HashMap<String, String> params) {
        super(application, params);
        Log.d(API_NAME, "api_started!");

        /**
         * Handle Response
         */
        String response = getResponseFromRemote();
        if (response == null) {
            return;
        }

        Log.d(API_NAME, response);
        foods = getFoodsFromResponse(response);
    }

    private ArrayList<Food> getFoodsFromResponse(String response) {

        ArrayList<Food> foods = null;
        try {

            foods = new ArrayList<>();

            JSONArray jsonArray = new JSONObject(response).getJSONArray("payload");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject foodObject = jsonArray.getJSONObject(i);
                foods.add(getFoodFromFoodObject(foodObject));
            }

            return foods;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return foods;
    }

    private Food getFoodFromFoodObject(JSONObject foodObject) throws JSONException {

        Food food = new Food();

        if (!foodObject.isNull("menu")) {
            JSONObject menuObject = foodObject.getJSONObject("menu");

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

        if (!foodObject.isNull("store")) {

            JSONObject storeObject = foodObject.getJSONObject("store");
            Store store = new Store();

            if (!storeObject.isNull("id")) {
                int id = storeObject.getInt("id");
                store.setId(id);
            }

            if (!storeObject.isNull("name")) {
                String name = storeObject.getString("name");
                store.setName(name);
            }

            if (!storeObject.isNull("phone")) {
                String phone = storeObject.getString("phone");
                store.setTel(phone);
            }

            food.setStore(store);
        }

        return food;
    }

    /**
     * @return
     */
    protected String getResponseFromRemote() {

        /**
         *
         */
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
            return super.getResponseFromRemote("/comparison/" + comparison_id + "/recommendations", "GET", "httpa");
        } else {
            return super.getResponseFromRemote("/comparison/recommendations", "GET", "httpa");
        }
    }

    /**
     * @return
     */
    public ArrayList<Food> getResult() {

        return foods;
    }

    /**
     * @param comparison_id
     * @return
     */
    public static HashMap<String, String> createParams(int comparison_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_COMPARISON_ID, "" + comparison_id);
        return params;
    }
}
