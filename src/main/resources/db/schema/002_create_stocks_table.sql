--liquibase formatted sql

--changeset scholanova:1
CREATE TABLE IF NOT EXISTS STOCKS (
  ID                  SERIAL          NOT NULL,
  ID_STORE			INTEGER NOT NULL,
  NAME                VARCHAR(255)    NOT NULL,
  TYPE                VARCHAR(255)    NOT NULL,
  VALUE               INTEGER   NOT NULL,

  FOREIGN KEY (ID_STORE) REFERENCES STORES(ID),
  PRIMARY KEY (ID)
);
