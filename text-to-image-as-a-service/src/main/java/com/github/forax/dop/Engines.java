package com.github.forax.dop;

import java.util.List;
import java.util.Objects;

public class Engines {
  private static final List<String> RESOURCES =
      """
      dope-as-picasso.png
      matching-as-picasso.png
      pablo-picasso-as-picasso.png
      questions-as-picasso.png
      fantastic-unicorn-as-picasso.png
      oops-as-picasso.png
      patterns-as-picasso.png
      school-class-as-picasso.png
      """
          .lines().map("images/"::concat).toList();

  private static String resource(int hash) {
    var index = (hash & 0x7FFFFFFF) % RESOURCES.size();
    return RESOURCES.get(index);
  }

  public static String dall_e(String text, int width, int height) {
    // fake it !
    var hash = Objects.hash(text, width, height);
    return resource(hash);
  }

  public static String stable_diffusion(String text, int width, int height, boolean plms) {
    // fake it !
    var hash = Objects.hash(text, width, height, plms);
    return resource(hash);
  }
}
