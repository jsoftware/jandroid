#!/bin/sh

# change to your own $SOTREPASS and $KEYPASS

keytool -genkey -v -storepass $SOTREPASS -keypass $KEYPASS -keystore $HOME/.android/release-key.keystore -alias jandroid -keyalg RSA -keysize 2048 -validity 10000
