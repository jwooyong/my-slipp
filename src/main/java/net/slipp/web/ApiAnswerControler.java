package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.slipp.domain.Answer;
import net.slipp.domain.AnswerRepository;
import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.User;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerControler {
		
	@Autowired
	QuestionRepository questionRepository;
		
	@Autowired
	AnswerRepository answerRepository;

	@PostMapping("")
	public Answer create(@PathVariable Long questionId, String contents, HttpSession session) {
		
		if (!HttpSessionUtils.isLoginUser(session)) {
			return null;
		}		
		User loginUser = HttpSessionUtils.getUserFormSession(session);		
		Question  question = questionRepository.findOne(questionId);
		Answer answer = new Answer(loginUser, question, contents);			
		return answerRepository.save(answer);				
	}
}
