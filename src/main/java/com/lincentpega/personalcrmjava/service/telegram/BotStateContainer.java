package com.lincentpega.personalcrmjava.service.telegram;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class BotStateContainer {

    private final StringRedisTemplate redisTemplate;

    private final String KEY_PREFIX = "bot-state:";

    public BotStateContainer(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @NonNull
    public TelegramBotState getState(String chatId) {
        String key = KEY_PREFIX + chatId;
        String stateRaw = redisTemplate.opsForValue().get(key);
        TelegramBotState state = null;
        if (stateRaw != null) {
            state = TelegramBotState.valueOf(stateRaw);
        }
        if (state == null) {
            state = TelegramBotState.INITIAL;
            redisTemplate.opsForValue().set(key, state.toString());
        }
        return state;
    }

    public void setState(@NonNull String chatId, @NonNull TelegramBotState state) {
        String key = KEY_PREFIX + chatId;
        redisTemplate.opsForValue().set(key, state.toString());
    }
}
