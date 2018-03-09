package glass.brew.napkin.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import glass.brew.napkin.R;
import glass.brew.napkin.api.ComparisonPutApi;
import glass.brew.napkin.api.SelectionAddApi;
import glass.brew.napkin.api.ComparisonApi;
import glass.brew.napkin.api.SelectionApi;
import glass.brew.napkin.api.ComparisonRecommendationsApi;
import glass.brew.napkin.api.UserLocationApi;
import glass.brew.napkin.model.Comparison;
import glass.brew.napkin.model.Selection;
import glass.brew.napkin.model.Food;
import glass.brew.napkin.model.LocationModel;
import glass.brew.napkin.utils.Application;
import glass.brew.napkin.utils.Argument;
import glass.brew.napkin.utils.CardManager;
import glass.brew.napkin.utils.PreferenceManager;
import glass.brew.napkin.utils.TelephoneManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    /**
     *
     */
    private Selection mSelection = null;
    private Comparison mComparison = null;

    /**
     *
     */
    private TextView mLocationTv;
    private View mLocationBtn;
    private TextView mMsgTv;

    private View mComparisonCardView;
    private ImageView mFoodAIv;
    private TextView mFoodANameTv;
    private TextView mFoodAStoreNameTv;
    private ImageView mFoodBIv;
    private TextView mFoodBNameTv;
    private TextView mFoodBStoreNameTv;

    private View mRecommendCardView;
    private ImageView mFoodRecommendIv;
    private TextView mFoodRecommendNameTv;
    private TextView mFoodRecommendStoreNameTv;
    private TextView mFoodRecommendPriceTv;
    private View mAnotherRecommendBtn;
    private View mTelBtn;

    /**
     *
     */
    private CardManager mComparisonCardManager;
    private CardManager mRecommendCardManager;

    /**
     *
     */
    private LocationDetailApiReadyTask mLocationDetailApiReadyTask = null;
    private UserLocationApiTask mLocationDetailApiTask = null;
    private ComparisonApiTask mComparisonApiTask = null;
    private SelectionApiTask mSelectionApiTask = null;
    private SelectionAddApiTask mSelectionAddApiTask = null;
    private RecommendListApiTask mRecommendListApiTask = null;
    private ComparisonPutApiTask mComparisonPutApiTask = null;

    /**
     * @param activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        /**
         *
         */
        Tracker t = ((Application) getApplication()).getTracker(Application.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        /**
         *
         */
        mLocationTv = (TextView) findViewById(R.id.location_tv);
        mLocationBtn = findViewById(R.id.location_btn);

        mMsgTv = (TextView) findViewById(R.id.msg_tv);

        mComparisonCardView = findViewById(R.id.comparison_card_view);
        mFoodAIv = (ImageView) findViewById(R.id.food_a_iv);
        mFoodANameTv = (TextView) findViewById(R.id.food_a_name_tv);
        mFoodAStoreNameTv = (TextView) findViewById(R.id.food_a_store_name_tv);
        mFoodBIv = (ImageView) findViewById(R.id.food_b_iv);
        mFoodBNameTv = (TextView) findViewById(R.id.food_b_name_tv);
        mFoodBStoreNameTv = (TextView) findViewById(R.id.food_b_store_name_tv);

        mRecommendCardView = findViewById(R.id.recommend_card_view);
        mFoodRecommendIv = (ImageView) findViewById(R.id.food_recommend_iv);
        mFoodRecommendNameTv = (TextView) findViewById(R.id.food_recommend_name_tv);
        mFoodRecommendStoreNameTv = (TextView) findViewById(R.id.food_recommend_store_name_tv);
        TextView mFoodRecommendPriceUnitTv = (TextView) findViewById(R.id.food_recommend_price_unit_tv);
        mFoodRecommendPriceTv = (TextView) findViewById(R.id.food_recommend_price_tv);
        mTelBtn = findViewById(R.id.tel_btn);

        /**
         *
         */
        mComparisonCardManager = new CardManager(MainActivity.this, mComparisonCardView);
        mRecommendCardManager = new CardManager(MainActivity.this, mRecommendCardView);

        /**
         *
         */
        char unit_won = 0xFFE6;
        mFoodRecommendPriceUnitTv.setText("" + unit_won);

        /**
         *
         */
        findViewById(R.id.location_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLocationDetailApiReadyTask != null) {
                    mLocationDetailApiReadyTask.cancel(true);
                }

                mLocationDetailApiReadyTask = new LocationDetailApiReadyTask();
                mLocationDetailApiReadyTask.execute();
            }
        });

        findViewById(R.id.food_a_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectionAddApiTask == null) {
                    mSelectionAddApiTask = new SelectionAddApiTask();
                    mSelectionAddApiTask.execute(1);
                }

                Tracker t = ((Application) getApplication()).getTracker(Application.TrackerName.APP_TRACKER);
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("MainActivity")
                        .setAction("Select Food")
                        .setLabel("a")
                        .setValue(-1)
                        .build());
            }
        });
        findViewById(R.id.food_b_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectionAddApiTask == null) {
                    mSelectionAddApiTask = new SelectionAddApiTask();
                    mSelectionAddApiTask.execute(2);
                }

                Tracker t = ((Application) getApplication()).getTracker(Application.TrackerName.APP_TRACKER);
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("MainActivity")
                        .setAction("Select Food")
                        .setLabel("b")
                        .setValue(1)
                        .build());
            }
        });

        findViewById(R.id.later_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mComparisonPutApiTask == null) {
                    mComparisonPutApiTask = new ComparisonPutApiTask(1);
                    mComparisonPutApiTask.execute();
                }
                Tracker t = ((Application) getApplication()).getTracker(Application.TrackerName.APP_TRACKER);
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("MainActivity")
                        .setAction("Later - Another Recommendation")
                        .build());
            }
        });

        findViewById(R.id.another_recommend_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mComparisonPutApiTask == null) {
                    mComparisonPutApiTask = new ComparisonPutApiTask(2);
                    mComparisonPutApiTask.execute();
                }
                Tracker t = ((Application) getApplication()).getTracker(Application.TrackerName.APP_TRACKER);
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("MainActivity")
                        .setAction("Another Recommendation")
                        .build());
            }
        });
        mTelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tel = (String) view.getTag();
                TelephoneManager telephoneManager = new TelephoneManager(MainActivity.this, tel);
                telephoneManager.call();

                Tracker t = ((Application) getApplication()).getTracker(Application.TrackerName.APP_TRACKER);
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("MainActivity")
                        .setAction("Telephone")
                        .build());

                if (mComparisonPutApiTask == null) {
                    mComparisonPutApiTask = new ComparisonPutApiTask(0);
                    mComparisonPutApiTask.execute();
                }
            }
        });

        /**
         *
         */
        if (mLocationDetailApiReadyTask == null) {
            mLocationDetailApiReadyTask = new LocationDetailApiReadyTask();
            mLocationDetailApiReadyTask.execute();
        }


    }


    private void updateSelection(Selection selection) {

        /**
         *
         */
        if (selection == null) {
            return;
        }
        this.mSelection = selection;
        /**
         *
         */
        Food food_a = selection.getFood_a();
        Food food_b = selection.getFood_b();
        if (mComparison.getThreshold() == 1) {
            mMsgTv.setText("마지막으로, 지금 더 먹고싶은 음식을 선택해주세요!");
        } else if (mComparison.getThreshold() == 2) {
            mMsgTv.setText("지금 더 먹고싶은 음식을 선택해주세요!");
        } else if (mComparison.getThreshold() == 3) {
            mMsgTv.setText("지금 더 먹고싶은 음식을 선택해주세요!");
        } else {
            mMsgTv.setText("지금 더 먹고싶은 음식을 선택해주세요!");
        }

        /**
         *
         */
        mFoodANameTv.setText(food_a.getName());
        mFoodAStoreNameTv.setText(food_a.getStore().getName());
        Picasso.with(MainActivity.this).load(food_a.getImg_url()).into(mFoodAIv);
        mFoodBNameTv.setText(food_b.getName());
        mFoodBStoreNameTv.setText(food_b.getStore().getName());
        Picasso.with(MainActivity.this).load(food_b.getImg_url()).into(mFoodBIv);

        /**
         *
         */
        mComparison.setThreshold(mComparison.getThreshold() - 1);
    }

    /**
     *
     */
    public class UserLocationApiTask extends AsyncTask<Void, Void, LocationModel> {

        @Override
        protected void onPreExecute() {

            /**
             *
             */
            mComparisonCardManager.dismiss();
            mRecommendCardManager.dismiss();

        }

        @Override
        protected LocationModel doInBackground(Void... params) {

            try {

                UserLocationApi locationDetailApi = new UserLocationApi(getApplication(),
                        null);

                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

                return locationDetailApi.getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(LocationModel locationModel) {

            if (locationModel != null) {

                /**
                 *
                 */
                mLocationTv.setText(locationModel.getName());

                /**
                 *
                 */
                if (mComparisonApiTask == null) {
                    mComparisonApiTask = new ComparisonApiTask();
                    mComparisonApiTask.execute();
                }

            } else {
                Toast.makeText(MainActivity.this, "위치를 얻어오지 못했습니다", Toast.LENGTH_SHORT).show();
            }


            /**
             *
             */
            mLocationDetailApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mLocationDetailApiTask = null;
        }
    }

    /**
     *
     */
    public class ComparisonApiTask extends AsyncTask<Void, Void, Comparison> {

        @Override
        protected void onPreExecute() {

            /**
             *
             */
            mRecommendCardManager.dismiss();

            /**
             *
             */
            String user_name = PreferenceManager.get(MainActivity.this, Argument.PREFS_USER_FIRST_NAME, null);
            if (user_name == null) {
                user_name = getString(R.string.msg_user_name_default);
            }
            mMsgTv.setText(user_name + getString(R.string.msg_comparison_list_api));

        }

        @Override
        protected Comparison doInBackground(Void... params) {

            try {
                ComparisonApi comparisonApi = new ComparisonApi(getApplication(),
                        ComparisonApi.createParams());
                Comparison comparison = (comparisonApi.getResult());

                //
                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

                //
                return comparison;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Comparison comparison) {

            /**
             *
             */
            mComparison = comparison;

            /**
             *
             */
            if (mSelectionApiTask == null) {
                mSelectionApiTask = new SelectionApiTask();
                mSelectionApiTask.execute();
            }

            /**
             *
             */
            mComparisonApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mComparisonApiTask = null;
        }
    }


    /**
     *
     */
    public class SelectionApiTask extends AsyncTask<Void, Void, Selection> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Selection doInBackground(Void... params) {

            try {
                SelectionApi selectionApi = new SelectionApi(getApplication(), SelectionApi.createParams(mComparison.getId()));
                Selection selection = (selectionApi.getResult());

                //
                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

                //
                return selection;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Selection selection) {

            /**
             *
             */
            updateSelection(selection);
            mComparisonCardManager.appear();

            /**
             *
             */
            mSelectionApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mSelectionApiTask = null;
        }
    }

    /**
     *
     */
    public class LocationDetailApiReadyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            /**
             *
             */
            mLocationTv.setText(R.string.main_activity_location_tv_default);

            /**
             *
             */
            String user_name = PreferenceManager.get(MainActivity.this, Argument.PREFS_USER_FIRST_NAME, null);
            if (user_name == null) {
                user_name = getString(R.string.msg_user_name_default);
            }
            mMsgTv.setText(user_name + getString(R.string.msg_location_detail_api));

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void param) {

            if (mLocationDetailApiTask != null) {
                mLocationDetailApiTask.cancel(true);
            }
            mLocationDetailApiTask = new UserLocationApiTask();
            mLocationDetailApiTask.execute();

            /**
             *
             */
            mLocationDetailApiReadyTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mLocationDetailApiReadyTask = null;
        }
    }

    /**
     *
     */
    public class SelectionAddApiTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            /**
             *
             */
            mComparisonCardManager.dismiss();

            /**
             *
             */
            mMsgTv.setText(R.string.msg_comparison_add_api);
        }

        @Override
        protected Void doInBackground(Integer... selecteds) {

            try {
                SelectionAddApi comparisonAddApi = new SelectionAddApi(getApplication(),
                        SelectionAddApi.createParams(mSelection.getId(), selecteds[0], mComparison.getId()));
                comparisonAddApi.getResult();

                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void param) {

            if (mComparison.getThreshold() > 0) {

                if (mSelectionApiTask == null) {
                    mSelectionApiTask = new SelectionApiTask();
                    mSelectionApiTask.execute();
                }

            } else {

                if (mRecommendListApiTask == null) {
                    mRecommendListApiTask = new RecommendListApiTask();
                    mRecommendListApiTask.execute();
                }

            }

            /**
             *
             */
            mSelectionAddApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mSelectionAddApiTask = null;
        }
    }

    /**
     *
     */
    public class ComparisonPutApiTask extends AsyncTask<Void, Void, Void> {

        private int conclusion = -1;

        public ComparisonPutApiTask(int conclusion) {
            this.conclusion = conclusion;
        }

        @Override
        protected void onPreExecute() {

            /**
             *
             */
            mRecommendCardManager.dismiss();

            /**
             *
             */
            if (conclusion == 0) {
                mMsgTv.setText(R.string.msg_comparison_put_api_conclusion_0);
            } else if (conclusion == 1) {
                mMsgTv.setText(R.string.msg_comparison_put_api_conclusion_1);
            } else if (conclusion == 2) {
                mMsgTv.setText(R.string.msg_comparison_put_api_conclusion_2);
            } else {
                mMsgTv.setText(R.string.msg_comparison_put_api_conclusion_unexpected);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                if (conclusion == 0) {
                    Thread.sleep(2500);
                }

                ComparisonPutApi comparisonPutApi = new ComparisonPutApi(getApplication(),
                        ComparisonPutApi.createParams(mComparison.getId(), conclusion));
                comparisonPutApi.getResult();

                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void param) {

            /**
             *
             */
            if (mComparisonApiTask == null) {
                mComparisonApiTask = new ComparisonApiTask();
                mComparisonApiTask.execute();
            }

            /**
             *
             */
            mComparisonPutApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mComparisonPutApiTask = null;
        }
    }

    /**
     *
     */
    public class RecommendListApiTask extends AsyncTask<Void, Void, ArrayList<Food>> {

        @Override
        protected void onPreExecute() {
            String user_name = PreferenceManager.get(MainActivity.this, Argument.PREFS_USER_FIRST_NAME, null);
            if (user_name == null) {
                user_name = getString(R.string.msg_user_name_default);
            }
            mMsgTv.setText(user_name + getString(R.string.msg_recommend_list_api));
            mComparisonCardManager.dismiss();
        }

        @Override
        protected ArrayList<Food> doInBackground(Void... params) {

            ArrayList<Food> foods = null;

            try {
                ComparisonRecommendationsApi recommendListApi = new ComparisonRecommendationsApi(getApplication(),
                        ComparisonRecommendationsApi.createParams(mComparison.getId()));
                foods = recommendListApi.getResult();

                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return foods;
        }

        @Override
        protected void onPostExecute(ArrayList<Food> foods) {

            /**
             *
             */
            Food food = foods.get(0);
            mFoodRecommendNameTv.setText(food.getName());
            mFoodRecommendStoreNameTv.setText(food.getStore().getName());
            Picasso.with(MainActivity.this).load(food.getImg_url()).into(mFoodRecommendIv);
            mFoodRecommendPriceTv.setText(String.format("%,d", food.getPrice()));
            mTelBtn.setTag(food.getStore().getTel());

            /**
             *
             */
            String user_name = PreferenceManager.get(MainActivity.this, Argument.PREFS_USER_FIRST_NAME, null);
            if (user_name == null) {
                user_name = getString(R.string.msg_user_name_default);
            }
            String msg = getString(R.string.msg_recommend_list_api_completed_pre) + " "
                    + user_name
                    + getString(R.string.msg_recommend_list_api_completed);
            mMsgTv.setText(msg);

            /**
             *
             */
            mRecommendCardManager.appear();

            /**
             *
             */
            mRecommendListApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mRecommendListApiTask = null;
        }
    }

    /**
     *
     */
    public void onDestroy() {
        super.onDestroy();

        if (mSelectionApiTask != null) {
            mSelectionApiTask.cancel(true);
            mSelectionApiTask = null;
        }

        if (mSelectionAddApiTask != null) {
            mSelectionAddApiTask.cancel(true);
            mSelectionAddApiTask = null;
        }

        if (mRecommendListApiTask != null) {
            mRecommendListApiTask.cancel(true);
            mRecommendListApiTask = null;
        }

        if (mLocationDetailApiReadyTask != null) {
            mLocationDetailApiReadyTask.cancel(true);
        }

        if (mComparisonApiTask != null) {
            mComparisonApiTask.cancel(true);
        }

        if (mLocationDetailApiTask != null) {
            mLocationDetailApiTask.cancel(true);
        }

        if (mComparisonPutApiTask != null) {
            mComparisonPutApiTask.cancel(true);
        }

    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
