package gr.clink.nopandroidclient.model;

/**
 * Created by themisp on 6/4/2017.
 */

public class CartProduct extends Product {
    private int quantity;

    CartProduct(Product p, int quantity){
        this.setCategoryName(p.getCategoryName());
        this.setQuantity(quantity);
        this.setFullDescription(p.getFullDescription());
        this.setPictureURLS(p.getPictureURLS());
        this.setProductId(p.getProductId());
        this.setProductName(p.getProductName());
        this.setProductPrice(p.getProductPrice());
        this.setShortDescription(p.getShortDescription());
        this.setStockQuantity(this.getStockQuantity());
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
