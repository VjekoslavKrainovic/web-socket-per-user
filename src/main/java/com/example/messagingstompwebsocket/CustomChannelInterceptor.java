package com.example.messagingstompwebsocket;

import java.util.List;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class CustomChannelInterceptor implements ChannelInterceptor {

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    StompCommand command = accessor.getCommand();

    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
    System.out.println(
        "Authorization Header in STOMP: " + authorizationHeader + ", and stomp command is: " + command.name());

    if (StompCommand.CONNECT.equals(command)) {
      if (authorizationHeader == null) {
        throw new IllegalArgumentException();
      }

      List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

      User user = new User(authorizationHeader, "password", authorities);

      Authentication authentication = new UsernamePasswordAuthenticationToken(user, "token", authorities);

      accessor.setUser(authentication);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    return message;
  }
}