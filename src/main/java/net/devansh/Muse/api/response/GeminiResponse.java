package net.devansh.Muse.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeminiResponse {
    @JsonProperty("candidates")
    private Candidate[] candidates;

    public Candidate[] getCandidates() {
        return candidates;
    }

    public void setCandidates(Candidate[] candidates) {
        this.candidates = candidates;
    }

    public static class Candidate {
        @JsonProperty("content")
        private Content content;

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }
    }

    public static class Content {
        @JsonProperty("parts")
        private Part[] parts;

        public Part[] getParts() {
            return parts;
        }

        public void setParts(Part[] parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        @JsonProperty("text")
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    // Helper method to get the first text response
    public String getFirstText() {
        if (candidates != null && candidates.length > 0 &&
            candidates[0].getContent() != null &&
            candidates[0].getContent().getParts() != null &&
            candidates[0].getContent().getParts().length > 0) {
            return candidates[0].getContent().getParts()[0].getText();
        }
        return null;
    }
}
