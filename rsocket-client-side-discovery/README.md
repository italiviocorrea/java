# Getting Started

In modern cloud-based microservices applications, communication between microservices has some challenges, one of which is service discovery.
In this type of application, the network locations of service instances are dynamically defined, and changes are also made dynamically due to autoscaling, or failures and updates.
As a result, the client of these services needs a more elaborate service discovery mechanism.
There are two service discovery patterns: client-side and server-side.
The idea in this example is to implement client-side service discovery for API's that use the rsocket protocol, for that we are going to implement a REST API as a client of an rsocket API.
For this purpose we will use spring boot, the two APIs should work in a CaaS environment based on kubernetes and Docker swarm.
The idea is that the Rest API client can discover at runtime and dynamically the instances of the Rsocket API that are healthy and ready to use. If there is a scaling of the number of Rsocket API instances, the Rest API client must update its list of rsockets endpoint instances.
There are several ways to implement this, but here I'm going to use the DNS-based, dynamic discovery approach offered by the orchestration environments: Kubernetes and Docker Swarm.

## modules:

**- rsocket-csd-core:** This module has the implementation of the client-side discovery method, and other utility classes necessary for the process.

**- api-payment-methods-client:** Rest API with webflux that will play the role of rsocket client, where Client Side Service Discovery will be implemented.

**- api-payment-methods-server:** Rsocket API which the client will access via the client-side service discovery method.

## Architecture

![Processo principal](./docs/rsocket_discovery_client_side.drawio.png)
