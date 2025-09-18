package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    private String inputType;

    @Column(columnDefinition = "TEXT")
    private String inputContent;

    @Column(columnDefinition = "LONGTEXT")
    private String aiResponse;

    @CreationTimestamp
    private LocalDateTime createdAt;
}