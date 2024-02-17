package org.example.validate.controller.advice;

import org.example.validate.controller.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

  // RestControllerでRequestの読み取りに失敗した時の処理
  // 想定: Jsonのパースエラー
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  ResponseEntity<ErrorResponse> onHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {

    var errorResponse = new ErrorResponse();

    // エラーメッセージを分割
    // 元のエラーメッセージだと詳細情報が多すぎるので、情報の一部のみ出力するようにするための対応
    var messages = e.getMessage().split(": ");
    var errorMessage = messages[0];

    var message = "リクエストの読み取りに失敗しました。原因: " + errorMessage;
    errorResponse.add(message);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  // RestControllerでRequestの各パラメータのバリデーションエラーが発生した時の処理
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    var errorResponse = new ErrorResponse();

    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      var message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
      errorResponse.add(message);
    }


    return ResponseEntity.badRequest().body(errorResponse);
  }

  // API実行時にサポートされていないメソッドを使用した際の処理
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  ResponseEntity<ErrorResponse> onHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {

    var errorResponse = new ErrorResponse();

    var message = e.getMethod() + "メソッドは許可されていません";
    errorResponse.add(message);

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
  }
}
