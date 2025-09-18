package com.ibb.library.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/** Tüm hatalarda tek tip JSON şeması. */
@Value
@Builder
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd MMMM yyyy HH:mm:ss",
            timezone = "Europe/Istanbul", locale = "tr-TR")
    Instant timestamp;

    int status;          // 400 / 401 / 403 / 404 / 409 / 500...
    String error;        // "Bad Request", "Not Found", "Conflict"...
    String message;      // İnsan okunur hata mesajı (TR)
    String path;         // İstek yolu (/api/...)

    // Validasyon hataları için alan -> mesaj
    Map<String, String> fieldErrors;

    // İsteğe bağlı detay listesi (örn. çoklu ihlal, ek notlar)
    List<String> details;
}
