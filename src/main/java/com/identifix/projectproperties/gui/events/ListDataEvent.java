package com.identifix.projectproperties.gui.events;

import com.identifix.projectproperties.gui.Identifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;

public class ListDataEvent extends ApplicationEvent {

    Identifier identifier;
    List<String> list;
    String selectedValue;

    public ListDataEvent(Object source, Identifier identifier, List<String> list, String selectedValue) {
        super(source);
        this.identifier = identifier;
        this.list = Objects.nonNull(list) ? list : new ArrayList<>();
        this.selectedValue = selectedValue;
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public List<String> getList() {
        return list;
    }

    public String getSelectedValue() {
        return selectedValue;
    }
}
