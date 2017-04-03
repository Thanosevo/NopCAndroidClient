package gr.clink.nopandroidclient.model;

public class Address {
    private String name;
    private  String city;
    private  String address1;
    private  String address2;
    private  String company;
    private  String zip;
    private  String phoneNumber;
    private  String fax;
    private  String country;
    private  String province;


    public Address(String name, String city, String address1, String address2, String company, String zip, String phoneNumber, String fax, String country, String province) {
        this.name = name;
        this.city = city;
        this.address1 = address1;
        this.address2 = address2;
        this.company = company;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.fax = fax;
        this.country = country;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}

