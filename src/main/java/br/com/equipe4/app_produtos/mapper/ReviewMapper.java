package br.com.equipe4.app_produtos.mapper;

import br.com.equipe4.app_produtos.model.Review;
import br.com.equipe4.app_produtos.service.dto.request.ReviewRequestDTO;
import br.com.equipe4.app_produtos.service.dto.response.ReviewResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper{

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Review toEntity(ReviewRequestDTO dto);

       ReviewResponseDTO toResponse(Review entity);
}
