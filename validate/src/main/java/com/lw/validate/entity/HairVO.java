package com.lw.validate.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Data;

/** 验证参数都设置符合条件的默认值 */
@Data
public class HairVO {
  @Positive private Double length = 10D;
  @Positive private Double Diameter = 1D;
  @NotBlank private String color = "black";
}
