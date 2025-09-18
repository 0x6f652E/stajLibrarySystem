package com.ibb.library.mapper;

import com.ibb.library.dto.request.LibraryCreateRequest;
import com.ibb.library.dto.response.LibraryResponse;
import com.ibb.library.entity.Library;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface LibraryMapper {
    LibraryResponse toDto(Library e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookshelves", ignore = true)
    Library toEntity(LibraryCreateRequest req);
}
