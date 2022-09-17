package com.github.forax.dop;

import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static com.github.forax.dop.Data3.ImageSize.*;

public interface Data3 {

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

  sealed interface EngineParameter /*permits DalleEngineParameter, SDEngineParameter*/ {}
  record DalleEngineParameter(ImageSize imageSize) implements EngineParameter {}
  record SDEngineParameter(ImageSize imageSize, boolean plms) implements EngineParameter {}

  /*
  static int imagePrice(ImageSize size) {
    return switch(size) {
      case small -> 125;
      case medium -> 400;
      case big -> 800;
    };
  }

  static int plmsPrice(boolean plms) {
    return plms? 25: 0;
  }

  static int price(EngineParameter engineParameter) {
    return switch (engineParameter) {
      case DalleEngineParameter(ImageSize __) -> 1_000;
      case SDEngineParameter(ImageSize size, boolean plms) -> imagePrice(size) + plmsPrice(plms);
    };
  }*/

  /*
  static int price(EngineParameter engineParameter) {
    return switch (engineParameter) {
      case DalleEngineParameter(ImageSize imageSize) -> 1_000;
      case SDEngineParameter(ImageSize size, boolean plms) when size == small && !plms -> 125;
      case SDEngineParameter(ImageSize size, boolean plms) when size == small && plms -> 150;
      case SDEngineParameter(ImageSize size, boolean plms) when size == medium && !plms -> 400;
      case SDEngineParameter(ImageSize size, boolean plms) when size == medium && plms -> 425;
      case SDEngineParameter(ImageSize size, boolean plms) when size == big && !plms -> 800;
      case SDEngineParameter(ImageSize size, boolean plms) when size == big && plms -> 825;
    };
  }*/

  static int price(EngineParameter engineParameter) {
    return switch (engineParameter) {
      case DalleEngineParameter(ImageSize __) -> 1_000;
      case SDEngineParameter(ImageSize size, boolean plms) when size == small && !plms -> 125;
      case SDEngineParameter(ImageSize size, boolean plms) when size == small -> 150;
      case SDEngineParameter(ImageSize size, boolean plms) when size == medium && !plms -> 400;
      case SDEngineParameter(ImageSize size, boolean plms) when size == medium -> 425;
      case SDEngineParameter(ImageSize size, boolean plms) when size == big && !plms -> 800;
      case SDEngineParameter(ImageSize _1, boolean _2) -> 825;
    };
  }
}
