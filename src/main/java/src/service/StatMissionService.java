package src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import src.model.Adresses;
import src.model.Agents;
import src.model.Missions;
import src.model.StatMission;
import src.model.dto.CreatedStatMissionDTO;
import src.repository.AdressesRepository;
import src.repository.AgentsRepository;
import src.repository.MissionsRepository;
import src.repository.StatMissionRepository;

import java.util.List;

@Service
public class StatMissionService {

    @Autowired
    private StatMissionRepository statMissionRepository;

    @Autowired
    private MissionsRepository missionsRepository;

    @Autowired
    private AdressesRepository adressesRepository;

    @Autowired
    private AgentsRepository agentsRepository;

    /**
     * Crée une nouvelle mission statistique à partir des données du DTO
     * et l'associe aux agents participants
     * 
     * @param dto Les données de la mission à créer
     * @return La mission créée et sauvegardée en base de données
     */
    @Transactional
    public StatMission createStatMission(CreatedStatMissionDTO dto) {
        // Créer une nouvelle mission statistique
        StatMission statMission = new StatMission();
        
        // Définir la date et la durée de la mission
        statMission.setDateMission(dto.getDateMission());
        statMission.setDureeMission(dto.getDuree());
        
        // Définir le commerce si présent
        statMission.setCommerce(dto.getCommerce());

        // Récupérer et associer la mission
        Missions mission = missionsRepository.findById(dto.getIdMission())
                .orElseThrow(() -> new RuntimeException("Mission non trouvée"));
        statMission.setMissions(mission);

        // Récupérer et associer l'adresse
        Adresses adresse = adressesRepository.findById(dto.getIdAdresse())
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée"));
        statMission.setLieuMission(adresse);

        // Associer les agents
        List<Agents> agents = dto.getAgents();
        if (agents != null && !agents.isEmpty()) {
            statMission.setAgents(agents);
        }

        // Sauvegarder la mission
        return statMissionRepository.save(statMission);
    }
}