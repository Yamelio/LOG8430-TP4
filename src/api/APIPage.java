package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/hi")
public class APIPage
{
    @GET
    @Path("/{name}")
    public Response getMessage(@PathParam("name") String name)
    {
        String outMsg = "BONJOUR CE CERVISE MARCHE " + name + "!";
        return Response.status(200).entity(outMsg).build();
    }

}
