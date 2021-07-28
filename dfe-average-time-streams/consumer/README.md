# Projeto consumer

O objetivo deste projeto é obter as mensagens enviadas através dos tópicos : nf3e-evt-aut, nf3e-evt-rej e nf3e-ws-service, aplicar um pipeline de transformação nestes dados e armazenar no tópico nf3e-evt-tempo-medio, o resultado do processamento deste pipeline.
O resultado final deverá ser o tempo médio de processamento por serviço nos últimos 5 minutos. Além da média será calculado :
- quantidade de mensagens processadas nos últimos 5 minutos
- o maior tempo médio nos últimos 5 minutos
- o menor tempo médio nos últimos 5 minutos
- o somatório de tempo médio nos últimos 5 minutos
