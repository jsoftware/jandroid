NB. image demo

coclass 'demoimage'

onStart=: imdemo_run

NB. =========================================================
imdemo_run=: 3 : 0
if. -. checkrequire_jademo_ 'bmp';'graphics/bmp' do. return. end.
require 'bmp'
wd 'pc imdemo closeok escclose'
wd 'bin v'
wd 'cc pic image'
wd 'cc jpg image '
wd 'cc png image fitxy'
wd 'cc blue image center'
wd 'bin z'
wd 'pshow'
wd 'set pic image *',jpath '~addons/graphics/bmp/toucan.bmp'
NB. convert to jpg
d=. readimg_ja_ jpath '~addons/graphics/bmp/toucan.bmp'
writeimg_ja_ d; (jpath '~temp/toucan.jpg');'jpeg'
wd 'set jpg image *',jpath '~temp/toucan.jpg'
NB. flip and save as png
d1=. |."1 d
(<jpath '~temp/toucan.png') 1!:2~ putimg_ja_ d1;'png'
wd 'set png image *',jpath '~temp/toucan.png'
NB. pure blue
d2=. setalpha 20 200$255
writeimg_ja_ d2;(jpath '~temp/blue.png');'png'
wd 'set blue image *',jpath '~temp/blue.png'
)

wd 'activity ', >coname''

