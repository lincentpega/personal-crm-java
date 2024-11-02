package com.lincentpega.personalcrmjava.service.telegram;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BotStateContainer {

    private final StringRedisTemplate redisTemplate;

    private final String STATE_KEY_FORMAT = "bot-state:%s";
    private final String VALUE_KEY_FORMAT = "bot-state-value-%s:%s";


    public BotStateContainer(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @NonNull
    public TelegramBotState getState(String chatId) {
        String key = String.format(STATE_KEY_FORMAT, chatId);
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
        String key = String.format(STATE_KEY_FORMAT, chatId);
        redisTemplate.opsForValue().set(key, state.toString());
    }

    public void setValue(@NonNull String chatId, @NonNull String key, @NonNull String value) {
        var redisKey = String.format(VALUE_KEY_FORMAT, chatId, key);
        redisTemplate.opsForValue().set(redisKey, value);
    }

    @Nullable
    public String getValue(@NonNull String chatId, @NonNull String key) {
        var redisKey = String.format(VALUE_KEY_FORMAT, chatId, key);
        return redisTemplate.opsForValue().get(redisKey);
    }

    public void clearValues(@NonNull String chatId) {
        Set<String> keys = redisTemplate.keys("bot-state-value-" + chatId + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
