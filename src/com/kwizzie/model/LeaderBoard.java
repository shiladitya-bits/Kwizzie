package com.kwizzie.model;

import java.util.List;

public class LeaderBoard {
	private List<Player> leaders;
	private String quizRoomCode;
	
	
	public LeaderBoard(List<Player> leaders, String quizRoomCode) {
		super();
		this.leaders = leaders;
		this.quizRoomCode = quizRoomCode;
	}
	public List<Player> getLeaders() {
		return leaders;
	}
	public void setLeaders(List<Player> leaders) {
		this.leaders = leaders;
	}
	public String getQuizRoomCode() {
		return quizRoomCode;
	}
	public void setQuizRoomCode(String quizRoomCode) {
		this.quizRoomCode = quizRoomCode;
	}

}
