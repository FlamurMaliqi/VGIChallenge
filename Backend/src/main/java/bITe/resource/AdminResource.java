package bITe.resource;

import bITe.service.AdminService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import java.util.Set;

@Path("/v1/admin")
public class AdminResource {

    @Inject
    AdminService adminService;

    @GET
    @Path("/route-short-names")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRouteShortNames() {
        Set<String> routeShortNames = adminService.getRouteShortNames();
        if (routeShortNames.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No route short names found")
                    .build();
        } else {
            return Response.ok(routeShortNames).build();
        }
    }

    @GET
    @Path("/trip-headsigns")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTripHeadsigns() {
        Set<String> tripHeadsigns = adminService.getTripHeadsigns();
        if (tripHeadsigns.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No trip headsigns found")
                    .build();
        } else {
            return Response.ok(tripHeadsigns).build();
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(MultipartFormDataInput input) {
        boolean success = adminService.uploadFile(input);
        if (!success) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error uploading or processing the file")
                    .build();
        } else {
            return Response.ok().build();
        }
    }
}
