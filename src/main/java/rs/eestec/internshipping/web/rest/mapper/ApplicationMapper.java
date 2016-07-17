package rs.eestec.internshipping.web.rest.mapper;

import rs.eestec.internshipping.domain.*;
import rs.eestec.internshipping.web.rest.dto.ApplicationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Application and its DTO ApplicationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationMapper {

    @Mapping(source = "resume.id", target = "resumeId")
    @Mapping(source = "resume.name", target = "resumeName")
    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "job.title", target = "jobTitle")
    ApplicationDTO applicationToApplicationDTO(Application application);

    List<ApplicationDTO> applicationsToApplicationDTOs(List<Application> applications);

    @Mapping(source = "resumeId", target = "resume")
    @Mapping(source = "jobId", target = "job")
    Application applicationDTOToApplication(ApplicationDTO applicationDTO);

    List<Application> applicationDTOsToApplications(List<ApplicationDTO> applicationDTOs);

    default Resume resumeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Resume resume = new Resume();
        resume.setId(id);
        return resume;
    }

    default Job jobFromId(Long id) {
        if (id == null) {
            return null;
        }
        Job job = new Job();
        job.setId(id);
        return job;
    }
}
