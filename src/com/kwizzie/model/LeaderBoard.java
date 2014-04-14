package com.kwizzie.model;

import java.util.List;

public class LeaderBoard {
	private List<Leader> leaders;
	private String quizRoomCode;
	
	public LeaderBoard(){}
	
	public LeaderBoard(List<Leader> leaders, String quizRoomCode) {
		super();
		this.leaders = leaders;
		this.quizRoomCode = quizRoomCode;
	}
	public List<Leader> getLeaders() {
		return leaders;
	}
	public void setLeaders(List<Leader> leaders) {
		this.leaders = leaders;
	}
	public String getQuizRoomCode() {
		return quizRoomCode;
	}
	public void setQuizRoomCode(String quizRoomCode) {
		this.quizRoomCode = quizRoomCode;
	}

}
