package bITe.resource;

import bITe.common.BlockRouteRequest;
import bITe.entity.RouteBlock;
import bITe.service.RouteBlockService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Path("/v1/routes")
public class RouteBlockResource {

    @Inject
    RouteBlockService routeBlockService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRouteBlocks(@QueryParam("timestamp") String timestamp) {
        try {
            // Parse the timestamp query parameter as an Instant (timestamp)
            Instant fromTimestamp = Instant.parse(timestamp);

            List<RouteBlock> routeBlocks = routeBlockService.getRouteBlocksAfter(fromTimestamp);

            if (routeBlocks.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No route blocks found after the given time")
                        .build();
            } else {
                return Response.ok(routeBlocks).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid timestamp format or error processing the request")
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response blockRoute(@Valid BlockRouteRequest request) {
        Optional<RouteBlock> routeBlock =  routeBlockService.blockRoute(request);
        if (routeBlock.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error blocking route")
                    .build();
        } else {
            return Response.ok(routeBlock).build();
        }
    }
}
