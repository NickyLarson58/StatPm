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
    private static final String DB_HOST = "localhost";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DB_NAME = "statweb";
    private static final String MYSQL_DUMP_PATH = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe";

    @PostMapping("/backup-database")
    public ResponseEntity<?> backupDatabase(@RequestParam("fileName") String fileName) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Créer le répertoire de sauvegarde s'il n'existe pas
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
            
            // Afficher la commande pour le débogage (sans le mot de passe)
            System.out.println("Commande exécutée : " + MYSQL_DUMP_PATH + " -h " + DB_HOST + " -u " + DB_USER + " -proot --databases " + DB_NAME);

            // Rediriger l'erreur vers la console pour le débogage
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            // Exécuter la commande
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            // Vérifier si le fichier a été créé et n'est pas vide
            File outputFile = new File(backupFile);
            if (exitCode == 0 && outputFile.exists() && outputFile.length() > 0) {
                response.put("success", true);
                response.put("message", "Sauvegarde de la base de données effectuée avec succès");
                response.put("backupFile", backupFile);
                response.put("fileSize", outputFile.length());
            } else {
                response.put("success", false);
                response.put("message", "Erreur lors de la sauvegarde de la base de données: fichier vide ou non créé");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la sauvegarde: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}