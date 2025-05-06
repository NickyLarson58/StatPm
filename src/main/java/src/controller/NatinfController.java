package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/natinf")
public class NatinfController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchNatinf(
            @RequestParam String term,
            @RequestParam(required = false, defaultValue = "false") boolean exact,
            @RequestParam(required = false, defaultValue = "false") boolean wholeWord) {
        
        String sql;
        String searchTerm;
        
        if (exact) {
            if (wholeWord) {
                sql = "SELECT DISTINCT natinf, qualification_infraction FROM natinf " +
                      "WHERE LOWER(qualification_infraction) REGEXP CONCAT('\\\\b', LOWER(?), '\\\\b') OR natinf = ? " +
                      "ORDER BY CASE " +
                      "  WHEN natinf = ? THEN 1 " +
                      "  WHEN LOWER(qualification_infraction) REGEXP CONCAT('\\\\b', LOWER(?), '\\\\b') THEN 2 " +
                      "  ELSE 3 " +
                      "END ";
                searchTerm = term;
            } else {
                sql = "SELECT DISTINCT natinf, qualification_infraction FROM natinf " +
                      "WHERE LOWER(qualification_infraction) = LOWER(?) OR natinf = ? " +
                      "ORDER BY CASE " +
                      "  WHEN natinf = ? THEN 1 " +
                      "  WHEN LOWER(qualification_infraction) = LOWER(?) THEN 2 " +
                      "  ELSE 3 " +
                      "END ";
                searchTerm = term;
            }
        } else {
            sql = "SELECT DISTINCT natinf, qualification_infraction FROM natinf " +
                  "WHERE LOWER(qualification_infraction) LIKE CONCAT('%', LOWER(?), '%') OR natinf LIKE ? " +
                  "ORDER BY CASE " +
                  "  WHEN natinf LIKE ? THEN 1 " +
                  "  WHEN LOWER(qualification_infraction) LIKE CONCAT('%', LOWER(?), '%') THEN 2 " +
                  "  ELSE 3 " +
                  "END ";
            searchTerm = term;
        }
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
            sql,
            searchTerm,
            searchTerm,
            searchTerm,
            searchTerm
        );
        
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{natinf}")
    public ResponseEntity<Map<String, Object>> getNatinfDetails(@PathVariable String natinf) {
        String sql = "SELECT natinf, nature_infraction, qualification_infraction, definie_par, reprimee_par " +
                    "FROM natinf WHERE natinf = ?";
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, natinf);
        
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(results.get(0));
    }
}