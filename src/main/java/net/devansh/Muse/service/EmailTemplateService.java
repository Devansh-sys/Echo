package net.devansh.Muse.service;

import net.devansh.Muse.entity.SentimentReport;
import net.devansh.Muse.enums.Sentiment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailTemplateService {

    public String processWeeklyReportTemplate(SentimentReport report, String userName) {
        String template = loadTemplate();

        // Replace template variables with actual data
        template = template.replace("{{USERNAME}}", userName != null ? userName : "User");
        template = template.replace("{{OVERALL_SCORE}}",
            report.getAverageSentimentScore() != null ?
            String.format("%.1f", report.getAverageSentimentScore()) : "N/A");
        template = template.replace("{{SCORE_INTERPRETATION}}",
            report.getAverageSentimentScore() != null ?
            getScoreInterpretation(report.getAverageSentimentScore()) : "Score not available");

        template = template.replace("{{SENTIMENT_BREAKDOWN}}", generateSentimentBreakdown(report));
        template = template.replace("{{MOST_FREQUENT_SENTIMENT}}", generateMostFrequentSentiment(report));
        template = template.replace("{{DETAILED_ANALYSIS}}",
            report.getDetailedAnalysis() != null ? report.getDetailedAnalysis() : "Analysis not available");
        template = template.replace("{{INSIGHTS_LIST}}", generateInsightsList(report));

        return template;
    }

    private String loadTemplate() {
        // Template for weekly sentiment report email
        StringBuilder template = new StringBuilder();
        template.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Weekly Emotional Wellness Report</title>")
                .append("<style>")
                .append("body {")
                .append("font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;")
                .append("line-height: 1.6;")
                .append("color: #333;")
                .append("max-width: 600px;")
                .append("margin: 0 auto;")
                .append("padding: 20px;")
                .append("background-color: #f8f9fa;")
                .append("}")
                .append(".header {")
                .append("background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);")
                .append("color: white;")
                .append("padding: 30px 20px;")
                .append("text-align: center;")
                .append("border-radius: 10px 10px 0 0;")
                .append("margin-bottom: 0;")
                .append("}")
                .append(".header h1 {")
                .append("margin: 0;")
                .append("font-size: 24px;")
                .append("font-weight: 300;")
                .append("}")
                .append(".header p {")
                .append("margin: 10px 0 0 0;")
                .append("opacity: 0.9;")
                .append("font-size: 16px;")
                .append("}")
                .append(".content {")
                .append("background: white;")
                .append("padding: 30px;")
                .append("border-radius: 0 0 10px 10px;")
                .append("box-shadow: 0 2px 10px rgba(0,0,0,0.1);")
                .append("}")
                .append(".score-section {")
                .append("text-align: center;")
                .append("margin: 20px 0;")
                .append("padding: 20px;")
                .append("background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);")
                .append("border-radius: 10px;")
                .append("}")
                .append(".score {")
                .append("font-size: 36px;")
                .append("font-weight: bold;")
                .append("color: #1565c0;")
                .append("margin: 10px 0;")
                .append("}")
                .append(".sentiment-grid {")
                .append("display: grid;")
                .append("grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));")
                .append("gap: 15px;")
                .append("margin: 20px 0;")
                .append("}")
                .append(".sentiment-item {")
                .append("text-align: center;")
                .append("padding: 15px;")
                .append("border-radius: 8px;")
                .append("color: white;")
                .append("font-weight: bold;")
                .append("}")
                .append(".sentiment-item.happy { background: linear-gradient(135deg, #66bb6a 0%, #4caf50 100%); }")
                .append(".sentiment-item.sad { background: linear-gradient(135deg, #ef5350 0%, #f44336 100%); }")
                .append(".sentiment-item.angry { background: linear-gradient(135deg, #ff7043 0%, #ff5722 100%); }")
                .append(".sentiment-item.anxious { background: linear-gradient(135deg, #ab47bc 0%, #9c27b0 100%); }")
                .append(".insights {")
                .append("background: linear-gradient(135deg, #e8f5e8 0%, #c8e6c9 100%);")
                .append("padding: 20px;")
                .append("border-radius: 8px;")
                .append("margin: 20px 0;")
                .append("border-left: 4px solid #4caf50;")
                .append("}")
                .append(".insights h3 {")
                .append("margin-top: 0;")
                .append("color: #2e7d32;")
                .append("}")
                .append(".footer {")
                .append("text-align: center;")
                .append("margin-top: 30px;")
                .append("padding: 20px;")
                .append("background: #f5f5f5;")
                .append("border-radius: 8px;")
                .append("font-size: 14px;")
                .append("color: #666;")
                .append("}")
                .append(".button {")
                .append("display: inline-block;")
                .append("padding: 12px 24px;")
                .append("background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);")
                .append("color: white;")
                .append("text-decoration: none;")
                .append("border-radius: 6px;")
                .append("font-weight: bold;")
                .append("margin: 10px 5px;")
                .append("}")
                .append(".highlight {")
                .append("background: linear-gradient(135deg, #fff3e0 0%, #ffcc02 20%);")
                .append("padding: 2px 6px;")
                .append("border-radius: 4px;")
                .append("}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class=\"header\">")
                .append("<h1>📊 Your Weekly Wellness Report</h1>")
                .append("<p>Hello {{USERNAME}}! Here's your emotional journey summary</p>")
                .append("</div>")
                .append("<div class=\"content\">")
                .append("<div class=\"score-section\">")
                .append("<h2>Overall Wellness Score</h2>")
                .append("<div class=\"score\">{{OVERALL_SCORE}}/10</div>")
                .append("<p>{{SCORE_INTERPRETATION}}</p>")
                .append("</div>")
                .append("{{SENTIMENT_BREAKDOWN}}")
                .append("{{MOST_FREQUENT_SENTIMENT}}")
                .append("<div class=\"insights\">")
                .append("<h3>🤖 AI Analysis & Insights</h3>")
                .append("<p>{{DETAILED_ANALYSIS}}</p>")
                .append("</div>")
                .append("<div class=\"insights\">")
                .append("<h3>💡 Key Insights</h3>")
                .append("<ul>")
                .append("{{INSIGHTS_LIST}}")
                .append("</ul>")
                .append("</div>")
                .append("<div class=\"footer\">")
                .append("<p><strong>Remember:</strong> This report is generated from your journal entries.")
                .append("Continue journaling regularly for more accurate insights!</p>")
                .append("<p>Questions about your report? <a href=\"mailto:support@journalapp.com\">Contact Support</a></p>")
                .append("<p style=\"font-size: 12px; margin-top: 15px; color: #888;\">")
                .append("This is an automated report. Please do not reply to this email.")
                .append("</p>")
                .append("</div>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return template.toString();
    }

    private String generateSentimentBreakdown(SentimentReport report) {
        if (report.getSentimentCounts() == null || report.getSentimentCounts().isEmpty()) {
            return "<p><em>No sentiment data available for this week.</em></p>";
        }

        int totalEntries = report.getSentimentCounts().values().stream().mapToInt(Integer::intValue).sum();
        StringBuilder html = new StringBuilder();

        html.append("<h3>Emotional Breakdown</h3>");
        html.append("<div class=\"sentiment-grid\">");

        for (Map.Entry<Sentiment, Integer> entry : report.getSentimentCounts().entrySet()) {
            double percentage = (entry.getValue() * 100.0) / totalEntries;
            html.append(String.format(
                "<div class=\"sentiment-item %s\">" +
                "<div style=\"font-size: 18px;\">%s</div>" +
                "<div>%d entries</div>" +
                "<div>%.1f%%</div>" +
                "</div>",
                entry.getKey().toString().toLowerCase(),
                entry.getKey().toString(),
                entry.getValue(),
                percentage
            ));
        }

        html.append("</div>");
        return html.toString();
    }

    private String generateMostFrequentSentiment(SentimentReport report) {
        if (report.getMostFrequentSentiment() == null) {
            return "";
        }

        return String.format(
            "<div style=\"background: linear-gradient(135deg, #fff3e0 0%, #ffcc02 20%); padding: 15px; border-radius: 8px; margin: 20px 0; text-align: center;\">" +
            "<h3>🏆 Most Frequent Emotion</h3>" +
            "<p style=\"font-size: 20px; font-weight: bold; margin: 10px 0;\">%s</p>" +
            "<p>was your dominant emotional state this week</p>" +
            "</div>",
            report.getMostFrequentSentiment().toString()
        );
    }

    private String generateInsightsList(SentimentReport report) {
        if (report.getKeyInsights() == null || report.getKeyInsights().isEmpty()) {
            return "<li>No specific insights available for this week</li>";
        }

        StringBuilder html = new StringBuilder();
        String[] insights = report.getKeyInsights().split(";");

        for (String insight : insights) {
            if (!insight.trim().isEmpty()) {
                html.append("<li>").append(insight.trim()).append("</li>");
            }
        }

        return html.toString();
    }

    private String getScoreInterpretation(double score) {
        if (score >= 8.5) return "Excellent! Your week shows very positive emotional well-being.";
        if (score >= 7.0) return "Good! You're maintaining a positive emotional state.";
        if (score >= 5.5) return "Moderate. There's room for improvement in your emotional wellness.";
        if (score >= 4.0) return "Low. Consider focusing on activities that boost your mood.";
        return "Very Low. Please consider reaching out to a trusted friend or professional for support.";
    }
}
