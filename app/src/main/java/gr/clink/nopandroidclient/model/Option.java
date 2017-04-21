package gr.clink.nopandroidclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

/**
 * Created by themisp on 18/4/2017.
 */

public class Option implements Parcelable{
    private String name;
    private String group;
    private Object value;

    public Option(){}

    public Option(String name, String group, Object value) {
        this.name = name;
        this.group = group;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    public static ArrayList<Option> getOptionsOfGroup(ArrayList<Option> options, String group){
        ArrayList<Option> result = new ArrayList<>();
        for(Option opt : options){
            if(opt.getGroup().equals(group)){
                result.add(opt);
            }
        }
        return result;
    }

    public static ArrayList<Option> getListOfOptionsFrom(Set<String> list, String groupName,Object initialValue){
        ArrayList<Option> result = new ArrayList<>();
        for (String v: list) {
            result.add(new Option(v,groupName,initialValue));
        }
        return result;
    }

    public static ArrayList<Option> getPriceRangeListOfOptions(Float minPrice, Float maxPrice,Integer numOfOptions){
        ArrayList<Option> result = new ArrayList<>();
        Float diff = maxPrice - minPrice;
        float distance=(float) Math.ceil(diff.doubleValue() / numOfOptions.doubleValue());
        while(distance < 50 && numOfOptions >3){
            numOfOptions--;
            distance=(float) Math.ceil(diff.doubleValue() / numOfOptions.doubleValue());
        }

        float step = minPrice;
        for (int i = 1;i<= numOfOptions;i++) {
            if(i == numOfOptions)
                result.add(new Option(String.format(Locale.getDefault(),"%.2f€ - %.2f€",step,maxPrice),"Price Range",false));
            else
                result.add(new Option(String.format(Locale.getDefault(),"%.2f€ - %.2f€",step,step+distance),"Price Range",false));
            step += distance;
        }
        return result;
    }

    public Float[] getPriceRangeFromOption(){
        Float[] result = {0f,0f};
        if(this.getGroup().equals("Price Range")){
            String[] array = getName().split("-");
            result[0] = Float.valueOf(array[0].replace("€",""));
            result[1] = Float.valueOf(array[1].replace("€",""));
        }
        return result;
    }


    //region PARCELLABLE
    protected Option(Parcel in) {
        name = in.readString();
        group = in.readString();
        value = (Object) in.readValue(Object.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(group);
        dest.writeValue(value);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
    //endregion
}
