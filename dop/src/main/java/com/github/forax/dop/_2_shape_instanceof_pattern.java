package com.github.forax.dop;

import java.util.List;
import java.util.Objects;

public interface _2_shape_instanceof_pattern {
  interface Shape {
  }

  static double surface(Shape shape) {
    if (shape instanceof Circle circle) {
      if (circle.radius() < 0) {
        throw new AssertionError("danger danger");
      }
      return Math.PI * circle.radius() * circle.radius();
    }
    if (shape instanceof Box box) {
      return box.shapes().stream()
          .mapToDouble(s -> surface(s))
          .sum();
    }
    throw new AssertionError();
  }

  final class Circle implements Shape {
    private final int radius;

    public Circle(int radius) { this.radius = radius; }

    public int radius() { return radius; }

    @Override
    public boolean equals(Object other ) {
      return other instanceof Circle circle &&
          radius == circle.radius;
    }

    @Override
    public int hashCode() {
      return Objects.hash(radius);
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
      return other instanceof Box box &&
          shapes.equals(box.shapes);
    }

    @Override
    public int hashCode() {
      return shapes.hashCode();
    }
  }

  static void main(String[] args){
    Shape shape1 = new Circle(10);
    Shape shape2 = new Box(List.of(new Circle(10)));

    System.out.println("shape1.surface: " + surface(shape1));
    System.out.println("shape2.surface: " + surface(shape2));
  }
}
