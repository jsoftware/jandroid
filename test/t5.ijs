cocurrent 't5'

create=: 3 : 0
wd 'activity ',>coname''
)

onCreate=: 3 : 0
wd 'pc test'
wd 'menupop "&File"'
wd 'menu new "&New File"'
wd 'menu open "&Open File"'
wd 'menusep'
wd 'menu quit "Quit" "Ctrl+Q"'
wd 'menupopz'
wd 'menupop "&Edit"'
wd 'menu inputlog "Input &Log"'
wd 'menusep'
wd 'menupop "&Configure"'
wd 'menu base "&Base"'
wd 'menu launchpad "Launch &Pad"'
wd 'menupopz'
wd 'menusep'
wd 'menu sidebar "&Sidebar" "Ctrl+B"'
wd 'menu font "Session &Font"'
wd 'menupopz'
wd 'menupop "&Help"'
wd 'menu about "&About"'
wd 'menupopz'
NB. wd 'set sidebar checked 1'
NB. wd 'setenable font 0'
NB. wd 'cc list listbox'
NB. wd 'set list items one two three'
NB. wd 'pmove 700 10 300 200'
wd 'pshow'
)

NB. =========================================================
test_close=: 3 : 0
wd 'pclose'
)

cocurrent 'base'

''conew't5'

