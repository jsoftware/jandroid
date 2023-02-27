NB.  'jco' cojhs 3 5

coclass'jco'
coinsert'jhs'

HBS=: 0 : 0
jhclose''
'run'   jhb    'run'
'count' jhtext '0';5
)

create=: 3 : 0
t=. y jpagedefault 5 7
'must be 2 numbers'assert (2=#ty)*.2~:3!:0 t
base=: {.t
jsdata=: 'count';{:t
)

jev_get=: jpageget

ev_run_click=: 3 : 0
jhrjson 'r';":base+0".getv'count' NB. count from jdoajax
)

JS=: 0 : 0
function ev_body_load(){jset('count',jsdata.count);} // initialize from jsdata dictionary

function ev_run_click(){jdoj('count');}
function ev_run_click_ajax_json(t){jset('count',t.r);}
function ev_count_enter(){jscdo('run');}

)
