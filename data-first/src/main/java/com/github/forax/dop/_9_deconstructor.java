package com.github.forax.dop;

import java.util.Objects;

public interface _9_deconstructor {
  sealed interface Vehicle { }

  final class Car implements Vehicle {
    private final int seats;

    public Car(int seats) {
      this.seats = seats;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof Car car && car.seats == seats;
    }

    @Override
    public int hashCode() {
      return Objects.hash(seats);
    }

    @Override
    public String toString() {
      return "Car[seats=" + seats + ']';
    }

    record $Result(int seats) {}

    public Object deconstructor() {
      return new $Result(seats);
    }
  }
  record TowTruck(Vehicle vehicle) implements Vehicle {}

  static int price(Vehicle vehicle) {
    return switch (vehicle) {
      // case Car(var seats) -> 10 * seats;
      case Car car when car.deconstructor() instanceof Car.$Result(var seats) -> 10 * seats;
      case Car car -> throw new MatchException("Car deconstructor does not match", null);
      case TowTruck(var towed) -> 20 + price(towed);
    };
  }


  static void main(String[] args) {
    Vehicle v1 = new Car(5);
    Vehicle v2 = new TowTruck(v1);

    System.out.println(price(v1));
    System.out.println(price(v2));
  }
}
