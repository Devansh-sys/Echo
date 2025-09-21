package net.devansh.Muse.entity;

import lombok.*;
import net.devansh.Muse.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Document(collection = "sentiment_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentReport {
    @Id
    private ObjectId id;

    @NonNull
    private String userId;

    @NonNull
    private LocalDateTime weekStart;

    @NonNull
    private LocalDateTime weekEnd;

    private Map<Sentiment, Integer> sentimentCounts;

    private Sentiment mostFrequentSentiment;

    private Double averageSentimentScore;

    private String detailedAnalysis;

    private String keyInsights;

    private LocalDateTime createdAt;

    public SentimentReport(String userId, LocalDateTime weekStart, LocalDateTime weekEnd) {
        this.userId = userId;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.createdAt = LocalDateTime.now();
    }
}
