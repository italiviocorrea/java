package eti.italiviocorrea.rsocket.csd.core.services;

import eti.italiviocorrea.rsocket.csd.core.models.EndpointInstance;

import java.util.List;

public abstract class ServiceRegistry {

    private int discoveryMethod;
    private List<EndpointInstance> services;

    public void setServices(List<EndpointInstance> services) {
        this.services = services;
    }
    public List<EndpointInstance> getServices() {
        return this.services;
    }

    public int getDiscoveryMethod() { return discoveryMethod;   }
    public void setDiscoveryMethod(int discoveryMethod) { this.discoveryMethod = discoveryMethod;  }

}
