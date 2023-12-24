#!/bin/sh
git grep -l 'com\.jsoftware\.j\.android'|xargs sed -i "" -e 's/com\.jsoftware\.j\.android/com.jsoftware.j.beta.android/g'
sed -i "" -e 's/com\.jsoftware\.j\.android/com.jsoftware.j.beta.android/g' build.gradle
sed -i "" -e 's/drawable\/jblue/drawable\/jgray/g' AndroidManifest.xml
sed -i "" -e 's/J Android/J Android beta/g' res/values/strings.xml
sed -i "" -e 's/"9.5"/"9.6"/g' src/com/jsoftware/j/android/JConsoleApp.java
