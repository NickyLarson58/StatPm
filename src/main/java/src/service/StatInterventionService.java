package src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import src.model.Adresses;
import src.model.Agents;
import src.model.Infraction;
import src.model.Interventions;
import src.model.Mad;
import src.model.StatIntervention;
import src.model.dto.CreatedStatInterventionDTO;
import src.repository.AdressesRepository;
import src.repository.AgentsRepository;
import src.repository.InfractionRepository;
import src.repository.InterventionsRepository;
import src.repository.MadRepository;
import src.repository.StatInterventionRepository;
import src.repository.BrigadeRepository;
import src.model.Brigade;

import java.util.List;

@Service
public class StatInterventionService {

    @Autowired
    private StatInterventionRepository statInterventionRepository;

    @Autowired
    private InterventionsRepository interventionsRepository;

    @Autowired
    private AdressesRepository adressesRepository;

    @Autowired
    private AgentsRepository agentsRepository;

    @Autowired
    private MadRepository madRepository;

    @Autowired
    private InfractionRepository infractionRepository;

    @Autowired
    private BrigadeRepository brigadeRepository;

    @Transactional
    public StatIntervention createStatIntervention(CreatedStatInterventionDTO dto) {
        // Log the values being inserted
        System.out.println("\nInserting into stat_intervention:");
        System.out.println("////  id_adresse: " + dto.getIdAdresse());
        System.out.println("////  nomBrigade: " + dto.getBrigade());
        System.out.println("////  date_interventions: " + dto.getDateIntervention());
        System.out.println("////  id_infraction: " + dto.getIdInfraction());
        System.out.println("////  id_interventions: " + dto.getIdIntervention());
        System.out.println("////  id_mad: " + dto.getIdMad());
        System.out.println("////  nombre_intervention: " + dto.getNombreIntervention());

        // Créer une nouvelle intervention statistique
        StatIntervention statIntervention = new StatIntervention();
        
        // Définir la date et le nombre d'interventions
        statIntervention.setDateInterventions(dto.getDateIntervention());
        statIntervention.setNombreIntervention(dto.getNombreIntervention());

        // Récupérer et associer l'intervention
        Interventions intervention = interventionsRepository.findById(dto.getIdIntervention())
                .orElseThrow(() -> new RuntimeException("Intervention non trouvée"));
        statIntervention.setIntervention(intervention);

        // Récupérer et associer l'adresse
        Adresses adresse = adressesRepository.findById(dto.getIdAdresse())
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée"));
        statIntervention.setAdresse(adresse);

        // Vérifier le type d'intervention
        Integer typeId = intervention.getIdInterventions();
        
        // Associer la MAD si présente et autorisée (uniquement pour les types 2 et 3)
        if (dto.getIdMad() != null) {
            if (typeId != 2 && typeId != 3) {
                throw new RuntimeException("La MAD n'est autorisée que pour les types d'intervention 2 et 3");
            }
            Mad mad = madRepository.findById(dto.getIdMad())
                    .orElseThrow(() -> new RuntimeException("MAD non trouvée"));
            statIntervention.setMad(mad);
        }

        // Associer l'infraction si présente et autorisée (uniquement pour les types 2 et 3)
        if (dto.getIdInfraction() != null) {
            if (typeId != 2 && typeId != 3) {
                throw new RuntimeException("L'infraction n'est autorisée que pour les types d'intervention 2 et 3");
            }
            Infraction infraction = infractionRepository.findById(dto.getIdInfraction())
                    .orElseThrow(() -> new RuntimeException("Infraction non trouvée"));
            statIntervention.setInfraction(infraction);
        }

        // Associer la brigade
        if (dto.getBrigade() != null) {
            Brigade brigade = brigadeRepository.findByNomBrigade(dto.getBrigade())
                    .orElseThrow(() -> new RuntimeException("Brigade non trouvée"));
            statIntervention.setBrigade(brigade.getNomBrigade());
        }

        // Associer les agents
        List<Agents> agents = dto.getAgents();
        if (agents != null && !agents.isEmpty()) {
            statIntervention.setAgents(agents);
            System.out.println("\nInserting into stat_intervention_agents:");
            // Save first to get the generated ID
            statIntervention = statInterventionRepository.saveAndFlush(statIntervention);
            for (Agents agent : agents) {
                System.out.println("StatIntervention_id_stat_intervention   ::::::::: " + statIntervention.getIdStatIntervention());
                System.out.println("agents_matricule   ::::::::::: " + agent.getMatricule());
            }
            return statIntervention;
        }

        // Sauvegarder l'intervention
        return statInterventionRepository.saveAndFlush(statIntervention);
    }
}