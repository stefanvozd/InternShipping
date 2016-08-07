package rs.eestec.internshipping.repository;

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
	//List<Resume> findByUserIsCurrentUser();
	Page<Resume> findByUserIsCurrentUser(Pageable page);
	
	
	
}
