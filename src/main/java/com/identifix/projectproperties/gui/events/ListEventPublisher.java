package com.identifix.projectproperties.gui.events;

import com.identifix.projectproperties.gui.Identifier;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ListEventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ListEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishCustomEvent(final Identifier identifier, final List<String> list, final String selectedValue) {
        ListDataEvent customSpringEvent = new ListDataEvent(this, identifier, list, selectedValue);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

}
