package gr.clink.nopandroidclient.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gr.clink.nopandroidclient.R;

/**
 * Created by themisp on 17/1/2017.
 */
public class UserInformation {
    public enum CustomerLoginResults{
        Successful(1),
        CustomerNotExist(2),
        WrongPassword(3),
        NotActive(4),
        Deleted(5),
        NotRegistered(6);

        private int value;

        CustomerLoginResults(int value){
            this.value = value;
        }

        public String toString(Context context){
            switch (this){
                case Successful:
                    return context.getString(R.string.login_successful);
                case CustomerNotExist: case WrongPassword:
                    return  context.getString(R.string.login_invalid_credentials);
                case NotActive:
                    return  context.getString(R.string.login_account_not_activated);
                case Deleted:
                    return  context.getString(R.string.login_account_deleted);
                case NotRegistered:
                    return  context.getString(R.string.login_account_not_registered);
                default:
                    return "";
            }
        }

        public static CustomerLoginResults typeFromInt(Integer i){
            switch (i){
                case 1:
                    return Successful;
                case 2:
                    return  CustomerNotExist;
                case 3:
                    return  WrongPassword;
                case 4:
                    return NotActive;
                case 5:
                    return Deleted;
                case 6:
                    return  NotRegistered;
            }
            return NotRegistered;
        }
    }
    private static UserInformation ourInstance = new UserInformation();
    public static UserInformation getInstance() {
        return ourInstance;
    }

    private String userName;
    private String name;
    private String guid;
    private List<Integer> roles;
    private List<Address> addresses;
    private List<CartProduct> cartProducts;

    public String getGuid() {
        return guid;
    }
    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void addCartProduct(CartProduct np){
        Boolean productFound = false;
        for (CartProduct p: cartProducts) {
            if(p.getProductId().equals(np.getProductId())){
                p.setQuantity(p.getQuantity() + np.getQuantity());
                productFound = true;
                break;
            }
        }
        if(!productFound)
            cartProducts.add(np);
    }

    public void updateCartProductQuantity(int Id,int qty){
        for (CartProduct p: cartProducts) {
            if(p.getProductId().equals(Id)){
                p.setQuantity(qty);
            }
        }
    }

    public void removeCartProduct(int productId){
        for (CartProduct p: cartProducts) {
            if(p.getProductId() == productId){
                cartProducts.remove(p);
            }
        }
    }

    public Boolean hasEmptyCart(){
        return cartProducts.isEmpty();
    }

    public void clearCart(){
        cartProducts.clear();
    }

    private UserInformation() {
        cartProducts = new ArrayList<CartProduct>();
    }

    public boolean isAuthenticated(){
        return userName != null;
    }

    private static class CustomerConstants{
        static String EMAIL = "Email";
        static String FULLNAME = "FullName";
        static String GUID = "GUID";
        static String ROLES = "Roles";
        static String ADDRESSES = "Addresses"; //jsonobject
        private static class AddressInfo{
            static String RECIPIENTNAME = "FullRecipientName";
            static String CITY = "City";
            static String ADDRESS1 = "Address1";
            static String ADDRESS2 = "Address2";
            static String COMPANY = "Company";
            static String ZIP = "ZipPostalCode";
            static String PHONENUMBER = "PhoneNumber";
            static String FAX = "FaxNumber";
            static String COUNTRY = "Country";
            static String PROVINCE = "Province";
        }
    }

    public void setCustomerFrom(JSONObject json) throws JSONException{
        userName = json.getString(CustomerConstants.EMAIL);
        name = json.getString(CustomerConstants.FULLNAME);
        guid = json.getString(CustomerConstants.GUID);
        JSONArray _addresses = json.getJSONArray(CustomerConstants.ADDRESSES);
        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i< _addresses.length();i++){
            JSONObject address = _addresses.getJSONObject(i);
            addressList.add(new Address(address.getString(CustomerConstants.AddressInfo.RECIPIENTNAME),
                    address.getString(CustomerConstants.AddressInfo.CITY),
                    address.getString(CustomerConstants.AddressInfo.ADDRESS1),
                    address.getString(CustomerConstants.AddressInfo.ADDRESS2),
                    address.getString(CustomerConstants.AddressInfo.COMPANY),
                    address.getString(CustomerConstants.AddressInfo.ZIP),
                    address.getString(CustomerConstants.AddressInfo.PHONENUMBER),
                    address.getString(CustomerConstants.AddressInfo.FAX),
                    address.getString(CustomerConstants.AddressInfo.COUNTRY),
                    address.getString(CustomerConstants.AddressInfo.PROVINCE)
            ));
        }
        addresses = addressList;
        JSONArray _roles = json.getJSONArray(CustomerConstants.ROLES);
        List<Integer> roleList = new ArrayList<>();
        for (int i = 0; i< _roles.length();i++){
            roleList.add(_roles.getInt(i));
        }
        roles = roleList;
    }

    public void logOut(){
        userName = null;
        name = null;
    }
}
