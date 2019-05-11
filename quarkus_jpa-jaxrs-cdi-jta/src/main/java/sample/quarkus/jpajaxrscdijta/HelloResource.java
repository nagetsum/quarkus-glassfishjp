package sample.quarkus.jpajaxrscdijta;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class HelloResource {
    @GET
    public String helloWorld() {
        return "Hello, World!";
    }
}
