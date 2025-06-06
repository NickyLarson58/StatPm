package src.service.impl;

import src.model.*;
import src.model.dto.CreatedStatMissionDTO;
import src.repository.*;
import src.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MissionServiceImpl implements MissionService {

    @Autowired
    private MissionsRepository missionsRepository;

    @Autowired
    private AdressesRepository adressesRepository;

    @Autowired
    private StatMissionRepository statMissionRepository;

    @Override
    @Transactional
    public StatMission createMission(CreatedStatMissionDTO missionDTO) {
        StatMission statMission = new StatMission();
        Missions mission = missionsRepository.findById(missionDTO.getIdMission()).orElse(null);
        if (mission != null) {
            statMission.setMissions(mission);
        }
        statMission.setDateMission(missionDTO.getDateMission());
        statMission.setDureeMission(missionDTO.getDuree());
        statMission.setBrigade(missionDTO.getBrigade());
        Adresses adresse = adressesRepository.findById(missionDTO.getIdAdresse()).orElse(null);
        if (adresse != null) {
            statMission.setLieuMission(adresse);

        }
        if(missionDTO.getCommerce() != null){
            statMission.setCommerce(missionDTO.getCommerce());
        }
        statMission.setAgents(missionDTO.getAgents());
        return statMissionRepository.saveAndFlush(statMission);
    }
}