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

import com.telerik.android.common.Function;
import com.telerik.widget.list.RadListView;
import com.telerik.widget.list.StickyHeaderBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.activity.MainActivity;
import gr.clink.nopandroidclient.activity.GetProductsActivity;
import gr.clink.nopandroidclient.adapters.CategoryAdapter;
import gr.clink.nopandroidclient.model.Category;
import gr.clink.nopandroidclient.other.Globals;
import gr.clink.nopandroidclient.other.JSONParser;


public class CategoriesFragment extends FragmentBase {

    private List<Category> categories = new ArrayList<>();
    StickyHeaderBehavior stickyHeaderBehavior = new StickyHeaderBehavior();
    RadListView listView;
    private ArrayList<String> availableFilters = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    private Deque<CategoryAdapter> adapterStack = new ArrayDeque<>();

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
        listView.addBehavior(stickyHeaderBehavior);

        listView.addItemClickListener(new ListViewClickListener());


        /*ImageButton btnShowSettings = (ImageButton) rootView.findViewById(R.id.btnShowSettings);
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
        });*/
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
                categories.add(new Category(parentName,id,name,imURL,true));
                setListOfCategories(subCategories,name);
            }else{
                categories.add(new Category(parentName,id,name,imURL,false));
            }
        }
    }

    private List<Category> getCategoriesWithParent(String parentName){
        List<Category> result = new ArrayList<>();
        if (categories!=null)
            for(Category c : categories)
                if(c.getParentCategoryName().equals(parentName))
                    result.add(c);
        return result;
    }

    @Override
    protected boolean usesInternet() {return true;}


    class ListViewClickListener implements RadListView.ItemClickListener {
        @Override
        public void onItemClick(int itemPosition, MotionEvent motionEvent) {
            if( categoryAdapter != null ){
                if(categoryAdapter.getItem(itemPosition) instanceof Category ) {
                    Category tappedCategory = (Category) categoryAdapter.getItem(itemPosition);
                    showChildrenOfCategory(tappedCategory);
                }
            }
        }

        @Override
        public void onItemLongClick(int i, MotionEvent motionEvent) {

        }
    }


    private void showChildrenOfCategory(Category category){
        if(!category.hasSubcategories()) {
            Intent intent = new Intent(getActivity(), GetProductsActivity.class);
            intent.putExtra(Globals.GET_PRODUCTS_ACTIVITY_TYPE, Globals.ProductsActivityType.GET_PRODUCTS_BY_CATEGORY);
            intent.putExtra(Globals.ProductsByCategoryIdActivityProperties.ID, category.getId());
            intent.putExtra(Globals.ProductsByCategoryIdActivityProperties.CATEGORY_NAME, category.getName());
            startActivityForResult(intent, Globals.REQUEST_CODE_ADD_TO_CART);
        }else{
            if(category.getParentCategoryName().equals("")){
                ((MainActivity) getActivity()).enableBackButton();
            }


            List<Category> subCategories = getCategoriesWithParent(category.getName());
            adapterStack.push(categoryAdapter);
            categoryAdapter = new CategoryAdapter(subCategories, CategoriesFragment.this.getActivity(), R.layout.category_list_item_layout );
            listView.removeBehavior(stickyHeaderBehavior);
            categoryAdapter.addGroupDescriptor(new Function<Object, Object>() {
                @Override
                public Object apply(Object argument) {
                    Category container = (Category) argument;
                    return container.getParentCategoryName();
                }
            });
            stickyHeaderBehavior = new StickyHeaderBehavior();
            listView.setAdapter(categoryAdapter);
            listView.addBehavior(stickyHeaderBehavior);
        }

    }

    public void popAdapter(){
        if(!adapterStack.isEmpty()) {
            categoryAdapter = adapterStack.pop();
            listView.removeBehavior(stickyHeaderBehavior);
            stickyHeaderBehavior = new StickyHeaderBehavior();
            listView.setAdapter(categoryAdapter);
            listView.addBehavior(stickyHeaderBehavior);

            if(adapterStack.isEmpty()){
                ((MainActivity) getActivity()).enableBurgerButton();
            }
        }
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
                    setListOfCategories(result,"");
                    List<Category> shownCategories = getCategoriesWithParent("");
                    categoryAdapter = new CategoryAdapter(shownCategories, CategoriesFragment.this.getActivity(), R.layout.category_list_item_layout );

                    categoryAdapter.addGroupDescriptor(new Function<Object, Object>() {
                        @Override
                        public Object apply(Object argument) {
                            return "Main Categories";
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
