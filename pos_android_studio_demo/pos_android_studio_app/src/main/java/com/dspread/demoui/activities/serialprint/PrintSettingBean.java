package com.dspread.demoui.activities.serialprint;

public class PrintSettingBean {

    private String name;

    private String value;

    private  int id;

    public PrintSettingBean(String name, String value, int id) {
        this.name = name;
        this.value = value;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PrintSettingBean{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", id=" + id +
                '}';
    }
}
