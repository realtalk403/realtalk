package com.example.realtalk;

import java.util.Date;

public class Message {
	String message;
	User user;
	Date dateTimestamp;
	
	public Message(String message, User user, Date dateTimestamp) {
		this.message = message;
		this.user = user;
		this.dateTimestamp = dateTimestamp;
	}
}
