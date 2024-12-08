package bITe.common;

import bITe.entity.RouteBlock;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record Connection(
        @Valid
        ConnectionStop from,

        @Valid
        ConnectionStop to,

        @NotNull(message = "To must not be null")
        List<ConnectionSegment> connectionSegments,

        @Positive
        @NotNull(message = "Duration must not be null")
        Long durationInMinutes,

        @NotNull(message = "Is blocked must not be null")
        Boolean isBlocked
) {
    public List<RouteBlock> mapToRouteBlocks() {
        List<RouteBlock> routeBlocks = new ArrayList<>();

        // Iterate over each segment of the connection and create RouteBlock instances
        for (ConnectionSegment connectionSegment : connectionSegments) {
            routeBlocks.add(connectionSegment.mapToRouteBlock());
        }

        // Sort the list by departure time in ascending order
        routeBlocks.sort(Comparator.comparing(RouteBlock::getDepartureTime));

        return routeBlocks;
    }

    public static class Builder {
        private ConnectionStop from;
        private ConnectionStop to;
        private List<ConnectionSegment> connectionSegments = new ArrayList<>();
        private Long durationInMinutes;
        private boolean isBlocked;

        public Builder setFrom(ConnectionStop from) {
            this.from = from;
            return this;
        }

        public Builder setTo(ConnectionStop to) {
            this.to = to;
            return this;
        }

        public Builder addSegment(ConnectionSegment segment) {
            this.connectionSegments.add(segment);
            return this;
        }

        public Builder setConnectionSegments(List<ConnectionSegment> connectionSegments) {
            this.connectionSegments = new ArrayList<>(connectionSegments);
            return this;
        }

        public Builder setDurationInMinutes(Long duration) {
            this.durationInMinutes = duration;
            return this;
        }

        public Builder setIsBlocked(boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public Builder reset() {
            this.from = null;
            this.to = null;
            this.connectionSegments = new ArrayList<>();
            this.durationInMinutes = null;
            this.isBlocked = false;
            return this;
        }

        public Connection build() {
            Connection connection = new Connection(from, to, connectionSegments, durationInMinutes, isBlocked);
            reset();
            return connection;
        }
    }
}

