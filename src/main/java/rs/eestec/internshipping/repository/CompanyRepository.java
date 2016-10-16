package rs.eestec.internshipping.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.eestec.internshipping.domain.Company;

import org.springframework.data.jpa.repository.*;
import rs.eestec.internshipping.domain.Resume;

import java.util.List;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query("select company from Company company where company.user.login = ?#{principal.username}")
    Page<Company> findByUserIsCurrentUser(Pageable page);

    @Query("select company from Company company where company.user.login = ?#{principal.username}")
    Company findByUserIsCurrentUser();

}
