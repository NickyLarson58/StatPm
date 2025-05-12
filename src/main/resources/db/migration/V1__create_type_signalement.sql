-- Création de la table type_signalement
CREATE TABLE type_signalement (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ajout des types de signalement par défaut
INSERT INTO type_signalement (libelle, description) VALUES
('Dêpots', 'Constation de dépots sauvages'),
('Affichages', 'Problèmes d’affichages sauvages'),
('Tags', 'Constations de tags'),
('Autre', 'Autres types de signalements');

-- Ajout de la colonne type_id dans la table signalement
ALTER TABLE signalement
ADD COLUMN type_id INTEGER,
ADD CONSTRAINT fk_signalement_type
FOREIGN KEY (type_id) REFERENCES type_signalement(id);

-- Ajout de la colonne adresse dans la table signalement
ALTER TABLE signalement ADD COLUMN adresse VARCHAR(255);