package rs.eestec.internshipping.repository;

import rs.eestec.internshipping.domain.MailingList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MailingList entity.
 */
@SuppressWarnings("unused")
public interface MailingListRepository extends JpaRepository<MailingList,Long> {

}
