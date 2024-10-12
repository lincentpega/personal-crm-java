
package com.lincentpega.personalcrmjava.service.telegram;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final List<TelegramUpdateProcessor> handlers = new ArrayList<>();

    public void addUpdateHandler(TelegramUpdateProcessor handler) {
        handlers.add(handler);
    }

    @Override
    @SneakyThrows
    public void consume(Update update) {
        for (TelegramUpdateProcessor handler : handlers) {
            var handled = handler.process(update);
            if (handled) {
                return;
            }
        }
        log.info("Update {} was not handled by any handler", update.getUpdateId());
    }
}