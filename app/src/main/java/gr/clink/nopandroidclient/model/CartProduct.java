package gr.clink.nopandroidclient.model;

/**
 * Created by themisp on 6/4/2017.
 */

public class CartProduct{
    private int quantity;
    private Product product;

    public CartProduct(Product p, int quantity){
        this.setQuantity(quantity);
        this.product = p;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
