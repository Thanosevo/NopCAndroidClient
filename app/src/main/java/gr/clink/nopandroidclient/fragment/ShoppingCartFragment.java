package gr.clink.nopandroidclient.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.telerik.widget.list.RadListView;
import com.telerik.widget.list.SwipeExecuteBehavior;

import customfonts.MyTextView;
import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.adapters.ShoppingCartAdapter;
import gr.clink.nopandroidclient.model.CartProduct;

public class ShoppingCartFragment extends FragmentBase {
    private RadListView listView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private SwipeExecuteBehavior swipeExecuteBehavior;
    private MyTextView checkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if(UserInformation.getInstance().hasEmptyCart()){
//            return inflater.inflate(R.layout.fragment_shopping_cart_empty, container, false);
//        }
        View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        listView = (RadListView) rootView.findViewById(R.id.listView);
        checkout = (MyTextView) rootView.findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Proceed to checkout",Toast.LENGTH_SHORT).show();
            }
        });


        shoppingCartAdapter = new ShoppingCartAdapter(CartProduct.RandomCollectionOfCartProducts() /*UserInformation.getInstance().getCartProducts()*/,ShoppingCartFragment.this.getActivity(), R.layout.shopping_cart_list_item_layout );
        listView.setAdapter(shoppingCartAdapter);

        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 1);
        listView.setLayoutManager(gridLayout);
        listView.addItemClickListener(new ListViewClickListener());

        this.swipeExecuteBehavior = new SwipeExecuteBehavior();
        this.swipeExecuteBehavior.addListener(new SwipeListener());
        this.swipeExecuteBehavior.setAutoDissolve(false);
        this.listView.addBehavior(this.swipeExecuteBehavior);

        return rootView;
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
}
