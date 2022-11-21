package com.github.forax.dop;

public interface  _4_switch_and_null {
  interface Vehicle { }
  record Car(int seats) implements Vehicle { }
  record TowTruck(Vehicle vehicle) implements Vehicle {}

  static int price(Vehicle vehicle) {
    return switch (vehicle) {
      case null -> 0;
      case Car car -> 10 * car.seats();
      case TowTruck towTruck -> 20 + price(towTruck.vehicle());
      default -> throw new AssertionError("oops");
    };
  }


  static void main(String[] args) {
    Vehicle v1 = new Car(5);
    Vehicle v2 = new TowTruck(v1);

    System.out.println(price(v1));
    System.out.println(price(v2));

    System.out.println(price(null));
  }
}
