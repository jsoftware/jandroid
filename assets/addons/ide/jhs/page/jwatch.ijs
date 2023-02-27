NB. 'jwatch' cojhs 'i.?10 10'

coclass'jwatch'
coinsert'jhs'

HBS=: 0 : 0
jhclose''
'run'jhb'run'
'sentence'jhtext'<SENTENCE>';30
'<div id="display" class="jcode"><TEXT></div>'
)

create=: 3 : 0
t=. y jpagedefault '5?5'
jsdata=: 'sentence';t;'display';calc t
)

jev_get=: jpageget

calc=: 3 : 0
try. r=. ":do__ y catch. r=. 13!:12'' end. 
if. 2=$$r do. r=. ,r,.LF end.
utf8_from_jboxdraw jhtmlfroma fmt0 r
)

ev_run_click=: 3 : 0
jhrjson 'r';calc getv'sentence'
)

JS=: 0 : 0
function ev_body_load(){
 jset('sentence',jsdata.sentence);
 jseth('display',jsdata.display);
}

function ev_run_click(){jdoj('sentence');}
function ev_run_click_ajax_json(t){jseth('display',t.r);}
function ev_sentence_enter(){jscdo('run');}
function ajax(ts){;}

)
