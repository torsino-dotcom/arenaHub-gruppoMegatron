SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS partite;
DROP TABLE IF EXISTS utenti_tornei;
DROP TABLE IF EXISTS tornei;
DROP TABLE IF EXISTS giochi;
DROP TABLE IF EXISTS utenti;
DROP TABLE IF EXISTS impostazioni_sito;

CREATE TABLE giochi (
  id_giochi bigint NOT NULL AUTO_INCREMENT,
  nome_giochi varchar(255) DEFAULT NULL,
  url_img varchar(255) DEFAULT NULL,
  PRIMARY KEY (id_giochi),
  UNIQUE KEY nome_giochi_UNIQUE (nome_giochi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE utenti (
  id_utenti bigint NOT NULL AUTO_INCREMENT,
  nome varchar(255) DEFAULT NULL,
  cognome varchar(255) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  ruolo varchar(255) DEFAULT NULL,
  PRIMARY KEY (id_utenti),
  UNIQUE KEY email_UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE impostazioni_sito (
  id_impostazioni bigint NOT NULL AUTO_INCREMENT,
  max_tornei_attivi_utente int DEFAULT NULL,
  aggiornato_il datetime DEFAULT NULL,
  PRIMARY KEY (id_impostazioni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tornei (
  id_tornei bigint NOT NULL AUTO_INCREMENT,
  nome_torneo varchar(45) DEFAULT NULL,
  descrizione varchar(1000) DEFAULT NULL,
  id_giochi bigint DEFAULT NULL,
  data_ora datetime DEFAULT NULL,
  data_inizio_iscrizioni date DEFAULT NULL,
  data_fine_iscrizioni date DEFAULT NULL,
  stato varchar(255) DEFAULT NULL,
  posti_max int DEFAULT NULL,
  url_img varchar(255) DEFAULT NULL,
  nome varchar(255) DEFAULT NULL,
  PRIMARY KEY (id_tornei),
  KEY FK7ds4cai1f90rud2sk6oc0qowu (id_giochi),
  CONSTRAINT FK7ds4cai1f90rud2sk6oc0qowu FOREIGN KEY (id_giochi) REFERENCES giochi (id_giochi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE utenti_tornei (
  id_utenti_tornei bigint NOT NULL AUTO_INCREMENT,
  id_utenti bigint DEFAULT NULL,
  id_tornei bigint DEFAULT NULL,
  data_iscrizione date DEFAULT NULL,
  stato varchar(255) DEFAULT NULL,
  PRIMARY KEY (id_utenti_tornei),
  KEY FK6ow4vq4u9uk8vt1xpkwr2md37 (id_tornei),
  KEY FKhdd8u2ufq5p7gfngotp7rylc3 (id_utenti),
  CONSTRAINT FK6ow4vq4u9uk8vt1xpkwr2md37 FOREIGN KEY (id_tornei) REFERENCES tornei (id_tornei),
  CONSTRAINT FKhdd8u2ufq5p7gfngotp7rylc3 FOREIGN KEY (id_utenti) REFERENCES utenti (id_utenti)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE partite (
  id_partite bigint NOT NULL AUTO_INCREMENT,
  id_tornei bigint DEFAULT NULL,
  id_utenti1 bigint DEFAULT NULL,
  id_utenti2 bigint DEFAULT NULL,
  orario_inizio datetime DEFAULT NULL,
  punteggio_1 int DEFAULT NULL,
  punteggio_2 int DEFAULT NULL,
  id_persona_vincitrice bigint DEFAULT NULL,
  formato_match varchar(255) DEFAULT NULL,
  stato varchar(255) DEFAULT NULL,
  fase varchar(255) DEFAULT NULL,
  PRIMARY KEY (id_partite),
  KEY FKg5f1axrtht8c17lkt4v6scjuy (id_utenti1),
  KEY FKtrsvufdrp9951ghgv3lpayja5 (id_utenti2),
  KEY FK601t7an55ulds1wy61hhvi0xu (id_tornei),
  CONSTRAINT FK601t7an55ulds1wy61hhvi0xu FOREIGN KEY (id_tornei) REFERENCES tornei (id_tornei),
  CONSTRAINT FKg5f1axrtht8c17lkt4v6scjuy FOREIGN KEY (id_utenti1) REFERENCES utenti (id_utenti),
  CONSTRAINT FKtrsvufdrp9951ghgv3lpayja5 FOREIGN KEY (id_utenti2) REFERENCES utenti (id_utenti)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;