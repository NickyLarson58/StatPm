package src.service;

import src.model.dto.CreatedStatInterventionDTO;

public interface InterventionService {
    /**
     * Creates a new intervention based on the provided DTO
     * @param interventionDTO the DTO containing intervention details
     * @return the created intervention's ID
     */
    Long createIntervention(CreatedStatInterventionDTO interventionDTO);
}