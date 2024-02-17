package org.example.validate.controller.request;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  private final String endPoint = "/echo";

  // 正常系の代表
  @Test
  public void test_NormalCase() throws Exception {
    // 正常系のテストケースの代表値
    var request = """
            {
              "id": "123456789012",
              "name": "name",
              "age": 20
            }
            """;

    var expectedResponse = """
            {
              "id": "123456789012",
              "name": "name",
              "age": 20,
              "description": null
            }
            """;

    mockMvc.perform(
            MockMvcRequestBuilders
                .post(endPoint)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
  }

  // 異常系 Requestが読み取れないエラー
  @Test
  public void test_AbnormalCase_RequestNotReadable() throws Exception {
    var request = """
            {
              "id": "123456789012",
              "name": "name",
              "age": 20
            
            """;

    var expectedResponse = """
            {
              "messages":
                [
                  "リクエストの読み取りに失敗しました。原因: JSON parse error"
                ]
            }
            """;

    mockMvc.perform(
            MockMvcRequestBuilders
                .post(endPoint)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
  }

  // 異常系 Requestパラメータでバリデーションエラー
  @Test
  public void test_AbnormalCase_MethodArgumentNotValid() throws Exception {
    // バリデーションエラーの代表値
    var request = """
            {
              "id": "1",
              "name": "name",
              "age": 20,
            
            """;

    var expectedResponse = """
            {
              "messages":
                [
                  "id: IDのフォーマットが正しくありません"
                ]
            }
            """;

    mockMvc.perform(
            MockMvcRequestBuilders
                .post(endPoint)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
  }

  // 異常系 サポートしていないHTTPメソッドを指定した場合
  @Test
  public void test_AbnormalCase_MethodNotSupported() throws Exception {
    var request = """
            {
              "id": "123456789012",
              "name": "name",
              "age": 20,
            
            """;

    var expectedResponse = """
            {
              "messages":
                [
                  "PUTメソッドは許可されていません"
                ]
            }
            """;

    mockMvc.perform(
            MockMvcRequestBuilders
                .put(endPoint)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
  }
}
