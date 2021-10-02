#!/bin/sh
git grep -l 'com\.jsoftware\.j\.android'|xargs sed -i 's/com\.jsoftware\.j\.android/com.jsoftware.j.beta.android/g'
sed -i 's/drawable\/jblue/drawable\/jgray/g' AndroidManifest.xml
sed -i 's/J Android/J Android beta/g' res/values/strings.xml
sed -i 's/"902"/"903"/g' src/com/jsoftware/j/android/JConsoleApp.java
