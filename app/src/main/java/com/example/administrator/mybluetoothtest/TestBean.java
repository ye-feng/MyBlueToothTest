package com.example.administrator.mybluetoothtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestBean {
    private String sample_name;
    private String evaluation_methods;
    private String person;
    private int wet;
    private int gluten;
    private int water;
    private int dough_time;
    private Date test_date;
    private List<Float> test_datas=new ArrayList<>();

    public TestBean() {
    }

    public String getSample_name() {
        return sample_name;
    }

    public void setSample_name(String sample_name) {
        this.sample_name = sample_name;
    }

    public String getEvaluation_methods() {
        return evaluation_methods;
    }

    public void setEvaluation_methods(String evaluation_methods) {
        this.evaluation_methods = evaluation_methods;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getWet() {
        return wet;
    }

    public void setWet(int wet) {
        this.wet = wet;
    }

    public int getGluten() {
        return gluten;
    }

    public void setGluten(int gluten) {
        this.gluten = gluten;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getDough_time() {
        return dough_time;
    }

    public void setDough_time(int dough_time) {
        this.dough_time = dough_time;
    }

    public Date getTest_date() {
        return test_date;
    }

    public void setTest_date(Date test_date) {
        this.test_date = test_date;
    }

    public List<Float> getTest_datas() {
        return test_datas;
    }

    public void setTest_datas(List<Float> test_datas) {
        this.test_datas = test_datas;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "sample_name='" + sample_name + '\'' +
                ", evaluation_methods='" + evaluation_methods + '\'' +
                ", person='" + person + '\'' +
                ", wet=" + wet +
                ", gluten=" + gluten +
                ", water=" + water +
                ", dough_time=" + dough_time +
                ", test_date=" + test_date +
                ", test_datas=" + test_datas +
                '}';
    }
}
