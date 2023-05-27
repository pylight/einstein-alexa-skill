package com.xavidop.alexa.gpt;

import com.xavidop.alexa.configuration.WebConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = WebConfig.class)
public class GptTest
{
    @Autowired
    private GptClient client;

    @Test
    public void askFollowupTest()
    {
        String result = client.query("What is the capital of Germany?");

        System.out.println(result);
        Assertions.assertTrue(result.contains("Berlin"));

        List<Message> messages = client.getAllMessages();
        String result2 = client.query("What was the name of the former capital city?", messages);
        System.out.println(result2);
        Assertions.assertTrue(result2.contains("Bonn"));
    }

    @Test
    public void createRoleTest()
    {
        System.out.println(client.createSystemMessage().getContent());
    }
}
