package com.identifix.projectproperties.gui.utils;

import java.util.Objects;
import java.util.function.Predicate;
import javafx.collections.ObservableList;

public class predicates {

    public static Predicate<ObservableList<String>> listNotNull = list -> Objects.nonNull(list);
    public static Predicate<String> stringNotNull = string -> Objects.nonNull(string);
    public static Predicate<ObservableList<String>> listNotEmpty = list -> list.size() > 0;
    public static Predicate<String> stringNotEmpty = string -> string.length() > 0;

}
