package ch.inacta.tezos.kiss.gateway.core.remote;

import ch.inacta.tezos.kiss.gateway.api.v1.NodeResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * Support service to create rest clients with custom headers
 *
 * @author inacta AG
 * @since 1.0.0
 */
@ApplicationScoped
public class RemoteServiceBuilder {

    /**
     * Retruns blockchain rest client
     *
     * @return cliet
     */
    public NodeResource nodeEnpoint() {

        Map<String, String> headers = new HashMap<>();
        return buildClient(NodeResource.class, "http://localhost:7000", headers);
    }

    private <T> T buildClient(final Class<T> resourceEndpoint, String hostWithPort, Map<String, String> headers) {

        final ResteasyClient client = (ResteasyClient) ClientBuilder.newBuilder().build();

        final ResteasyWebTarget target = client.target(UriBuilder.fromPath(hostWithPort));

        target.register((ClientRequestFilter) requestContext -> headers.forEach((key, value) -> requestContext.getHeaders().putSingle(key, value)));
        return target.proxy(resourceEndpoint);
    }
}
