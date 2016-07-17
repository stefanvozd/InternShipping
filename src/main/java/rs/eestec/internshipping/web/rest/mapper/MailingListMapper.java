package rs.eestec.internshipping.web.rest.mapper;

import rs.eestec.internshipping.domain.*;
import rs.eestec.internshipping.web.rest.dto.MailingListDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity MailingList and its DTO MailingListDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MailingListMapper {

    MailingListDTO mailingListToMailingListDTO(MailingList mailingList);

    List<MailingListDTO> mailingListsToMailingListDTOs(List<MailingList> mailingLists);

    MailingList mailingListDTOToMailingList(MailingListDTO mailingListDTO);

    List<MailingList> mailingListDTOsToMailingLists(List<MailingListDTO> mailingListDTOs);
}
