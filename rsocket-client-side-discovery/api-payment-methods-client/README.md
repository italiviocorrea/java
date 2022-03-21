# API de meios de pagamentos

API REST que permite gerenciar e consultar os meios de pagamentos disponíveis.
Esta API é assíncrona, é utilizado : spring boot + webflux.
O objetivo principal desta API é demonstrar como acessar outra API implementada com o 
protocolo RSOCKET, com balanceamento de carga round robin e descoberta de serviço do lado cliente.

A implementação do RSOCKET no spring boot, oferece um mecanismo de balanceamento de carga round robin,
mas não tem nenhuma implementação de descoberta de serviço dinâmica.
O objetivo é mostrar um exemplo de Descoberta de Serviço do Lado Cliente, baseado no DNS, e aproveitando
os recursos de implantação de serviços, oferecidos pelos orquestradores de container como o kubernetes
e o docker swarm. Portanto, o exemplo funcionará nestes dois ambientes.

Neste caso a API Rsocket, poderá ser replicada automáticamente e o cliente deverá conseguir descobrir
os endpoints de cada réplica (IP + porta), através do nome do serviço no ambiente kubernetes ou
docker swarm.

Para isso no kubernetes deveremos implantar o serviço como sendo do tipo headless, o que faz com que o
kubernetes não atribua um IP para este serviço, mas quando acessado ele retorna a lista de endpoints
(IP + porta) de cada POD ligado ao serviço. Veja o arquivo deployment.yaml da API Rsocket.

Já no docker swarm devemos modificar o endpoint_mode, cujo default é vip, para dnsrr (DNS round robin),
ou seja, não gera um IP para o serviço, mas quando acessado ele retorna a lista de endpoints (IP + porta)
de cada réplica do serviço. Veja o arquivo docker-compose.yaml da API Rsocket.


## Recursos disponíveis

**- findAll:**  Retorna todos os meios de pagamentos cadastrados. Os meios de pagamentos serão retornados como fluxo.

**- findById:** Retorna os dados do meio de pagamento, cujo o ID foi informado na consulta. 

