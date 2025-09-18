package com.ibb.library.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import java.time.ZoneId;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        Locale tr = new Locale("tr", "TR");
        ZoneId zone = ZoneId.of("Europe/Istanbul");

        DateTimeFormatter DT = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", tr).withZone(zone);
        DateTimeFormatter D  = DateTimeFormatter.ofPattern("dd MMMM yyyy", tr).withZone(zone);
        DateTimeFormatter T  = DateTimeFormatter.ofPattern("HH:mm:ss", tr).withZone(zone);

        JavaTimeModule module = new JavaTimeModule();

        // --- Serializers (JSON çıktısı) ---
        module.addSerializer(Instant.class, new JsonSerializer<Instant>() {
            @Override public void serialize(Instant value, JsonGenerator gen, SerializerProvider sp) throws IOException {
                gen.writeString(value == null ? null : DT.format(value));
            }
        });
        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider sp) throws IOException {
                gen.writeString(value == null ? null : DT.format(value.atZone(zone)));
            }
        });
        module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider sp) throws IOException {
                gen.writeString(value == null ? null : D.format(value));
            }
        });
        module.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
            @Override public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider sp) throws IOException {
                gen.writeString(value == null ? null : T.format(value));
            }
        });

        // --- Deserializers (isteğe bağlı: request’te de aynı formatı kabul eder) ---
        module.addDeserializer(Instant.class, new JsonDeserializer<Instant>() {
            @Override public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String s = p.getValueAsString();
                if (s == null || s.isBlank()) return null;
                LocalDateTime ldt = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", tr));
                return ldt.atZone(zone).toInstant();
            }
        });
        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String s = p.getValueAsString();
                return (s == null || s.isBlank()) ? null
                        : LocalDateTime.parse(s, DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", tr));
            }
        });
        module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String s = p.getValueAsString();
                return (s == null || s.isBlank()) ? null
                        : LocalDate.parse(s, DateTimeFormatter.ofPattern("dd MMMM yyyy", tr));
            }
        });
        module.addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {
            @Override public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String s = p.getValueAsString();
                return (s == null || s.isBlank()) ? null
                        : LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm:ss", tr));
            }
        });

        return builder -> {
            builder.locale(tr);
            builder.timeZone(TimeZone.getTimeZone(zone));
            builder.modules(module);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // epoch yerine string
        };
    }
}
