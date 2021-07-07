/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.exception;

/**
 * <p>Extend {@link UnprocessableEntityException} to provide a specific error message
 * in the REST response. The error message is added to the response in
 * {@link DSpaceApiExceptionControllerAdvice#handleCustomUnprocessableEntityException},
 * hence it should not contain sensitive or security-compromising info.</p>
 */
public class TranslatableUnprocessableEntityException
        extends UnprocessableEntityException
        implements TranslatableException {

    public TranslatableUnprocessableEntityException(String messagekey) {
        super(messagekey);
    }

    @Override
    public String getMessageKey() {
        return getMessage();
    }
}
