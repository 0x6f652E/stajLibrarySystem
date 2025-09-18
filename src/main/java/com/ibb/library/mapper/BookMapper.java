package com.ibb.library.mapper;

import com.ibb.library.dto.request.BookCreateRequest;
import com.ibb.library.dto.response.BookResponse;
import com.ibb.library.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class}, builder = @Builder(disableBuilder = true))
public interface BookMapper {

    @Mapping(source = "bookshelf.id", target = "bookshelfId")
    BookResponse toDto(Book e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookshelf", ignore = true) // service'de set edilir
    @Mapping(target = "authors", ignore = true)   // service'de set edilir
    Book toEntity(BookCreateRequest req);
}
