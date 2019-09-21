package com.lw.validate;

import static org.junit.Assert.assertEquals;

import com.lw.validate.entity.ValidatorManual;
import com.lw.validate.entity.ValidatorVO;
import com.lw.validate.entity.group.HasIdGroup;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

/** 手动使用工具校验 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ValidateManualTest {

  @Autowired WebApplicationContext context;
  private Validator validator;

  @Before
  public void setMvc() throws Exception {
    Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  public void validateSucWhenGroupNotMatched() {
    ValidatorManual vm = new ValidatorManual();
    Set<ConstraintViolation<ValidatorManual>> validateResult = validator.validate(vm);
    assertEquals(0, validateResult.size());
  }

  @Test(expected = AssertionError.class)
  public void validateFailedWhenGroupMatched() {
    ValidatorManual vm = new ValidatorManual();
    Set<ConstraintViolation<ValidatorManual>> validateResult =
        validator.validate(vm, HasIdGroup.class);
    for (ConstraintViolation msg : validateResult) {
      log.error(msg.getMessage());
    }
    assertEquals(0, validateResult.size());
  }

  @Test(expected = AssertionError.class)
  public void validateFailedWhenCascadeNotPassed() {
    ValidatorManual vm = new ValidatorManual();
    vm.setId(1);
    ValidatorVO vv = new ValidatorVO();
    vv.setAge(-1);
    vm.setValidatorVO(vv);
    Set<ConstraintViolation<ValidatorManual>> validateResult = validator.validate(vm);
    for (ConstraintViolation msg : validateResult) {
      log.error(msg.getMessage());
    }
    assertEquals(0, validateResult.size());
  }
}
