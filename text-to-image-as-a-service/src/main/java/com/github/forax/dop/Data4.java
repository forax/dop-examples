package com.github.forax.dop;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static com.github.forax.dop.Data4.ImageSize.*;

public interface Data4 {

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

  record ImageResponse(String imagePath) {}

  record InvoiceResponse(String userName, String message, int price, LocalDateTime dateTime) {}

  sealed interface EngineParameter {}
  record DalleEngineParameter(ImageSize imageSize) implements EngineParameter {}
  record SDEngineParameter(ImageSize imageSize, boolean plms) implements EngineParameter {}


  static int price(EngineParameter engineParameter) {
    return switch (engineParameter) {
      case DalleEngineParameter parameter -> 1_000;
      case SDEngineParameter(ImageSize size, boolean plms) when size == small && !plms -> 125;
      case SDEngineParameter(ImageSize size, boolean plms) when size == small -> 150;
      case SDEngineParameter(ImageSize size, boolean plms) when size == medium && !plms -> 400;
      case SDEngineParameter(ImageSize size, boolean plms) when size == medium -> 425;
      case SDEngineParameter(ImageSize size, boolean plms) when size == big && !plms -> 800;
      case SDEngineParameter(ImageSize _1, boolean _2) -> 825;
    };
  }

  /*
  static int price(EngineParameter engineParameter) {
    return switch (engineParameter) {
      case DalleEngineParameter(var __) -> 1_000;
      case SDEngineParameter(var size, var plms) when size == small && !plms -> 125;
      case SDEngineParameter(var size, var plms) when size == small -> 150;
      case SDEngineParameter(var size, var plms) when size == medium && !plms -> 400;
      case SDEngineParameter(var size, var plms) when size == medium -> 425;
      case SDEngineParameter(var size, var plms) when size == big && !plms -> 800;
      case SDEngineParameter(var _1, var _2) -> 825;
    };
  }*/

  sealed interface ImageRequest {
    String user();
    String text();
  }
  record DalleImageRequest(String user, String text, ImageSize imageSize) implements ImageRequest {}
  record SDImageRequest(String user, String text, ImageSize imageSize, boolean plms) implements ImageRequest {}

  static EngineParameter parameter(ImageRequest imageRequest) {
    return switch (imageRequest) {
      case DalleImageRequest request -> new DalleEngineParameter(request.imageSize());
      case SDImageRequest request -> new SDEngineParameter(request.imageSize(), request.plms());
    };
  }

  static String imageToText(String text, EngineParameter engineParameter) {
    return switch (engineParameter) {
      case DalleEngineParameter(ImageSize imageSize) -> Engines.dall_e(text, imageSize.size(), imageSize.size());
      case SDEngineParameter(ImageSize imageSize, boolean plms) -> Engines.stable_diffusion(text, imageSize.size(), imageSize.size(), plms);
    };
  }
}
