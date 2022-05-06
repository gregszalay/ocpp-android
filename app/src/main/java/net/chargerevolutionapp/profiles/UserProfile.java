package net.chargerevolutionapp.profiles;

public class UserProfile {

    public UserProfile(String carConnector, String carModel, String userEmail) {
        this.carConnector = carConnector;
        this.carModel = carModel;
        this.userEmail = userEmail;
    }

    private String carConnector;
    private String carModel;
    private String userEmail;

    public UserProfile() {
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
}
