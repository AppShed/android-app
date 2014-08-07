package com.rightutils.collections;

public interface Mapper<E, T> {

	public E apply(T value);
}
