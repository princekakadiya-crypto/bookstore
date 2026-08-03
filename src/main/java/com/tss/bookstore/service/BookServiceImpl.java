package com.tss.bookstore.service;

import com.tss.bookstore.dto.BookRequestDto;
import com.tss.bookstore.dto.BookResponseDto;
import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.StockRequestDto;
import com.tss.bookstore.entity.Author;
import com.tss.bookstore.entity.Book;
import com.tss.bookstore.entity.Category;
import com.tss.bookstore.entity.Publisher;
import com.tss.bookstore.exception.DuplicateResourceException;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.mapper.BookMapper;
import com.tss.bookstore.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public BookResponseDto addBook(BookRequestDto requestDto) {

        if(bookRepository.existsByTitleIgnoreCase(requestDto.getTitle())){
            throw new DuplicateResourceException("Book already exists with title : " + requestDto.getTitle());
        }

        Book book = bookMapper.toEntity(requestDto);

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(requestDto.getAuthorIds()));

        if(authors.size()!=requestDto.getAuthorIds().size()){
            throw new NotFoundException("One or more authors not found");
        }

        Publisher publisher = publisherRepository.findByPublisherIdAndIsActiveTrue(requestDto.getPublisherId())
                        .orElseThrow(()->new NotFoundException("Publisher not found"));

        Category category = categoryRepository.findByCategoryIdAndIsActiveTrue(requestDto.getCategoryId())
                        .orElseThrow(()->new NotFoundException("Category not found"));

        book.setAuthors(authors);
        book.setPublisher(publisher);
        book.setCategory(category);
        Book savedBook = bookRepository.save(book);

        return convertToResponse(savedBook);
    }

    @Override
    @Transactional
    public BookResponseDto updateBook(Long bookId, BookRequestDto requestDto) {

        Book book = bookRepository.findByBookIdAndIsActiveTrue(bookId).orElseThrow(
                        ()->new NotFoundException("Book not found with id : " + bookId));

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(requestDto.getAuthorIds()));

        Publisher publisher = publisherRepository.findByPublisherIdAndIsActiveTrue(requestDto.getPublisherId())
                .orElseThrow(
                        ()->new NotFoundException("Publisher not found"));

        Category category = categoryRepository.findByCategoryIdAndIsActiveTrue(requestDto.getCategoryId())
                .orElseThrow(
                        ()->new NotFoundException("Category not found"));

        book.setTitle(requestDto.getTitle());
        book.setPrice(requestDto.getPrice());
        book.setStock(requestDto.getStock());
        book.setAuthors(authors);
        book.setPublisher(publisher);
        book.setCategory(category);

        return convertToResponse(book);
    }

    @Override
    public BookResponseDto getBookById(Long bookId) {
        Book book = bookRepository.findByBookIdAndIsActiveTrue(bookId)
                        .orElseThrow(()->new NotFoundException("Book not found"));

        return convertToResponse(book);
    }

    @Override
    public PageDto<BookResponseDto> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findByIsActiveTrue(pageable);

        List<BookResponseDto> responseDtos = new ArrayList<>();
        for(Book book : books.getContent()){
            responseDtos.add(convertToResponse(book));
        }

        PageDto<BookResponseDto> pageDto = new PageDto<>();
        pageDto.setContent(responseDtos);
        pageDto.setCurrentPage(books.getNumber());
        pageDto.setPageSize(books.getSize());
        pageDto.setTotalPages(books.getTotalPages());
        pageDto.setTotalElements(books.getTotalElements());
        pageDto.setFirst(books.isFirst());
        pageDto.setLast(books.isLast());
        pageDto.setEmpty(books.isEmpty());

        return pageDto;
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findByBookIdAndIsActiveTrue(bookId)
                        .orElseThrow(()->new NotFoundException("Book not found"));

        book.setIsActive(false);
    }

    @Override
    public PageDto<BookResponseDto> getBooksBuAuthorId(Long authorId, Pageable pageable) {
        authorRepository.findByAuthorIdAndIsActiveTrue(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found with id : " + authorId));

        Page<Book> books = bookRepository.findByIsActiveTrue(pageable);

        List<BookResponseDto> responseDtos = new ArrayList<>();
        for(Book book : books.getContent()){
            responseDtos.add(convertToResponse(book));
        }

        PageDto<BookResponseDto> pageDto = new PageDto<>();
        pageDto.setContent(responseDtos);
        pageDto.setCurrentPage(books.getNumber());
        pageDto.setPageSize(books.getSize());
        pageDto.setTotalPages(books.getTotalPages());
        pageDto.setTotalElements(books.getTotalElements());
        pageDto.setFirst(books.isFirst());
        pageDto.setLast(books.isLast());
        pageDto.setEmpty(books.isEmpty());

        return pageDto;
    }

    @Override
    @Transactional
    public void updateStock(Long bookId, StockRequestDto stockRequestDto) {

        Book book = bookRepository.findByBookIdAndIsActiveTrue(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id : " + bookId));

        book.setStock(stockRequestDto.getStock());

        Book updatedBook = bookRepository.save(book);
    }

    @Override
    public Double getAverageRating(Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException("Book not found with id : " + bookId);
        }

        return reviewRepository.getAverageRating(bookId);
    }

    private BookResponseDto convertToResponse(Book book){
        BookResponseDto dto = bookMapper.toDto(book);

        Set<String> authorNames = new HashSet<>();
        for(Author author : book.getAuthors()){
            authorNames.add(author.getName());
        }
        dto.setAuthorNames(authorNames);
        dto.setPublisherName(
                book.getPublisher().getName()
        );
        dto.setCategoryName(
                book.getCategory().getName()
        );
        return dto;
    }
}
