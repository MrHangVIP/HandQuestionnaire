package com.jyz.handquestionnaire.bean;

import java.io.Serializable;

/**
 * @discription 选项实体
 * @autor songzhihang
 * @time 2017/10/19  下午8:45
 **/
public class SelectionItem implements Serializable{

    private int id;

    private String title;

    private String isSelect;//是否默认

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }
}
