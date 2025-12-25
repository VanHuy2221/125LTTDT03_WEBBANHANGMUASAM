package com.example.bc_quanlibanhangonline.models;

public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private Integer exchangeId;
    private String content;
    private String image;
    private String sentAt;

    public Message(int messageId, int senderId, int receiverId,
                   Integer exchangeId, String content,
                   String image, String sentAt) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.exchangeId = exchangeId;
        this.content = content;
        this.image = image;
        this.sentAt = sentAt;
    }

    public int getMessageId() { return messageId; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public Integer getExchangeId() { return exchangeId; }
    public String getContent() { return content; }
    public String getImage() { return image; }
    public String getSentAt() { return sentAt; }

    // Có thể thêm setter nếu cần
    public void setContent(String content) { this.content = content; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }
}
