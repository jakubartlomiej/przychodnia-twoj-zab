package com.example.przychodnia.repository;

import com.example.przychodnia.entity.Schedule;
import com.example.przychodnia.entity.VisitsCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Iterable<Schedule> findAllByDoctorId(Long id);

    @Query(value = "SELECT * FROM schedule where YEARWEEK(date) = YEARWEEK(:inputDate) and doctor_id = :doctor_id",
                nativeQuery = true)
    Iterable<Schedule> findWeekByDate(@Param("inputDate") String date, Long doctor_id);
}
