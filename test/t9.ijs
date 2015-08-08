cocurrent 't9'

NB. nested scrollview doesn't work

create=: 3 : 0
wd 'activity ',>coname''
)

onCreate=: 3 : 0
wd 'pc form'
wd 'bin v'
wd 'bin h'
wd 'wh 150 150'
wd 'cc g1 image'
wd 'wh 150 150'
wd 'cc g2 image fitxy'
wd 'bin z'
wd 'bin h'
wd 'wh 150 150'
wd 'cc g3 image centercrop'
wd 'wh 150 150'
wd 'cc g4 image centerinside'
wd 'bin z'
wd 'bin h'
wd 'wh 150 150'
wd 'cc g5 image fitstart'
wd 'wh 150 150'
wd 'cc g6 image center'
wd 'bin z'
wd 'bin z'
wd 'pshow'
wd 'set g1 image /sdcard/a1.png'
wd 'set g2 image /sdcard/a1.png'
wd 'set g3 image /sdcard/a1.png'
wd 'set g4 image /sdcard/a1.png'
wd 'set g5 image /sdcard/a1.png'
wd 'set g6 image /sdcard/a1.png'
EMPTY
)

cocurrent 'base'

''conew't9'

