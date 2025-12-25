package com.write.write.repository;

import com.write.write.entity.PracticeBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeBookRepository extends JpaRepository<PracticeBook, Long> {
    List<PracticeBook> findByLibraryIdOrderByIdAsc(Long libraryId);
    List<PracticeBook> findBySetIdOrderByIdAsc(Long setId);
    List<PracticeBook> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);
}
