# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

JUnitWithParams is a Java library that enables parameterized JUnit 4 tests using a `@Rule` instead of a custom runner. This allows it to work with any JUnit runner (Robolectric, Spring, Mockito, etc.). Published to Maven Central as `com.github.ignaciotcrespo:junitwithparams`.

## Build Commands

```bash
./gradlew build          # Build + run tests + JaCoCo coverage
./gradlew test           # Run all tests
./gradlew test --tests "com.github.ignaciotcrespo.junitwithparams.WithParamsRuleTest.methodName"  # Run a single test
./gradlew jacocoTestReport  # Generate coverage report (build/reports/jacoco/)
```

## Architecture

The library is a single-module Gradle Java project (source compatibility: Java 1.7).

**Core mechanism:** `WithParamsRule` implements `MethodRule` (not `TestRule`) so it can intercept per-method execution. When a test method has a `@WithParams` annotation, the rule's inner `ParameterizedStatement` iterates over the annotation's values, populating a `HashMap<String, String>` that the test reads via typed accessors (`asInt()`, `asBoolean()`, `as(Transform)`, etc.).

- `@WithParams` — annotation holding parameter names and string values; single-param tests use the default name `"param1"`
- `@WithBooleanParams` — shorthand that synthesizes a `@WithParams` with `{"true", "false"}`
- `ErrorCollector` — accumulates failures across parameter iterations so all combinations run before reporting
- `WithParamsException` — custom exception for parameter validation and formatted error output

## Publishing

Uses `maven-push.gradle` for Sonatype/Maven Central deployment. Credentials (`NEXUS_PASSWORD`, `signing.password`) are passed via command line or `~/.gradle/gradle.properties`.
