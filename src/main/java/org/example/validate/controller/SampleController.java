package org.example.validate.controller;

import org.example.validate.controller.request.Request;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  // リクエストをそのまま返すAPI
  // リクエストバリデーションの動作確認用
  @RequestMapping(value = "/echo",
      method = {RequestMethod.POST},
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Request> execute(@RequestBody @Validated Request request) {
    return ResponseEntity.ok(request);
  }
}
