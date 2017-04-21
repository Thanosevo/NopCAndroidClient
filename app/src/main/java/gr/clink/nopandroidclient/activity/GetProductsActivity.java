package gr.clink.nopandroidclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.telerik.android.common.Function;
import com.telerik.widget.list.ListViewDataSourceAdapter;
import com.telerik.widget.list.RadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.adapters.ProductAdapter;
import gr.clink.nopandroidclient.model.Option;
import gr.clink.nopandroidclient.model.Product;
import gr.clink.nopandroidclient.other.Globals;
import gr.clink.nopandroidclient.other.JSONParser;
import gr.clink.nopandroidclient.other.TypefaceSpan;

public class GetProductsActivity extends AppCompatActivity {
    private RadListView listView;
    private Toolbar toolbar;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private RelativeLayout back_dim_layout;


    private ArrayList<Option> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_get_products);

        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ImageView btnShowSettings = (ImageView) findViewById(R.id.filter_btn);
        btnShowSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetProductsActivity.this,FilterTableActivity.class);
                i.putParcelableArrayListExtra(Globals.FILTERS, options);
                enableDimBackground();
                startActivityForResult(i,Globals.REQUEST_CODE_FILTER_OPTIONS);
            }
        });

        listView = (RadListView) findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(GetProductsActivity.this));
        listView.addItemClickListener(new ListViewClickListener());

        Integer activityType = intent.getIntExtra(Globals.GET_PRODUCTS_ACTIVITY_TYPE,0);

        if(activityType.equals(Globals.ProductsActivityType.GET_PRODUCTS_BY_CATEGORY)){
            Integer categoryId = intent.getIntExtra(Globals.ProductsByCategoryIdActivityProperties.ID, 0);
            String toolbarTitle = intent.getStringExtra(Globals.ProductsByCategoryIdActivityProperties.CATEGORY_NAME);
            setToolbarTitle(toolbarTitle);
            new GetAsyncProductsByCategory().execute(categoryId.toString());
        }else if(activityType.equals(Globals.ProductsActivityType.GET_PRODUCTS_BY_SEARCH)){
            String searchTerm = intent.getStringExtra(Globals.ProductsBySearchActivityProperties.SEARCH_TERM);
            String toolbarTitle = getString(R.string.product_search);
            setToolbarTitle(toolbarTitle);
            //TODO: search term > 3
            new GetAsyncProductsBySearch().execute(searchTerm);
        }

    }
    public void enableDimBackground(){
        back_dim_layout.setVisibility(View.VISIBLE);
    }

    public void disableDimBackground(){
        back_dim_layout.setVisibility(View.GONE);
    }

    private void setToolbarTitle(String title) {
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, "RobotoSlab300.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        disableDimBackground();
        if (requestCode == Globals.REQUEST_CODE_ADD_TO_CART) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString(Globals.POPUPRESULT);
                switch (result) {
                    case Globals.PopupResponses.CONTINUESHOPPING:
                        break;
                    case Globals.PopupResponses.GOTOCART:
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(Globals.POPUPRESULT, Globals.PopupResponses.GOTOCART);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }else if(requestCode == Globals.REQUEST_CODE_FILTER_OPTIONS){
            if(resultCode == Activity.RESULT_OK){
                options = data.getParcelableArrayListExtra(Globals.FILTERS);

                ListViewDataSourceAdapter adapter = (ListViewDataSourceAdapter)listView.getAdapter();
                adapter.clearFilterDescriptors();
                adapter.addFilterDescriptor(new Function<Object, Boolean>() {
                    @Override
                    public Boolean apply(Object argument) {
                        Product p = (Product) argument;
                        boolean hasSelectedFilter = false;
                        for(Option opt: options){
                            if((Boolean) opt.getValue()){
                                hasSelectedFilter = true;
                                break;
                            }
                        }
                        if(!hasSelectedFilter){
                            return true;
                        }

                        ArrayList<Option> categoryOptions = Option.getOptionsOfGroup(options,"Category");
                        boolean categoryFilter = true;
                        for (Option opt: categoryOptions){
                            if ((Boolean) opt.getValue()){
                                if(p.getCategories().contains(opt.getName())) {
                                    categoryFilter = true;
                                    break;
                                }else{
                                    categoryFilter = false;
                                }
                            }
                        }

                        ArrayList<Option> manufacturerOptions = Option.getOptionsOfGroup(options,"Manufacturer");
                        boolean manufacturerFilter = true;
                        for (Option opt: manufacturerOptions){
                            if ((Boolean) opt.getValue()){
                                if(p.getManufacturers().contains(opt.getName())) {
                                    manufacturerFilter = true;
                                    break;
                                }else{
                                    manufacturerFilter = false;
                                }
                            }
                        }

                        ArrayList<Option> priceRangeOptions = Option.getOptionsOfGroup(options,"Price Range");
                        Boolean priceFilter = true;
                        for (Option opt: priceRangeOptions){
                            Float[] range = opt.getPriceRangeFromOption();
                            if((Boolean) opt.getValue()) {
                                if(p.getProductPrice() >= range[0] && p.getProductPrice() <= range[1] ) {
                                    priceFilter = true;
                                    break;
                                }
                                else{
                                    priceFilter = false;
                                }
                            }
                        }

                        return categoryFilter && manufacturerFilter && priceFilter;
                    }
                });

            }
        }
    }

    class ListViewClickListener implements RadListView.ItemClickListener {
        @Override
        public void onItemClick(int itemPosition, MotionEvent motionEvent) {
            if( productAdapter != null ){
                try {
                    Product tappedProduct = (Product) productAdapter.getItem(itemPosition);
                    showProductDetail(tappedProduct);
                }catch (ClassCastException e){
                    System.out.println(e.getStackTrace().toString());
                }
            }
        }

        @Override
        public void onItemLongClick(int i, MotionEvent motionEvent) {

        }
    }


    private void showProductDetail(Product product){
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(Globals.PRODUCT,product);
        startActivityForResult(intent,Globals.REQUEST_CODE_ADD_TO_CART);
    }


    class GetAsyncProductsByCategory extends AsyncTask<String, String, JSONObject> {

        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private final String LOGIN_URL = Globals.HOST + Globals.GET_PRODUCTS;

        private static final String RESPONSE_CODE = "responseCode";
        private static final String RESULT = "result";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(GetProductsActivity.this);
            System.out.println("preexecute");
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                System.out.println("background");
                HashMap<String, String> params = new HashMap<>();
                params.put(Globals.PARAM_CATEGORY_ID, args[0]);
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

            System.out.println("post");

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
                    productList = Product.getListOfProducts(result);
                    options = Option.getListOfOptionsFrom(Product.getManufacturersFromProducts(productList),"Manufacturer",false);
                    Float[] prices = Product.getPriceRangeForProducts(productList);
                    options.addAll(Option.getPriceRangeListOfOptions(prices[0],prices[1],6));

                    productAdapter = new ProductAdapter(productList, GetProductsActivity.this, R.layout.product_list_item_layout);
                    listView.setAdapter(productAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }



                System.out.println(result);

            }else{
                Log.d("Failure! : ", String.format("Response Code = %d", responseCode));
            }
        }

    }

    class GetAsyncProductsBySearch extends AsyncTask<String, String, JSONObject> {

        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private final String LOGIN_URL = Globals.HOST + Globals.GET_PRODUCTS_SEARCH;

        private static final String RESPONSE_CODE = "responseCode";
        private static final String RESULT = "result";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(GetProductsActivity.this);
            System.out.println("preexecute");
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                System.out.println("background");
                HashMap<String, String> params = new HashMap<>();
                params.put(Globals.PARAM_SEARCH_TERM, "mac");
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
                    productList = Product.getListOfProducts(result);
                    options = Option.getListOfOptionsFrom(Product.getManufacturersFromProducts(productList),"Manufacturer",false);
                    Float[] prices = Product.getPriceRangeForProducts(productList);
                    options.addAll(Option.getPriceRangeListOfOptions(prices[0],prices[1],6));

                    productAdapter = new ProductAdapter(productList, GetProductsActivity.this, R.layout.product_list_item_layout);
                    listView.setAdapter(productAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }



                System.out.println(result);

            }else{
                Log.d("Failure! : ", String.format("Response Code = %d", responseCode));
            }
        }

    }
}
