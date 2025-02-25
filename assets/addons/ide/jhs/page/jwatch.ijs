coclass'jwatch'
coinsert'jhs'

HBS=: 0 : 0
jhclose''
'run'jhb'run'
'sentence'jhtext'<SENTENCE>';30
'display'jhdiv''
)

ev_create=: 3 : 0
t=. y jpagedefault '5?5'
jhcmds ('set sentence *',t);'set display *',calc t
)

calc=: 3 : 0
try. r=. ":do__ y catch. r=. 13!:12'' end. 
if. 2=$$r do. r=. ,r,.LF end.
utf8_from_jboxdraw jhtmlfroma fmt0 r
)

ev_run_click=: {{ jhrcmds 'set display *',calc getv'sentence' }}

ev_sentence_enter=: ev_run_click

CSS=: 0 : 0
#sentence,#display{<PS_FONTCODE>}
)
