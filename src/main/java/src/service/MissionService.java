package src.service;

import src.model.StatMission;
import src.model.dto.CreatedStatMissionDTO;

public interface MissionService {
    /**
     * Creates a new mission based on the provided DTO
     * @param missionDTO the DTO containing mission details
     * @return the created mission
     */
    StatMission createMission(CreatedStatMissionDTO missionDTO);
}