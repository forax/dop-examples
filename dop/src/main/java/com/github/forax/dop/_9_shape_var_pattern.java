package com.github.forax.dop;

import java.util.List;

public interface _9_shape_var_pattern {
  sealed interface Shape /*permits Circle, Box*/ { }

  static double surface(Shape shape) {
    return switch(shape) {
      case Circle(var radius) when radius < 0 ->
          throw new AssertionError("danger danger");
      case Circle(var radius) -> Math.PI * radius * radius;
      case Box(var shapes) ->
          shapes.stream()
            .mapToDouble(s -> surface(s))
            .sum();
    };
  }

  record Circle(int radius) implements Shape { }

  record Box(List<Shape> shapes) implements Shape {
      public Box {
        shapes = List.copyOf(shapes);
      }
  }

  static void main(String[] args){
    Shape shape1 = new Circle(10);
    Shape shape2 = new Box(List.of(new Circle(10)));

    System.out.println("shape1.surface: " + surface(shape1));
    System.out.println("shape2.surface: " + surface(shape2));
  }
}
