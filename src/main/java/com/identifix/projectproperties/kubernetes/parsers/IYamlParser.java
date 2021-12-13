package com.identifix.projectproperties.kubernetes.parsers;

import java.io.IOException;

public interface IYamlParser {

    <T> T read(Class<T> clazz, String yaml) throws IOException;

}
