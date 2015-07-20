package org.flossola.common.utilities.exceptions;

/**
 * Dynamic form exception, used to throw validation exceptions, derived from 
 * form template constraints. Used in OpenTenure Community server methods
 */
public class DynamicFormException extends RuntimeException {
    public DynamicFormException(){
        super();
    }
    
    public DynamicFormException(String message){
        super(message);
    }
    
    public DynamicFormException(Throwable cause){
        super(cause);
    }
    
    public DynamicFormException(String message, Throwable cause){
        super(message, cause);
    }
}
