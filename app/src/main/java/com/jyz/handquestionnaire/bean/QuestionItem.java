package com.jyz.handquestionnaire.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @discription 单个问题实体
 * @autor songzhihang
 * @time 2017/10/19  下午8:47
 **/
public class QuestionItem implements Serializable{

    private int questionId;

    private int questionnaireId;

    private String title;

    private List<SelectionItem> selectionItemList;

    private String type;//类型

    private String least;//最少选项

    private String more;//最多选项

    private String isMust;//是否必填

    private String answerStr;//答案 可以是文字,可以是选项的列表

    public int getQuestionId() {
        return questionId;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SelectionItem> getSelectionItemList() {
        return selectionItemList;
    }

    public void setSelectionItemList(List<SelectionItem> selectionItemList) {
        this.selectionItemList = selectionItemList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeast() {
        return least;
    }

    public void setLeast(String least) {
        this.least = least;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getIsMust() {
        return isMust;
    }

    public void setIsMust(String isMust) {
        this.isMust = isMust;
    }

    public String getAnswerStr() {
        return answerStr;
    }

    public void setAnswerStr(String answerStr) {
        this.answerStr = answerStr;
    }
}
