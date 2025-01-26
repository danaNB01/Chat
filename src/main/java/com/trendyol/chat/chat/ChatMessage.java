package com.trendyol.chat.chat;

public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;

    // Private constructor to prevent direct instantiation
    private ChatMessage(String content, String sender, MessageType type) {
        this.content = content;
        this.sender = sender;
        this.type = type;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public MessageType getType() {
        return type;
    }

    // Static Builder class
    public static class Builder {
        private String content;
        private String sender;
        private MessageType type;

        // Setters for Builder class
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder type(MessageType type) {
            this.type = type;
            return this;
        }

        // Build method to create ChatMessage
        public ChatMessage build() {
            return new ChatMessage(content, sender, type);
        }
    }

    // Provide a way to get an instance of the Builder class
    public static Builder builder() {
        return new Builder();
    }
}

