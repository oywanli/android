package com.dspread.demoui.beans;

public class VerifyTerminalTaskBean {
    private String name;
    private int networkStrategy;
    private String packageName;
    private String version;
    private String versionName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNetworkStrategy() {
        return networkStrategy;
    }

    public void setNetworkStrategy(int networkStrategy) {
        this.networkStrategy = networkStrategy;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
