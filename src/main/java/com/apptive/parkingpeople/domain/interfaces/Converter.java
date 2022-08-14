package com.apptive.parkingpeople.domain.interfaces;

@FunctionalInterface
public interface Converter<I, O> {
  O convert(I input);
}
