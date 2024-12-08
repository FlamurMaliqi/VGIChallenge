package bITe.repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import bITe.entity.RouteBlock;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RouteBlockRepository implements PanacheRepository<RouteBlock> {

    public List<RouteBlock> findRouteBlocksWithDepartureWithinTimeRange(
            String shortName,
            String headSign,
            ZonedDateTime startDepartureTime,
            ZonedDateTime endDepartureTime) {

        return find(
                "shortName = ?1 and headSign = ?2 and "
                        + "departureTime BETWEEN ?3 AND ?4",
                shortName, headSign, startDepartureTime, endDepartureTime
        ).list();
    }

    public Optional<RouteBlock> findExactRouteBlock(
            String shortName,
            String headSign,
            ZonedDateTime departureTime,
            ZonedDateTime arrivalTime) {

        return find(
                "shortName = ?1 and headSign = ?2 and departureTime = ?3 and arrivalTime = ?4",
                shortName, headSign, departureTime, arrivalTime
        ).firstResultOptional();
    }

    public List<RouteBlock> getRouteBlocksAfter(ZonedDateTime zonedDateTime) {
        return streamAll()
            .filter(routeBlock -> routeBlock.getDepartureTime().isAfter(zonedDateTime))
            .toList();
    }
}
