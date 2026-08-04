package com.tss.bookstore.service;

import com.tss.bookstore.dto.BookResponseDto;
import com.tss.bookstore.entity.Book;
import com.tss.bookstore.mapper.AuthorMapper;
import com.tss.bookstore.mapper.BookMapper;
import com.tss.bookstore.repository.AuthorRepository;
import com.tss.bookstore.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.tss.bookstore.dto.AuthorRequestDto;
import com.tss.bookstore.dto.AuthorResponseDto;
import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.entity.Author;
import com.tss.bookstore.exception.DuplicateResourceException;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.mapper.AuthorMapper;
import com.tss.bookstore.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Override
    @Transactional
    public AuthorResponseDto addAuthor(AuthorRequestDto requestDto) {

        log.info("Creating author with name: {}", requestDto.getName());

        if (authorRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new DuplicateResourceException("Author already exists with name : " + requestDto.getName());
        }

        Author author = authorMapper.toEntity(requestDto);
        Author savedAuthor = authorRepository.save(author);
        log.info("Author created successfully. authorId={}, name={}", savedAuthor.getAuthorId(), savedAuthor.getName());
        return authorMapper.toDto(savedAuthor);
    }

    @Override
    @Transactional
    public AuthorResponseDto updateAuthor(Long authorId, AuthorRequestDto requestDto) {

        log.info("Updating author. authorId={}", authorId);
        Author author = authorRepository.findByAuthorIdAndIsActiveTrue(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found with id : " + authorId));

        if (authorRepository.existsByNameIgnoreCaseAndAuthorIdNot(requestDto.getName(), authorId)) {
            throw new DuplicateResourceException("Author already exists with name : " + requestDto.getName());
        }

        author.setName(requestDto.getName());
        author.setCountry(requestDto.getCountry());

        log.info("Author updated successfully. authorId={}", authorId);
        return authorMapper.toDto(author);
    }

    @Override
    public AuthorResponseDto getAuthorById(Long authorId) {
        log.debug("Fetching Author. authorId={}", authorId);
        Author author = authorRepository.findByAuthorIdAndIsActiveTrue(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found with id : " + authorId));

        return authorMapper.toDto(author);
    }

    @Override
    public PageDto<AuthorResponseDto> getAllAuthors(Pageable pageable) {

        log.debug(
                "Fetching Authors. page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Author> authors = authorRepository.findByIsActiveTrue(pageable);

        List<AuthorResponseDto> responseDtos = authors.getContent()
                .stream()
                .map(authorMapper::toDto)
                .toList();

        PageDto<AuthorResponseDto> pageDto = new PageDto<>();

        pageDto.setContent(responseDtos);
        pageDto.setCurrentPage(authors.getNumber());
        pageDto.setPageSize(authors.getSize());
        pageDto.setTotalPages(authors.getTotalPages());
        pageDto.setTotalElements(authors.getTotalElements());
        pageDto.setFirst(authors.isFirst());
        pageDto.setLast(authors.isLast());
        pageDto.setEmpty(authors.isEmpty());

        return pageDto;
    }

    @Override
    @Transactional
    public void deleteAuthor(Long authorId) {

        log.info("Soft deleting author. authorId={}", authorId);

        Author author = authorRepository.findByAuthorIdAndIsActiveTrue(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found with id : " + authorId));

        author.setIsActive(false);
        authorRepository.save(author);

        log.info("Author deleted successfully. authorId={}", authorId);
    }

}
