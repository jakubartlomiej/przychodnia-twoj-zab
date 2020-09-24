package com.example.przychodnia.repository;

import com.example.przychodnia.entity.VisitsCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VisitsCalendarRepository extends JpaRepository<VisitsCalendar, Long> {

    @Query(value = "SELECT * FROM visits_calendar WHERE patient_id = :patient order by date",
            nativeQuery = true)
    Iterable<VisitsCalendar> findByIdPatient(@Param("patient") Long patient);

    @Query(value = "SELECT * FROM visits_calendar WHERE doctor_id = :doctor",
            nativeQuery = true)
    Iterable<VisitsCalendar> findByIdDoctor(@Param("doctor") Long doctor);

    @Query(value = "SELECT count(*) FROM visits_calendar WHERE doctor_id = :doctor and date = :date and patient_id > 0",
            nativeQuery = true)
    long findByDoctorAndDateCount(@Param("doctor") Long doctor, @Param("date") String date);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM visits_calendar WHERE date = :date AND doctor_id = :doctor",
            nativeQuery = true)
    void deleteByDateAndDoctor(@Param("date") String date, @Param("doctor") Long doctor);
}
