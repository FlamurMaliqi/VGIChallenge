package bITe.repository;

import bITe.entity.Route;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RouteRepository implements PanacheRepository<Route> {

    public List<Route> findAllRoutes() {
        return listAll();
    }

    public Set<String> findAllShortNames() {
        return findAll().stream()
                        .map(Route::getRouteShortName)
                        .collect(Collectors.toSet());
    }
}
