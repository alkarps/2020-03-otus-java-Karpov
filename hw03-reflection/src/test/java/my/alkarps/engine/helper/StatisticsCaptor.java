package my.alkarps.engine.helper;

import my.alkarps.engine.model.Statistics;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author alkarps
 * create date 23.07.2020 10:04
 * Хак для получения результата операции
 */
public class StatisticsCaptor implements Answer<Statistics> {
    private Statistics result;

    public Statistics getResult() {
        return result;
    }

    @Override
    public Statistics answer(InvocationOnMock invocation) throws Throwable {
        result = (Statistics) invocation.callRealMethod();
        return result;
    }
}
