package com.identifix.projectproperties.kubernetes.impl.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.identifix.projectproperties.kubernetes.parsers.IYamlParser;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class YamlParser implements IYamlParser {

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public <T> T read(Class<T> clazz, String yaml) throws IOException {
        try {
            return mapper.readValue(yaml, clazz);
        } catch (MismatchedInputException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

}
