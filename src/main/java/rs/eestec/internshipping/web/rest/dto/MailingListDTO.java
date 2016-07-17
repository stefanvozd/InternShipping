package rs.eestec.internshipping.web.rest.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the MailingList entity.
 */
public class MailingListDTO implements Serializable {

    private Long id;

    private String email;

    private LocalDate dateCreated;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MailingListDTO mailingListDTO = (MailingListDTO) o;

        if ( ! Objects.equals(id, mailingListDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MailingListDTO{" +
            "id=" + id +
            ", email='" + email + "'" +
            ", dateCreated='" + dateCreated + "'" +
            '}';
    }
}
