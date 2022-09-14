package com.github.forax.dop;

import java.net.URI;
import java.nio.file.Path;

public class Engines {
  public static URI dall_e(String text, int width, int height) {
    // fake it !
    var path = Path.of("/dall-e/images/", text, width + "x" + height);
    return path.toUri();
  }

  public static URI stable_diffusion(String text, int width, int height, boolean plms) {
    // fake it !
    var path = Path.of("/sd/images/", plms? "plms": "no-plms", text, width + "x" + height);
    return path.toUri();
  }
}
