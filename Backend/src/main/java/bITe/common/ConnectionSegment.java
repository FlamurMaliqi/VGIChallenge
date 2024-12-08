package bITe.common;

import bITe.entity.RouteBlock;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConnectionSegment(
        @NotBlank(message = "Route short name must not be null")
        String routeShortName,

        @NotBlank(message = "Trip headsign must not be null")
        String tripHeadsign,

        Integer expectedOccupation,

        @NotNull(message = "Is blocked must not be null")
        Boolean isBlocked,

        @Valid
        ConnectionStop fromStop,

        @Valid
        ConnectionStop toStop
) {
    public RouteBlock mapToRouteBlock() {
        RouteBlock routeBlock = new RouteBlock();
        routeBlock.setDepartureTime(fromStop().departure());
        routeBlock.setArrivalTime(toStop().arrival());
        routeBlock.setShortName(routeShortName());
        routeBlock.setHeadSign(tripHeadsign());
        routeBlock.setDepartureStopName(fromStop().stopName());
        routeBlock.setDepartureStopLatitude(fromStop().coordinates().latitude());
        routeBlock.setDepartureStopLongitude(fromStop().coordinates().longitude());
        routeBlock.setArrivalStopName(toStop().stopName());
        routeBlock.setArrivalStopLatitude(toStop().coordinates().latitude());
        routeBlock.setArrivalStopLongitude(toStop().coordinates().longitude());
        return routeBlock;
    }

    public static class Builder {
        private String routeShortName;
        private String tripHeadsign;
        private int expectedOccupation = -1;
        private boolean isBlocked;
        private ConnectionStop fromStop;
        private ConnectionStop toStop;

        public Builder setRouteShortName(String routeShortName) {
            this.routeShortName = routeShortName;
            return this;
        }

        public Builder setTripHeadsign(String tripHeadsign) {
            this.tripHeadsign = tripHeadsign;
            return this;
        }

        public Builder setExpectedOccupation(int expectedOccupation) {
            this.expectedOccupation = expectedOccupation;
            return this;
        }

        public Builder setIsBlocked(boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public Builder setFromStop(ConnectionStop fromStop) {
            this.fromStop = fromStop;
            return this;
        }

        public Builder setToStop(ConnectionStop toStop) {
            this.toStop = toStop;
            return this;
        }

        public Builder reset() {
            this.routeShortName = null;
            this.tripHeadsign = null;
            this.expectedOccupation = -1;
            this.isBlocked = false;
            this.fromStop = null;
            this.toStop = null;
            return this;
        }

        public ConnectionSegment build() {
            ConnectionSegment connectionSegment = new ConnectionSegment(routeShortName, tripHeadsign, expectedOccupation, isBlocked, fromStop, toStop);
            reset();
            return connectionSegment;
        }
    }
}