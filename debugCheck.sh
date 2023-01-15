#!/usr/bin/env sh

if [ "$1" == "--fast" ]; then
  ./gradlew assembleDebug ktlintFormat testDebug lintDebug assembleDebugAndroidTest
else
  ./gradlew assembleDebug ktlintFormat testDebug lintDebug connectedDebugAndroidTest
fi
