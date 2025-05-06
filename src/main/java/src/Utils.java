package src;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    @Value("${database.ip:localhost}")
    public String ip;
    
    @Value("${database.user:root}")
    public String utilisateur;
    
    @Value("${database.port:3306}")
    public String port;
    
    @Value("${database.password:root}")
    public String password;
    
    @Value("${app.path.doc-administratif:./documents/administratif}")
    public String pathDocAdministratif;
    
    @Value("${app.path.destination:./documents/destination}")
    public String pathDestionation;
    
    @Value("${app.path.bin-sql:./bin/sql}")
    public String pathBinSql;
    
    @Value("${app.path.bilan:./documents/bilan}")
    public String pathbilan;
    
    @Value("${app.max-type-vignette:10}")
    public int nbMaxDeTypeVignette;

    protected Object dbName;
}
