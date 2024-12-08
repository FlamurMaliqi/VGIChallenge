package bITe.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "route_block")
public class RouteBlock {

    private static final ZoneId BERLIN_TIME_ZONE = ZoneId.of("Europe/Berlin");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_block_id")
    private Long routeBlockId;

    @Column(name = "departure_time", nullable = false)
    private ZonedDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private ZonedDateTime arrivalTime;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "head_sign", nullable = false)
    private String headSign;

    @Column(name = "departure_stop_name", nullable = false)
    private String departureStopName;

    @Column(name = "departure_stop_latitude", nullable = false)
    @Min(value = 48, message = "Latitude must be between 48 and 50")
    @Max(value = 50, message = "Latitude must be between 48 and 50")
    private Double departureStopLatitude;

    @Column(name = "departure_stop_longitude", nullable = false)
    @Min(value = 10, message = "Longitude must be between 10 and 13")
    @Max(value = 13, message = "Longitude must be between 10 and 13")
    private Double departureStopLongitude;

    @Column(name = "arrival_stop_name", nullable = false)
    private String arrivalStopName;

    @Column(name = "arrival_stop_latitude", nullable = false)
    @Min(value = 48, message = "Latitude must be between 48 and 50")
    @Max(value = 50, message = "Latitude must be between 48 and 50")
    private Double arrivalStopLatitude;

    @Column(name = "arrival_stop_longitude", nullable = false)
    @Min(value = 10, message = "Longitude must be between 10 and 13")
    @Max(value = 13, message = "Longitude must be between 10 and 13")
    private Double arrivalStopLongitude;

    public RouteBlock() {
    }

    public RouteBlock(Long routeBlockId, ZonedDateTime departureTime, ZonedDateTime arrivalTime,
                      String shortName, String headSign,
                      String departureStopName, Double departureStopLatitude, Double departureStopLongitude,
                      String arrivalStopName, Double arrivalStopLatitude, Double arrivalStopLongitude) {
        this.routeBlockId = routeBlockId;
        this.departureTime = departureTime.withZoneSameInstant(BERLIN_TIME_ZONE);
        this.arrivalTime = arrivalTime.withZoneSameInstant(BERLIN_TIME_ZONE);
        this.shortName = shortName;
        this.headSign = headSign;
        this.departureStopName = departureStopName;
        this.departureStopLatitude = departureStopLatitude;
        this.departureStopLongitude = departureStopLongitude;
        this.arrivalStopName = arrivalStopName;
        this.arrivalStopLatitude = arrivalStopLatitude;
        this.arrivalStopLongitude = arrivalStopLongitude;
    }

    public RouteBlock(ZonedDateTime departureTime, ZonedDateTime arrivalTime,
                      String shortName, String headSign,
                      String departureStopName, Double departureStopLatitude, Double departureStopLongitude,
                      String arrivalStopName, Double arrivalStopLatitude, Double arrivalStopLongitude) {
        this.departureTime = departureTime.withZoneSameInstant(BERLIN_TIME_ZONE);
        this.arrivalTime = arrivalTime.withZoneSameInstant(BERLIN_TIME_ZONE);
        this.shortName = shortName;
        this.headSign = headSign;
        this.departureStopName = departureStopName;
        this.departureStopLatitude = departureStopLatitude;
        this.departureStopLongitude = departureStopLongitude;
        this.arrivalStopName = arrivalStopName;
        this.arrivalStopLatitude = arrivalStopLatitude;
        this.arrivalStopLongitude = arrivalStopLongitude;
    }

    public Long getRouteBlockId() {
        return routeBlockId;
    }

    public void setRouteBlockId(Long routeBlockId) {
        this.routeBlockId = routeBlockId;
    }

    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ZonedDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public ZonedDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ZonedDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getHeadSign() {
        return headSign;
    }

    public void setHeadSign(String headSign) {
        this.headSign = headSign;
    }

    public String getDepartureStopName() {
        return departureStopName;
    }

    public void setDepartureStopName(String departureStopName) {
        this.departureStopName = departureStopName;
    }

    public @Min(value = 48, message = "Latitude must be between 48 and 50") @Max(value = 50, message = "Latitude must be between 48 and 50") Double getDepartureStopLatitude() {
        return departureStopLatitude;
    }

    public void setDepartureStopLatitude(@Min(value = 48, message = "Latitude must be between 48 and 50") @Max(value = 50, message = "Latitude must be between 48 and 50") Double departureStopLatitude) {
        this.departureStopLatitude = departureStopLatitude;
    }

    public @Min(value = 10, message = "Longitude must be between 10 and 13") @Max(value = 13, message = "Longitude must be between 10 and 13") Double getDepartureStopLongitude() {
        return departureStopLongitude;
    }

    public void setDepartureStopLongitude(@Min(value = 10, message = "Longitude must be between 10 and 13") @Max(value = 13, message = "Longitude must be between 10 and 13") Double departureStopLongitude) {
        this.departureStopLongitude = departureStopLongitude;
    }

    public String getArrivalStopName() {
        return arrivalStopName;
    }

    public void setArrivalStopName(String arrivalStopName) {
        this.arrivalStopName = arrivalStopName;
    }

    public @Min(value = 48, message = "Latitude must be between 48 and 50") @Max(value = 50, message = "Latitude must be between 48 and 50") Double getArrivalStopLatitude() {
        return arrivalStopLatitude;
    }

    public void setArrivalStopLatitude(@Min(value = 48, message = "Latitude must be between 48 and 50") @Max(value = 50, message = "Latitude must be between 48 and 50") Double arrivalStopLatitude) {
        this.arrivalStopLatitude = arrivalStopLatitude;
    }

    public @Min(value = 10, message = "Longitude must be between 10 and 13") @Max(value = 13, message = "Longitude must be between 10 and 13") Double getArrivalStopLongitude() {
        return arrivalStopLongitude;
    }

    public void setArrivalStopLongitude(@Min(value = 10, message = "Longitude must be between 10 and 13") @Max(value = 13, message = "Longitude must be between 10 and 13") Double arrivalStopLongitude) {
        this.arrivalStopLongitude = arrivalStopLongitude;
    }
}
