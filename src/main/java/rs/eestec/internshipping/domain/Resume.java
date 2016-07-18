package rs.eestec.internshipping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import rs.eestec.internshipping.domain.enumeration.Education;

/**
 * A Resume.
 */
@Entity
@Table(name = "resume")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "resume")
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "overview")
    private String overview;

    @Enumerated(EnumType.STRING)
    @Column(name = "education")
    private Education education;

    @NotNull
    @Column(name = "faculty", nullable = false)
    private String faculty;

    @NotNull
    @Column(name = "enrollment_year", nullable = false)
    private String enrollmentYear;

    @Column(name = "location")
    private String location;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "json_resume")
    private String jsonResume;

    @Lob
    @Column(name = "cv_file")
    private byte[] cvFile;

    @Column(name = "cv_file_content_type")
    private String cvFileContentType;

    @Column(name = "cv_file_content")
    private String cvFileContent;

    @Column(name = "receive_job_alerts")
    private Boolean receiveJobAlerts;

    @Column(name = "social_linkedin")
    private String socialLinkedin;

    @Column(name = "representative_skills")
    private String representativeSkills;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Application> applications = new HashSet<>();

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

    public String getCvFileContent() {
        return cvFileContent;
    }

    public void setCvFileContent(String cvFileContent) {
        this.cvFileContent = cvFileContent;
    }

    public Boolean isReceiveJobAlerts() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resume resume = (Resume) o;
        if(resume.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, resume.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Resume{" +
            "id=" + id +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
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
            ", cvFileContentType='" + cvFileContentType + "'" +
            ", cvFileContent='" + cvFileContent + "'" +
            ", receiveJobAlerts='" + receiveJobAlerts + "'" +
            ", socialLinkedin='" + socialLinkedin + "'" +
            ", representativeSkills='" + representativeSkills + "'" +
            '}';
    }
}
