package sample.quarkus.jpajaxrscdijta;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
@Transactional
public class EmployeeResource {

    @Inject
    EmployeeService service;

    @GET
    @Path("all")
    @Produces("application/json")
    public List<Employee> getAll() {
        return service.getAll();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Employee get(@PathParam("id") long id) {
        return service.get(id)
                .orElseThrow(() -> new WebApplicationException(404));
    }

    @POST
    public Response register(@QueryParam("name") String name, @Context UriInfo uriInfo) {
        long createdId = service.register(name);
        return Response.created(
                uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdId)).build())
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response resign(@PathParam("id") long id) {
        try {
            service.resign(id);
            return Response.noContent().build();
        } catch (ApplicationException e) {
            throw new WebApplicationException(404);
        }
    }
}
