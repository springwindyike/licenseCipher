CREATE DATABASE IF NOT EXISTS license DEFAULT CHARACTER SET utf8mb4;
SET SESSION sql_mode='STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION';
flush privileges;

use license;

CREATE TABLE IF NOT EXISTS ucVersion (
  name varchar(64)  NOT NULL,
  version   int    NOT NULL,
  PRIMARY KEY (name)
) DEFAULT CHARSET=utf8mb4;

insert into ucVersion(name,version)values('license',-1)