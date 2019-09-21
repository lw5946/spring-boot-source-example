package com.lw.validate.entity;

import com.lw.validate.entity.group.DefaultInherGroup;
import com.lw.validate.entity.group.HasIdGroup;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/** 验证参数都设置符合条件的默认值 */
@Data
public class ValidatorVO {

  @NotNull (groups = HasIdGroup.class)
  @NotNull (groups = DefaultInherGroup.class)
  private Integer id = 1;

  @NotBlank private String name = "1";

  @Min(0)
  @Max(200)
  private Integer age = 20;

  @PastOrPresent
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime birthday = LocalDateTime.now().minusDays(1);

  @Digits(integer = 4, fraction = 2)
  @DecimalMax(value = "1000")
  @DecimalMin(value = "0")
  private BigDecimal money = new BigDecimal(10);

  @Email private String email = "123456@qq.com";

  @NotBlank private String username = "username";

  @Size(max = 2)
  private List<String> nickname;

  @Positive /*(message = "身高不能为负数")*/ private Double height = 100D;

  @FutureOrPresent
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime nextBirthday = LocalDateTime.now().plusDays(1);

  @NotNull @Valid private HairVO hair = new HairVO();
}
