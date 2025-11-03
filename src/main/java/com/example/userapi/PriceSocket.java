package com.example.userapi;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@WebSocket
public class PriceSocket {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
        System.out.println("🟢 Connected socket: " + session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
        System.out.println("🔴 Disconnected socket");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException {
        System.out.println("💬 Received: " + message);
    }

    public static void broadcastPriceChange(int productId, double newPrice) {
        String payload = String.format("{\"productId\":%d,\"newPrice\":%.2f}", productId, newPrice);
        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(payload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("📡 Broadcasted: " + payload);
    }
}