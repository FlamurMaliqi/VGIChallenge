package bITe.client;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.Optional;

@ApplicationScoped
public class GoogleGeocodingClient {

    @Inject
    Logger LOGGER;

    private static GeoApiContext geoApiContext;

    public GoogleGeocodingClient(@ConfigProperty(name = "google.api.key")  String googleApiKey) {
        geoApiContext = new GeoApiContext.Builder().apiKey(googleApiKey).build();
    }

    public Optional<String> getPlaceId(double latitude, double longitude) {
        LOGGER.info("Getting place Google PlaceId...");
        LatLng location = new LatLng(latitude, longitude);
        GeocodingApiRequest geocodingApiRequest = GeocodingApi.reverseGeocode(geoApiContext, location);
        try {
            GeocodingResult[] results = geocodingApiRequest.await();
            if (results != null && results.length > 0) {
                return Optional.of(results[0].placeId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
