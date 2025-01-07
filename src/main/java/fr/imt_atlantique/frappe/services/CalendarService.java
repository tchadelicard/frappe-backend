package fr.imt_atlantique.frappe.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class CalendarService {

    private final RestTemplate restTemplate;
    private final String calendarApiUrl = "http://localhost:5000/"; 

    public CalendarService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getAvailabilities(String duration) {
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(calendarApiUrl+"availabilities")
                .queryParam("duration", duration)
                .toUriString();

        ResponseEntity<List> response = restTemplate.getForEntity(urlWithParams, List.class);

        return response.getBody();
    }
    public List<Map<String, Object>> getDailyAvailabilities(String date ,String duration) {
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(calendarApiUrl+"dailyavailabilities")
                .queryParam("date", date)
                .queryParam("duration", duration)
                .toUriString();

        ResponseEntity<List> response = restTemplate.getForEntity(urlWithParams, List.class);

        return response.getBody();
    }


    
}
