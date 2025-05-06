package src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SQL.ConnexionSql;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@Service
public class StatService {
    
    @Autowired(required = false)
    private ConnexionSql connexionSql;
    
    private boolean isDatabaseAvailable() {
        return connexionSql != null;
    }
    
    public List<String> getBrigades() throws Exception {
        if (!isDatabaseAvailable()) {
            // Retourner des données de test si la base de données n'est pas disponible
            List<String> testData = new ArrayList<>();
            testData.add("Brigade A");
            testData.add("Brigade B");
            testData.add("Brigade C");
            return testData;
        }
        Vector brigades = connexionSql.RecupererDonnee("brigades", "nomBrigade", "*");
        List<String> result = new ArrayList<>(brigades);
        Collections.sort(result); // Tri alphabétique
        return result;
    }
    
    public List<String> getInterventions() throws Exception {
        if (!isDatabaseAvailable()) {
            // Retourner des données de test si la base de données n'est pas disponible
            List<String> testData = new ArrayList<>();
            testData.add("Contrôle routier");
            testData.add("Surveillance");
            testData.add("Intervention");
            return testData;
        }
        Vector interventions = connexionSql.RecupererDonnee("interventions", "nom_interventions", "*");
        List<String> result = new ArrayList<>(interventions);
        Collections.sort(result); // Tri alphabétique
        return result;
    }
    
    public List<String> getAgents() throws Exception {
        List<String> agents = new ArrayList<>();
        if (!isDatabaseAvailable()) {
            return agents;
        }
        
        Vector matricules = connexionSql.RecupererDonnee("agents", "matricule", "*");
        Vector noms = connexionSql.RecupererDonnee("agents", "nom_agent", "*");
        Vector prenoms = connexionSql.RecupererDonnee("agents", "prenom_agent", "*");
        
        for (int i = 0; i < matricules.size(); i++) {
            String matricule = matricules.get(i).toString();
            String nom = noms.get(i).toString();
            String prenom = prenoms.get(i).toString();
            agents.add(nom + "  " + prenom + " - " + matricule);
        }
        Collections.sort(agents); // Tri alphabétique
        return agents;
    }
    
    public List<String> getTypesInterventions() throws Exception {
        List<String> interventions = new ArrayList<>();
        if (!isDatabaseAvailable()) {
            return interventions;
        }
        
        Vector result = connexionSql.RecupererDonnee("interventions", "nom_interventions", "*");
        for (Object intervention : result) {
            interventions.add(intervention.toString());
        }
        Collections.sort(interventions); // Tri alphabétique
        return interventions;
    }
    
    public void creerStat(String nomBrigade, String intervention, String adresse, String date) throws Exception {
        if (!isDatabaseAvailable()) {
            // Simuler la création d'une stat
            System.out.println("Mode test : Création d'une stat pour la brigade " + nomBrigade);
            return;
        }
        String values = "'" + nomBrigade + "','" + intervention + "',1,'" + adresse + "','" + java.time.LocalDate.now().toString() + "'";
        connexionSql.executerRequeteStatBrigade(values);
    }
    
    public void creerStatAgent(String matricule, String nom, String intervention, String adresse, String date) throws Exception {
        if (!isDatabaseAvailable()) {
            // Simuler la création d'une stat agent
            System.out.println("Mode test : Création d'une stat pour l'agent " + nom);
            return;
        }
        String values = "'" + matricule + "','" + nom + "','" + intervention + "',1,'" + java.time.LocalDate.now().toString() + "','" + adresse + "'";
        connexionSql.ajouterStatAgent(values);
    }
    
    public List<Map<String, String>> getStatsBrigade() throws Exception {
        List<Map<String, String>> stats = new ArrayList<>();
        
        if (!isDatabaseAvailable()) {
            // Retourner des données de test
            Map<String, String> stat1 = new HashMap<>();
            stat1.put("brigade", "Brigade A");
            stat1.put("intervention", "Contrôle routier");
            stat1.put("total", "5");
            stat1.put("adresse", "Centre ville");
            stat1.put("date", "2025-02-18");
            stats.add(stat1);
            
            Map<String, String> stat2 = new HashMap<>();
            stat2.put("brigade", "Brigade B");
            stat2.put("intervention", "Surveillance");
            stat2.put("total", "3");
            stat2.put("adresse", "Zone portuaire");
            stat2.put("date", "2025-02-18");
            stats.add(stat2);
            
            return stats;
        }
        
        Vector result = connexionSql.RecupererDonnee("statsbrigades", "*", "*");
        for (int i = 0; i < result.size(); i += 5) {
            Map<String, String> stat = new HashMap<>();
            stat.put("brigade", (String) result.get(i));
            stat.put("intervention", (String) result.get(i + 1));
            stat.put("total", String.valueOf(result.get(i + 2)));
            stat.put("adresse", (String) result.get(i + 3));
            stat.put("date", (String) result.get(i + 4));
            stats.add(stat);
        }
        return stats;
    }
    
