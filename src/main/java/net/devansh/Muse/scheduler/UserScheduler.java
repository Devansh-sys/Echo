package net.devansh.Muse.scheduler;

import net.devansh.Muse.entity.JournalEntry;
import net.devansh.Muse.entity.SentimentReport;
import net.devansh.Muse.entity.User;
import net.devansh.Muse.enums.Sentiment;
import net.devansh.Muse.repository.SentimentReportRepository;
import net.devansh.Muse.repository.UserRepositoryImpl;
import net.devansh.Muse.service.EmailService;
import net.devansh.Muse.service.SentimentAnalysisService;
import net.devansh.Muse.service.EmailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private SentimentReportRepository sentimentReportRepository;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail() {
        List<User> users = userRepository.getUserForSA();

        for (User user : users) {
            try {
                // Get the date range for the past week (Sunday to Saturday)
                LocalDateTime weekEnd = LocalDateTime.now().with(java.time.DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59);
                LocalDateTime weekStart = weekEnd.minus(6, ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

                // Get journal entries from the past week
                List<JournalEntry> journalEntries = user.getJournalEntries();

                // Filter entries from the past week
                List<JournalEntry> weeklyEntries = journalEntries.stream()
                    .filter(entry -> entry.getDate().isAfter(weekStart) && entry.getDate().isBefore(weekEnd))
                    .collect(Collectors.toList());

                // Check if user has entries for this week
                if (weeklyEntries.isEmpty()) {
                    continue; // Skip users with no entries this week
                }

                // Calculate sentiment counts
                Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
                for (JournalEntry entry : weeklyEntries) {
                    if (entry.getSentiment() != null) {
                        sentimentCounts.put(entry.getSentiment(),
                            sentimentCounts.getOrDefault(entry.getSentiment(), 0) + 1);
                    }
                }

                // Find most frequent sentiment
                Sentiment mostFrequentSentiment = null;
                int maxCount = 0;
                for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        mostFrequentSentiment = entry.getKey();
                    }
                }

                // Extract journal content for AI analysis
                List<String> journalContents = weeklyEntries.stream()
                    .map(entry -> entry.getContent() != null ? entry.getContent() : "")
                    .collect(Collectors.toList());

                // Get AI-powered analysis
                Map<String, Object> aiAnalysis = sentimentAnalysisService.analyzeWeeklySentiment(journalContents);

                // Create comprehensive report
                SentimentReport report = new SentimentReport(
                    user.getId().toString(),
                    weekStart,
                    weekEnd
                );
                report.setSentimentCounts(sentimentCounts);
                report.setMostFrequentSentiment(mostFrequentSentiment);
                report.setAverageSentimentScore((Double) aiAnalysis.get("overallScore"));
                report.setDetailedAnalysis((String) aiAnalysis.get("detailedAnalysis"));
                report.setKeyInsights(String.join("; ", (List<String>) aiAnalysis.get("keyInsights")));

                // Save the report
                sentimentReportRepository.save(report);

                // Send comprehensive HTML email report
                String htmlReport = emailTemplateService.processWeeklyReportTemplate(report, user.getUsername());
                emailService.sendEmail(
                    user.getEmail(),
                    "Your Weekly Emotional Wellness Report",
                    htmlReport
                );

            } catch (Exception e) {
                // Fallback to basic email in case of errors
                System.err.println("Error generating report for user: " + user.getUsername() + ", " + e.getMessage());
                try {
                    List<Sentiment> sentiments = user.getJournalEntries().stream()
                        .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                        .map(x -> x.getSentiment())
                        .collect(Collectors.toList());

                    if (!sentiments.isEmpty()) {
                        Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
                        for (Sentiment sentiment : sentiments) {
                            if (sentiment != null)
                                sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                        }

                        Sentiment mostFrequentSentiment = null;
                        int maxCount = 0;
                        for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                            if (entry.getValue() > maxCount) {
                                maxCount = entry.getValue();
                                mostFrequentSentiment = entry.getKey();
                            }
                        }

                        if (mostFrequentSentiment != null) {
                            emailService.sendEmail(
                                user.getEmail(),
                                "Sentiment Analysis Report",
                                "Your most frequent sentiment this week was: " + mostFrequentSentiment.toString()
                            );
                        }
                    }
                } catch (Exception fallbackError) {
                    System.err.println("Fallback email also failed for user: " + user.getUsername());
                }
            }
        }
    }
}
