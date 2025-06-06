package src.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DatabaseBackupController {

    private static final String BACKUP_DIRECTORY = "sauvegardeBdd";
    private static final String DB_HOST = "10.13.9.68";
    private static final String DB_USER = "pom48";
    private static final String DB_PASSWORD = "root";
    private static final String DB_NAME = "statweb";
    private static final String MYSQL_DUMP_PATH = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe";

    @PostMapping("/backup-database")
    public ResponseEntity<?> backupDatabase(@RequestParam("fileName") String fileName) {
        Map<String, Object> response = new HashMap<>();

        try {
            // CrÃ©er le rÃ©pertoire de sauvegarde s'il n'existe pas
            File backupDir = new File(BACKUP_DIRECTORY);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            // Construire le chemin du fichier de sauvegarde
            String backupFile = BACKUP_DIRECTORY + File.separator + fileName + ".sql";
            
            // Construire la commande mysqldump avec ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(
                MYSQL_DUMP_PATH,
                "-h", DB_HOST,
                "-u", DB_USER,
                "-proot",
                "--databases", DB_NAME,
                "--result-file=" + backupFile
            );
            
            // Configuration de l'environnement pour le mot de passe MySQL
            Map<String, String> env = processBuilder.environment();
            env.put("MYSQL_PWD", DB_PASSWORD);
            
            // Afficher la commande pour le dÃ©bogage (sans le mot de passe)
            System.out.println("Commande exÃ©cutÃ©e : " + MYSQL_DUMP_PATH + " -h " + DB_HOST + " -u " + DB_USER + " -proot --databases " + DB_NAME);

            // Rediriger l'erreur vers la console pour le dÃ©bogage
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            // ExÃ©cuter la commande
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            // VÃ©rifier si le fichier a Ã©tÃ© crÃ©Ã© et n'est pas vide
            File outputFile = new File(backupFile);
            if (exitCode == 0 && outputFile.exists() && outputFile.length() > 0) {
                response.put("success", true);
                response.put("message", "Sauvegarde de la base de donnÃ©es effectuÃ©e avec succÃ¨s");
                response.put("backupFile", backupFile);
                response.put("fileSize", outputFile.length());
            } else {
                response.put("success", false);
                response.put("message", "Erreur lors de la sauvegarde de la base de donnÃ©es: fichier vide ou non crÃ©Ã©");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la sauvegarde: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}