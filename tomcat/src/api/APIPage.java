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
    @Path("/factures")
    public Response getFactures()
    {
        String outMsg = CassandraConnector.getFactures();
        return Response.status(200).entity(outMsg).build();
    }

    @GET
    @Path("/freq_Prod")
    public Responde getFrequentPruducts()
    {
        String outMsg = SparkConnector.getFrequentProducts();
        return Responce.status(200).entity(outMsg).build();
    }

    @POST
    @Path("/factures")
    public Response addFacture(@QueryParam("fid") int fid, @QueryParam("pid") int pid, @QueryParam("qte") int qte)
    {
        String outMsg = CassandraConnector.addFacture(fid,pid,qte);
        return Response.status(200).entity(outMsg).build();

    }

}
