package org.example.validate.controller.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ErrorResponse {

  private List<String> messages = new ArrayList<>();

  public void add(String message) {
    messages.add(message);
  }
}
