-- Cria o banco de dados
CREATE DATABASE lcr_db;
GO
USE lcr_db;

create table autoridade_certificadora
(
    idItc int identity
        primary key,
    alias varchar(255) not null,
    nome  varchar(100) not null
)
go

create table lista_certificado_revogado
(
    idLcr                       int identity
        primary key,
    data_primeira_disponib_lcr  datetime,
    data_proxima_atualz_lcr     datetime,
    data_ultima_atualz_lcr      datetime,
    indi_atualz_lcr             varchar not null,
    indi_lcr_delta              int     not null,
    info_url_lcr                varchar(200),
    qtde_horas_entre_atualz_lcr int
)
go

create unique index UQ_info_url_lcr on lista_certificado_revogado (info_url_lcr) include (idLcr)
go

grant select on lista_certificado_revogado to sa
go

-- auto-generated definition
create table certificado_revogado
(
    idLcr        int          not null
        constraint FK_CRR_PERTENCE_LCR
            references lista_certificado_revogado
            on delete cascade,
    idCrr        int identity
        primary key,
    numero_serie varchar(100) not null
)
go

create index IX_numero_serie on certificado_revogado (numero_serie) include (idLcr)
go

create index IX_idLcr
    on certificado_revogado (idLcr)
go

create unique index IX_serie_idLcr on certificado_revogado (idLcr, numero_serie)
go

create index IX_serie on certificado_revogado (numero_serie)
go

grant select on certificado_revogado to sa
go

create table ac_possui_lcr
(
    idItc int not null
        constraint FK_ACL_POSSUI_ATC
            references autoridade_certificadora
            on delete cascade,
    idLcr int not null
        constraint FK_ACL_POSSUI_LCR
            references lista_certificado_revogado
            on delete cascade,
    primary key (idLcr, idItc)
)
go

create index IX_idItc on ac_possui_lcr (idItc)
go

insert into autoridade_certificadora(alias, nome) values ('ac_soluti_v5','AC SOLUTI Multipla v5')
go
