package com.anunciadores.security;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Limite simple de intentos en memoria (ventana deslizante) para frenar fuerza bruta y spam
 * en los endpoints publicos. Se reinicia si se reinicia el servidor, lo cual es aceptable aqui.
 */
@Component
public class RateLimiter {

    private final int maxAttempts;
    private final long windowMillis;
    private final Map<String, Deque<Long>> attempts = new ConcurrentHashMap<>();

    public RateLimiter(@Value("${security.rate-limit.max-attempts:10}") int maxAttempts,
                       @Value("${security.rate-limit.window-minutes:15}") long windowMinutes) {
        this.maxAttempts = maxAttempts;
        this.windowMillis = windowMinutes * 60_000L;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    /** Indica si la clave ya alcanzo el limite dentro de la ventana. */
    public boolean isBlocked(String key, int limit) {
        Deque<Long> deque = attempts.get(key);
        if (deque == null) {
            return false;
        }
        synchronized (deque) {
            prune(deque, System.currentTimeMillis());
            return deque.size() >= limit;
        }
    }

    public boolean isBlocked(String key) {
        return isBlocked(key, maxAttempts);
    }

    /** Registra un intento para la clave. */
    public void record(String key) {
        Deque<Long> deque = attempts.computeIfAbsent(key, k -> new ArrayDeque<>());
        synchronized (deque) {
            deque.addLast(System.currentTimeMillis());
        }
    }

    /** Registra el intento y devuelve false si con el ya se supera el limite. */
    public boolean tryAcquire(String key, int limit) {
        if (isBlocked(key, limit)) {
            return false;
        }
        record(key);
        return true;
    }

    public void reset(String key) {
        attempts.remove(key);
    }

    @Scheduled(fixedRate = 15 * 60_000L)
    public void cleanup() {
        long now = System.currentTimeMillis();
        attempts.entrySet().removeIf(entry -> {
            Deque<Long> deque = entry.getValue();
            synchronized (deque) {
                prune(deque, now);
                return deque.isEmpty();
            }
        });
    }

    private void prune(Deque<Long> deque, long now) {
        while (!deque.isEmpty() && now - deque.peekFirst() > windowMillis) {
            deque.pollFirst();
        }
    }

    /**
     * IP del cliente. Nginx envia la IP real en X-Real-IP; Tomcat solo debe escuchar en localhost
     * para que nadie pueda falsificar este header saltandose Nginx.
     */
    public static String clientIp(HttpServletRequest request) {
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}
