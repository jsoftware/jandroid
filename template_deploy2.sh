#!/bin/sh

# change to your own $SOTREPASS and $KEYPASS

M=jandroid
N=jandroid
jarsigner -digestalg SHA1 -sigalg MD5withRSA -storepass $STOREPASS -keypass $KEYPASS -keystore /home/$USER/.android/release-key.keystore build/outputs/apk/release/$M-release-unsigned.apk jandroid
rm -f dist/$N.apk
zipalign 4 build/outputs/apk/release/$M-release-unsigned.apk dist/$N.apk
