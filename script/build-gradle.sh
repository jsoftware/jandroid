#!/bin/sh
sudo apt install openjdk-17-jdk-headless
sudo update-alternatives --config java
sudo update-alternatives --config javac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
wget https://raw.githubusercontent.com/jsoftware/jsource/master/version.txt
mkdir -p temp
wget https://github.com/jsoftware/jsource/releases/download/build/androidlibs.zip
unzip -d temp androidlibs.zip
cp temp/libs/arm64-v8a/* libs/arm64-v8a/.
cp temp/libs/armeabi/* libs/armeabi/.
cp temp/libs/armeabi-v7a/* libs/armeabi-v7a/.
cp temp/libs/x86/* libs/x86/.
rm -rf assets/libexec
cp -r libs assets/libexec
find assets/libexec -type f -name '*.so' -delete
find assets/libexec -type f -name 'libj.a' -delete
find assets/libexec -type f -name 'jamalgam' -delete
find assets/libexec -type f -name 'hostdefs' -delete
find assets/libexec -type f -name 'netdefs' -delete
sed -i -e 's/targetSdkVersion="29"/targetSdkVersion="33"/' AndroidManifest.xml
sed -i -e 's/android-29/android-33/' project.properties
./gradlew build
./gradlew assemble
ls -ld /usr/local/lib/android/sdk/build-tools/*
