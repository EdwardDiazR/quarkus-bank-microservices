package bank.core.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {

        ErrorResponse error = new ErrorResponse(
                exception.getCode(),
                exception.getMessage()
        );

        return Response
                .status(exception.getStatus())
                .entity(error)
                .build();
    }
}