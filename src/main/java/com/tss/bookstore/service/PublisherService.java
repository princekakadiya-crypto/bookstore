package com.tss.bookstore.service;

import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.PublisherRequestDto;
import com.tss.bookstore.dto.PublisherResponseDto;
import org.springframework.data.domain.Pageable;

public interface PublisherService {
    PublisherResponseDto addPublisher(PublisherRequestDto dto);

    PublisherResponseDto updatePublisher(Long publisherId, PublisherRequestDto dto);

    PublisherResponseDto getPublisherById(Long publisherId);

    PageDto<PublisherResponseDto> getAllPublishers(Pageable pageable);

    void deletePublisher(Long publisherId);
}
