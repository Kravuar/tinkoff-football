package net.kravuar.tinkofffootball.domain.model.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.domain.model.events.BracketEvent;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

@RequiredArgsConstructor
public class TournamentHandler {
    private final SubscribableChannel eventChannel = MessageChannels.publishSubscribe().getObject();
    @Getter
    private final Tournament tournament;

//    TODO: Auto finish on Max duration expiration

    public void subscribe(MessageHandler handler) {
        eventChannel.subscribe(handler);
    }

    public void unsubscribe(MessageHandler handler) {
        eventChannel.unsubscribe(handler);
    }

    public void publishBracketEvent(GenericMessage<BracketEvent> eventMessage) {
        eventChannel.send(eventMessage);
    }
}
