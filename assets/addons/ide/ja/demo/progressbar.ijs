NB. progressbar demo
NB.
NB. cc prog progressbar [style] [max] [value];
NB.
NB. style : horizontal inverse large large_inverse small small_inverse
NB.    style must be horizontal to show progress status
NB.
NB. max,value should be integers

coclass 'demoprogressbar'

onStart=: pbdemo_run

NB. =========================================================
PBdemo=: 0 : 0
pc pbdemo closeok escclose;
wh _1 _2;cc prog progressbar horizontal 20 7;
)

NB. =========================================================
pbdemo_run=: 3 : 0
wd PBdemo
wd 'pshow'
)

NB. =========================================================
wd 'activity ', >coname''
