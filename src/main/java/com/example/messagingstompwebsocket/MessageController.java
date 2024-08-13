package com.example.messagingstompwebsocket;

import com.example.messagingstompwebsocket.dto.Greeting;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class MessageController {

  private final SimpMessagingTemplate template;

  public MessageController(SimpMessagingTemplate template) {
    this.template = template;
  }

  @GetMapping("/test/{username}")
  public void sendMessage(@PathVariable("username") String username) {
    String message = "This is a scheduled message!";
    Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(message) + "!");
    template.convertAndSendToUser(username, "/topic/greetings", greeting);
    System.out.println("Message sent to user: " + username);
  }
}