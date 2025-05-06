package src;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"src", "SQL"})
@ComponentScan(basePackages = {"src", "SQL"})
@EntityScan(basePackages = {"src", "SQL"})
@EnableJpaRepositories(basePackages = {"src", "SQL"})
public class WebApplication {
    private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {
        // Log d'information pour indiquer le démarrage de l'application
        logger.info("Démarrage de l'application StatPm...");
        try {
            // Démarrage de l'application Spring et récupération du contexte
            ConfigurableApplicationContext context = SpringApplication.run(WebApplication.class, args);
            // Log d'information pour indiquer que l'application a démarré avec succès
            logger.info("Application StatPm démarrée avec succès!");
            
            // Log des beans actifs
            String[] beanNames = context.getBeanDefinitionNames();
            // Log de débogage pour indiquer le nombre de beans chargés
            logger.debug("Beans chargés ({}):", beanNames.length);
            for (String beanName : beanNames) {
                // Log de débogage pour chaque bean chargé
                logger.debug(" - {}", beanName);
            }
        } catch (Exception e) {
            // Log d'erreur en cas d'exception lors du démarrage de l'application
            logger.error("Erreur lors du démarrage de l'application:", e);
            throw e;
        }
    }
}
