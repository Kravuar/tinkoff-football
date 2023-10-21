package net.kravuar.tinkofffootball.application.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlerService {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handle(Exception exception) {
        return exception.getMessage();
    }
}
