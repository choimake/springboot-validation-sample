package org.example.validate.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Request {

  // 半角英数字12文字という仕様
  //
  // NOTE:
  // 正規表現に関しては「どういった仕様のための実装なのか」を残しておくこと
  // 正規表現の実装から、正規表現を使って実現したかったことを読み取るのは難しいので
  @NotNull(message = "IDのフォーマットが正しくありません")
  @Pattern(regexp = "^[a-zA-Z0-9]{12}$", message = "IDのフォーマットが正しくありません")
  String id;

  @NotBlank(message = "名前を指定してください")
  String name;

  @Min(value = 0, message = "年齢は0から120までの整数で入力してください")
  @Max(value = 120, message = "年齢は0から120までの整数で入力してください")
  Integer age;

  String description;


}
