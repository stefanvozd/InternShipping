package rs.eestec.internshipping.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import rs.eestec.internshipping.domain.enumeration.JobType;
import rs.eestec.internshipping.domain.enumeration.JobLevel;
import rs.eestec.internshipping.domain.enumeration.Education;

/**
 * A DTO for the Job entity.
 */
public class JobDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5, max = 40)
    private String title;

    @NotNull
    @Size(min = 29, max = 300)
    private String shortDescription;

    @NotNull
    private String location;

    private String compensation;

    @NotNull
    private JobType jobType;

    @NotNull
    private JobLevel jobLevel;

    @NotNull
    private Education education;

    @NotNull
    private String longDescription;

    private String skills;

    private String socialLinkedin;

    private ZonedDateTime creationDate;

    @NotNull
    private LocalDate activeUntil;

    private Boolean active;


    private Long companyId;
    

    private String companyName;

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
    public String getCompensation() {
        return compensation;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
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
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobDTO jobDTO = (JobDTO) o;

        if ( ! Objects.equals(id, jobDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", shortDescription='" + shortDescription + "'" +
            ", location='" + location + "'" +
            ", compensation='" + compensation + "'" +
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
