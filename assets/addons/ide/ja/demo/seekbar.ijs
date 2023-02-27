NB. slider demo
NB.
NB. cc track slider [numeric options]
NB.
NB. numeric options are:
NB. step
NB. maximum
NB. position

NB.
NB. max,value should be integers

coclass 'demoseekbar'

onStart=: sliderdemo_run

NB. =========================================================
SLdemo=: 0 : 0
pc sliderdemo;
wh _1 _2; cc track seekbar 5 20 7;
)

NB. =========================================================
sliderdemo_close=: 3 : 0
wd 'pclose'
)

NB. =========================================================
sliderdemo_run=: 3 : 0
P=. jpath '~addons/ide/ja/images/'
wd SLdemo
wd 'pshow'
)

NB. =========================================================
wd 'activity ', >coname''
