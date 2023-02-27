NB. util.ijs

wd 'notacommand'  NB. error
wd 'qer'

wd 'pc abc'
[abc=: wd 'qhwndp'
wd 'pc def'
[def=: wd 'qhwndp'

wd 'psel'
wd 'qhwndp'       NB. error
wd 'qer'
wd 'psel def'
def -: wd 'qhwndp'
wd 'psel abc'
abc -: wd 'qhwndp'
wd 'psel ',def
def -: wd 'qhwndp'
