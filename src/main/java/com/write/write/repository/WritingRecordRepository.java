package com.write.write.repository;

import com.write.write.entity.WritingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingRecordRepository extends JpaRepository<WritingRecord, Long> {
}
