package project.mockshop.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.mockshop.response.Response;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response illegalStateException(IllegalStateException e) {
        log.error(e.getClass() + " " + e.getMessage());
        return Response.failure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response illegalArgumentException(IllegalArgumentException e) {
        log.error(e.getClass() + " " + e.getMessage());
        return Response.failure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response nullPointerException(NullPointerException e) {
        log.error(e.getClass() + " " + e.getMessage());
        return Response.failure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response runtimeException(RuntimeException e) {
        log.error(e.getClass() + " " + e.getMessage());
        return Response.failure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response ioException(IOException e) {
        log.error(e.getClass() + " " + e.getMessage());
        return Response.failure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
