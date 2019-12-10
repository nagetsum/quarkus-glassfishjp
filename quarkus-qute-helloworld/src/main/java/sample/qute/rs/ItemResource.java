package sample.qute.rs;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("items")
public class ItemResource {

    @Inject
    Template items;

    @GET
    @Path("/all")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getAll() {
        return items.data("items", getSnacks());
    }

    private List<Item> getSnacks() {
        Item ice = Item.of("ice cream", 120);
        Item cookie = Item.of("cookie", 80);
        Item cake = Item.of("cake", 200);
        Item chocolate = Item.of("chocolate", 90);
        return Arrays.asList(ice, cookie, cake, chocolate);
    }
}
