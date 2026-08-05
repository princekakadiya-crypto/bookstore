package com.tss.bookstore.service;

import com.tss.bookstore.dto.*;
import com.tss.bookstore.entity.Author;
import com.tss.bookstore.entity.Book;
import com.tss.bookstore.entity.Category;
import com.tss.bookstore.entity.Publisher;
import com.tss.bookstore.exception.DuplicateResourceException;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.mapper.BookMapper;
import com.tss.bookstore.repository.*;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    private static final Logger log= LoggerFactory.getLogger(BookServiceImpl.class);

    @Override
    @Transactional
    public BookResponseDto addBook(BookRequestDto requestDto) {

        log.info("Creating book with title: {}", requestDto.getTitle());

        if(bookRepository.existsByTitleIgnoreCase(requestDto.getTitle())){
            throw new DuplicateResourceException("Book already exists with title : " + requestDto.getTitle());
        }

        if(bookRepository.existsByISBN(requestDto.getISBN())){
            throw new DuplicateResourceException("Book already exists with ISBN : " + requestDto.getISBN());
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

        log.info(
                "Book created successfully. bookId={}, title={}",
                savedBook.getBookId(),
                savedBook.getTitle()
        );

        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public BookResponseDto updateBook(Long bookId, BookRequestDto requestDto) {

        log.info("Updating book. bookId={}", bookId);

        Book book = bookRepository.findByBookIdAndIsActiveTrue(bookId).orElseThrow(
                        ()->new NotFoundException("Book not found with id : " + bookId));

        if(bookRepository.existsByISBNAndBookIdNot(requestDto.getISBN(),bookId)){
            throw new DuplicateResourceException("Book already exists with ISBN : " + requestDto.getISBN());
        }

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

        log.info("Book updated successfully. bookId={}", book.getBookId());

        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto getBookById(Long bookId) {
        log.debug("Fetching book. bookId={}", bookId);
        Book book = bookRepository.findByBookIdAndIsActiveTrue(bookId)
                        .orElseThrow(()->new NotFoundException("Book not found"));

        return bookMapper.toDto(book);
    }

    @Override
    public PageDto<BookDetailsResponseDto> getAllBooksDetails(String title, Long categoryId,String category,Long authorId,String author, Double minPrice, Double maxPrice,Boolean inStock,Pageable pageable) {

        log.debug(
                "Fetching books. page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Maximum price must be greater than or equal to minimum price.");
        }

        Page<BookDetailsResponseDto> books = bookRepository.searchBooksDetails(title, categoryId,category,authorId,author, minPrice, maxPrice,inStock, pageable);

        PageDto<BookDetailsResponseDto> pageDto = new PageDto<>();
        pageDto.setContent(books.getContent());
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
    public PageDto<BookResponseDto> getAllBooks(String title, Long categoryId,String category,Long authorId,String author, Double minPrice, Double maxPrice,Boolean inStock,Pageable pageable) {

        log.debug(
                "Fetching books. page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Maximum price must be greater than or equal to minimum price.");
        }

        Page<BookResponseDto> books = bookRepository.searchBooks(title, categoryId,category,authorId,author, minPrice, maxPrice,inStock, pageable);

        PageDto<BookResponseDto> pageDto = new PageDto<>();
        pageDto.setContent(books.getContent());
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

        log.info("Soft deleting book. bookId={}", bookId);

        Book book = bookRepository.findByBookIdAndIsActiveTrue(bookId)
                        .orElseThrow(()->new NotFoundException("Book not found"));

        log.info("Book deleted successfully. bookId={}", bookId);
        book.setIsActive(false);
    }

    @Override
    public PageDto<BookResponseDto> getBooksBuAuthorId(Long authorId, Pageable pageable) {
        log.debug("Fetching books for author. authorId={}", authorId);
        authorRepository.findByAuthorIdAndIsActiveTrue(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found with id : " + authorId));

        Page<Book> books = bookRepository.findByIsActiveTrue(pageable);

        List<BookResponseDto> responseDtos = new ArrayList<>();
        for(Book book : books.getContent()){
            responseDtos.add(bookMapper.toDto(book));
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

        log.info(
                "Updating stock. bookId={}, quantity={}",
                bookId,
                stockRequestDto.getStock()
        );

        Book book = bookRepository.findByBookIdAndIsActiveTrue(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id : " + bookId));

        book.setStock(stockRequestDto.getStock());

        log.info(
                "Stock updated successfully. bookId={}, currentStock={}",
                book.getBookId(),
                book.getStock()
        );

        bookRepository.save(book);
    }

    @Override
    public Double getAverageRating(Long bookId) {

        log.debug("Calculating average rating for book. bookId={}", bookId);

        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException("Book not found with id : " + bookId);
        }

        return reviewRepository.getAverageRating(bookId);
    }

}
