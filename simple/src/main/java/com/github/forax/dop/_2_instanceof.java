package com.github.forax.dop;

public interface _2_instanceof {
  interface Vehicle { }
  record Car(int seats) implements Vehicle { }
  record TowTruck(Vehicle vehicle) implements Vehicle {}

  static int price(Vehicle vehicle) {
    if (vehicle instanceof Car car) {
      return 10 * car.seats();
    }
    if (vehicle instanceof TowTruck towTruck) {
      return 20 + price(towTruck.vehicle());
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
