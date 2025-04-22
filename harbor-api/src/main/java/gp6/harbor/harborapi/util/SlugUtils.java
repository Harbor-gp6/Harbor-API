package gp6.harbor.harborapi.util;

import java.text.Normalizer;

public class SlugUtils {
  public static String createSlug (String text) {
    if (text == null || text.isBlank()) {
      return "";
    }

    String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
      .replaceAll("\\p{M}", "")
      .replaceAll("[^\\p{ASCII}]", "");

    String slug = normalized.toLowerCase()
      .replaceAll("[^a-z0-9]+", "-")
      .replaceAll("^-+|-+$", "")
      .replaceAll("-{2,}", "-");

    return slug;
  }
}
