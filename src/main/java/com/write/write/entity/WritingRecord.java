package com.write.write.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "writing_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WritingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    private String inputType;

    @Column(columnDefinition = "TEXT")
    private String inputContent;

    @Column(columnDefinition = "LONGTEXT")
    private String aiResponse;

    private LocalDateTime createdAt = LocalDateTime.now();
}
