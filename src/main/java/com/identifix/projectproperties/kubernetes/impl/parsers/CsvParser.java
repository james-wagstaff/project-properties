package com.identifix.projectproperties.kubernetes.impl.parsers;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvReadException;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.identifix.projectproperties.kubernetes.parsers.ICsvParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CsvParser implements ICsvParser {

    CsvMapper mapper = new CsvMapper();
    public <T> List<T> read(Class<T> clazz, InputStream stream, boolean withHeaders) throws IOException {
        try {
            CsvSchema schema = mapper.schemaFor(clazz).withColumnReordering(true);
            ObjectReader reader;
            schema = schema.withColumnSeparator('|');
            if (withHeaders) {
                schema = schema.withHeader();
            } else {
                schema = schema.withoutHeader();
            }
            reader = mapper.readerFor(clazz).with(schema);
            return reader.<T>readValues(stream).readAll();
        } catch (CsvReadException | MismatchedInputException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    public <T> List<T> read(Class<T> clazz, String csv, boolean withHeaders) throws IOException {
        return read(clazz, new ByteArrayInputStream(csv.getBytes()), withHeaders);
    }

    static {
        CsvMapper mapper = new CsvMapper();

        mapper.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.TRIM_SPACES);
        mapper.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.ALLOW_TRAILING_COMMA);
        mapper.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.INSERT_NULLS_FOR_MISSING_COLUMNS);
        mapper.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.SKIP_EMPTY_LINES);
    }

}
