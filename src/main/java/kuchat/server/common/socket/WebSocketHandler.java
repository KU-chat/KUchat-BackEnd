package kuchat.server.common.socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // CLIENT 변수에 session을 저장
    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS
            = new ConcurrentHashMap<String, WebSocketSession>();

    // 사용자가 웹소켓 서버에 접속할 때 동작을 구현
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        CLIENTS.put(session.getId(), session);      // 접속 시 생성되는 session을 CLIENT에 담는다.
    }

    // 사용자의 웹소켓 서버 접속이 끝났을 때 동작을 구현
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        CLIENTS.remove(session.getId());        // 저장된 세션을 제거
    }

    // 웹소켓 서버가 사용자의 메세지를 받을 때 동작을 구현
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String id = session.getId();        // 메세지를 보낸 사람의 세션 아이디
        CLIENTS.entrySet().forEach(arg -> {
            if(!arg.getKey().equals(id)){       // 보낸 사람 제외 모든 사용자에게 메세지 전송
                try{
                    arg.getValue().sendMessage(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
