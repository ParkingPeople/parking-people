package com.apptive.parkingpeople.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class TypeUtil {

  private TypeUtil() {
  }

  public static <T> List<T> castList(Class<? extends T> innerType, Collection<?> collection) {
    List<T> casted = new ArrayList<T>(collection.size());
    for (Object element : collection) {
      casted.add(innerType.cast(element));
    }
    return casted;
  }

}
