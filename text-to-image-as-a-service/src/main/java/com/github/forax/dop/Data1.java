package com.github.forax.dop;

import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDateTime;

public interface Data1 {

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

}
