package net.devansh.Muse.service;

import net.devansh.Muse.entity.SentimentReport;
import net.devansh.Muse.enums.Sentiment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class SentimentReportFormatter {

    public String formatWeeklyReport(SentimentReport report, String userName) {
        StringBuilder htmlReport = new StringBuilder();

        htmlReport.append("<!DOCTYPE html>")
                  .append("<html><head>")
                  .append("<style>")
                  .append("body { font-family: Arial, sans-serif; margin: 20px; }")
                  .append(".header { color: #333; text-align: center; }")
                  .append(".sentiment-score { font-size: 24px; font-weight: bold; color: #2e7d32; }")
                  .append(".section { margin: 20px 0; padding: 15px; border-left: 4px solid #2196f3; background-color: #f5f5f5; }")
                  .append(".insights { background-color: #e8f5e8; padding: 15px; border-radius: 5px; }")
                  .append(".sentiment-breakdown { display: flex; justify-content: space-around; margin: 20px 0; }")
                  .append(".sentiment-item { text-align: center; padding: 10px; border-radius: 5px; }")
                  .append(".happy { background-color: #c8e6c9; }")
                  .append(".sad { background-color: #ffcdd2; }")
                  .append(".angry { background-color: #ffccbc; }")
                  .append(".anxious { background-color: #d1c4e9; }")
                  .append("</style>")
                  .append("</head><body>");

        htmlReport.append("<div class='header'>")
                  .append("<h1>Weekly Sentiment Report</h1>")
                  .append("<p>Hello ").append(userName).append("! Here's your emotional wellness summary for the past week.</p>")
                  .append("</div>");

        // Overall Score Section
        if (report.getAverageSentimentScore() != null) {
            htmlReport.append("<div class='section'>")
                      .append("<h2>Overall Wellness Score</h2>")
                      .append("<div class='sentiment-score'>")
                      .append(String.format("%.1f", report.getAverageSentimentScore()))
                      .append("/10</div>")
                      .append("<p>").append(getScoreInterpretation(report.getAverageSentimentScore())).append("</p>")
                      .append("</div>");
        }

        // Sentiment Breakdown
        if (report.getSentimentCounts() != null && !report.getSentimentCounts().isEmpty()) {
            htmlReport.append("<div class='section'>")
                      .append("<h2>Emotional Breakdown</h2>")
                      .append("<div class='sentiment-breakdown'>");

            int totalEntries = report.getSentimentCounts().values().stream().mapToInt(Integer::intValue).sum();

            for (Map.Entry<Sentiment, Integer> entry : report.getSentimentCounts().entrySet()) {
                double percentage = (entry.getValue() * 100.0) / totalEntries;
                htmlReport.append("<div class='sentiment-item ")
                          .append(entry.getKey().toString().toLowerCase())
                          .append("'>")
                          .append("<h3>").append(entry.getKey()).append("</h3>")
                          .append("<p>").append(entry.getValue()).append(" entries (")
                          .append(String.format("%.1f", percentage)).append("%)</p>")
                          .append("</div>");
            }

            htmlReport.append("</div></div>");
        }

        // Most Frequent Sentiment
        if (report.getMostFrequentSentiment() != null) {
            htmlReport.append("<div class='section'>")
                      .append("<h2>Dominant Emotion</h2>")
                      .append("<p><strong>").append(report.getMostFrequentSentiment())
                      .append("</strong> was your most frequent emotional state this week.</p>")
                      .append("</div>");
        }

        // Detailed Analysis
        if (report.getDetailedAnalysis() != null && !report.getDetailedAnalysis().isEmpty()) {
            htmlReport.append("<div class='section insights'>")
                      .append("<h2>AI Analysis & Insights</h2>")
                      .append("<p>").append(formatDetailedAnalysis(report.getDetailedAnalysis())).append("</p>")
                      .append("</div>");
        }

        // Key Insights
        if (report.getKeyInsights() != null && !report.getKeyInsights().isEmpty()) {
            htmlReport.append("<div class='section insights'>")
                      .append("<h2>Key Insights</h2>")
                      .append("<ul>");
            for (String insight : report.getKeyInsights().split(";")) {
                if (!insight.trim().isEmpty()) {
                    htmlReport.append("<li>").append(insight.trim()).append("</li>");
                }
            }
            htmlReport.append("</ul></div>");
        }

        htmlReport.append("<div class='section' style='text-align: center; background-color: #e3f2fd;'>")
                  .append("<p><em>Remember: This report is generated based on your journal entries. Continue journaling regularly to get more accurate insights into your emotional well-being.</em></p>")
                  .append("</div>");

        htmlReport.append("</body></html>");

        return htmlReport.toString();
    }

    private String getScoreInterpretation(double score) {
        if (score >= 8.5) return "Excellent! Your week shows very positive emotional well-being.";
        if (score >= 7.0) return "Good! You're maintaining a positive emotional state.";
        if (score >= 5.5) return "Moderate. There's room for improvement in your emotional wellness.";
        if (score >= 4.0) return "Low. Consider focusing on activities that boost your mood.";
        return "Very Low. Please consider reaching out to a trusted friend or professional for support.";
    }

    private String formatDetailedAnalysis(String analysis) {
        // Basic formatting for the AI analysis text
        return analysis.replace("\n", "<br>").replace("**", "<strong>").replace("**", "</strong>");
    }

    public String formatWeeklyReportText(SentimentReport report, String userName) {
        StringBuilder textReport = new StringBuilder();

        textReport.append("WEEKLY SENTIMENT REPORT\n")
                  .append("======================\n\n")
                  .append("Hello ").append(userName).append("!\n\n")
                  .append("Here's your emotional wellness summary for the past week:\n\n");

        // Overall Score
        if (report.getAverageSentimentScore() != null) {
            textReport.append("OVERALL WELLNESS SCORE: ")
                      .append(String.format("%.1f", report.getAverageSentimentScore()))
                      .append("/10\n")
                      .append(getScoreInterpretation(report.getAverageSentimentScore()))
                      .append("\n\n");
        }

        // Sentiment Breakdown
        if (report.getSentimentCounts() != null && !report.getSentimentCounts().isEmpty()) {
            textReport.append("EMOTIONAL BREAKDOWN:\n");
            int totalEntries = report.getSentimentCounts().values().stream().mapToInt(Integer::intValue).sum();

            for (Map.Entry<Sentiment, Integer> entry : report.getSentimentCounts().entrySet()) {
                double percentage = (entry.getValue() * 100.0) / totalEntries;
                textReport.append("- ").append(entry.getKey())
                          .append(": ").append(entry.getValue())
                          .append(" entries (").append(String.format("%.1f", percentage)).append("%)\n");
            }
            textReport.append("\n");
        }

        // Most Frequent Sentiment
        if (report.getMostFrequentSentiment() != null) {
            textReport.append("DOMINANT EMOTION: ").append(report.getMostFrequentSentiment())
                      .append(" was your most frequent emotional state this week.\n\n");
        }

        // Detailed Analysis
        if (report.getDetailedAnalysis() != null && !report.getDetailedAnalysis().isEmpty()) {
            textReport.append("AI ANALYSIS & INSIGHTS:\n")
                      .append(report.getDetailedAnalysis())
                      .append("\n\n");
        }

        textReport.append("Keep journaling regularly to continue tracking your emotional well-being!\n\n")
                  .append("Best regards,\n")
                  .append("Your Journal App Team");

        return textReport.toString();
    }
}
