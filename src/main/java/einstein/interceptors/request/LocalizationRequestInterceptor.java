package einstein.interceptors.request;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import einstein.localization.LocalizationManager;

import java.util.Locale;

public class LocalizationRequestInterceptor implements RequestInterceptor {

    @Override
    public void process(HandlerInput input) {
        String localeString = input.getRequestEnvelope().getRequest().getLocale();
        Locale locale = new Locale.Builder().setLanguageTag(localeString).build();
        LocalizationManager.getInstance(locale);
    }
}