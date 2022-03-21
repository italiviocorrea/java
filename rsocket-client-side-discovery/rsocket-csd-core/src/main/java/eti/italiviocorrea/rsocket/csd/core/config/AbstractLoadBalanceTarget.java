package eti.italiviocorrea.rsocket.csd.core.config;


import eti.italiviocorrea.rsocket.csd.core.models.EndpointInstance;
import eti.italiviocorrea.rsocket.csd.core.services.ServiceRegistry;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author icorrea
 */
public abstract class AbstractLoadBalanceTarget {

    /**
     * Get the list of available endpoints for the service.
     * In Kubernetes, the service must be Headless
     * and in Docker swarm the service deploy has to be: endpoint_mode: dnsrr
     * @param serviceRegistry
     * @return
     */
    protected List<EndpointInstance> getEndpoints(ServiceRegistry serviceRegistry) {
        if (serviceRegistry.getDiscoveryMethod() == 1) {
            List<EndpointInstance> hosts = new ArrayList<>();
            try {
                InetAddress[] addresses = InetAddress.getAllByName(serviceRegistry.getServices().get(0).getHost());
                for (InetAddress address : addresses) {
                    EndpointInstance rsinst = new EndpointInstance();
                    rsinst.setHost(address.getHostAddress());
                    rsinst.setPort(serviceRegistry.getServices().get(0).getPort());
                    hosts.add(rsinst);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            return hosts;
        } else {
            return serviceRegistry.getServices();
        }
    }


}
