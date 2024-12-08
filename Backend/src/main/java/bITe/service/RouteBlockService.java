package bITe.service;

import bITe.common.*;
import bITe.entity.RouteBlock;
import bITe.repository.RouteBlockRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class RouteBlockService {

    @Inject
    Logger LOGGER;
    private static final ZoneId BERLIN_TIME_ZONE = ZoneId.of("Europe/Berlin");

    @Inject
    RouteBlockRepository routeBlockRepository;

    public List<RouteBlock> getRouteBlocksAfter(Instant timestamp) {
        LOGGER.info("Getting route blocks after: " + timestamp.toString());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(timestamp, BERLIN_TIME_ZONE);
        return routeBlockRepository.getRouteBlocksAfter(zonedDateTime);
    }

    public List<RouteBlock> getRouteBlocksDepartureBetween(String shortName, String headSign, ZonedDateTime startDepartureTime, ZonedDateTime endDepartureTime) {
        LOGGER.info("Getting route blocks between: " + startDepartureTime.toString() + " and " + endDepartureTime.toString() + " for the route " + shortName + " " + headSign);
        return routeBlockRepository.findRouteBlocksWithDepartureWithinTimeRange(shortName, headSign, startDepartureTime, endDepartureTime);
    }

    public Optional<RouteBlock> getRouteExactRouteBlock(String shortName, String headSign, ZonedDateTime startDepartureTime, ZonedDateTime endDepartureTime) {
        return routeBlockRepository.findExactRouteBlock(shortName, headSign, startDepartureTime, endDepartureTime);
    }

    public boolean isBlocked(RouteBlock routeBlock) {
        // TODO tokenize both headsigns and compare them
        LOGGER.info("Checking if route block is blocked...");
        return !getRouteBlocksDepartureBetween(routeBlock.getShortName(), routeBlock.getHeadSign(), routeBlock.getDepartureTime(), routeBlock.getArrivalTime()).isEmpty();
    }

    public boolean isBlocked(String routeShortName, String tripHeadsign, ZonedDateTime departureTime, ZonedDateTime arrivalTime) {
        // TODO tokenize both headsigns and compare them
        LOGGER.info("Checking if route block is blocked...");
        return !getRouteBlocksDepartureBetween(routeShortName, tripHeadsign, departureTime, arrivalTime).isEmpty();
    }

    public Optional<RouteBlock> blockRoute(BlockRouteRequest blockRouteRequest) {
        LOGGER.info("Blocking new route...");

        RouteBlock routeBlock = new RouteBlock();
        routeBlock.setDepartureStopName(blockRouteRequest.fromStopName());
        routeBlock.setDepartureTime(blockRouteRequest.blockFrom());
        routeBlock.setDepartureStopLatitude(blockRouteRequest.fromCoordinates().latitude());
        routeBlock.setDepartureStopLongitude(blockRouteRequest.fromCoordinates().longitude());
        routeBlock.setArrivalStopName(blockRouteRequest.toStopName());
        routeBlock.setArrivalTime(blockRouteRequest.blockTo());
        routeBlock.setArrivalStopLatitude(blockRouteRequest.toCoordinates().latitude());
        routeBlock.setArrivalStopLongitude(blockRouteRequest.toCoordinates().longitude());
        routeBlock.setShortName(blockRouteRequest.shortName());
        routeBlock.setHeadSign(blockRouteRequest.headSign());

        LOGGER.info("Persisting...");
        routeBlockRepository.persist(routeBlock);

        // Check if the booking was persisted successfully
        if(routeBlockRepository.isPersistent(routeBlock)) {
            LOGGER.info("Route block has been persisted successfully.");
            return Optional.of(routeBlock);
        } else {
            LOGGER.info("Error persisting the route block to the database.");
            return Optional.empty();
        }
    }
}
