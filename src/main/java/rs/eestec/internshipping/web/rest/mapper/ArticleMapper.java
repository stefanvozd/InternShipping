package rs.eestec.internshipping.web.rest.mapper;

import rs.eestec.internshipping.domain.*;
import rs.eestec.internshipping.web.rest.dto.ArticleDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Article and its DTO ArticleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArticleMapper {

    ArticleDTO articleToArticleDTO(Article article);

    List<ArticleDTO> articlesToArticleDTOs(List<Article> articles);

    Article articleDTOToArticle(ArticleDTO articleDTO);

    List<Article> articleDTOsToArticles(List<ArticleDTO> articleDTOs);
}
