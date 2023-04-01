package com.github.forax.dop;

import java.util.List;
import java.util.Objects;

public interface _0_shape_oop {
  interface Shape {
    double surface();
  }

  final class Circle implements Shape {
    private final int radius;

    public Circle(int radius) { this.radius = radius; }

    public int radius() { return radius; }

    @Override
    public boolean equals(Object other ) {
      if (other instanceof Circle) {
        Circle circle = (Circle) other;
        return radius == circle.radius;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(radius);
    }

    @Override
    public double surface() {
      if (radius < 0) {
        throw new AssertionError("danger danger");
      }
      return Math.PI * radius * radius;
    }
  }

  final class Box implements Shape {
    private final List<Shape> shapes;

    public Box(List<Shape> shapes) {
      this.shapes = List.copyOf(shapes);
    }

    public List<Shape> shapes() {
      return shapes;
    }

    @Override
    public boolean equals(Object other ) {
      if (other instanceof Box) {
        Box box = (Box) other;
        return shapes.equals(box.shapes);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return shapes.hashCode();
    }

    @Override
    public double surface() {
      return shapes.stream().mapToDouble(Shape::surface).sum();
    }
  }

  static void main(String[] args){
    Shape shape1 = new Circle(10);
    Shape shape2 = new Box(List.of(new Circle(10)));

    System.out.println("shape1.surface: " + shape1.surface());
    System.out.println("shape2.surface: " + shape2.surface());
  }
}
