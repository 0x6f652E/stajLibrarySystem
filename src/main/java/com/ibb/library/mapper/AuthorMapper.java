package com.ibb.library.mapper;

import com.ibb.library.dto.request.AuthorCreateRequest;
import com.ibb.library.dto.response.AuthorResponse;
import com.ibb.library.entity.Author;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AuthorMapper {
    AuthorResponse toDto(Author e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorCreateRequest req);
}
