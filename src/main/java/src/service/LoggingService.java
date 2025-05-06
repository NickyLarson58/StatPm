package src.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LoggingService {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logError(String type, String matricule, String message, String location) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] Type: %s, Matricule: %s, Location: %s - %s", 
            timestamp, type, matricule, location, message);
        logger.error(logMessage);
    }

    public void logInfo(String type, String matricule, String message, String location) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] Type: %s, Matricule: %s, Location: %s - %s", 
            timestamp, type, matricule, location, message);
        logger.info(logMessage);
    }

    public void logDebug(String type, String matricule, String message, String location) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] Type: %s, Matricule: %s, Location: %s - %s", 
            timestamp, type, matricule, location, message);
        logger.debug(logMessage);
    }
}
