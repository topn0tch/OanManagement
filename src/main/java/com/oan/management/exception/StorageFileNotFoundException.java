package com.oan.management.exception;

/**
 * Exception class thrown when storage couldn't be located
 * @author Oan Stultjens
 * @since 15/03/2018
 */
public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}