package com.example.przychodnia.repository;

import com.example.przychodnia.entity.ContactData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactDataRepository extends JpaRepository<ContactData, Long> {
}
