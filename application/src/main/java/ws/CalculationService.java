package ws;

import io.swagger.annotations.Api;
import model.CalculationRequest;
import model.CalculationResult;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("calculation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api("calculation-service for adding numbers")
public class CalculationService extends AbstractService {

    private final String ADD = "add";

    @GET
    @Path(ADD)
    public CalculationResult add(@QueryParam("n1") int n1, @QueryParam("n2") int n2) {
        return new CalculationResult(ADD, n1 + n2);
    }

    @POST
    @Path(ADD)
    public CalculationResult add(CalculationRequest request) {
        return new CalculationResult(ADD, request.n1 + request.n2);
    }

    @GET
    @Path("example")
    public CalculationRequest example() {
        return new CalculationRequest(42, 13);
    }

}