package sample.quarkus.reactivepgclient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/")
public class EmployeeResource {

    @Inject
    EmployeeRepository employee;

    @GET
    @Path("all")
    @Produces("application/json")
    public CompletionStage<List<Employee>> getAll() {
        return employee.getAll();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public CompletionStage<Response> get(@PathParam("id") long id) {
        return employee.get(id)
                .thenApply(employee -> employee != null ? Response.ok(employee) : Response.status(Response.Status.NOT_FOUND))
                .thenApply(Response.ResponseBuilder::build);
    }

    @POST
    public CompletionStage<Response> register(@QueryParam("name") String name) {
        return employee.register(name)
                .thenApply(id -> URI.create("/" + id))
                .thenApply(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("{id}")
    public CompletionStage<Response> resign(@PathParam("id") long id) {
        return employee.resign(id)
                .thenApply(deleted -> deleted ? Response.noContent() : Response.status(Response.Status.NOT_FOUND))
                .thenApply(Response.ResponseBuilder::build);
    }
}
