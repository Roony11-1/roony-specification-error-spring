package io.github.roony11_1.specification.error.spring;

import io.github.roony11_1.error.core.ErrorHandler;
import io.github.roony11_1.error.core.ErrorResponse;
import io.github.roony11_1.error.core.exceptions.InvalidInputException;
import io.github.roony11_1.error.rest.HttpStatusRegistry;
import io.github.roony11_1.specification.core.FilterException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpecificationExceptionAdvice 
{
    private static final Logger log = LoggerFactory.getLogger(SpecificationExceptionAdvice.class);

    private final HttpServletRequest request;

    public SpecificationExceptionAdvice(HttpServletRequest request) 
    {
        this.request = request;
    }

    @ExceptionHandler(FilterException.class)
    public ResponseEntity<ErrorResponse> handleFilterException(FilterException ex) 
    {
        log.warn("FilterException: {}", ex.getMessage());

        InvalidInputException appEx = new InvalidInputException(ex.getMessage());
        ErrorResponse body = buildEnrichedErrorResponse(appEx);
        int status = HttpStatusRegistry.getStatus(appEx.getCategory());

        return ResponseEntity.status(status).body(body);
    }

    private ErrorResponse buildEnrichedErrorResponse(Throwable throwable) 
    {
        ErrorResponse response = ErrorHandler.toErrorResponse(throwable);
        response.setPath(request.getRequestURI());
        response.setTraceId(MDC.get("traceId"));
        return response;
    }
}