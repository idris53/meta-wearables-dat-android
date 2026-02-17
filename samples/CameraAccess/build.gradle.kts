/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.6.0")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
  }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
allprojects {
  repositories {
    google()
    mavenCentral()
  }
}
