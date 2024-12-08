package bITe.repository;

import bITe.entity.Trip;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TripRepository implements PanacheRepository<Trip> {

    public List<Trip> findAllTrips() {
        return listAll();
    }

    public Set<String> findAllHeadsigns() {
        return findAll().stream()
                        .map(Trip::getTripHeadsign)
                        .collect(Collectors.toSet());
    }
}
