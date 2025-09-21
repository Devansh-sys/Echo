package net.devansh.Muse.repository;

import net.devansh.Muse.entity.SentimentReport;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SentimentReportRepository extends MongoRepository<SentimentReport, ObjectId> {

    List<SentimentReport> findByUserId(String userId);

    List<SentimentReport> findByUserIdAndWeekStartBetween(String userId, LocalDateTime start, LocalDateTime end);

    SentimentReport findByUserIdAndWeekStartAndWeekEnd(String userId, LocalDateTime weekStart, LocalDateTime weekEnd);
}
