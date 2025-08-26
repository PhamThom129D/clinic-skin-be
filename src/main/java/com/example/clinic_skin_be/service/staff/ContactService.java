package com.example.clinic_skin_be.service.staff;


import com.example.clinic_skin_be.model.staff.Contacts;
import com.example.clinic_skin_be.repository.staff.IContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactService {
    private final IContactRepository contactRepository;

    public Contacts save(Contacts contact) {
        return contactRepository.save(contact);
    }
}
