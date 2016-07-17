package rs.eestec.internshipping.web.rest.mapper;

import rs.eestec.internshipping.domain.*;
import rs.eestec.internshipping.web.rest.dto.ResumeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Resume and its DTO ResumeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface ResumeMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ResumeDTO resumeToResumeDTO(Resume resume);

    List<ResumeDTO> resumesToResumeDTOs(List<Resume> resumes);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "applications", ignore = true)
    Resume resumeDTOToResume(ResumeDTO resumeDTO);

    List<Resume> resumeDTOsToResumes(List<ResumeDTO> resumeDTOs);
}
