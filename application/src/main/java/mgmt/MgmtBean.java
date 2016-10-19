package mgmt;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import util.BaseObject;

@Singleton
@Startup
public class MgmtBean extends BaseObject {

    private static final String VCS_SERVICE_DISCOVERY = "service_discovery";
    private static final String VCS_SERVICE_DISCOVERY_AUTH_TOKEN = "auth_token";
    private static final String VCS_SERVICE_DISCOVERY_CREDENTIALS = "credentials";
    private static final String VCS_SERVICE_DISCOVERY_URL = "url";

    private Optional<Pair<String, String>> serviceDisoveryCredentials;

    @PostConstruct
    private void startup() {
        this.serviceDisoveryCredentials = getServiceDiscoveryCredentials();
    }

    public Optional<Pair<String, String>> getServiceDisoveryCredentials() {
        return serviceDisoveryCredentials;
    }

    private Optional<Pair<String, String>> getServiceDiscoveryCredentials() {
        ObjectMapper m = new ObjectMapper();
        JsonNode vcapServices = null;

        try {
            vcapServices = m.readTree(CONFIG.getString("vcap.services"));
        } catch (IOException e) {
            LOG.warn("Unable to parse VCAP_SERVICES from configuration variable `vcap.services`.");
        }

        if (vcapServices != null && vcapServices.get(VCS_SERVICE_DISCOVERY) != null) {
            try {
                Iterator<JsonNode> elements = vcapServices.get(VCS_SERVICE_DISCOVERY).elements();

                while (elements.hasNext()) {
                    JsonNode serviceDiscovery = elements.next();
                    JsonNode credentials = serviceDiscovery.get(VCS_SERVICE_DISCOVERY_CREDENTIALS);
                    String authToken = credentials.get(VCS_SERVICE_DISCOVERY_AUTH_TOKEN).asText();
                    String url = credentials.get(VCS_SERVICE_DISCOVERY_URL).asText();
                    return Optional.of(Pair.of(authToken, url));
                }
            } catch (Exception e) {
                LOG.warn("Unable to register at service discovery", e.getMessage());
            }
        } else {
            LOG.warn("No VCAP_SERVICES defined in variable `vcap.services`.");
        }

        return Optional.empty();
    }

}
