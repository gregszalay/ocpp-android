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

    public Charger(String address, long chargingStartTime, long chargingStopTime, String connectorTypes, String country, boolean isCharging, String name, String postCode,
                   boolean reserved, long reservedUntil, int maxPowerInkW) {
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
