package com.tss.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfile {
    @Column(name = "user_profile_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String phone;
    @Column
    private String address;
    @Column
    private LocalDate dateOfBirth;
    @Column
    private String avatar;

    @OneToOne(mappedBy = "userProfile")
    private User user;
}
