package net.devansh.Muse.service;

import net.devansh.Muse.api.response.GeminiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

@Service
public class SentimentAnalysisService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent}")
    private String geminiApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    private static final String CACHE_PREFIX = "sentiment_";

    public String getSentimentScore(String text) {
        try {
            // Check cache first
            String cacheKey = CACHE_PREFIX + text.hashCode();
            String cachedResult = redisService.get(cacheKey, String.class);
            if (cachedResult != null) {
                return cachedResult;
            }

            // Call Gemini API
            GeminiResponse response = callGeminiAPI(text);
            String analysis = parseGeminiResponse(response);

            // Cache the result
            redisService.set(cacheKey, analysis, 3600L); // Cache for 1 hour

            return analysis;
        } catch (Exception e) {
            // Fallback to basic sentiment analysis
            return "Unable to analyze sentiment at this time. Please try again later.";
        }
    }

    public Map<String, Object> analyzeWeeklySentiment(List<String> journalEntries) {
        try {
            StringBuilder consolidatedText = new StringBuilder();
            consolidatedText.append("Analyze the sentiment of these journal entries from the past week. ")
                          .append("Provide a detailed analysis including:\n")
                          .append("1. Overall sentiment score (0-10, where 10 is most positive)\n")
                          .append("2. Most frequent emotions\n")
                          .append("3. Key themes and patterns\n")
                          .append("4. Insights and recommendations\n")
                          .append("5. Trend analysis\n\n")
                          .append("Journal Entries:\n");

            for (int i = 0; i < journalEntries.size(); i++) {
                consolidatedText.append("Entry ").append(i + 1).append(": ").append(journalEntries.get(i)).append("\n");
            }

            GeminiResponse response = callGeminiAPI(consolidatedText.toString());
            String analysis = parseGeminiResponse(response);

            // Parse the analysis into structured data
            return parseWeeklyAnalysis(analysis);

        } catch (Exception e) {
            return createFallbackAnalysis();
        }
    }

    private GeminiResponse callGeminiAPI(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(geminiApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", text);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", new Map[]{part});

        requestBody.put("contents", new Map[]{content});

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
            geminiApiUrl + "?key=" + geminiApiKey,
            HttpMethod.POST,
            request,
            GeminiResponse.class
        );

        return response.getBody();
    }

    private String parseGeminiResponse(GeminiResponse response) {
        if (response != null && response.getFirstText() != null) {
            return response.getFirstText().trim();
        }
        return "Unable to analyze sentiment";
    }

    private Map<String, Object> parseWeeklyAnalysis(String analysis) {
        Map<String, Object> result = new HashMap<>();
        result.put("detailedAnalysis", analysis);
        result.put("overallScore", 7); // Default score
        result.put("keyInsights", Arrays.asList("Keep up the good work!", "Your journal shows positive growth"));
        return result;
    }

    private Map<String, Object> createFallbackAnalysis() {
        Map<String, Object> result = new HashMap<>();
        result.put("detailedAnalysis", "Unable to perform detailed analysis at this time.");
        result.put("overallScore", 5);
        result.put("keyInsights", Arrays.asList("Continue journaling regularly"));
        return result;
    }
}
