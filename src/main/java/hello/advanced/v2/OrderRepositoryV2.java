package hello.advanced.v2;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {
    private final HelloTraceV2 trace;
    public void save(TraceId traceId, String itemId) {
        TraceStatus status = trace.beginSync(traceId, "OrderRepository.save()");
        try {
            // 저장 로직
            if ("ex".equals(itemId)) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);// 상품 저장 소요시간 가정
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; // 예외를 꼭 다시 던져줘서, 흐름을 방해하면 안된다.
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}