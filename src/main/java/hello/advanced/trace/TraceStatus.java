package hello.advanced.trace;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 로그 상태 정보(로그를 시작할 떄의 정보를 저장 => 이 상태 정보는 로그를 종료할 때 사용됨.)
 */
@Getter
@RequiredArgsConstructor
public class TraceStatus {
    private final TraceId traceId;
    private final Long startTimeMs;
    private final String message; // 시작시 사용한 메세지
}
