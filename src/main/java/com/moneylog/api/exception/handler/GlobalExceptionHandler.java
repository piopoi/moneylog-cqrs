package com.moneylog.api.exception.handler;

import static com.moneylog.api.exception.domain.ErrorCode.*;
import static org.springframework.boot.web.servlet.server.Encoding.DEFAULT_CHARSET;
import static org.springframework.http.HttpHeaders.CONTENT_ENCODING;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.exception.domain.ErrorCode;
import com.moneylog.api.exception.dto.CustomErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final int FIRST_ERROR_INDEX = 0;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e) {
        logError(e);
        ErrorCode errorCode = e.getErrorCode();
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            String message = String.format("잘못된 %s 값 입니다.", mismatchedInputException.getPath().get(0).getFieldName());
            return createResponseEntity(BAD_REQUEST, message);
        }
        return createResponseEntity(BAD_REQUEST, "확인할 수 없는 형태의 데이터가 들어왔습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logError(e);
        String firstErrorMessage = getFirstErrorMessage(e);
        ErrorCode errorCode = errorMessageToErrorCode(firstErrorMessage, COMMON_INVALID_PARAMETER);
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<CustomErrorResponse> handleNumberFormatException(NumberFormatException e) {
        logError(e);
        return createResponseEntity(BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        logError(e);
        return createResponseEntity(COMMON_ACCESS_DENIED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthenticationException(AuthenticationException e) {
        logError(e);
        if (e.getCause() instanceof CustomException customException) {
            return createResponseEntity(customException.getErrorCode());
        }
        return createResponseEntity(AUTH_AUTHENTICATION_FAILED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception e) {
        logError(e);
        return createResponseEntity(INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ErrorCode errorMessageToErrorCode(String errorMessage, ErrorCode defaultErrorCode) {
        try {
            return ErrorCode.valueOf(errorMessage);
        } catch (IllegalArgumentException | NullPointerException ex) {
            return defaultErrorCode;
        }
    }

    private String getFirstErrorMessage(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return bindingResult
                .getAllErrors()
                .get(FIRST_ERROR_INDEX) //첫 번째 에러만 반환
                .getDefaultMessage();
    }

    private ResponseEntity<CustomErrorResponse> createResponseEntity(ErrorCode errorCode) {
        return createResponseEntity(errorCode.getHttpStatus(), errorCode.getMessage());
    }

    private ResponseEntity<CustomErrorResponse> createResponseEntity(HttpStatus httpStatus, String message) {
        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .code(httpStatus.value())
                .message(message)
                .build();
        return ResponseEntity
                .status(httpStatus)
                .header(CONTENT_ENCODING, DEFAULT_CHARSET.name())
                .contentType(APPLICATION_JSON)
                .body(customErrorResponse);
    }

    private void logError(Exception e) {
        log.error(e.getClass().getSimpleName(), e);
    }
}
