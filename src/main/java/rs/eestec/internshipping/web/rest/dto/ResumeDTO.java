package rs.eestec.internshipping.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

import rs.eestec.internshipping.domain.enumeration.Education;

/**
 * A DTO for the Resume entity.
 */
public class ResumeDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] image;

    private String imageContentType;
    @NotNull
    private String name;

    private String title;

    private String overview;

    private Education education;

    @NotNull
    private String faculty;

    @NotNull
    private String enrollmentYear;

    private String location;

    private String contactEmail;

    private LocalDate birthDate;

    private String jsonResume;

    @Lob
    private byte[] cvFile;

    private String cvFileContentType;
    private Boolean receiveJobAlerts;

    private String socialLinkedin;

    private String representativeSkills;


    private Long userId;
    

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }
    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    public String getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(String enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public String getJsonResume() {
        return jsonResume;
    }

    public void setJsonResume(String jsonResume) {
        this.jsonResume = jsonResume;
    }
    public byte[] getCvFile() {
        return cvFile;
    }

    public void setCvFile(byte[] cvFile) {
        this.cvFile = cvFile;
    }

    public String getCvFileContentType() {
        return cvFileContentType;
    }

    public void setCvFileContentType(String cvFileContentType) {
        this.cvFileContentType = cvFileContentType;
    }
    public Boolean getReceiveJobAlerts() {
        return receiveJobAlerts;
    }

    public void setReceiveJobAlerts(Boolean receiveJobAlerts) {
        this.receiveJobAlerts = receiveJobAlerts;
    }
    public String getSocialLinkedin() {
        return socialLinkedin;
    }

    public void setSocialLinkedin(String socialLinkedin) {
        this.socialLinkedin = socialLinkedin;
    }
    public String getRepresentativeSkills() {
        return representativeSkills;
    }

    public void setRepresentativeSkills(String representativeSkills) {
        this.representativeSkills = representativeSkills;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResumeDTO resumeDTO = (ResumeDTO) o;

        if ( ! Objects.equals(id, resumeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ResumeDTO{" +
            "id=" + id +
            ", image='" + image + "'" +
            ", name='" + name + "'" +
            ", title='" + title + "'" +
            ", overview='" + overview + "'" +
            ", education='" + education + "'" +
            ", faculty='" + faculty + "'" +
            ", enrollmentYear='" + enrollmentYear + "'" +
            ", location='" + location + "'" +
            ", contactEmail='" + contactEmail + "'" +
            ", birthDate='" + birthDate + "'" +
            ", jsonResume='" + jsonResume + "'" +
            ", cvFile='" + cvFile + "'" +
            ", receiveJobAlerts='" + receiveJobAlerts + "'" +
            ", socialLinkedin='" + socialLinkedin + "'" +
            ", representativeSkills='" + representativeSkills + "'" +
            '}';
    }
}
