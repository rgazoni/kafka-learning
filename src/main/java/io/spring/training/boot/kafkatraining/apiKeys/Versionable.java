package io.spring.training.boot.kafkatraining.apiKeys;

import java.util.SortedSet;

public interface Versionable<I> {
    I fromVersion(short v);
    SortedSet<Short> getRangeOfSupportedVersions();
}
