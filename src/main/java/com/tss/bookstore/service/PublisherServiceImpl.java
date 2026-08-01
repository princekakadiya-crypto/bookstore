package com.tss.bookstore.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.PublisherRequestDto;
import com.tss.bookstore.dto.PublisherResponseDto;
import com.tss.bookstore.entity.Publisher;
import com.tss.bookstore.exception.DuplicateResourceException;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.mapper.PublisherMapper;
import com.tss.bookstore.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PublisherServiceImpl implements PublisherService{

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    @Override
    @Transactional
    public PublisherResponseDto addPublisher(PublisherRequestDto requestDto) {

        if (publisherRepository.existsByEmailIgnoreCase(requestDto.getEmail())) {
            throw new DuplicateResourceException("Publisher already exists with email : " + requestDto.getEmail());
        }

        Publisher publisher = publisherMapper.toEntity(requestDto);
        Publisher savedPublisher = publisherRepository.save(publisher);
        return publisherMapper.toDto(savedPublisher);
    }

    @Override
    @Transactional
    public PublisherResponseDto updatePublisher(Long publisherId, PublisherRequestDto requestDto) {

        Publisher publisher = publisherRepository.findByPublisherIdAndIsActiveTrue(publisherId)
                .orElseThrow(() -> new NotFoundException("Publisher not found with id : " + publisherId));

        if (publisherRepository.existsByEmailIgnoreCaseAndPublisherIdNot(requestDto.getEmail(), publisherId)) {
            throw new DuplicateResourceException("Publisher already exists with email : " + requestDto.getEmail());
        }

        publisher.setName(requestDto.getName());
        publisher.setEmail(requestDto.getEmail());
        publisher.setPhone(requestDto.getPhone());
        publisher.setAddress(requestDto.getAddress());

        publisherRepository.save(publisher);
        return publisherMapper.toDto(publisher);
    }

    @Override
    public PublisherResponseDto getPublisherById(Long publisherId) {

        Publisher publisher = publisherRepository.findByPublisherIdAndIsActiveTrue(publisherId)
                .orElseThrow(() -> new NotFoundException("Publisher not found with id : " + publisherId));

        return publisherMapper.toDto(publisher);
    }

    @Override
    public PageDto<PublisherResponseDto> getAllPublishers(Pageable pageable) {

        Page<Publisher> publishers = publisherRepository.findByIsActiveTrue(pageable);

        List<PublisherResponseDto> responseDtos = new ArrayList<>();

        for (Publisher publisher : publishers.getContent()) {
            PublisherResponseDto dto = publisherMapper.toDto(publisher);
            responseDtos.add(dto);
        }

        PageDto<PublisherResponseDto> pageDto = new PageDto<>();

        pageDto.setContent(responseDtos);
        pageDto.setCurrentPage(publishers.getNumber());
        pageDto.setPageSize(publishers.getSize());
        pageDto.setTotalPages(publishers.getTotalPages());
        pageDto.setTotalElements(publishers.getTotalElements());
        pageDto.setFirst(publishers.isFirst());
        pageDto.setLast(publishers.isLast());
        pageDto.setEmpty(publishers.isEmpty());

        return pageDto;
    }

    @Override
    @Transactional
    public void deletePublisher(Long publisherId) {

        Publisher publisher = publisherRepository.findByPublisherIdAndIsActiveTrue(publisherId)
                .orElseThrow(() -> new NotFoundException("Publisher not found with id : " + publisherId));

        publisher.setIsActive(false);
        publisherRepository.save(publisher);
    }

}
