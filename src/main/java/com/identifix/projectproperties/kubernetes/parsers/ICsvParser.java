package com.identifix.projectproperties.kubernetes.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ICsvParser {

    <T> List<T> read(Class<T> clazz, InputStream stream, boolean withHeaders) throws IOException;
    <T> List<T> read(Class<T> clazz, String csv, boolean withHeaders) throws IOException;

}
