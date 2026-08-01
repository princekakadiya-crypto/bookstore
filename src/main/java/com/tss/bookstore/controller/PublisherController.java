package com.tss.bookstore.controller;

import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.PublisherRequestDto;
import com.tss.bookstore.dto.PublisherResponseDto;
import com.tss.bookstore.service.PublisherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/publishers")
@AllArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponseDto> addPublisher(@Valid @RequestBody PublisherRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(publisherService.addPublisher(requestDto));
    }

    @PutMapping("/{publisherId}")
    public ResponseEntity<PublisherResponseDto> updatePublisher(@PathVariable Long publisherId, @Valid @RequestBody PublisherRequestDto requestDto) {

        return ResponseEntity.ok(publisherService.updatePublisher(publisherId, requestDto));
    }

    @GetMapping("/{publisherId}")
    public ResponseEntity<PublisherResponseDto> getPublisher(@PathVariable Long publisherId) {

        return ResponseEntity.ok(publisherService.getPublisherById(publisherId));
    }

    @GetMapping
    public ResponseEntity<PageDto<PublisherResponseDto>> getPublishers(Pageable pageable) {

        return ResponseEntity.ok(publisherService.getAllPublishers(pageable));
    }

    @DeleteMapping("/{publisherId}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long publisherId) {

        publisherService.deletePublisher(publisherId);

        return ResponseEntity.noContent().build();
    }
}
