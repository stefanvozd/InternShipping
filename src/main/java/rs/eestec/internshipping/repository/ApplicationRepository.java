package rs.eestec.internshipping.repository;

import rs.eestec.internshipping.domain.Application;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    Long countByJobId(long id);

}
