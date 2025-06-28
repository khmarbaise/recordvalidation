package com.soebes.records;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.support.HierarchyTraversalMode;
import org.junit.platform.commons.support.ReflectionSupport;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class RecordValidationTest {


  @Nested
  class TestingWithBuilderLike {
    static class BuildDto {
      private String name;
      private String surname;
      private String secondName;
      private List<String> emails;

      private int age;

      private BuildDto() {
        this.name = "TheDefaultName";
        this.surname = "TheDefaultSurname";
        this.secondName = "TheDefaultSecondName";
        this.emails = List.of("TheDefaultEmail");
        this.age = 1;
      }

      private RequestDto buildIt() {
        return new RequestDto(this.name, this.surname, this.secondName, this.emails, this.age);
      }

      RequestDto name(String name) {
        this.name = name;
        return buildIt();
      }

      RequestDto surname(String surname) {
        this.surname = surname;
        return buildIt();
      }

      RequestDto secondName(String secondName) {
        this.secondName = secondName;
        return buildIt();
      }

      RequestDto emails(List<String> emails) {
        this.emails = emails;
        return buildIt();
      }

      RequestDto age(int age) {
        this.age = age;
        return buildIt();
      }

    }

    static BuildDto createTo() {
      return new BuildDto();
    }

    static Stream<Arguments> testValidation() {
      return Stream.of(
          of(createTo().name("some invalid name!!"), "Name is invalid"),
          of(createTo().surname("some invalid surname!!"), "Surname is invalid"),
          of(createTo().secondName("some invalid secondName!!"), "SecondName is invalid"),
          of(createTo().emails(List.of("Invalid Email ")), "emails contains invalid mail"),
          of(createTo().age(-1), "Age is invalid. Must be positive.")
      );
    }

    @ParameterizedTest
    @MethodSource
    void testValidation(RequestDto requestDto, String expectedExceptionMessage) {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> Validation.validationMethod(requestDto))
          .withMessage(expectedExceptionMessage);
    }

  }

  @Test
  void testingToSeeIfMethodsCanBeFoundAsRequiredForARecord() {
    var defaultMethods = List.of("equals", "toString", "hashCode");
    Predicate<Method> check = m -> !m.isSynthetic() && !m.isBridge() && !defaultMethods.contains(m.getName());
    ReflectionSupport.findMethods(RequestDto.class, check, HierarchyTraversalMode.TOP_DOWN).stream()
        .map(Method::getName)
        .forEach(System.out::println);

    var constructor = RequestDto.class.getDeclaredConstructors()[0];
    var parameterTypes = Arrays.stream(constructor.getParameters()).map(Parameter::getName).toList();

    var parameterNames = Arrays.stream(constructor.getParameters()).map(Parameter::getName).toList();
    System.out.println("parameterTypes = " + parameterTypes);
    System.out.println("parameterNames = " + parameterNames);
  }

}
