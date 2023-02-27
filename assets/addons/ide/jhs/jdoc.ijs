coclass'jdoc'
coinsert'jhs'

HBS=: 0 : 0
jhclose''
'title'jhtitle'JHS framework'
'jhs locale - utils - verbs/nouns/... for working with JHS'
jhbr
mfix    '';'general utils';'~addons/ide/jhs/util.ijs'
mfix'html';'html elements';'~addons/ide/jhs/utilh.ijs'
mfix  'js';'javascript utils';'~addons/ide/jhs/utiljs.ijs'
)

CSS=: 0 : 0
form{margin:10px;}
div{font-family:<PC_FONTFIXED>;white-space:pre;padding-left:10px}
)

fix=: 3 : 0
t=. doc y
t=. t rplc '<';'&lt;'
''jhdiv t;'jcode'
)

mfix=: 3 : 0
'doc header url'=. y
t=. ''jhline''
t=. t,''jhhn 3;header
t=. t,fix doc
)

create=: 3 : 0
jsdata=: ''
)

jev_get=: jpageget  

