cocurrent 't7'

create=: 3 : 0
wd 'activity ',>coname''
)

onCreate=: 3 : 0
wd 'pc form'
wd 'bin v'
wd 'bin h'
wd 'cc b1 button'
wd 'cc b2 button'
wd 'cc b3 button'
wd 'cc b4 button'
wd 'bin z'
wd 'bin h'
wd 'cc b5 button'
wd 'cc b6 button'
wd 'cc b7 button'
wd 'cc b8 button'
wd 'bin z'
wd 'bin h'
wd 'cc b9 button'
wd 'cc b10 button'
wd 'cc b11 button'
wd 'bin z'
wd 'bin z'
wd 'pshow'
EMPTY
)

form_b1_button=: 3 : 0
wd 'mb toast "hello j short"'
)

form_b2_button=: 3 : 0
wd 'mb toast "hello j long" 1'
)

form_b3_button=: 3 : 0
wd 'mb about "about j"'
)

form_b4_button=: 3 : 0
wd 'mb info "info j" what but'
)

form_b5_button=: 3 : 0
wd 'mb info "" "info j" why'
)

form_b6_button=: 3 : 0
wd 'mb query "" "query"'
)

form_b7_button=: 3 : 0
wd 'mb query "" "query" pos'
)

form_b8_button=: 3 : 0
wd 'mb query "" "query" pos neg'
)

form_b9_button=: 3 : 0
wd 'mb query "" "query" pos neg neu'
)

form_b10_button=: 3 : 0
wd 'mb query "" "query" "" "" neu'
)

form_b11_button=: 3 : 0
wd 'mb query "" "query"'
)

form_dialog_positive=: 3 : 0
wd'mb toast pos'
)

form_dialog_negative=: 3 : 0
wd'mb toast neg'
)

form_dialog_neutral=: 3 : 0
wd'mb toast neu'
)

cocurrent 'base'
NB. showevents_jca_ 2

''conew't7'

