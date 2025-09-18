package com.ibb.library.exception;

/** İş kuralı ihlallerinde (stok bitti, aynı kitabı ikinci kez alma vb.) 409 döndürülecek. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
