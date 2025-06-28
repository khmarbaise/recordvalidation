package com.soebes.records;

public interface Validation {
  /**
   * Example implementation for a validation which needs to be tested.
   */
  static void validationMethod(RequestDto requestDto) {
    var name = requestDto.name().replaceAll("\\s+", "");
    if (name.length() != requestDto.name().length()) {
      throw new IllegalArgumentException("Name is invalid");
    }

    var surname = requestDto.surname().replaceAll("\\s+", "");
    if (surname.length() != requestDto.surname().length()) {
      throw new IllegalArgumentException("Surname is invalid");
    }
    var secondName = requestDto.secondName().replaceAll("\\s+", "");
    if (secondName.length() != requestDto.secondName().length()) {
      throw new IllegalArgumentException("SecondName is invalid");
    }

    requestDto.emails().forEach(email -> {
      var replaced = email.replaceAll("\\s+", "");
      if (replaced.length() != email.length()) {
        throw new IllegalArgumentException("emails contains invalid mail");
      }
    });
    if (requestDto.age() < 0) {
      throw new IllegalArgumentException("Age is invalid. Must be positive.");
    }
  }
}
