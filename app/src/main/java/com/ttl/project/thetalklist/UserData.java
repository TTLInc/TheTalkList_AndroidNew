package com.ttl.project.thetalklist;

/**
 * Created by Saubhagyam on 3/30/2017.
 */

public class UserData {
    String email;
    String pass;
    int roleId;
    String loginServResponse;
    Boolean LoginStatus;

    private static final UserData ourInstance = new UserData();

    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }

    public Boolean getLoginStatus() {
        return LoginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        LoginStatus = loginStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getLoginServResponse() {
        return loginServResponse;
    }

    public void setLoginServResponse(String loginServResponse) {
        this.loginServResponse = loginServResponse;
    }
}
