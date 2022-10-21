package com.wrc.QueryUs.service;

import com.wrc.QueryUs.dto.AnswerDto;
import com.wrc.QueryUs.entity.Answer;
import com.wrc.QueryUs.repository.AnswerRepository;
import com.wrc.QueryUs.repository.QuestionRepository;
import com.wrc.QueryUs.security.QueryUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    public final QueryUtils queryUtils;

    public void addAnswer(int questionId, String answer) {

        Answer ans = new Answer();
        ans.setAnswer(answer);
        ans.setQuestion(questionRepository.findById(questionId).orElseThrow());
        ans.setUser(queryUtils.getCurrentLoggedInUser());
        ans.setUpVotes(0);
        answerRepository.save(ans);
    }

    public void updateAnswer(int id, String answer) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Answer ans = answerRepository.findById(id).orElseThrow(() -> new RuntimeException("Answer does not exist."));
        if (ans.getUser().getEmail().equals(email)) {
            ans.setAnswer(answer);
            answerRepository.save(ans);
        } else {
            throw new RuntimeException("Unauthorized");
        }

    }

    public AnswerDto entityToDto(Answer answer) {
        AnswerDto dto = new AnswerDto();
        dto.setId(answer.getId());
        dto.setAnswer(answer.getAnswer());
        dto.setQuestionId(answer.getQuestion().getId());
        dto.setUserId(answer.getUser().getId());
        dto.setDate(answer.getDate());
        dto.setVoteCount(answer.getUpVotes());
        dto.setUpVoted(answer.getUpVotedUsers().contains(queryUtils.getCurrentLoggedInUser()));
        return dto;
    }
}
