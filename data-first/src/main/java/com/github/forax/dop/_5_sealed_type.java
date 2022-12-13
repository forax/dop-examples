package com.github.forax.dop;

public interface _5_sealed_type {
  sealed interface Vehicle /*permits Car, TowTruck*/ { }
  record Car(int seats) implements Vehicle { }
  record TowTruck(Vehicle vehicle) implements Vehicle {}

  static int price(Vehicle vehicle) {
    return switch (vehicle) {
      case Car car -> 10 * car.seats();
      case TowTruck towTruck -> 20 + price(towTruck.vehicle());
    };
  }


  static void main(String[] args) {
    Vehicle v1 = new Car(5);
    Vehicle v2 = new TowTruck(v1);

    System.out.println(price(v1));
    System.out.println(price(v2));
  }
}
