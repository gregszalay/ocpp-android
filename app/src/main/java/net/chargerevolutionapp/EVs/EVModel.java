package net.chargerevolutionapp.EVs;

import androidx.annotation.NonNull;

import net.chargerevolutionapp.FirestoreModel;

public class EVModel implements FirestoreModel {

    private String ID;
    private String connector;
    private String manufacturer;
    private String model;

    public EVModel(String connector, String manufacturer, String model) {
        this.connector = connector;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public EVModel() {
    }

    public String getConnector() {
        return connector;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String _getDocID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getManufacturer() + " " + this.getModel(); // What to display in the Spinner list.
    }
}
