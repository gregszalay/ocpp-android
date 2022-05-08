package net.chargerevolutionapp.profiles;

import net.chargerevolutionapp.FirestoreModel;

public class UserProfile implements FirestoreModel {

    private String ID;

    private boolean isAdmin;

    public void setCarConnector(String carConnector) {
        this.carConnector = carConnector;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    private String carConnector;
    private String carModel;
    private String userEmail;

    public UserProfile(boolean isAdmin, String carConnector, String carModel, String userEmail) {
        this.isAdmin = isAdmin;
        this.carConnector = carConnector;
        this.carModel = carModel;
        this.userEmail = userEmail;
    }

    public UserProfile() {
    }

    public String _getDocID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCarConnector() {
        return carConnector;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }
}
