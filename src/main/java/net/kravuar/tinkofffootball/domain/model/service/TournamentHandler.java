package net.kravuar.tinkofffootball.domain.model.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.domain.model.events.TournamentEvent;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.*;

@RequiredArgsConstructor
public class TournamentHandler {
    private final SubscribableChannel eventChannel = MessageChannels.publishSubscribe().getObject();
    @Getter
    private final Tournament tournament;

    public void subscribe(MessageHandler handler) {
        eventChannel.subscribe(handler);
    }

    public void unsubscribe(MessageHandler handler) {
        eventChannel.unsubscribe(handler);
    }

    public void publishEvent(GenericMessage<TournamentEvent> eventMessage) {
        eventChannel.send(eventMessage);
    }
}
