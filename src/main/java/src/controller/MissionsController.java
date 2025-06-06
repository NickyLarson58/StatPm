package src.controller;

import src.model.Missions;
import src.model.StatMission;
import src.model.dto.CreatedStatMissionDTO;
import src.repository.MissionsRepository;
import src.repository.StatMissionRepository;
import src.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MissionsController {

    @Autowired
    StatMissionRepository statMissionRepository;

    @Autowired
    MissionsRepository missionRepository;

    @Autowired
    MissionService missionService;

    @GetMapping("/missions")
    public ResponseEntity<List<Missions>> getAllMissions() {
        try {
            List<Missions> missions = missionRepository.findAll();
            if (missions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/missions/{id}")
    public ResponseEntity<Missions> getMissionById(@PathVariable("id") long id) {
        Optional<Missions> missionData = missionRepository.findById(id);

        if (missionData.isPresent()) {
            return new ResponseEntity<>(missionData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/missions/{id}")
    public ResponseEntity<HttpStatus> deleteMission(@PathVariable("id") long id) {
        try {
            missionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/missions")
    public ResponseEntity<HttpStatus> deleteAllMissions() {
        try {
            missionRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/missions/stat")
    public ResponseEntity<List<StatMission>> getAllStatMissions() {
        try {
            List<StatMission> missions = statMissionRepository.findAll();
            if (missions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/missions/{id}/stat")
    public ResponseEntity<StatMission> getStatMissionById(@PathVariable("id") long id) {
        Optional<StatMission> missionData = statMissionRepository.findById(id);
        return missionData.map(mission -> new ResponseEntity<>(mission, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/missions/stat")
    public ResponseEntity<StatMission> createStatMission(@RequestBody CreatedStatMissionDTO CreatedStatMissionDTO) {
        StatMission createdMission = missionService.createMission(CreatedStatMissionDTO);
        return ResponseEntity.ok(createdMission);
    }
    
    @PutMapping("/missions/{id}/stat")
    public ResponseEntity<StatMission> updateStatMission(@PathVariable("id") long id, @RequestBody StatMission mission) {
        Optional<StatMission> missionData = statMissionRepository.findById(id);

        if (missionData.isPresent()) {
            StatMission _mission = missionData.get();
            _mission.setDateMission(mission.getDateMission());
            _mission.setLieuMission(mission.getLieuMission());
            _mission.setMissions(mission.getMissions());
            _mission.setCommerce(mission.getCommerce());
            _mission.setDureeMission(mission.getDureeMission());
            _mission.setCommerce(mission.getCommerce());
            _mission.setBrigade(mission.getBrigade());
            return new ResponseEntity<>(statMissionRepository.save(_mission), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/missions/{id}/stat")
    public ResponseEntity<HttpStatus> deleteStatMission(@PathVariable("id") long id) {
        try {
            statMissionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/missions/stat")
    public ResponseEntity<HttpStatus> deleteAllStatMissions() {
        try {
            statMissionRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}