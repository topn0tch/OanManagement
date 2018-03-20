package com.oan.management.exception;

/**
 * @author Oan Stultjens
 * @since 15/03/2018
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
