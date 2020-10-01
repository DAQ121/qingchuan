package com.qingchuan.coderman.web.myenums;

/**
 * 问题所属类型
 */
public enum  QuestionCategory {
    Put_Questions(1,"表白"),
    Share(2,"招领"),
    Discuss(3,"二手"),
    Advise(4,"提问"),
    Bug(5,"分享"),
    FOR_JOB(6,"吐槽"),
    NOTICE(7,"广告");
//    TEACH(8,"教程"),
//    INTERVIEW(9,"面试");
    private Integer value;
    private  String name;

    QuestionCategory(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static   String getnameByVal(Integer value){
        QuestionCategory[] values = QuestionCategory.values();
        for (Integer i = 0; i < value; i++) {
            if(values[i].getValue()==value){
                return values[i].name;
            }
        }
        return "";
    }
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
