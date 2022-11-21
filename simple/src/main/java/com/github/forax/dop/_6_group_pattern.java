package com.github.forax.dop;

public interface _6_group_pattern {
  sealed interface Vehicle { }
  record Car(int seats) implements Vehicle { }
  record TowTruck(Vehicle vehicle) implements Vehicle {}

  static int price(Vehicle vehicle) {
    return switch (vehicle) {
      case Car(int seats) -> 10 * seats;
      case TowTruck(Vehicle towed) -> 20 + price(towed);
    };
  }


  static void main(String[] args) {
    Vehicle v1 = new Car(5);
    Vehicle v2 = new TowTruck(v1);

    System.out.println(price(v1));
    System.out.println(price(v2));
  }
}
