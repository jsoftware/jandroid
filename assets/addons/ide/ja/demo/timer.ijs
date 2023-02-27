NB. timer
NB.
NB. argument is milliseconds

coclass 'demotimer'

onStart=: timer_run

timer_run=: 3 : 0
wd 'pc timer'
wd 'bin v'
wd 'bin h'
wd 'cc b1 button; cn start'
wd 'cc b2 button; cn stop'
wd 'bin z'
wd 'cc e static'
wd 'bin z'
wd 'pshow'
)

NB. =========================================================
timer_b1_button=: 3 : 0
wd 'ptimer 2000'
)

NB. =========================================================
timer_b2_button=: 3 : 0
wd 'ptimer 0'
)

NB. =========================================================
timer_timer=: 3 : 0
wd 'set e text ',(":6!:0$0)
)

wd 'activity ', >coname''

