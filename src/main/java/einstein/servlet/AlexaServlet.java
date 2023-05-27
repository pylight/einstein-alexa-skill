package einstein.servlet;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.servlet.SkillServlet;
import einstein.handlers.*;
import einstein.interceptors.request.LocalizationRequestInterceptor;
import einstein.interceptors.request.LogRequestInterceptor;
import einstein.interceptors.response.LogResponseInterceptor;

public class AlexaServlet extends SkillServlet {

    public AlexaServlet() {
        super(getSkill());
    }

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HelloWorldIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler(),
                        new ErrorHandler())
                .addExceptionHandler(new MyExceptionHandler())
                .addRequestInterceptors(
                        new LogRequestInterceptor(),
                        new LocalizationRequestInterceptor())
                .addResponseInterceptors(new LogResponseInterceptor())
                // Add your skill id below
                //.withSkillId("[unique-value-here]")
                .build();
    }

}