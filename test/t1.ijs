cocurrent 't1'

create=: 3 : 0
wd 'activity ',>coname''
)

onCreate=: 3 : 0
wd 'pc form'
wd 'pn test1'
wd 'bin v'
wd 'weight 1'
wd 'wh _1 _2'
wd 'cc b button'
wd 'weight 2'
wd 'wh 200 _2'
wd 'cc b1 button'
wd 'bin z'
wd 'bin s'
wd 'bin h'
wd 'wh _2 _2'
wd 'cc e edit'
wd 'bin s'
wd 'bin v'
wd 'cc d edit'
wd 'cc c button'
wd 'weight 1'
wd 'wh 100 0'
wd 'cc f button'
wd 'weight 3'
wd 'wh 100 0'
wd 'cc g button'
wd 'bin z'
wd 'bin s'
wd 'bin z'
wd 'pshow'
wd 'set b text button'
wd 'set e text edit'
wd 'set c text button2'
wd 'set d text edit2'
NB. smoutput wd 'version'
NB. smoutput wd'get e text'
EMPTY
)

form_b1_button=: 3 : 0
smoutput 'b1'
smoutput wd'qform'
smoutput wd'qchildxywh b1'
)

form_b_button=: 3 : 0
smoutput 'b1'
smoutput wdq
smoutput wdforms''
wd 'set e text *', 'cat',~ wd'get e text'
)

form_e_button=: 3 : 0
smoutput 'e1'
smoutput wdq
)

cocurrent 'base'

''conew't1'

