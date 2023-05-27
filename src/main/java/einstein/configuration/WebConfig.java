package einstein.configuration;

import com.amazon.ask.servlet.ServletConstants;
import einstein.gpt.GptClient;
import einstein.properties.PropertiesUtils;
import einstein.servlet.AlexaServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;

@Configuration
public class WebConfig
{
    @Bean
    public GptClient gptClient(@Value("${openai.model}") String model, @Value("${openai.api.url}") String apiUrl, RestTemplate restTemplate, @Value("${openai.api.url}")  String systemRoleText)
    {
        return new GptClient(model, apiUrl, restTemplate, systemRoleText);
    }

    @Bean
    public RestTemplate openaiRestTemplate(@Value("${openai.api.key:#{environment.OPENAI_API_KEY}}") String openaiApiKey) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> alexaServlet() {

        loadProperties();

        ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
        servRegBean.setServlet(new AlexaServlet());

        servRegBean.addUrlMappings(PropertiesUtils.getPropertyValue(Constants.ALEXA_SKILL_ROOT_PATH_KEY) + "*");
        servRegBean.setLoadOnStartup(1);
        return servRegBean;
    }

    private void loadProperties() {
        System.setProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, PropertiesUtils.getPropertyValue(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY));
        System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, PropertiesUtils.getPropertyValue(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY));
    }

}