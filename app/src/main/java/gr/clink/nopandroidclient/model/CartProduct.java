package gr.clink.nopandroidclient.model;

import java.util.ArrayList;
import java.util.List;

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

    public static List<CartProduct> RandomCollectionOfCartProducts(){
        ArrayList<CartProduct> list = new ArrayList<>();
        ArrayList<String> p = new ArrayList<String>();
        p.add("http://www.novelupdates.com/img/noimagefound.jpg");
        list.add(new CartProduct(new Product(
                "item1",
                "category1",
                1,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vestibulum risus nec rhoncus tincidunt. Praesent sit amet suscipit metus, tempor viverra diam. ",
                4000,
                350f,
                p,
                null)
                ,1));

        list.add(new CartProduct(new Product(
                "item2",
                "category2",
                2,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vestibulum risus nec rhoncus tincidunt. Praesent sit amet suscipit metus, tempor viverra diam. ",
                4000,
                350f,
                p,
                null)
                ,1));


        list.add(new CartProduct(new Product(
                "item3",
                "category3",
                3,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vestibulum risus nec rhoncus tincidunt. Praesent sit amet suscipit metus, tempor viverra diam. ",
                4000,
                350f,
                p,
                null)
                ,1));

        list.add(new CartProduct(new Product(
                "item4",
                "category4",
                4,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vestibulum risus nec rhoncus tincidunt. Praesent sit amet suscipit metus, tempor viverra diam. ",
                4000,
                350f,
                p,
                null)
                ,1));

        list.add(new CartProduct(new Product(
                "item5",
                "category5",
                5,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vestibulum risus nec rhoncus tincidunt. Praesent sit amet suscipit metus, tempor viverra diam. ",
                4000,
                350f,
                p,
                null)
                ,1));

        return list;
    }
}
