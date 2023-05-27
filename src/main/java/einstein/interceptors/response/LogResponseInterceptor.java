package einstein.interceptors.response;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.model.Response;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Optional;

public class LogResponseInterceptor implements ResponseInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogResponseInterceptor.class);
    @Override
    public void process(HandlerInput input, Optional<Response> output) {
        logger.info(output.toString());
    }
}