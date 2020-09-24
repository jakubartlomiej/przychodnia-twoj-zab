package com.example.przychodnia.service;

import com.example.przychodnia.entity.VisitsCalendar;
import com.example.przychodnia.repository.VisitsCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitsCalendarService {

    private final VisitsCalendarRepository visitsCalendarRepository;

    public List<VisitsCalendar> findAll(){
        return visitsCalendarRepository.findAll();
    }

    public Iterable<VisitsCalendar> findByPatientId(String id){
        return visitsCalendarRepository.findByIdPatient(Long.parseLong(id));
    }

    public Iterable<VisitsCalendar> findByDoctorId(String id){
        return visitsCalendarRepository.findByIdDoctor(Long.parseLong(id));
    }

    public VisitsCalendar findById(Long id){
        return visitsCalendarRepository.findById(id).orElse(null);
    }

    public VisitsCalendar addVisit(VisitsCalendar visitsCalendar){
        return visitsCalendarRepository.save(visitsCalendar);
    }

    public long findByDoctorAndDateCount(String doctor, String date){
        return visitsCalendarRepository.findByDoctorAndDateCount(Long.parseLong(doctor),date);
    }

    public void deleteByDateAndDoctor(String date, Long doctor){
        visitsCalendarRepository.deleteByDateAndDoctor(date,doctor);
    }
    public void deleteVisitsById(String id){
        visitsCalendarRepository.deleteById(Long.parseLong(id));
    }

}