    public List<Map<String, String>> getStatsAgent() throws Exception {
        List<Map<String, String>> stats = new ArrayList<>();
        
        if (!isDatabaseAvailable()) {
            // Retourner des données de test
            Map<String, String> stat1 = new HashMap<>();
            stat1.put("matricule", "A123");
            stat1.put("nom", "Jean Dupont");
            stat1.put("intervention", "Contrôle routier");
            stat1.put("total", "2");
            stat1.put("date", "2025-02-18");
            stat1.put("adresse", "Centre ville");
            stats.add(stat1);
            
            Map<String, String> stat2 = new HashMap<>();
            stat2.put("matricule", "B456");
            stat2.put("nom", "Marie Martin");
            stat2.put("intervention", "Surveillance");
            stat2.put("total", "1");
            stat2.put("date", "2025-02-18");
            stat2.put("adresse", "Zone portuaire");
            stats.add(stat2);
            
            return stats;
        }
        
        Vector result = connexionSql.RecupererDonnee("statsagents", "*", "*");
        for (int i = 0; i < result.size(); i += 6) {
            Map<String, String> stat = new HashMap<>();
            stat.put("matricule", (String) result.get(i));
            stat.put("nom", (String) result.get(i + 1));
            stat.put("intervention", (String) result.get(i + 2));
            stat.put("total", String.valueOf(result.get(i + 3)));
            stat.put("date", (String) result.get(i + 4));
            stat.put("adresse", (String) result.get(i + 5));
            stats.add(stat);
        }
        return stats;
    }
    
    public List<Map<String, String>> searchStats(String type, String critere, String valeur) throws Exception {
        if (!isDatabaseAvailable()) {
            // En mode test, retourner les mêmes données que getStatsBrigade ou getStatsAgent
            return type.equals("brigade") ? getStatsBrigade() : getStatsAgent();
        }
        
        String table = type.equals("brigade") ? "statsbrigades" : "statsagents";
        Vector result = connexionSql.rechercheInfrac(critere, table, valeur);
        
        List<Map<String, String>> stats = new ArrayList<>();
        for (Object item : result) {
            Map<String, String> stat = new HashMap<>();
            stat.put(critere, (String) item);
            stats.add(stat);
        }
        return stats;
    }
    
    public Map<String, Object> creerIntervention(String nomBrigade, String typeIntervention, String lieu, int nombre) throws Exception {
        if (!isDatabaseAvailable()) {
            throw new Exception("Base de données non disponible");
        }
        
        // Créer l'intervention
        String values = "'" + nomBrigade + "','" + typeIntervention + "'," + nombre + ",'" + lieu + "','" + 
                       java.time.LocalDate.now().toString() + "'";
        connexionSql.executerRequeteStatBrigade(values);
        
        // Retourner les données de l'intervention créée
        Map<String, Object> intervention = new HashMap<>();
        intervention.put("type", typeIntervention);
        intervention.put("nombre", nombre);
        intervention.put("lieu", lieu);
        return intervention;
    }
    
    public Map<String, Object> modifierIntervention(String id, String typeIntervention, String lieu, int nombre) throws Exception {
        if (!isDatabaseAvailable()) {
            throw new Exception("Base de données non disponible");
        }
        
        String values = "type_intervention = '" + typeIntervention + "', " +
                       "nombre = " + nombre + ", " +
                       "lieu = '" + lieu + "'";
        connexionSql.ExecuterRequeteSimple("statsbrigades", "id", values);
        
        Map<String, Object> intervention = new HashMap<>();
        intervention.put("id", id);
        intervention.put("type", typeIntervention);
        intervention.put("nombre", nombre);
        intervention.put("lieu", lieu);
        return intervention;
    }
    
    public void supprimerIntervention(String id) throws Exception {
        if (!isDatabaseAvailable()) {
            throw new Exception("Base de données non disponible");
        }
        
        connexionSql.Supprimer("statsbrigades", "id = '" + id + "'");
    }
    
    public List<Map<String, Object>> getInterventions(String nomBrigade) throws Exception {
        List<Map<String, Object>> interventions = new ArrayList<>();
        
        if (!isDatabaseAvailable()) {
            return interventions;
        }
        
        Vector result = connexionSql.RecupererDonnee("statsbrigades", "*", nomBrigade);
        
        for (int i = 0; i < result.size(); i += 5) {
            Map<String, Object> intervention = new HashMap<>();
            intervention.put("id", result.get(i));
            intervention.put("type", result.get(i + 1));
            intervention.put("nombre", Integer.parseInt(result.get(i + 2).toString()));
            intervention.put("lieu", result.get(i + 3));
            intervention.put("date", result.get(i + 4));
            interventions.add(intervention);
        }
        
        return interventions;
    }
}
