package bITe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "contact")
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email must be valid")
    private String email;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "institution", nullable = false)
    @NotBlank(message = "Institution must not be blank")
    private String institution;

    public Contact() {}

    public Contact(String email, String firstName, String lastName, String phoneNumber, String institution) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.institution = institution;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public @Email(message = "Email must be valid") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email must be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "First name must not be blank") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name must not be blank") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last name must not be blank") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name must not be blank") String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "Institution must not be blank") String getInstitution() {
        return institution;
    }

    public void setInstitution(@NotBlank(message = "Institution must not be blank") String institution) {
        this.institution = institution;
    }
}
