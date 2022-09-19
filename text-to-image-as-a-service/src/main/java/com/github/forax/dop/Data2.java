package com.github.forax.dop;

import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDateTime;

public interface Data2 {

  enum ImageSize {
    small, medium, big;

    public int size() {
      return switch (this) {
        case small -> 512;
        case medium -> 1024;
        case big -> 2048;
      };
    }
  }

  record DalleImageRequest(String user, String text, ImageSize imageSize) {}
  record SDImageRequest(String user, String text, ImageSize imageSize, boolean plms) {}
  record ImageResponse(String imagePath) {}

  record InvoiceResponse(String userName, String message, int price, LocalDateTime dateTime) {}

  interface EngineParameter {}
  record DalleEngineParameter(ImageSize imageSize) implements EngineParameter {}
  record SDEngineParameter(ImageSize imageSize, boolean plms) implements EngineParameter {}

  private static int imagePrice(ImageSize size) {
    return switch(size) {
      case small -> 125;
      case medium -> 400;
      case big -> 800;
    };
  }
  private static int plmsPrice(boolean plms) {
    return plms? 25: 0;
  }

  static int price(EngineParameter engineParameter) {
    return switch (engineParameter) {
      case DalleEngineParameter __ -> 1_000;
      case SDEngineParameter parameter -> imagePrice(parameter.imageSize()) + plmsPrice(parameter.plms());
      default -> throw new IllegalStateException("Unexpected value: " + engineParameter);
    };
  }
}
