package br.com.simplehealth.armazenamento.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Deserializador customizado para LocalDate que aceita múltiplos formatos.
 * Lida com datas vindas do backend em diferentes formatos.
 */
public class FlexibleLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
        DateTimeFormatter.ISO_LOCAL_DATE,
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_ZONED_DATE_TIME
    };

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateStr = parser.getText();
        
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        // Tentar diferentes formatos
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                // Primeiro tenta como LocalDate
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // Se falhar, tenta como LocalDateTime e extrai a data
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                    return dateTime.toLocalDate();
                } catch (DateTimeParseException ex) {
                    // Continua tentando outros formatos
                }
            }
        }

        // Se nada funcionou, tenta os parsers padrão do Java Time
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(dateStr).toLocalDate();
            } catch (DateTimeParseException ex) {
                throw new IOException("Não foi possível fazer parse da data: " + dateStr, ex);
            }
        }
    }
}