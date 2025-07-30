package es.menasoft.juniemvc.exceptions;

/**
 * Exception thrown when an entity is not found in the database.
 */
public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String entityType, Object id) {
        super(String.format("%s not found with ID: %s", entityType, id));
    }
}