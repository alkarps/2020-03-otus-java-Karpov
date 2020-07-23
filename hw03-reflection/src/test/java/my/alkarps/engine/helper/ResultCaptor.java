package my.alkarps.engine.helper;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author alkarps
 * create date 23.07.2020 10:04
 * Хак для получения результата операции
 */
public class ResultCaptor<T> implements Answer {
    private T result;

    public T getResult() {
        return result;
    }

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        result = (T) invocation.callRealMethod();
        return result;
    }
}
