package gr.clink.nopandroidclient.adapters;

/**
 * Created by Thanos on 1/10/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.telerik.widget.list.ListViewDataSourceAdapter;
import com.telerik.widget.list.ListViewHolder;

import java.util.List;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.CartProduct;


public class ShoppingCartAdapter extends ListViewDataSourceAdapter {
    private Context context;
    private int itemLayoutId;
    private long deletedItemId = -1;
    private int listViewCurrentSwipeIndex;

    public ShoppingCartAdapter(List items, Context context, int itemLayoutId) {
        super(items);
        this.context = context;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public ListViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        return new ShoppingCartItemViewHolder(LayoutInflater.from(this.context).inflate(itemLayoutId, viewGroup, false),context);
    }

    @Override
    public void onBindItemViewHolder(ListViewHolder holder, Object entity) {
        ShoppingCartItemViewHolder typedViewHolder = (ShoppingCartItemViewHolder) holder;
        long itemId = getItemId(entity);
        if (deletedItemId == itemId) {
            //TODO: REMOVE ITEM
        }
        typedViewHolder.bind((CartProduct) entity);
    }

    @Override
    public ListViewHolder onCreateSwipeContentHolder(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cart_list_view_selection_item_swipe_content, viewGroup, false);
        final CartItemSwipeViewHolder viewHolder = new CartItemSwipeViewHolder(view);

        viewHolder.imgFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartProduct productToFav = getSwipedCartProduct();
                //productToFav.setFavourite(!postToFav.isFavourite());
                notifySwipeExecuteFinished();
            }
        });

        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartProduct CartProductToDelete = getSwipedCartProduct();
                deletedItemId = getItemId(CartProductToDelete);
                notifyItemChanged(listViewCurrentSwipeIndex);
                notifySwipeExecuteFinished();
            }
        });
        return new CartItemSwipeViewHolder(view);
    }

    @Override
    public void onBindSwipeContentHolder(ListViewHolder viewHolder, int position) {
        listViewCurrentSwipeIndex = position;
    }

    class CartItemSwipeViewHolder extends ListViewHolder {

        public ImageButton imgDelete;
        public ImageButton imgFavourite;
        public CartItemSwipeViewHolder(View itemView) {
            super(itemView);
            this.imgFavourite = (ImageButton) itemView.findViewById(R.id.imgFavourite);
            this.imgDelete = (ImageButton) itemView.findViewById(R.id.imgDelete);
        }
    }

    private CartProduct getSwipedCartProduct() {
        return (CartProduct) this.getItem(listViewCurrentSwipeIndex);
    }

}