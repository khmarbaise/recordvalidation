package com.soebes.records;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.support.HierarchyTraversalMode;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class DynamicValidationTest {

  private List<String> existingMethods;
  private Object[] arguments;
  private Constructor<?> constructor;
  private List<String> parameterNames;

  static Stream<Arguments> dynamicTesting() {
    return Stream.of(
        of("name", "some invalid name!!", "Name is invalid"),
        of("surename", "some invalid surname!!", "Surname is invalid"), // Must fail based on wrong method name "surename" instead of "surname"
        of("surname", "some invalid surname!!", "Surname is invalid"),
        of("secondName", "some invalid secondName!!", "SecondName is invalid"),
        of("emails", List.of("Invalid Email "), "emails contains invalid mail")
    );
  }

  @BeforeEach
  void beforeEach() {
    // Define the default parameters for all fields.
    this.arguments = new Object[]{"defaultName", "defaultSurname", "defaultSecondName", List.of("defaultEmail"), 1};
    this.constructor = RequestDto.class.getDeclaredConstructors()[0];
    // Get the parameter names from the given constructor.
    this.parameterNames = Arrays.stream(constructor.getParameters()).map(Parameter::getName).toList();

    Predicate<Method> check = m -> !List.of("equals", "toString", "hashCode").contains(m.getName());
    this.existingMethods = ReflectionSupport.findMethods(RequestDto.class, check, HierarchyTraversalMode.TOP_DOWN).stream()
        .map(Method::getName)
        .toList();
  }

  /**
   * This method will extract the method names based on reflection.
   */
  @ParameterizedTest
  @MethodSource
  void dynamicTesting(String methodName, Object value, String expectedExceptionMessage) {
    assertThat(this.existingMethods)
        .as("The method '%s' does not exist for %s.", methodName, RequestDto.class)
        .contains(methodName);

    var parameterPosition = this.parameterNames.indexOf(methodName);
    this.arguments[parameterPosition] = value;

    RequestDto requestDto = (RequestDto) ReflectionUtils.newInstance(this.constructor, this.arguments);
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Validation.validationMethod(requestDto))
        .withMessage(expectedExceptionMessage);
  }

}
