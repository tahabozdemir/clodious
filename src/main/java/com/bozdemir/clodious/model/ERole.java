package com.bozdemir.clodious.model;

import org.springframework.security.core.GrantedAuthority;

public enum ERole implements GrantedAuthority {
  ROLE_USER("USER"),
  ROLE_ADMIN("ADMIN");

  private String value;

  ERole(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public String getAuthority() {
    return name();
  }
}
