package org.example.validate.controller.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

// リクエストのバリデーション処理の単体テスト
public class RequestUnitTest {

  private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private final Validator validator = factory.getValidator();


  // Requestの各パラメータの正常値の代表値
  private final String validId = "123456789012";
  private final String validName = "validName";
  private final Integer validAge = 60;
  private final String validDescription = "";


  // 正常系
  @Test
  void test_NormalCase() {
    Request request = new Request();
    request.setId(validId);
    request.setName(validName);
    request.setAge(validAge);
    request.setDescription(validDescription);

    Set<ConstraintViolation<Request>> violations = validator.validate(request);

    // エラーが発生していないことを確認
    assertEquals(0, violations.size());

  }

  // 正常系 description
  @ParameterizedTest
  @NullAndEmptySource
  void test_NormalCase_DescriptionValidation(String description) {
    Request request = new Request();
    request.setId(validId);
    request.setName(validName);
    request.setAge(validAge);
    request.setDescription(description);

    Set<ConstraintViolation<Request>> violations = validator.validate(request);

    // エラーが発生していないことを確認
    assertEquals(0, violations.size());

  }

  // 異常系 id
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {
      " ", // 半角スペース　
      "aaaaaaaaaaaaa", // 文字数オーバー（13文字）
      "bbbbbbbbbbb" // 文字数アンダー（11文字）
  })
  void test_AbnormalCase_IdValidationError(String id) {
    Request request = new Request();
    request.setId(id);
    request.setName(validName);
    request.setAge(validAge);
    request.setDescription(validDescription);

    Set<ConstraintViolation<Request>> violations = validator.validate(request);

    // エラーが1つ発生していることを確認
    assertEquals(1, violations.size());

    // エラーに対応したエラーメッセージかどうか確認
    assertEquals("IDのフォーマットが正しくありません", violations.iterator().next().getMessage());
  }

  // 異常系 name
  @ParameterizedTest
  @NullAndEmptySource
  void test_AbnormalCase_NameValidationError(String name) {
    Request request = new Request();
    request.setId(validId);
    request.setName(name);
    request.setAge(validAge);
    request.setDescription(validDescription);

    Set<ConstraintViolation<Request>> violations = validator.validate(request);

    // エラーが1つ発生していることを確認
    assertEquals(1, violations.size());

    // エラーに対応したエラーメッセージかどうか確認
    assertEquals("名前を指定してください", violations.iterator().next().getMessage());
  }

  // 異常系 age
  @ParameterizedTest
  @ValueSource(ints = {
      -1, // 最小値未満
      121, // 最大値より大きい
  })
  void test_AbnormalCase_AgeValidationError(int age) {
    Request request = new Request();
    request.setId(validId);
    request.setName(validName);
    request.setAge(age);
    request.setDescription(validDescription);

    Set<ConstraintViolation<Request>> violations = validator.validate(request);

    // エラーが1つ発生していることを確認
    assertEquals(1, violations.size());

    // エラーに対応したエラーメッセージかどうか確認
    assertEquals("年齢は0から120までの整数で入力してください",
        violations.iterator().next().getMessage());
  }

  // 異常系 複数のパラメータが同時にエラー
  @Test
  void test_AbnormalCase_MultipleValidationError() {
    String invalidId = ""; // 空白文字
    String invalidName = " "; // 空白文字
    int invalidAge = -1; // 最小値未満

    Request request = new Request();
    request.setId(invalidId);
    request.setName(invalidName);
    request.setAge(invalidAge);
    request.setDescription(validDescription);

    Set<ConstraintViolation<Request>> violations = validator.validate(request);

    // エラーが3つ発生していることを確認
    assertEquals(3, violations.size());

    // エラーメッセージを確認
    for (ConstraintViolation<Request> violation : violations) {
      switch (violation.getPropertyPath().toString()) {
        case "id":
          assertEquals("IDのフォーマットが正しくありません", violation.getMessage());
          break;
        case "name":
          assertEquals("名前は1文字以上20文字以内で入力してください", violation.getMessage());
          break;
        case "age":
          assertEquals("年齢は0から120までの整数で入力してください", violation.getMessage());
          break;
      }
    }
  }


}
