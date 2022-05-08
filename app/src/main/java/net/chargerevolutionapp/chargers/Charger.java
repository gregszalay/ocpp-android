package net.chargerevolutionapp.chargers;

import net.chargerevolutionapp.FirestoreModel;

public class Charger implements FirestoreModel {

    private String ID;
    private String address;
    private long chargingStartTime;
    private long chargingStopTime;
    private String connectorTypes;
    private String country;
    private boolean isCharging;
    private String name;
    private String postCode;
    private boolean reserved;
    private long reservedUntil;
    private int maxPowerInkW;
    private String reservedByUserEmail;

    private String whoIsChargingEmail;

    public Charger(String address, long chargingStartTime, long chargingStopTime, String connectorTypes, String country, boolean isCharging, String name, String postCode,
                   boolean reserved, long reservedUntil, int maxPowerInkW, String reservedByUserEmail, String whoIsChargingEmail) {
        this.address = address;
        this.chargingStartTime = chargingStartTime;
        this.chargingStopTime = chargingStopTime;
        this.connectorTypes = connectorTypes;
        this.country = country;
        this.isCharging = isCharging;
        this.name = name;
        this.postCode = postCode;
        this.reserved = reserved;
        this.reservedUntil = reservedUntil;
        this.maxPowerInkW = maxPowerInkW;
        this.reservedByUserEmail = reservedByUserEmail;
        this.whoIsChargingEmail = whoIsChargingEmail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setChargingStartTime(long chargingStartTime) {
        this.chargingStartTime = chargingStartTime;
    }

    public void setChargingStopTime(long chargingStopTime) {
        this.chargingStopTime = chargingStopTime;
    }

    public void setConnectorTypes(String connectorTypes) {
        this.connectorTypes = connectorTypes;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setMaxPowerInkW(int maxPowerInkW) {
        this.maxPowerInkW = maxPowerInkW;
    }

    public Charger(
            String name,
            String country,
            String postCode,
            String address,
            String connectorTypes,
            int maxPowerInkW
    ) {
        this.address = address;
        this.chargingStartTime = 0;
        this.chargingStopTime = 0;
        this.connectorTypes = connectorTypes;
        this.country = country;
        this.isCharging = false;
        this.name = name;
        this.postCode = postCode;
        this.reserved = false;
        this.reservedUntil = 0;
        this.maxPowerInkW = maxPowerInkW;
        this.reservedByUserEmail = "";
        this.whoIsChargingEmail = "";
    }

    public Charger() {
    }

    public String getReservedByUserEmail() {
        return reservedByUserEmail;
    }

    public void setReservedByUserEmail(String reservedByUserEmail) {
        this.reservedByUserEmail = reservedByUserEmail;
    }

    public int getMaxPowerInkW() {
        return maxPowerInkW;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public long getChargingStartTime() {
        return chargingStartTime;
    }

    public long getChargingStopTime() {
        return chargingStopTime;
    }

    public String getConnectorTypes() {
        return connectorTypes;
    }

    public String getCountry() {
        return country;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public String getPostCode() {
        return postCode;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public long getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(long reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public String _getDocID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWhoIsChargingEmail() {
        return whoIsChargingEmail;
    }

    public void setWhoIsChargingEmail(String whoIsChargingEmail) {
        this.whoIsChargingEmail = whoIsChargingEmail;
    }
}
