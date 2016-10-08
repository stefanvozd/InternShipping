package rs.eestec.internshipping.repository;

import org.springframework.data.repository.query.Param;
import rs.eestec.internshipping.domain.Resume;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Resume entity.
 */
@SuppressWarnings("unused")
public interface ResumeRepository extends JpaRepository<Resume,Long> {

	@Query("select resume from Resume resume where resume.user.login = ?#{principal.username}")
	Page<Resume> findByUserIsCurrentUser(Pageable page);

    @Query("select resume.name from Application app where job.id = :job_id and job in (select job from Job job where job.company.id = (select company from Company company where company.user.login = ?#{principal.username} ))")
    Page<Resume> getResumesForJobId(Pageable page,@Param("job_id") long job_id);

}
