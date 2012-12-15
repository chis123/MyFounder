package com.founder.e5.sys.note;

import java.sql.Timestamp;

/**
 * ¹«¸æbean
 * @author sun.xf
 */
public class Note {
	private static boolean privyAllowed = true;

	protected int noteID;

	protected int noteType;

	protected String sender;

	protected Timestamp sendTime;

	protected String topic;

	protected String content;

	protected boolean read=true;

	protected String receivers;

	public static boolean getPrivyAllowed() {
		return privyAllowed;
	}

	public static void setPrivyAllowed(boolean allowed) {
		privyAllowed = allowed;
	}

	public int getnoteID() {
		return noteID;
	}

	public void setNoteID(int NoteId) {
		this.noteID = NoteId;
	}

	public int getNoteType() {
		return noteType;
	}
	
	public void setNoteType(int type) {
		this.noteType=type;
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean isRead) {
		this.read = isRead;

	}

	public void setReceivers(String Receivers) {
		this.receivers = Receivers;

	}

	public String getReceivers() {
		return receivers;

	}
}
