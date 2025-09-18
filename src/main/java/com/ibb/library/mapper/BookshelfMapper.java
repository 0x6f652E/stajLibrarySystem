package com.ibb.library.mapper;

import com.ibb.library.dto.request.BookshelfCreateRequest;
import com.ibb.library.dto.response.BookshelfResponse;
import com.ibb.library.entity.Bookshelf;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface BookshelfMapper {

    @Mapping(source = "library.id", target = "libraryId")
    BookshelfResponse toDto(Bookshelf e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "library", ignore = true) // service'de set edilir
    @Mapping(target = "books", ignore = true)
    Bookshelf toEntity(BookshelfCreateRequest req);
}
