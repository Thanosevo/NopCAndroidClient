package gr.clink.nopandroidclient.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.telerik.android.common.Function;
import com.telerik.widget.list.ListViewDataSourceAdapter;
import com.telerik.widget.list.RadListView;
import com.telerik.widget.list.StickyHeaderBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.activity.ProductsByCategoryIdActivity;
import gr.clink.nopandroidclient.model.Category;
import gr.clink.nopandroidclient.model.CategoryAdapter;
import gr.clink.nopandroidclient.other.Globals;
import gr.clink.nopandroidclient.other.JSONParser;


public class CategoriesFragment extends FragmentBase {

    private List<Category> categories = new ArrayList<>();
    RadListView listView;
    private ArrayList<String> availableFilters = new ArrayList<>();
    CategoryAdapter categoryAdapter;

    public CategoriesFragment() {
    }

    private List<String> getAllCategories(){
        List<String> result = new ArrayList<>();
        for (Category category: categories) {
            if(!result.contains(category.getParentCategoryName())){
                result.add(category.getParentCategoryName());
            }
        }
        return result;
    }

    private boolean[] getfilterSelections(){
        List<Boolean> list = new ArrayList<>();
        Set<String> groupCategories = new LinkedHashSet<>();
        for (Category category: categories) {
            groupCategories.add(category.getParentCategoryName());
        }
        for (String category: groupCategories) {
             list.add(availableFilters.contains(category));
        }
        boolean[] result = new boolean[list.size()];
        int i = 0;
        for (Boolean s: list){
            result[i] = s.booleanValue();
            i++;
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new GetAsync().execute();

        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        listView = (RadListView) rootView.findViewById(R.id.listView);

        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 2);
        listView.setLayoutManager(gridLayout);

        StickyHeaderBehavior stickyHeaderBehavior = new StickyHeaderBehavior();
        listView.addBehavior(stickyHeaderBehavior);

        listView.addItemClickListener(new ListViewClickListener());






        ImageButton btnShowSettings = (ImageButton) rootView.findViewById(R.id.btnShowSettings);
        btnShowSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CategoriesFragment.this.categories.isEmpty()){
                    Toast.makeText(getActivity(), "Please wait for the photos to download...", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setTitle(R.string.categories_layout_modes_dialog_title);
                dialogBuilder.setMultiChoiceItems(
                        getAllCategories().toArray(new String[0]),
                        getfilterSelections(),
                        new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                String[] typedArray = getAllCategories().toArray(new String[0]);
                                if (isChecked) {
                                    availableFilters.add(typedArray[which]);
                                } else {
                                    availableFilters.remove(typedArray[which]);
                                }
                            }
                        });

                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListViewDataSourceAdapter adapter = (ListViewDataSourceAdapter)listView.getAdapter();
                        adapter.clearFilterDescriptors();
                        adapter.addFilterDescriptor(new Function<Object, Boolean>() {
                            @Override
                            public Boolean apply(Object argument) {
                                Category categories = (Category) argument;
                                boolean passesFilter = false;

                                for (String s: availableFilters){
                                    if (s.equalsIgnoreCase(categories.getParentCategoryName())){
                                        return true;
                                    }
                                }

                                return passesFilter;
                            }
                        });

                        dialog.dismiss();
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialogBuilder.create().show();

            }
        });
        return rootView;
    }



    /**
     *
     * @param arr
     * @throws JSONException
     * sets the category list from JSON
     */
    private void setListOfCategories(JSONArray arr,String parentName) throws JSONException {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject category = arr.getJSONObject(i);
            String name = category.getString("Name");
            Integer id = category.getInt("Id");
            JSONArray subCategories = category.getJSONArray("SubCategories");
            String imURL = category.getString("PictureURL");
            // Add filters
            availableFilters.add(name);
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
            if( categoryAdapter != null ){
                try {
                    Category tappedCategory = (Category) categoryAdapter.getItem(itemPosition);
                    showProductListViewForCategory(tappedCategory);
                }catch (ClassCastException e){

                }
            }
        }

        @Override
        public void onItemLongClick(int i, MotionEvent motionEvent) {

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
                    categoryAdapter = new CategoryAdapter(categories, CategoriesFragment.this.getActivity(), R.layout.category_list_item_layout );

                    categoryAdapter.addGroupDescriptor(new Function<Object, Object>() {
                        @Override
                        public Object apply(Object argument) {
                            Category container = (Category) argument;
                            return container.getParentCategoryName();
                        }
                    });
                    listView.setAdapter(categoryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                Log.d("Failure! : ", String.format("Response Code = %d", responseCode));
            }
        }

    }
}
