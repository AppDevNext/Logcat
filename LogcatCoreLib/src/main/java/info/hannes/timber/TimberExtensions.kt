package info.hannes.timber

import timber.log.Timber

fun fileLoggingTree() = Timber.forest().filterIsInstance<FileLoggingTree>().firstOrNull()