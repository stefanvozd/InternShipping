package rs.eestec.internshipping.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import rs.eestec.internshipping.domain.Company;
import rs.eestec.internshipping.domain.Job;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
public interface JobRepository extends JpaRepository<Job, Long> {
    
    @Query("select job from Job job where job.company.id = (select company from Company company where company.user.login = ?#{principal.username})")
    Page<Job> findAllOurJobs(Pageable pageable);

    @Query("select count(j) FROM Job j WHERE j.company.id=?1")
    Long countJobsByCompany(Long companyId);

    @Query("select job from Application app where app.resume.id in (select id from Resume resume where resume.user.login = ?#{principal.username})")
    Page<Job> allJobsApplyedTo(Pageable pageable);
}



