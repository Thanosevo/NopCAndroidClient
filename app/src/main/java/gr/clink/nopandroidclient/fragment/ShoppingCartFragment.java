package gr.clink.nopandroidclient.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.telerik.widget.list.RadListView;
import com.telerik.widget.list.SwipeExecuteBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.activity.ProductsByCategoryIdActivity;
import gr.clink.nopandroidclient.adapters.ShoppingCartAdapter;
import gr.clink.nopandroidclient.model.Category;
import gr.clink.nopandroidclient.model.UserInformation;
import gr.clink.nopandroidclient.other.Globals;
import gr.clink.nopandroidclient.other.JSONParser;

public class ShoppingCartFragment extends FragmentBase {
    private List<Category> categories = new ArrayList<>();
    private RadListView listView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private SwipeExecuteBehavior swipeExecuteBehavior;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(UserInformation.getInstance().hasEmptyCart()){
            return inflater.inflate(R.layout.fragment_shopping_cart_empty, container, false);
        }
        new GetAsync().execute();

        View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        listView = (RadListView) rootView.findViewById(R.id.listView);

        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 1);
        listView.setLayoutManager(gridLayout);
        listView.addItemClickListener(new ListViewClickListener());

        this.swipeExecuteBehavior = new SwipeExecuteBehavior();
        this.swipeExecuteBehavior.addListener(new SwipeListener());
        this.swipeExecuteBehavior.setAutoDissolve(false);
        this.listView.addBehavior(this.swipeExecuteBehavior);

        return rootView;
    }

    /**
     *
     * @param arr
     * @throws JSONException
     * sets the category list from JSON
     */
    private void setListOfCategories(JSONArray arr, String parentName) throws JSONException {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject category = arr.getJSONObject(i);
            String name = category.getString("Name");
            Integer id = category.getInt("Id");
            JSONArray subCategories = category.getJSONArray("SubCategories");
            String imURL = category.getString("PictureURL");

            if (subCategories.length() != 0){
                setListOfCategories(subCategories,name);
            }else{
                if(parentName == null)
                    categories.add(new Category(name,id,name,imURL));
                else
                    categories.add(new Category(parentName,id,name,imURL));
            }
        }
    }


    @Override
    protected boolean usesInternet() {return true;}

//    class BlogPostViewCallback implements ActionMode.Callback {
//        int itemPosition;
//
//        BlogPostViewCallback(int position) {
//            this.itemPosition = position;
//        }
//
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            System.out.println("blogpostviewCallback oncreateActionmode");
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.list_view_selection_menu, menu);
//            ExampleActivity ea = (ExampleActivity) getActivity();
//            ea.toggleExampleInfoMenuStripVisibility(false);
//
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            System.out.println("blogpostviewCallback oncreateActionmode");
//            mode.setCustomView(actionModeTitleView);
//
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            System.out.println("blogpostviewCallback onActionItemClicked");
//            BlogPost currentPost = (BlogPost) adapter.getItem(itemPosition);
//            if (item.getItemId() == R.id.list_view_selection_example_delete_action) {
//                adapter.remove(currentPost);
//            } else if (item.getItemId() == R.id.list_view_selection_example_fav_action) {
//                toggleItemFavourite(currentPost);
//                adapter.notifyItemChanged(itemPosition);
//            }
//            mode.finish();
//            return true;
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            System.out.println("blogpostviewCallback onDestroyActionMode");
//            showBlogPostView(false);
//            adapter.setCurrentItemId(INVALID_ID);
//            ExampleActivity ea = (ExampleActivity) getActivity();
//            ea.toggleExampleInfoMenuStripVisibility(true);
//        }
//    }

    class ListViewClickListener implements RadListView.ItemClickListener {
        @Override
        public void onItemClick(int itemPosition, MotionEvent motionEvent) {
            Toast.makeText(getContext(),"item clicked",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemLongClick(int i, MotionEvent motionEvent) {

        }
    }

    class SwipeListener implements SwipeExecuteBehavior.SwipeExecuteListener {
        public SwipeListener() {
        }

        @Override
        public void onSwipeStarted(int position) {
        }

        @Override
        public void onSwipeProgressChanged(int position, int currentOffset, View swipeContent) {
            if (swipeContent == null) {
                return;
            }

            View leftLayout = ((ViewGroup) swipeContent).getChildAt(0);
            View rightLayout = ((ViewGroup) swipeContent).getChildAt(1);

            if (currentOffset > 0) {
                leftLayout.setVisibility(View.INVISIBLE);
                rightLayout.setVisibility(View.INVISIBLE);
            } else {
                leftLayout.setVisibility(View.INVISIBLE);
                rightLayout.setVisibility(View.VISIBLE);
            }

//            if(selectionBehavior.isInProgress()) {
//                selectionBehavior.endSelection();
//            }
        }

        @Override
        public void onSwipeEnded(int position, int finalOffset) {

        }

        @Override
        public void onExecuteFinished(int position) {

        }
    }


    private void showProductListViewForCategory(Category category){
        Intent intent = new Intent(getActivity(), ProductsByCategoryIdActivity.class);

        intent.putExtra(Globals.ProductsByCatagoryIdActivityProperties.ID,category.getId());
        intent.putExtra(Globals.ProductsByCatagoryIdActivityProperties.CATEGORY_NAME,category.getName());
        startActivity(intent);

    }


    /**
     *  Class to Handle JSON from NOPCommerce
     */
    class GetAsync extends AsyncTask<String, String, JSONObject> {

        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private final String LOGIN_URL = Globals.HOST + Globals.GET_CATEGORIES;

        private static final String RESPONSE_CODE = "responseCode";
        private static final String RESULT = "result";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                // params.put("name", args[0]);
                // params.put("password", args[1]);

                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "GET", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {

            int responseCode = 0;
            JSONArray result = new JSONArray();

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }


            if (json != null) {
                Toast.makeText(getActivity(), getString(R.string.available_categories),
                        Toast.LENGTH_LONG).show();

                try {
                    responseCode = json.getInt(RESPONSE_CODE);
                    result = json.getJSONArray(RESULT);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (responseCode == 1) {
                Log.d("Success! : ", String.format("Response Code = %d", responseCode));
                try {
                    // Set List
                    setListOfCategories(result,null);
                    shoppingCartAdapter = new ShoppingCartAdapter(categories,ShoppingCartFragment.this.getActivity(), R.layout.shopping_cart_list_item_layout );

                    listView.setAdapter(shoppingCartAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                Log.d("Failure! : ", String.format("Response Code = %d", responseCode));
            }
        }

    }
}
