package gr.clink.nopandroidclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.activity.GetProductsActivity;
import gr.clink.nopandroidclient.other.Globals;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Button btn = (Button) rootView.findViewById(R.id.search_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GetProductsActivity.class);
                intent.putExtra(Globals.GET_PRODUCTS_ACTIVITY_TYPE, Globals.ProductsActivityType.GET_PRODUCTS_BY_SEARCH);
                intent.putExtra(Globals.ProductsBySearchActivityProperties.SEARCH_TERM, "macbook");
                startActivityForResult(intent, Globals.REQUEST_CODE_ADD_TO_CART);
            }
        });
        return rootView;
    }

}
