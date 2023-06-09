package pet.store.controller.error;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
  private enum LogStatus {
    STACK_TRACE, MESSAGE_ONLY
  }
  
  @Data
  private class ExceptionMessage {
    private String message;
    private String statusReason;
    private int statusCode;
    private String timestamp;
    private String uri;
  }

  
  /**
   * A method handles general exception.
   * @param ex
   * @param webRequest
   * @return Custom exception message object type ExceptionMessage.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionMessage handleException(Exception ex, WebRequest webRequest) {
    return buildExceptionMessage(ex, HttpStatus.INTERNAL_SERVER_ERROR, webRequest, LogStatus.STACK_TRACE);
  }
  
  
  /**
   * A method handles NoSuchElementException web request.
   * @param ex NoSuchElementException
   * @param webRequest WebRequest
   * @return Custom no such element found exception message object type ExceptionMessage.
   */
  @ExceptionHandler(NoSuchElementException.class)  
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ExceptionMessage handleNoSuchElementException(NoSuchElementException ex, WebRequest webRequest) {
    return buildExceptionMessage(ex, HttpStatus.NOT_FOUND, webRequest, LogStatus.MESSAGE_ONLY);
  }
  
  
  /**
   * A method constructs exception message based on the parameters.
   * @param ex Exception
   * @param status HttpStatus
   * @param webRequest WebRequest
   * @param logStatus LogStatus
   * @return Custom exception message object type ExceptionMessage.
   */
  private ExceptionMessage buildExceptionMessage(Exception ex, HttpStatus status,
      WebRequest webRequest, LogStatus logStatus) {
    String message = ex.toString();
    String statusReason = status.getReasonPhrase();
    int statusCode = status.value();
    String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    String uri = null;
    
    if(webRequest instanceof ServletWebRequest servletWebRequest) {
      uri = servletWebRequest.getRequest().getRequestURI();
    }
    
    if(logStatus == LogStatus.MESSAGE_ONLY) {
      log.error("Exception: {}", ex.toString());
    }else {
      log.error("Exception: ", ex);
    }
    
    ExceptionMessage exeMsg = new ExceptionMessage();
    exeMsg.setMessage(message);
    exeMsg.setStatusReason(statusReason);
    exeMsg.setStatusCode(statusCode);
    exeMsg.setTimestamp(timestamp);
    exeMsg.setUri(uri);
    
    return exeMsg;
  }
}
