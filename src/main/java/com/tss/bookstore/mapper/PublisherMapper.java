package com.tss.bookstore.mapper;

import com.tss.bookstore.dto.PublisherRequestDto;
import com.tss.bookstore.dto.PublisherResponseDto;
import com.tss.bookstore.entity.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    Publisher toEntity(PublisherRequestDto dto);
    PublisherResponseDto toDto(Publisher publisher);
}
