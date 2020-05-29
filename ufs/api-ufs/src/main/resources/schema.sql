CREATE TABLE ufs (
  id INT PRIMARY KEY IDENTITY (1, 1),
  codigo SMALLINT NOT NULL,
  nome NVARCHAR(64) NOT NULL,
  sigla NVARCHAR(2) NOT NULL,
  inicioVigencia DATE NOT NULL,
  fimVigencia DATE,
  CONSTRAINT unique_codigo UNIQUE(codigo)
);
