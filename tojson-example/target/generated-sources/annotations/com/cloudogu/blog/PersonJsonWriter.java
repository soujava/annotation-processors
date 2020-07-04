package com.cloudogu.blog;


public final class PersonJsonWriter {

  private PersonJsonWriter() {
  }

  /**
   * Creates an json object to the given {@link Person}.
   *
   * return json representation
   */
  public static String toJson(Person object) {
    StringBuilder builder = new StringBuilder("{");
    builder.append("\"class\": \"").append(object.getClass()).append("\",");
    builder.append("\"username\": \"").append(object.getUsername()).append("\",");
    builder.append("\"email\": \"").append(object.getEmail()).append("\"");
    return builder.append("}").toString();
  }

}