package src.service.impl;

import src.model.StatIntervention;
import src.model.StatMission;
import src.model.Adresses;
import src.model.Agents;
import src.model.dto.StatistiqueDTO;
import src.repository.StatInterventionRepository;
import src.repository.StatMissionRepository;
import src.service.StatisticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatInterventionRepository statInterventionRepository;

    @Autowired
    private StatMissionRepository statMissionRepository;

    @Override
    public List<StatistiqueDTO> getStatistiquesFiltrees(
        String brigade, 
        Integer agentId, 
        LocalDate startDate, 
        LocalDate endDate, 
        Long adresseId,
        boolean isMission
    ) {
        List<StatMission> statMissions = Lists.newArrayList();
        List<StatIntervention> statInterventions = Lists.newArrayList();
        if(isMission){
            if(agentId != null){
                if(adresseId!=null){
                    statMissions = statMissionRepository.findAllByAgents_MatriculeAndLieuMission_IdadresseAndDateMissionBetween(agentId, adresseId, startDate, endDate);
                }else{
                    statMissions = statMissionRepository.findAllByAgents_MatriculeAndDateMissionBetween(agentId, startDate, endDate);
                }
            }else if(brigade != null && !brigade.equals("")){
                    if(adresseId!=null){
                        statMissions = statMissionRepository.findAllByBrigadeAndLieuMission_IdadresseAndDateMissionBetween(brigade, adresseId, startDate, endDate);
                    }else{
                        statMissions = statMissionRepository.findAllByBrigadeAndDateMissionBetween(brigade, startDate, endDate);
                    }
            }else{
                if(adresseId!=null){
                    statMissions = statMissionRepository.findAllByDateMissionBetweenAndLieuMission_Idadresse(startDate, endDate,adresseId);
                }else{
                    statMissions = statMissionRepository.findAllByDateMissionBetween(startDate, endDate);
                }
            }
        }else{
            if(agentId != null){
                if(adresseId!=null){
                    statInterventions = statInterventionRepository.findAllByAgents_MatriculeAndAdresse_IdadresseAndDateInterventionsBetween(agentId, adresseId, startDate, endDate);
                }else{
                    statInterventions = statInterventionRepository.findAllByAgents_MatriculeAndDateInterventionsBetween(agentId, startDate, endDate);
                }
            }else if(brigade != null && !brigade.equals("")){
                    if(adresseId!=null){
                        statInterventions = statInterventionRepository.findAllByBrigadeAndAdresse_IdadresseAndDateInterventionsBetween(brigade, adresseId, startDate, endDate);
                    }else{
                        statInterventions = statInterventionRepository.findAllByBrigadeAndDateInterventionsBetween(brigade, startDate, endDate);
                    }
            } else{
                if(adresseId!=null){
                    statInterventions = statInterventionRepository.findAllByAdresse_IdadresseAndDateInterventionsBetween(adresseId, startDate, endDate);
                }else{
                    statInterventions = statInterventionRepository.findAllByDateInterventionsBetween(startDate, endDate);
                }
            }
        }

        return mapToStatistiqueDTO(statMissions, statInterventions, brigade, agentId, adresseId);
    }
    public List<StatistiqueDTO> mapToStatistiqueDTO(
        List<StatMission> statMissions, 
        List<StatIntervention> statInterventions, 
        String brigade, 
        Integer agentId, 
        Long adresseId
    ) {
        List<StatistiqueDTO> dtoList = new ArrayList<>();
        
        // Si adresseId est null, on groupe par adresse et par nom d'intervention
        if (adresseId == null) {
            // Grouper les missions par adresse et par nom de mission
            if (!statMissions.isEmpty()) {
                // Grouper d'abord par adresse
                Map<Adresses, List<StatMission>> missionsByAdresse = statMissions.stream()
                    .collect(Collectors.groupingBy(mission -> mission.getLieuMission()));
                
                for (Map.Entry<Adresses, List<StatMission>> entry : missionsByAdresse.entrySet()) {
                    Adresses adresse = entry.getKey();
                    List<StatMission> missionsForAdresse = entry.getValue();
                    
                    // Puis grouper par nom de mission pour cette adresse
                    Map<String, List<StatMission>> missionsByName = missionsForAdresse.stream()
                        .collect(Collectors.groupingBy(mission -> mission.getMissions().getNomMission()));
                    
                    for (Map.Entry<String, List<StatMission>> nameEntry : missionsByName.entrySet()) {
                        String nomMission = nameEntry.getKey();
                        List<StatMission> missionsForName = nameEntry.getValue();
                        
                        StatistiqueDTO dto = new StatistiqueDTO();
                        dto.setAdresse(adresse);
                        dto.setNomIntervention(nomMission);
                        dto.setNombre(missionsForName.size());  // Nombre de missions pour cette adresse et ce nom

                        
                        if (agentId == null) {
                            dto.setBrigade(brigade);  // Si agentId est null, on met seulement la brigade
                        } else {
                            // Filtrer les agents par agentId
                            List<Agents> listWithCurrentAgent = missionsForName.stream()
                                .flatMap(mission -> mission.getAgents().stream())
                                .filter(agent -> agent.getMatricule() == agentId)
                                .collect(Collectors.toList());
                            dto.setAgents(listWithCurrentAgent);
                        }
                        dtoList.add(dto);
                    }
                }
            }
            
            // Grouper les interventions par adresse et par nom d'intervention
            if (!statInterventions.isEmpty()) {
                // Grouper d'abord par adresse
                Map<Adresses, List<StatIntervention>> interventionsByAdresse = statInterventions.stream()
                    .collect(Collectors.groupingBy(intervention -> intervention.getAdresse()));
                
                for (Map.Entry<Adresses, List<StatIntervention>> entry : interventionsByAdresse.entrySet()) {
                    Adresses adresse = entry.getKey();
                    List<StatIntervention> interventionsForAdresse = entry.getValue();
                    
                    // Puis grouper par nom d'intervention pour cette adresse
                    Map<String, List<StatIntervention>> interventionsByName = interventionsForAdresse.stream()
                        .collect(Collectors.groupingBy(intervention -> intervention.getIntervention().getNomInterventions()));
                    
                    for (Map.Entry<String, List<StatIntervention>> nameEntry : interventionsByName.entrySet()) {
                        String nomIntervention = nameEntry.getKey();
                        List<StatIntervention> interventionsForName = nameEntry.getValue();
                        
                        StatistiqueDTO dto = new StatistiqueDTO();
                        dto.setAdresse(adresse);
                        dto.setNomIntervention(nomIntervention);
                        dto.setNombre(interventionsForName.size());  // Nombre d'interventions pour cette adresse et ce nom
                        if (agentId == null) {
                            dto.setBrigade(brigade);  // Si agentId est null, on met seulement la brigade
                        } else {
                            // Filtrer les agents par agentId
                            List<Agents> listWithCurrentAgent = interventionsForName.stream()
                                .flatMap(intervention -> intervention.getAgents().stream())
                                .filter(agent -> agent.getMatricule() == agentId)
                                .collect(Collectors.toList());
                            dto.setAgents(listWithCurrentAgent);
                        }
                        dtoList.add(dto);
                    }
                }
            }
            
        } else {
            // Si adresseId est renseigné, on continue avec ton traitement habituel
            if (!statMissions.isEmpty()) {
                for (StatMission mission : statMissions) {
                    StatistiqueDTO dto = new StatistiqueDTO();
                    dto.setNomIntervention(mission.getMissions().getNomMission());
                    dto.setAdresse(mission.getLieuMission());
                    dto.setNombre(1);  // Chaque mission compte pour 1 dans ce cas
                    
                    if (agentId == null) {
                        dto.setBrigade(brigade);  // Si agentId est null, on met seulement la brigade
                    } else {
                        List<Agents> listWithCurrentAgent = mission.getAgents().stream().map(agent -> {
                            return agent.getMatricule() == agentId ? agent : null;
                        }).collect(Collectors.toList());
                        dto.setAgents(listWithCurrentAgent);  // Sinon on met la liste des agents
                    }
                    dtoList.add(dto);
                }
            }
            
            // Si on a des interventions
            if (!statInterventions.isEmpty()) {
                for (StatIntervention intervention : statInterventions) {
                    StatistiqueDTO dto = new StatistiqueDTO();
                    dto.setNomIntervention(intervention.getIntervention().getNomInterventions());
                    dto.setAdresse(intervention.getAdresse());
                    dto.setNombre(1); // Chaque intervention compte pour 1 dans ce cas
                    
                    if (agentId == null) {
                        dto.setBrigade(brigade);  // Si agentId est null, on met seulement la brigade
                    } else {
                        List<Agents> listWithCurrentAgent = intervention.getAgents().stream().map(agent -> {
                            return agent.getMatricule() == agentId ? agent : null;
                        }).collect(Collectors.toList());
                        dto.setAgents(listWithCurrentAgent);  // Sinon on met la liste des agents
                    }
                    dtoList.add(dto);
                }
            }
        }
        


        //regroup in case of filtered by agent
        if(agentId != null && adresseId!=null){
            Map<Integer, StatistiqueDTO> mapRegroupement = new HashMap<>();
            for (StatistiqueDTO stat : dtoList) {
                for (Agents agent : stat.getAgents()) {
                    if(agent != null){
                        int agentKey = agent.getMatricule(); // Adapter selon ton modèle Agents
        
                        if (mapRegroupement.containsKey(agentKey)) {
                            // Si l'agent existe déjà dans la map, on cumule le nombre d'interventions
                            StatistiqueDTO existingStat = mapRegroupement.get(agentKey);
                            existingStat.setNombre(existingStat.getNombre() + stat.getNombre());
                        } else {
                            // Sinon, on ajoute une nouvelle entrée
                            mapRegroupement.put(agentKey, stat);
                        }
                    }
                }
            }
            dtoList = new ArrayList<>(mapRegroupement.values());        
        }
        return dtoList;
    }

}