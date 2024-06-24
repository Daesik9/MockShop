package project.mockshop.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Failure implements Result {
    private final String msg;
}
