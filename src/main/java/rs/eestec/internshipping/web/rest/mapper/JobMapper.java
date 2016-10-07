package rs.eestec.internshipping.web.rest.mapper;

import rs.eestec.internshipping.domain.*;
import rs.eestec.internshipping.web.rest.dto.JobDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Job and its DTO JobDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    //@Mapping(source = "company.logo", target = "companyLogo")
  //  @Mapping(source = "company.companyLogoContentType", target = "companyLogoContentType")
    JobDTO jobToJobDTO(Job job);

    List<JobDTO> jobsToJobDTOs(List<Job> jobs);

    @Mapping(target = "applications", ignore = true)
    @Mapping(source = "companyId", target = "company")
    Job jobDTOToJob(JobDTO jobDTO);

    List<Job> jobDTOsToJobs(List<JobDTO> jobDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
