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

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    private boolean reserved;

    public void setReservedUntil(long reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    private long reservedUntil;
    private int maxPowerInkW;

    public void setReservedByUserEmail(String reservedByUserEmail) {
        this.reservedByUserEmail = reservedByUserEmail;
    }

    private String reservedByUserEmail;

    public String getReservedByUserEmail() {
        return reservedByUserEmail;
    }


    public Charger(String address, long chargingStartTime, long chargingStopTime, String connectorTypes, String country, boolean isCharging, String name, String postCode,
                   boolean reserved, long reservedUntil, int maxPowerInkW, String reservedByUserEmail) {
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
    }

    public Charger() {
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

    public long getReservedUntil() {
        return reservedUntil;
    }


    public String _getDocID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
