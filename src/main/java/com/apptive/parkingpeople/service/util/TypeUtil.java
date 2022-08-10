package com.apptive.parkingpeople.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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

  public static <T, R> Stream<? extends R> castStream(Stream<? super T> source, Class<R> toType) {
    return source.filter(o -> toType.isAssignableFrom(o.getClass())).map(o -> toType.cast(o));
  }

}
