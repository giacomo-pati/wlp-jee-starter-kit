package calls;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import util.BaseObject;
import util.Configuration;
import util.Logger;

public class AbstractServiceDiscoveryCommand<REQUEST extends BaseObject, RESPONSE> extends HystrixCommand<RESPONSE> {

    private final Class<RESPONSE> clazz;

    public final REQUEST request;

    public final String callUri;

    public final Optional<Pair<String, String>> serviceDiscoveryCredentials;

    public final String serviceName;

    public AbstractServiceDiscoveryCommand(String serviceName,
                                           Optional<Pair<String, String>> serviceDiscoveryCredentials, String callUri, REQUEST request,
                                           Class<RESPONSE> clazz) {
        super(Setter //
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")) //
                .andCommandPropertiesDefaults( //
                        HystrixCommandProperties.Setter() //
                                .withExecutionTimeoutEnabled(false)));

        this.callUri = callUri;
        this.clazz = clazz;
        this.request = request;
        this.serviceDiscoveryCredentials = serviceDiscoveryCredentials;
        this.serviceName = serviceName;
    }

    @Override
    protected RESPONSE run() throws Exception {
        return unmarshalResponse(callService(getServiceUrl()));
    }

    private RESPONSE unmarshalResponse(String marshalled) throws Exception {
        ObjectMapper m = new ObjectMapper();
        RESPONSE result = m.readerFor(this.clazz).readValue(marshalled);
        Logger.DEFAULT.debug("Result from Servcie", result);
        return result;
    }

    private String callService(String serviceUrl) throws Exception {
        HttpResponse<String> response = Unirest //
                .post("http://" + serviceUrl + "/" + serviceName + callUri) //
                .header("Content-Type", "application/json") //
                .header("Accept", "application/json") //
                .body(request.toString()) //
                .asString();

        if (response.getStatus() == 200) {
            return response.getBody();
        } else {
            String message = "Did not receive response from upstream system.";
            Exception ex = new Exception(message);
            Logger.DEFAULT.warn(message);
            throw ex;
        }
    }

    private String getServiceUrl() throws Exception {
        String defaultHost = Configuration.CONFIG.getString(serviceName + ".defaultHost");

        if (this.serviceDiscoveryCredentials.isPresent()) {
            HttpResponse<JsonNode> response = Unirest //
                    .get(this.serviceDiscoveryCredentials.get().getRight() + "/api/v1/instances") //
                    .header("Content-Type", "application/json") //
                    .header("Accept", "application/json") //
                    .header("Authorization", "Bearer " + this.serviceDiscoveryCredentials.get().getLeft()) //
                    .routeParam("service_name", serviceName) //
                    .asJson();

            if (response.getStatus() == 200) {
                return response //
                        .getBody() //
                        .getObject() //
                        .getJSONArray("instances") //
                        .getJSONObject(0) //
                        .getJSONObject("endpoint") //
                        .getString("value");
            } else {
                Logger.DEFAULT.warn(
                        "Did not receive response from Service Discovery, using default host `" + defaultHost + "`");
                return defaultHost;
            }
        } else {
            Logger.DEFAULT.warn("Do not have Service Discovery credentials, using default host `" + defaultHost + "`");
            return defaultHost;
        }
    }

}
