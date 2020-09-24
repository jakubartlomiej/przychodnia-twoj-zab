package com.example.przychodnia.repository;

import com.example.przychodnia.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m order by m.opened")
    List<Message> findAll();

    @Query("select count(id) from Message m where opened = false")
    int newMessagesCount();

    List<Message> findAllByUserIdOrderByCreateDateTimeDesc(Long id);
}
