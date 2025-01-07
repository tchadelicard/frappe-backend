package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.services.CalendarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import fr.imt_atlantique.frappe.dtos.*;


import java.util.List;
import java.util.Map;



@RestController
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }


    @GetMapping("/availabilities")
    public List<Map<String, Object>> getAvailabilities(@RequestBody AvailabilityRequest request ) {
        return calendarService.getAvailabilities(request.getDuration());
    }
    @GetMapping("/dailyavailabilities")
    public List<Map<String, Object>> getDailyAvailabilities(@RequestBody DailyAvailabilityRequest request ) {
        return calendarService.getDailyAvailabilities(request.getDate() ,request.getDuration());
    }
}
