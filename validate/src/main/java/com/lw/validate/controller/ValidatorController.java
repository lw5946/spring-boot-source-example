package com.lw.validate.controller;

import com.lw.validate.entity.ValidatorVO;
import com.lw.validate.entity.group.DefaultInherGroup;
import com.lw.validate.entity.group.HasIdGroup;
import com.lw.validate.entity.group.NoIdGroup;
import javax.validation.Valid;
import javax.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class ValidatorController {

  @GetMapping
  public boolean getUser(@Valid ValidatorVO user, BindingResult result) {
    if (result.hasErrors()) {
      for (ObjectError error : result.getAllErrors()) {
        log.error(error.getDefaultMessage());
      }
      return false;
    }
    return true;
  }

  /**
   * 分组校验
   * 分组不匹配时，校验注解不起作用
   * <p>
   * 不同于JSR-303(javax.validate)规范的实现，提供JSR-303 group的扩展实现
   */
  @PostMapping
  public boolean addUser(@Validated(NoIdGroup.class) ValidatorVO user, BindingResult result) {
    if (result.hasErrors()) {
      for (ObjectError error : result.getAllErrors()) {
        log.error(error.getDefaultMessage());
      }
      return false;
    }
    return true;
  }

  /**
   * 分组校验
   * 分组不匹配时，校验注解不起作用
   * <p>
   * 不同于JSR-303(javax.validate)规范的实现，提供JSR-303 group的扩展实现
   */
  @PostMapping("/1")
  public boolean addUser1(@Validated({Default.class,NoIdGroup.class}) ValidatorVO user, BindingResult result) {
    if (result.hasErrors()) {
      for (ObjectError error : result.getAllErrors()) {
        log.error(error.getDefaultMessage());
      }
      return false;
    }
    return true;
  }

  /**
   * 分组校验
   * 分组匹配时，校验注解起作用
   * <p>
   * 不同于JSR-303(javax.validate)规范的实现，提供JSR-303 group的扩展实现
   */
  @PutMapping
  public boolean updateUser(@Validated(HasIdGroup.class) ValidatorVO user, BindingResult result) {
    if (result.hasErrors()) {
      for (ObjectError error : result.getAllErrors()) {
        log.error(error.getDefaultMessage());
      }
      return false;
    }
    return true;
  }

  @GetMapping("/1")
  public boolean getUser1(@Validated(DefaultInherGroup.class) ValidatorVO user, BindingResult result) {
    if (result.hasErrors()) {
      for (ObjectError error : result.getAllErrors()) {
        log.error(error.getDefaultMessage());
      }
      return false;
    }
    return true;
  }
}
