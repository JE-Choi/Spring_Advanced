package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component // 싱글톤으로 Bean으로 등록해서 사용할 예정
public class HelloTraceV2 {
    private static final String START_PREFIX = "|-->";
    private static final String COMPLETE_PREFIX = "|<--";
    private static final String EX_PREFIX = "|<-x-";

    public TraceStatus begin(final String message) {
        final TraceId traceId = new TraceId();
        final long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), getPrefixOfLevel(traceId.getLevel(), START_PREFIX), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    // V2에서 추가
    public TraceStatus beginSync(final TraceId beforeTraceId ,final String message) {
        final TraceId nextId = beforeTraceId.createNextId();
        final long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", nextId.getId(), getPrefixOfLevel(nextId.getLevel(), START_PREFIX), message);
        return new TraceStatus(nextId, startTimeMs, message);
    }

    public void end(final TraceStatus status) {
        complete(status, null);
    }

    public void exception(final TraceStatus status, final Exception e) {
        complete(status, e);
    }

    private void complete(final TraceStatus status, final Exception e) {
        final long endTimeMs = System.currentTimeMillis();
        final long resultTimeMs = endTimeMs - status.getStartTimeMs();
        final TraceId traceId = status.getTraceId();
        if (Objects.nonNull(e)) {
            log.info("[{}] {}{} time={}ms {}", traceId.getId(), getPrefixOfLevel(traceId.getLevel(), EX_PREFIX), status.getMessage(), resultTimeMs, e.toString());

        } else {
            log.info("[{}] {}{} time={}ms"
                    , traceId.getId(), getPrefixOfLevel(traceId.getLevel(), COMPLETE_PREFIX), status.getMessage(), resultTimeMs);

        }

    }

    private String getPrefixOfLevel(final long level, final String prefix) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(i == level - 1 ? "|" + prefix : "|    ");
        }
        return sb.toString();
    }
}
