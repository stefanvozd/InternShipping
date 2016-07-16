package rs.eestec.internshipping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import rs.eestec.internshipping.domain.enumeration.JobType;

import rs.eestec.internshipping.domain.enumeration.JobLevel;

import rs.eestec.internshipping.domain.enumeration.Education;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 5, max = 70)
    @Column(name = "title", length = 70, nullable = false)
    private String title;

    @NotNull
    @Size(min = 10, max = 700)
    @Column(name = "short_description", length = 700, nullable = false)
    private String shortDescription;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "job_level", nullable = false)
    private JobLevel jobLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "education", nullable = false)
    private Education education;

    @NotNull
    @Size(max = 4000)
    @Column(name = "long_description", length = 4000, nullable = false)
    private String longDescription;

    @Column(name = "skills")
    private String skills;

    @Column(name = "social_linkedin")
    private String socialLinkedin;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @NotNull
    @Column(name = "active_until", nullable = false)
    private LocalDate activeUntil;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Application> applications = new HashSet<>();

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JobLevel getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(JobLevel jobLevel) {
        this.jobLevel = jobLevel;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSocialLinkedin() {
        return socialLinkedin;
    }

    public void setSocialLinkedin(String socialLinkedin) {
        this.socialLinkedin = socialLinkedin;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getActiveUntil() {
        return activeUntil;
    }

    public void setActiveUntil(LocalDate activeUntil) {
        this.activeUntil = activeUntil;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        if(job.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", shortDescription='" + shortDescription + "'" +
            ", location='" + location + "'" +
            ", jobType='" + jobType + "'" +
            ", jobLevel='" + jobLevel + "'" +
            ", education='" + education + "'" +
            ", longDescription='" + longDescription + "'" +
            ", skills='" + skills + "'" +
            ", socialLinkedin='" + socialLinkedin + "'" +
            ", creationDate='" + creationDate + "'" +
            ", activeUntil='" + activeUntil + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
