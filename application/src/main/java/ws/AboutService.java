package ws;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import model.About;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("about")
@Api("about-service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AboutService extends AbstractService {

    @GET
    @ApiOperation(value = "A sample REST endpoint", notes = "GET operation of a sample REST endpoint")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful operation")})
    public About about() {
        return new About(CONFIG.getString("app.version"), CONFIG.getString("app.name"));
    }

}
