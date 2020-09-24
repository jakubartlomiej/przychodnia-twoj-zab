package com.example.przychodnia.service;

import com.example.przychodnia.entity.ContactData;
import com.example.przychodnia.repository.ContactDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactDataService {

    private final ContactDataRepository contactDataRepository;

    public List<ContactData> findAll() {
        return contactDataRepository.findAll();
    }

    public long count() {
        return contactDataRepository.count();
    }

    public void save(ContactData contactData) {
        contactDataRepository.save(contactData);
    }
}
