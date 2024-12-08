package bITe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import bITe.repository.RouteRepository;
import bITe.repository.TripRepository;

import java.util.Set;

@ApplicationScoped
@Transactional
public class AdminService {

    @Inject
    Logger LOGGER;
    @Inject
    RouteRepository routeRepository;
    @Inject
    TripRepository tripRepository;

    public Set<String> getRouteShortNames() {
        LOGGER.info("Getting route short names...");
        return routeRepository.findAllShortNames();
    }

    public Set<String> getTripHeadsigns() {
        LOGGER.info("Getting trip headsigns...");
        return tripRepository.findAllHeadsigns();
    }

    public boolean uploadFile(MultipartFormDataInput input) {
        // TODO
        // TODO Make sure that only files of the format .csv can be uploaded
        // TODO Make sure that only files with the names ["stops.csv", "trips.csv", "routes.csv"] can be uploaded
        // TODO Make sure that depending on the file name all relevant columns are contained
        return false;
    }
}
