package einstein.gpt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GptClient
{
    static final Logger logger = LogManager.getLogger(GptClient.class);
    private final String model;
    private final String apiUrl;

    private final String systemRoleText;
    private final RestTemplate restTemplate;

    private final List<Message> allMessages;
    public GptClient(String model, String apiUrl, RestTemplate restTemplate, String systemRoleText)
    {
        this.model = model;
        this.apiUrl = apiUrl;
        this.systemRoleText = systemRoleText;
        this.restTemplate = restTemplate;
        allMessages = new ArrayList<>();
    }

     protected Message createSystemMessage()
     {
         // current date and weekday
         LocalDate dateObj = LocalDate.now();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy (EEEE)", Locale.US);
         String formattedDate = dateObj.format(formatter);

         // current time
         SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
         String formattedTime = sdf.format(new Date());

         String additionalUserInfo = System.getenv("OPENAI_SYSTEM_ROLE_ADDITIONAL_USERINFO");
         additionalUserInfo = additionalUserInfo == null ? "" : ", " + additionalUserInfo;

         String content = String.format("%s Current date: %s, current time: %s. %s",
                 systemRoleText, formattedDate, formattedTime, additionalUserInfo);

        return new Message("system", content);
     }

     public String query(String prompt)
     {
         return query(prompt, Collections.emptyList());
     }

    public String query(String prompt, List<Message> messages)
    {
        if (!containsSystemMessage(messages))
        {
            allMessages.add(createSystemMessage());
        }

        allMessages.addAll(messages);
        allMessages.add(new Message("user", prompt));

        Request request = new Request(model, allMessages);

        Response response = restTemplate.postForObject(
                apiUrl,
                request,
                Response.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }

        String responseText = response.getChoices().get(0).getMessage().getContent();
        allMessages.add(new Message("assistant", responseText));
        return responseText;
    }

    private boolean containsSystemMessage(List<Message> messages)
    {
        return  messages != null && messages.stream().anyMatch(m -> "system".equals(m.getRole()));
    }

    public List<Message> getAllMessages() {
        return allMessages;
    }
}
