package com.ali.medicalchat;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime datetime = LocalDateTime.now();
        messageTime = formatter.format(datetime);
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}