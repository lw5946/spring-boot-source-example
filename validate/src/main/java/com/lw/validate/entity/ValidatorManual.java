package com.lw.validate.entity;

import com.lw.validate.entity.group.HasIdGroup;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ValidatorManual {
  @NotNull(groups = HasIdGroup.class)
  private Integer id;

  @Valid private ValidatorVO validatorVO;
}
