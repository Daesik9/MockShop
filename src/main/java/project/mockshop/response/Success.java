package project.mockshop.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Success<T> implements Result {
    private final T data;
}
