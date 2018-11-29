package api;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path("/api")
public class APIPage
{
    @GET
    @Path("/produits")
    public Response getProduits()
    {
        String outMsg = CassandraConnector.getProduits();
        return Response.status(200).entity(outMsg).build();
    }

    @GET
    @Path("/facture")
    public Response getFactures()
    {
        String outMsg = CassandraConnector.getFactures();
        return Response.status(200).entity(outMsg).build();
    }

    @POST
    @Path("/facture")
    public Response addFacture(@FormParam("fid") int fid, @FormParam("items") String items)
    {

        try {
            HashMap<String,Object> result =
                    new ObjectMapper().readValue(items, HashMap.class);
            String outMsg = CassandraConnector.addFacture(fid,result);
            return Response.status(200).entity(outMsg).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(200).entity(e.getMessage()).build();
        }
    }

}
