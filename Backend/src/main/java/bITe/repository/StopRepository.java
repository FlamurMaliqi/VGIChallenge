package bITe.repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import bITe.entity.Stop;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StopRepository implements PanacheRepository<Stop> {

    public Optional<String> findStopIdByStopName(String stopName) {
        Optional<Stop> stop = find("stopName", stopName).firstResultOptional();
        return stop.map(Stop::getStopName);
    }

    public Optional<Double> findStopLatByStopName(String stopName) {
        Optional<Stop> stop = find("stopName", stopName).firstResultOptional();
        return stop.map(Stop::getStopLatitude);

    }

    public Optional<Double> findStopLonByStopName(String stopName) {
        Optional<Stop> stop = find("stopName", stopName).firstResultOptional();
        return stop.map(Stop::getStopLongitude);
    }

    public Set<String> findAllStopNames() {
        return listAll().stream()
                .map(Stop::getStopName)
                .collect(Collectors.toSet());
    }
}
