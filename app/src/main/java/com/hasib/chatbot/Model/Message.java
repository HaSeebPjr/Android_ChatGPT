package com.hasib.chatbot.Model;

public class Message {
    public static String MY_MSG = "MY_MSG", BOT_MSG = "BOT_MSG";
    private String sender, message, currentTime;

    public Message(){}
    public Message(String message, String sender, String currentTime){
        this.message = message;
        this.sender = sender;
        this.currentTime = currentTime;
    }

    public String getMessage(){
        return this.message;
    }
    public String getSender(){
        return this.sender;
    }
    public String getCurrentTime(){
        return this.currentTime;
    }


}
