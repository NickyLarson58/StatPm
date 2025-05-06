package SQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import src.model.Adresses;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

@Component
public class ConnexionSql {

    private  JdbcTemplate jdbcTemplate;
    private  DataSource dataSource;
    private Connection connexion;



    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.driver-class-name}")
    private String classname;

    Calendar c = Calendar.getInstance();
    int annee = c.get(Calendar.YEAR);
    LocalDate dteNow = LocalDate.now();
    public Object conn;

    @Autowired
    public DataSource ConnexionSql(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("Tentative de connexiont à la Base StatWeb");
        // Initialiser la connexion ici en utilisant les propriétés ip, port, utilisateur, password
        try {
            // Charger le driver JDBC (à adapter selon votre base de données)
            Class.forName(classname);  // Exemple pour MySQL
            
            // Établir la connexion
            connexion = DriverManager.getConnection(url, username, password);

            System.out.println("Connexion réussie !!!");

        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
        return  dataSource;
    }

    public Vector rechercheInfrac(String critere1, String table, String critere2) throws SQLException {
        Vector result = new Vector();
        String query = "SELECT " + critere1 + " FROM " + table + " WHERE " + critere1 + " LIKE '%" + critere2 + "%'";
        List<String> rows = jdbcTemplate.queryForList(query, String.class);
        result.addAll(rows);
        return result;
    }

    public void ajouterStatAgent(String values) throws Exception {
        String query = "INSERT INTO statagent (matricule, nom_agent, libelle, total, date, adresse) VALUES (" + values + ")";
        jdbcTemplate.execute(query);
    }

    public void executerRequeteStatBrigade(String values) throws Exception {
        String query = "INSERT INTO statbrigade (nomBrigade, libelle, total, adresse, date) VALUES (" + values + ")";
        jdbcTemplate.execute(query);
    }

    public void ExecuterRequeteSimple(String table, String numId, String values) throws Exception {
        String query = "INSERT INTO " + table + " VALUES (" + numId + ", " + values + ")";
        jdbcTemplate.execute(query);
    }

    public void Supprimer(String table, String values) throws Exception {
        String query = "DELETE FROM " + table + " WHERE numAttestation = " + values;
        jdbcTemplate.execute(query);
    }

    public Vector RecupererDonnee(String table, String critere, String critere2) throws Exception {
        Vector result = new Vector();
        String query = critere2.equals("*")
                ? "SELECT " + critere + " FROM " + table
                : "SELECT " + critere + " FROM " + table + " WHERE " + critere + " = '" + critere2 + "'";
        List<String> rows = jdbcTemplate.queryForList(query, String.class);
        result.addAll(rows);
        return result;
    }

    public String RecupererString(String colonne, String table, String conditionColonne, String conditionValeur) throws SQLException {
        String query = "SELECT " + colonne + " FROM " + table + " WHERE " + conditionColonne + " = '" + conditionValeur + "'";
        try (Connection connection = dataSource.getConnection();
             java.sql.Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getString(colonne);
            } else {
                return null;
            }
        }
    }


    public List<Adresses> RecupererAdresses() throws Exception {
        String query = "SELECT idadresse, nomadresse FROM adresses ORDER BY nomadresse";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Adresses adresse = new Adresses();
            adresse.setIdadresse(rs.getLong("idadresse"));
            adresse.setNomadresse(rs.getString("nomadresse"));
            return adresse;
        });
    }
}