package com.lw.validate;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/** 完全使用注解完成验证 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidateAnnotationTest {

  @Autowired WebApplicationContext context;

  private MockMvc mvc;
  private DateTimeFormatter formatter;
  @Autowired private ObjectMapper objectMapper;

  @Before
  public void setMvc() throws Exception {
    Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  }

  @Test
  public void verificationFailedWhenNameIsBlank() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("name", ""))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenAgeGreaterThan200() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("age", "201"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenBirthdayIsFuture() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.get("/user")
                .param("birthday", formatter.format(LocalDateTime.now().plusDays(1))))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenMoneyGreaterThan1000() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("money", "1001"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenFractionOverflow() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("money", "999.222"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenFractionOverflowAndGreaterThan1000() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("money", "1001.222"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenEmailNotMatchFormat() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("email", "111222@"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenUsernameIsNull() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("username", ""))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenNicknameGreaterThan2() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("nickname", "小明", "小蓝", "小兰"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenHeightIsNotPositive() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("height", "0"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationFailedWhenNextBirthdayIsPast() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.get("/user")
                .param("nextBirthday", formatter.format(LocalDateTime.now().minusDays(1))))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  /** 级联验证：当验证属性对象中包含的一个属性不满足要求，则验证失败 */
  @Test
  public void verificationFailedWhenPropertiesNotPassVerification() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user").param("hair.length", "-1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void validateFailedWhenGroupMatched() throws Exception {
    mvc.perform(MockMvcRequestBuilders.put("/user").param("id", "").param("name", ""))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  /** 匹配的分组起作用，不匹配的不起作用 */
  @Test
  public void validateFailedByGroup() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/user/1").param("id", "").param("name", ""))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void validateSucWhenGroupNotMatched() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/user").param("id", "").param("name", ""))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void verificationSucWhenNoParam() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }

  @Test
  public void validateFailedWhenGroupMatched1() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/user/1").param("id", "").param("name", ""))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
  }
}
