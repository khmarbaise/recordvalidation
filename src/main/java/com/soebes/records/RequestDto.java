package com.soebes.records;

import java.util.List;

public record RequestDto(String name, String surname, String secondName, List<String> emails, int age) {
  public RequestDto {
  }
}
