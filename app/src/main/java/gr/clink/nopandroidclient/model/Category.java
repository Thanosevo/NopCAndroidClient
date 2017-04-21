package gr.clink.nopandroidclient.model;

/**
 * Created by Thanos on 1/10/2017.
 */

public class Category {

    private String parentCategoryName;
    private Integer id;
    private Boolean _hasSubcategories;
    private String name;
    private String imageURL;

    public Category(){

    }

    public Category(String parentCategoryName, Integer id, String name, String imageURL, Boolean hasSubcategories) {
        this.parentCategoryName = parentCategoryName;
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this._hasSubcategories = hasSubcategories;
    }

    public Boolean hasSubcategories() {
        return _hasSubcategories;
    }

    public void hasSubcategories(Boolean hasSubcategories) {
        this._hasSubcategories = hasSubcategories;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
