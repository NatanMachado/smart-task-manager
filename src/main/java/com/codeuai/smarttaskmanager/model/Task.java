package com.codeuai.smarttaskmanager.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private boolean completed;

    private boolean archived;

    private LocalDateTime createdAt;

    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
