package com.write.write.repository;

import com.write.write.entity.WritingRecord;
import com.write.write.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WritingRecordRepository extends JpaRepository<WritingRecord, Long> {
    List<WritingRecord> findByUserOrderByCreatedAtDesc(UserAccount user);
}
