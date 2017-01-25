package gr.clink.nopandroidclient.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by themisp on 11/1/2017.
 */

public class SourceModel implements Serializable {

    private ArrayList<String> dependencies;
    private int currentIndex;

    public SourceModel(ArrayList<String> dependencyFileNames) {
        this.dependencies = dependencyFileNames;

        if (this.dependencies == null) {
            this.dependencies = new ArrayList<String>();
        }
    }

    public ArrayList<String> getDependencies() {
        return this.dependencies;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setCurrentIndex(int value) {
        this.currentIndex = value;
    }
}
