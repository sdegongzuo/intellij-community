package com.intellij.util

object TestRuntimeUtil {
  val isRunningUnderUnitTest by lazy {
    Thread.getAllStackTraces()
      .values
      .flatMap { elements -> elements.map { it.className } }
      .any { it.startsWith("org.junit.") || it.startsWith("org.testng.") }
  }
}