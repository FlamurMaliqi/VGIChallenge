package bITe.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "stop")
public class Stop implements Serializable {

    @Id
    @Column(name = "stop_id")
    private String stopId;

    @Column(name = "stop_name")
    private String stopName;

    @Column( name = "stop_lat")
    private double stopLatitude;

    @Column(name = "stop_lon")
    private double stopLongitude;

    @Column(name = "location_type")
    private int locationType;

    @Column(name = "parent_station")
    private String parentStation;

    public Stop() {
    }

    public Stop(String stopId, String stopName, double stopLatitude, double stopLongitude, int locationType, String parentStation) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.locationType = locationType;
        this.parentStation = parentStation;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public double getStopLatitude() {
        return stopLatitude;
    }

    public void setStopLatitude(double stopLatitude) {
        this.stopLatitude = stopLatitude;
    }

    public double getStopLongitude() {
        return stopLongitude;
    }

    public void setStopLongitude(double stopLongitude) {
        this.stopLongitude = stopLongitude;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public String getParentStation() {
        return parentStation;
    }

    public void setParentStation(String parentStation) {
        this.parentStation = parentStation;
    }
}
