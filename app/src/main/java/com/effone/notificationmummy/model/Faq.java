package com.effone.notificationmummy.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sarith.vasu on 30-05-2018.
 */

public class Faq {
    private String Question;
    private String Answer;

    public Faq(String question, String answer) {
        Question = question;
        Answer = answer;
    }

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The Question
     */
    public String getQuestion() {
        return Question;
    }

    /**
     *
     * @param Question
     *     The Question
     */
    public void setQuestion(String Question) {
        this.Question = Question;
    }

    /**
     *
     * @return
     *     The Answer
     */
    public String getAnswer() {
        return Answer;
    }

    /**
     *
     * @param Answer
     *     The Answer
     */
    public void setAnswer(String Answer) {
        this.Answer = Answer;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
