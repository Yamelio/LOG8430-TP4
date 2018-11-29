package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api")
public class APIPage
{
    @GET
    @Path("/produits")
    public Response getMessage()
    {
        String outMsg = CassandraConnector.getProduits();
        return Response.status(200).entity(outMsg).build();
    }

}
