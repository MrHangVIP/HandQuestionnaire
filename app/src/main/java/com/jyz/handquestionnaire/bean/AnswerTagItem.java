package com.jyz.handquestionnaire.bean;

import android.widget.CheckBox;
import android.widget.RadioButton;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @discription
 * @autor songzhihang
 * @time 2017/11/24  下午5:21
 **/
public class AnswerTagItem implements Serializable {

    private QuestionItem questionItem;

    private ArrayList<CheckBox> checkBoxes;

    private ArrayList<RadioButton> radioButtons;

    public QuestionItem getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItem questionItem) {
        this.questionItem = questionItem;
    }

    public ArrayList<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public ArrayList<RadioButton> getRadioButtons() {
        return radioButtons;
    }

    public void setRadioButtons(ArrayList<RadioButton> radioButtons) {
        this.radioButtons = radioButtons;
    }
}
