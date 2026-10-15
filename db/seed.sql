SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO giochi (id_giochi, nome_giochi, url_img) VALUES 
(1,'Valorant','img/valorant.jpg'),
(2,'Rocket League','img/rocket.jpg'),
(3,'Street Fighter 6','img/sf6.jpg'),
(4,'EA SPORTS FC 26','img/fc26.jpg'),
(5,'Apex Legends','img/apex.jpg');

INSERT INTO utenti (id_utenti, nome, cognome, username, email, password, ruolo) VALUES 
(1,'Paul','Megatron','mega_paul','megatron@email.com','megatron','utente'),
(2,'Jeson','Rullo','rul_jes','json@email.com','rullo','utente'),
(3,'Admin','Demo','admin','admin@arenahub.test','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','admin'),
(4,'Alice','Rossi','alice_rossi','alice.rossi@arenahub.test','test1234','utente'),
(5,'Marco','Bianchi','marco_bianchi','marco.bianchi@arenahub.test','test1234','utente'),
(6,'Giulia','Conti','giulia_conti','giulia.conti@arenahub.test','test1234','utente'),
(7,'Luca','Ferri','luca_ferri','luca.ferri@arenahub.test','test1234','utente'),
(8,'Sara','Greco','sara_greco','sara.greco@arenahub.test','test1234','utente'),
(9,'Davide','Neri','davide_neri','davide.neri@arenahub.test','test1234','utente'),
(10,'Pippo','Basso','pippos','pippo@gmail.com','$2a$10$4pGWh107P1nMYrht68QHjeeecd0a0LXKm7uFqcUejIdiU2eeb03/W','admin'),
(11,'Nicol','Bolas','nicolBolas','nicolbolas@gmail.com','$2a$10$ghOV7jHOe5/d6N82Ms8m4eh6x4X6Snj8V.AScGIaCdnCg3OStGlvm','utente');

INSERT INTO impostazioni_sito (id_impostazioni, max_tornei_attivi_utente, aggiornato_il) VALUES 
(1,3,'2026-10-14 09:00:00');

INSERT INTO tornei (id_tornei, nome_torneo, descrizione, id_giochi, data_ora, data_inizio_iscrizioni, data_fine_iscrizioni, stato, posti_max, url_img, nome) VALUES 
(1,'Arena Cup Valorant','Torneo Valorant 5v5',1,'2026-11-07 15:00:00','2026-10-01','2026-10-30','aperto',8,'valorant.jpg',NULL),
(2,'Rocket League Showdown','Coppa a eliminazione diretta',2,'2026-10-14 18:00:00','2026-09-15','2026-10-12','in_corso',8,'rocket.jpg',NULL),
(3,'Street Fighter Cup','Finale Street Fighter 6',3,'2026-09-20 16:00:00','2026-08-01','2026-09-10','concluso',16,'sf6.jpg',NULL),
(4,'FC Weekend Cup','Torneo EA SPORTS FC 26',4,'2026-10-25 17:00:00','2026-09-20','2026-10-10','chiuso',16,'fc26.jpg',NULL),
(5,'Apex Legends Draft','Torneo Apex Legends',5,'2026-12-06 15:00:00','2026-11-01','2026-11-30','bozza',20,'apex.jpg',NULL);

INSERT INTO utenti_tornei (id_utenti_tornei, id_utenti, id_tornei, data_iscrizione, stato) VALUES 
(1,1,1,'2026-10-02','confermato'),
(2,2,1,'2026-10-03','confermato'),
(3,4,1,'2026-10-04','confermato'),
(4,5,1,'2026-10-05','confermato'),
(5,6,1,'2026-10-06','confermato'),
(6,7,1,'2026-10-07','in_attesa'),
(7,8,1,'2026-10-08','annullato'),
(8,1,2,'2026-09-16','confermato'),
(9,4,2,'2026-09-17','confermato'),
(10,5,2,'2026-09-18','confermato'),
(11,6,2,'2026-09-19','confermato'),
(12,9,3,'2026-08-02','confermato'),
(13,8,3,'2026-08-03','confermato'),
(14,1,4,'2026-09-21','confermato'),
(15,2,4,'2026-09-22','confermato'),
(16,7,4,'2026-09-23','in_attesa'),
(17,11,1,'2026-10-14','confermato');

INSERT INTO partite (id_partite, id_tornei, id_utenti1, id_utenti2, orario_inizio, punteggio_1, punteggio_2, id_persona_vincitrice, formato_match, stato, fase) VALUES 
(91001,2,1,4,'2026-10-14 17:00:00',1,2,4,'BO3','conclusa','Semifinale'),
(91002,2,5,6,'2026-10-14 17:30:00',0,2,6,'BO3','conclusa','Semifinale'),
(91003,2,4,6,'2026-10-14 18:00:00',1,0,NULL,'BO5','in_corso','Finale'),
(91005,4,1,2,'2026-10-25 18:00:00',NULL,NULL,NULL,'BO3','programmata','Quarti di finale'),
(91006,3,9,8,'2026-10-23 21:34:00',0,0,NULL,'bo2','programmata','seminifinale');

SET FOREIGN_KEY_CHECKS = 1;