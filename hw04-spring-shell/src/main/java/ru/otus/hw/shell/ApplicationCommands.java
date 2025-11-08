package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.ExitRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@ShellComponent(value = "Testing service commands")
@RequiredArgsConstructor
public class ApplicationCommands {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final LocalizedIOService ioService;

    private final LoginContext loginContext;

    @ShellMethod(value = "Login command", key = {"login"})
    public String login(String userName) {
        if (loginContext.isLoginPresent()) {
            return ioService.getMessage("Login.already", loginContext.getLogin());
        }
        loginContext.setLogin(userName);
        return ioService.getMessage("Login.success", userName);
    }

    @ShellMethod(value = "Start testing command", key = {"start"})
    @ShellMethodAvailability(value = "isTestingAvailable")
    public void startTesting() {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
        throw new ExitRequest();
    }

    private Availability isTestingAvailable() {
        return loginContext.isLoginPresent()
                ? Availability.available()
                : Availability.unavailable(ioService.getMessage("Login.need"));
    }
}
