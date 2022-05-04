package net.chargerevolutionapp;

import androidx.annotation.NonNull;

public class EVModel {

    public String getConnector() {
        return connector;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    private String connector;
    private String manufacturer;
    private String model;

    public EVModel(String connector, String manufacturer, String model) {
        this.connector = connector;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getManufacturer() + " " + this.getModel(); // What to display in the Spinner list.
    }

    public EVModel() {
    }
}
