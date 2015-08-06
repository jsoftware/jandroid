#!/bin/sh

# change to your own $SOTREPASS and $KEYPASS

M=JActivity
N=jandroid
jarsigner -digestalg SHA1 -sigalg MD5withRSA -storepass $STOREPASS -keypass $KEYPASS -keystore /home/$USER/.android/release-key.keystore bin/$M-release-unsigned.apk jandroid
rm -f dist/$N.apk
zipalign 4 bin/$M-release-unsigned.apk dist/$N.apk
