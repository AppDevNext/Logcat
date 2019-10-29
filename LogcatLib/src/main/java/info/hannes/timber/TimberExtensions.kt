package info.hannes.timber

import timber.log.Timber

fun List<Timber.Tree>.fileLoggingTree() = Timber.forest().filterIsInstance<FileLoggingTree>().firstOrNull()