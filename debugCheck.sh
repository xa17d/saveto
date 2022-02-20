#!/usr/bin/env sh

if [ "$1" == "--fast" ]; then
  ./gradlew assembleDebug ktlintFormat testDebug assembleDebugAndroidTest
else
  ./gradlew assembleDebug ktlintFormat testDebug connectedDebugAndroidTest
fi