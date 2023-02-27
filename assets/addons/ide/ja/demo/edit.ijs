NB. alignment, password, readonly
NB.

coclass 'demoedit'


onStart=: edit

NB. =========================================================
edit=: 3 : 0
wd 'fontdef monospace 12'
wd 'pc edit'
wd 'bin v'
wd 'wh _1 _2;cc e0 edit'
wd 'wh _1 _2;cc e4 edit password'
wd 'wh _1 _2;cc e5 edit readonly'
wd 'wh _1 _2;cc e1 edit left'
wd 'wh _1 _2;cc e2 edit center'
wd 'wh _1 _2;cc e3 edit right'
wd 'bin sz'
wd 'pshow'
wd 'fontdef'
NB. wd 'set e0 limit 10'
wd 'set e0 text limit=10 (not yet works)'
wd 'set e1 text left'
wd 'set e2 text center'
wd 'set e3 text right'
wd 'set e4 text password'
wd 'set e5 text readonly'
)

NB. =========================================================
edit_e0_button=: 3 : 0
wd 'mb toast *e0 button event'
)

NB. =========================================================
edit_close=: 3 : 0
wd 'pclose'
)

NB. =========================================================
wd 'activity ', >coname''

