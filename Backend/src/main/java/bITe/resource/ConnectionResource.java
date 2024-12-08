package bITe.resource;

import java.util.List;
import java.util.Map;

import bITe.common.ConnectionRequest;
import bITe.common.Connection;
import bITe.common.Coordinates;
import bITe.service.ConnectionService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/connections")
public class ConnectionResource {
    
    @Inject
    ConnectionService connectionService;

    @GET
    @Path("/stops")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStops() {
        Map<String, Coordinates> stops = connectionService.getStopNamesWithCoordinates();
        if (stops.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No stops found")
                    .build();
        } else {
            return Response.ok(stops).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findConnections(@Valid ConnectionRequest request) {
        List<Connection> connections = connectionService.findConnections(request);
        return Response.ok(connections).build();
    }
}
