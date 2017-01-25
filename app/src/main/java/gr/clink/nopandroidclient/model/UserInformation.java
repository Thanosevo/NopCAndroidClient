package gr.clink.nopandroidclient.model;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private UserInformation() {
    }

    public boolean isAuthenticated(){
        if (userName != null) {
            return true;
        }
        return false;
    }

    private static class CustomerConstants{
        static String EMAIL = "Email";
        static String FULLNAME = "FullName";
        static String SHIPPINGADDRESS = "ShippingAddress"; //jsonobject
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

    }

    public void logOut(){
        userName = null;
        name = null;
    }
}
