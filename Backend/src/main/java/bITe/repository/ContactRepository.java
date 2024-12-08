package bITe.repository;

import bITe.entity.Contact;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ContactRepository implements PanacheRepository<Contact> {

    public Optional<Contact> findContactByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<Contact> findContactByInstitution(String institution) {
        return find("institution", institution).firstResultOptional();
    }

    public Contact saveContact(Contact contact) {
        Optional<Contact> existingContact = findContactByEmail(contact.getEmail());

        if (existingContact.isPresent()) {
            return existingContact.get();
        }

        persist(contact);
        return contact;
    }
}
