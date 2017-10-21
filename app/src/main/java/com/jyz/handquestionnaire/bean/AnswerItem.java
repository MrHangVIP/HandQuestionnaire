package com.jyz.handquestionnaire.bean;

import java.io.Serializable;

/**
 * @discription 答案实体
 * @autor songzhihang
 * @time 2017/10/20  下午2:43
 **/
public class AnswerItem implements Serializable{

    private int answerId;

    private int questionId;

    private int questionnaireId;

    private int userId;

    private String answer;

    private String createTime;

    private long createTimeStmp;//时间戳

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getCreateTimeStmp() {
        return createTimeStmp;
    }

    public void setCreateTimeStmp(long createTimeStmp) {
        this.createTimeStmp = createTimeStmp;
    }
}
