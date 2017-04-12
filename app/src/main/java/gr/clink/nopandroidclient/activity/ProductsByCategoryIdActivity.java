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
import android.widget.Toast;

import com.telerik.widget.list.RadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.adapters.ProductAdapter;
import gr.clink.nopandroidclient.model.Product;
import gr.clink.nopandroidclient.other.Globals;
import gr.clink.nopandroidclient.other.JSONParser;
import gr.clink.nopandroidclient.other.TypefaceSpan;

public class ProductsByCategoryIdActivity extends AppCompatActivity {
    private String categoryName;
    private Integer categoryId;
    private RadListView listView;
    private Toolbar toolbar;
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private RelativeLayout back_dim_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        categoryId = intent.getIntExtra(Globals.ProductsByCatagoryIdActivityProperties.ID, 0);
        categoryName = intent.getStringExtra(Globals.ProductsByCatagoryIdActivityProperties.CATEGORY_NAME);
        setContentView(R.layout.activity_products_by_category_id);

        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarTitle();

        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (RadListView) findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(ProductsByCategoryIdActivity.this));
        listView.addItemClickListener(new ListViewClickListener());
        new GetAsync().execute(categoryId.toString());

    }
    public void enableDimBackground(){
        back_dim_layout.setVisibility(View.VISIBLE);
    }

    public void disableDimBackground(){
        back_dim_layout.setVisibility(View.GONE);
    }

    private void setToolbarTitle() {
        SpannableString s = new SpannableString(categoryName);
        s.setSpan(new TypefaceSpan(this, "RobotoSlab300.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    private void setListOfProducts(JSONArray arr) throws JSONException {

            for (int i = 0; i < arr.length(); i++) {
                JSONObject product = arr.getJSONObject(i);
                String name = product.getString("Name");
                Integer id = product.getInt("Id");
                String shortDescription = product.getString("ShortDescription");
                String fullDescription = product.getString("FullDescription");
                Integer stockQuantity = product.getInt("StockQuantity");
                Float price = (float) product.getDouble("Price");
                JSONArray pictureArray = product.getJSONArray("PictureURLs");
                ArrayList<String> pictureURLs = new ArrayList<>();
                for (int j = 0;j < pictureArray.length(); j++) {
                    pictureURLs.add(pictureArray.getString(j));

                }
                JSONArray attributesArray = product.getJSONArray("ProductAttributes");
                HashMap<String, String> attributes = new HashMap<String, String>();
                for (int k = 0;k < attributesArray.length(); k++) {
                    attributes.put(attributesArray.getJSONObject(k).getString("Attribute"), attributesArray.getJSONObject(k).getString("Value"));
                }

                productList.add(new Product(name,categoryName,id,shortDescription,fullDescription,stockQuantity,price,pictureURLs, attributes));
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        disableDimBackground();
        if(resultCode == Activity.RESULT_OK){
            String result = data.getExtras().getString(Globals.POPUPRESULT);
            switch (result){
                case Globals.PopupResponses.CONTINUESHOPPING:
                    break;
                case Globals.PopupResponses.GOTOCART:
                    break;
                default:
                    break;
            }
        }
    }//onActivityResult

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
        startActivity(intent);
    }

    class GetAsync extends AsyncTask<String, String, JSONObject> {

        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private final String LOGIN_URL = Globals.HOST + Globals.GET_PRODUCTS;

        private static final String RESPONSE_CODE = "responseCode";
        private static final String RESULT = "result";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ProductsByCategoryIdActivity.this);
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
                Toast.makeText(ProductsByCategoryIdActivity.this, getString(R.string.available_categories),
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
                    setListOfProducts(result);
                    productAdapter = new ProductAdapter(productList, ProductsByCategoryIdActivity.this, R.layout.product_list_item_layout);
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
