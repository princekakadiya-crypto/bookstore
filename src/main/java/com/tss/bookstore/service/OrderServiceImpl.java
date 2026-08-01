package com.tss.bookstore.service;

import com.tss.bookstore.dto.OrderItemRequestDto;
import com.tss.bookstore.dto.OrderRequestDto;
import com.tss.bookstore.dto.OrderResponseDto;
import com.tss.bookstore.entity.Book;
import com.tss.bookstore.entity.Order;
import com.tss.bookstore.entity.OrderItem;
import com.tss.bookstore.entity.User;
import com.tss.bookstore.enums.OrderStatus;
import com.tss.bookstore.exception.InsufficientStockException;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.mapper.OrderMapper;
import com.tss.bookstore.repository.BookRepository;
import com.tss.bookstore.repository.OrderRepository;
import com.tss.bookstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {

        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found with id : " + requestDto.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PLACED);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequestDto itemDto : requestDto.getItems()) {

            Book book = bookRepository.findById(itemDto.getBookId()).orElseThrow(
                    () -> new NotFoundException("Book not found with id : " + itemDto.getBookId()));

            if (book.getStock() < itemDto.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for book : " + book.getTitle());
            }

            book.setStock(book.getStock() - itemDto.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(book.getPrice());
            orderItems.add(orderItem);
            totalAmount += book.getPrice() * itemDto.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.entityToDto(savedOrder);
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id : " + orderId));

        return orderMapper.entityToDto(order);
    }

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {

        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::entityToDto);
    }

    @Override
    public List<OrderResponseDto> getOrdersByUser(Long userId) {

        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found with id : " + userId));

        return orderMapper.entityToDto(orderRepository.findByUserUserId(userId));
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException("Order not found with id : " + orderId));

        order.setStatus(status);

        return orderMapper.entityToDto(orderRepository.save(order));
    }

    @Override
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id : " + orderId));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled.");
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException(
                    "Completed order cannot be cancelled.");
        }

        for (OrderItem item : order.getOrderItems()) {
            Book book = item.getBook();
            book.setStock(
                    book.getStock() + item.getQuantity()
            );
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

}
