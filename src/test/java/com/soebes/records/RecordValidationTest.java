package com.soebes.records;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
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

}
