package com.kwizzie.model;

import java.util.List;
import java.util.Map;

public class PublicQuizRoom extends QuizRoom{

	private Map<QuestionCategory,List<Question>> categoryQuestionMap;
	public PublicQuizRoom(
			Map<QuestionCategory, List<Question>> categoryQuestionMap,
			LeaderBoard leaderBoard, String description, String roomName,
			String roomID) {
		super();
		this.categoryQuestionMap = categoryQuestionMap;
		this.leaderBoard = leaderBoard;
		this.description = description;
		this.roomName = roomName;
		this.roomID = roomID;
	}

	public Map<QuestionCategory, List<Question>> getCategoryQuestionMap() {
		return categoryQuestionMap;
	}

	public void setCategoryQuestionMap(
			Map<QuestionCategory, List<Question>> categoryQuestionMap) {
		this.categoryQuestionMap = categoryQuestionMap;
	}
	
}
