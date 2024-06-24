package project.mockshop.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Response {
    private boolean success;
    private int code;
    private Result result;

    public static Response success() {
        return Response.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .build();
    }

    public static Response success(int code) {
        return Response.builder()
                .success(true)
                .code(code)
                .build();
    }

    public static <T> Response success(T data) {
        return Response.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .result(new Success<>(data))
                .build();
    }

    public static <T> Response success(int code, T data) {
        return Response.builder()
                .success(true)
                .code(code)
                .result(new Success<>(data))
                .build();
    }

    public static Response failure(int code, String msg) {
        return Response.builder()
                .success(false)
                .code(code)
                .result(new Failure(msg))
                .build();
    }

}
