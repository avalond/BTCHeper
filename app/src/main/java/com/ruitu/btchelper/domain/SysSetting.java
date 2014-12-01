package com.ruitu.btchelper.domain;

public class SysSetting {
    private int sys_icon;
    private String sys_text;
    private String sys_setting_text;
    private int sys_setting_icon;
    public int getSys_icon() {
        return sys_icon;
    }
    public void setSys_icon(int sys_icon) {
        this.sys_icon = sys_icon;
    }
    public String getSys_text() {
        return sys_text;
    }
    public void setSys_text(String sys_text) {
        this.sys_text = sys_text;
    }
    public String getSys_setting_text() {
        return sys_setting_text;
    }
    public void setSys_setting_text(String sys_setting_text) {
        this.sys_setting_text = sys_setting_text;
    }
    public int getSys_setting_icon() {
        return sys_setting_icon;
    }
    public void setSys_setting_icon(int sys_setting_icon) {
        this.sys_setting_icon = sys_setting_icon;
    }
    public SysSetting(int sys_icon, String sys_text, String sys_setting_text,
            int sys_setting_icon) {
        super();
        this.sys_icon = sys_icon;
        this.sys_text = sys_text;
        this.sys_setting_text = sys_setting_text;
        this.sys_setting_icon = sys_setting_icon;
    }
    
}
