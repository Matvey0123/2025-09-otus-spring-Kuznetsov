package ru.otus.hw.security;

import org.springframework.stereotype.Component;

@Component
public class LoginContext {

    private String login;

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return this.login;
    }

    public boolean isLoginPresent() {
        return this.login != null;
    }
}
