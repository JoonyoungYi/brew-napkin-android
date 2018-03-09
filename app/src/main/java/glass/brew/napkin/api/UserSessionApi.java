package glass.brew.napkin.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import glass.brew.napkin.utils.Argument;
import glass.brew.napkin.utils.PreferenceManager;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class UserSessionApi extends ApiBase {

    private static final String API_NAME = "AuthLoginFacebookApi";

    /**
     *
     */
    private static final String PARAM_TOKEN = "fb_token";

    /**
     * @param application
     * @param params
     */
    public UserSessionApi(Application application, HashMap<String, String> params) {
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
        getUserIdAndSessionKeyFromResponse(response);
    }

    private void getUserIdAndSessionKeyFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String session_key = jsonObject.getJSONObject("payload").getString("token");
            Log.d(API_NAME, "session_key -> " + session_key);
            PreferenceManager.put(mContext, Argument.PREFS_SESSION_KEY, session_key);

            int user_id = jsonObject.getJSONObject("payload").getInt("user_id");
            Log.d(API_NAME, "user_id -> " + user_id);
            PreferenceManager.put(mContext, Argument.PREFS_USER_ID, user_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResult() {

    }

    /**
     * @return
     */
    protected String getResponseFromRemote() {
        return super.getResponseFromRemote("/user/session", "POST", "http");
    }


    public static HashMap<String, String> createParams(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, token);
        return params;
    }
}
