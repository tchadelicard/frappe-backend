package fr.imt_atlantique.frappe.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Service
public class CalendarService {

    private final RestTemplate restTemplate;
    private final String calendarApiUrl = "http://localhost:5000/availabilities"; // URL de l'API Flask

    public CalendarService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    
    public List<Map<String, Object>> getAvailabilities() {
        ResponseEntity<List> response = restTemplate.getForEntity(calendarApiUrl, List.class);
        return response.getBody();
    }
}
