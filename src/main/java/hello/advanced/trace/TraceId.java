package hello.advanced.trace;

import lombok.Getter;
import java.util.UUID;

@Getter
public class TraceId {
    private String id;
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    public TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    // 트랜잭션 ID(HTTP요청 식별자) => 로그용이라서, 어짜다가 중복될 가능성은 배제함.
    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private TraceId createNextId() {
        return new TraceId(id, ++level);
    }

    private TraceId createPreviousId() {
        return new TraceId(id, --level);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }
}
