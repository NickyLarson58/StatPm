package src.service.impl;

import src.model.*;
import src.model.dto.CreatedStatInterventionDTO;
import src.repository.*;
import src.service.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterventionServiceImpl implements InterventionService {

    @Autowired
    private InterventionsRepository interventionsRepository;

    @Autowired
    private AdressesRepository adressesRepository;

    @Autowired
    private StatInterventionRepository statInterventionRepository;

    @Autowired
    private InfractionRepository infractionRepository;

    @Autowired
    private MadRepository madRepository;

    @Override
    @Transactional
    public Long createIntervention(CreatedStatInterventionDTO interventionDTO) {
        StatIntervention statIntervention = new StatIntervention();
        Interventions intervention = interventionsRepository.findById(interventionDTO.getIdIntervention()).orElse(null);
        if (intervention != null) {
            statIntervention.setIntervention(intervention);
        }
        statIntervention.setDateInterventions(interventionDTO.getDateIntervention());
        statIntervention.setNombreIntervention(interventionDTO.getNombreIntervention());
        Adresses adresse = adressesRepository.findById(interventionDTO.getIdAdresse()).orElse(null);
        if (adresse != null) {
            statIntervention.setAdresse(adresse);
        }
        if(interventionDTO.getIdInfraction() != null){
            Infraction infraction = infractionRepository.findById(interventionDTO.getIdInfraction()).orElse(null);
            statIntervention.setInfraction(infraction);
        } else if (interventionDTO.getIdMad() != null) {
            Mad mad = madRepository.findById(interventionDTO.getIdMad()).orElse(null);
            statIntervention.setMad(mad);
        }
        statIntervention.setAgents(interventionDTO.getAgents());
        statIntervention = statInterventionRepository.saveAndFlush(statIntervention);
        return statIntervention.getIdStatIntervention();
    }
}