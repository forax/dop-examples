package com.github.forax.dop;

public interface _8_instanceof_group {
  sealed interface Vehicle permits Car, TowTruck { }
  record Car(int seats) implements Vehicle { }
  record TowTruck(Vehicle vehicle) implements Vehicle {}

  static int price(Vehicle vehicle) {
    if (vehicle instanceof Car(var seats)) {
      return 10 * seats;
    }
    if (vehicle instanceof TowTruck(var towed)) {
      return 20 + price(towed);
    }
    throw new AssertionError("oops");
  }


  static void main(String[] args) {
    Vehicle v1 = new Car(5);
    Vehicle v2 = new TowTruck(v1);

    System.out.println(price(v1));
    System.out.println(price(v2));
  }
}
