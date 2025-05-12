-- Création de la table photo pour gérer plusieurs photos par signalement
CREATE TABLE photo (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    chemin VARCHAR(255) NOT NULL,
    signalement_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (signalement_id) REFERENCES signalement(id)
);