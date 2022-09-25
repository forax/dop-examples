package com.github.forax.dop.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Entity
public class Invoice {
  public @Id
  @GeneratedValue Long id;

  @Column(nullable = false)
  public String userName;

  @Column(nullable = false)
  public String message;

  @Column(nullable = false)
  public int price;

  @Column(nullable = false)
  public LocalDateTime dateTime;

  protected Invoice() {}  // called by hibernate

  public Invoice(String userName, String message, int price, LocalDateTime dateTime) {
    this.userName = requireNonNull(userName);
    this.message = requireNonNull(message);
    this.price = price;
    this.dateTime = requireNonNull(dateTime);
  }
}