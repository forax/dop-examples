package com.github.forax.dop;

public interface _1_type_hierarchy {
  interface Vehicle { int price(); }
  record Car(int seats) implements Vehicle {
    public int price() { return 10 * seats; }
  }
  record TowTruck(Vehicle vehicle) implements Vehicle {
    public int price() { return 20 + vehicle.price(); }
  }


  static void main(String[] args) {
    Vehicle v1 = new Car(5);
    Vehicle v2 = new TowTruck(v1);

    System.out.println(v1.price());
    System.out.println(v2.price());

    System.out.println(((Vehicle) null).price());
  }
}
