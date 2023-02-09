package com.example.adsfacebookads.mapper;

import java.util.List;

public interface GenericMapper<S, T> {

    T sourceToTarget(S source) throws Exception, IllegalAccessException;

    S targetToSource(T target);

    List<T> sourceListToTargetList(List<S> sources) throws Exception;

    List<S> targetListToSourceList(List<T> targets);
}
