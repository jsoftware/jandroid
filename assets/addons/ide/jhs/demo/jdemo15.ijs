coclass'jdemo15'
coinsert'jhs'
NB. override create, jev_get, saveonclose verbs to customize app

NB. html form definition
HBS=:  0 : 0
       jhclose''                  NB. redbar close    
       jhh1'Flip - no javascript' NB. header 1
'flip' jhb'flipem'                NB. button flip with label flipem    
't1'   jhtext'a ǂ text';10        NB. text field - note unicode char
't2'   jhtext'more ǂ text';10
       jhbr
       desc
       jhdemo''                     NB. link to open source file
)

NB. ids for id/value pairs for J handler with no javascript handler
JEVIDS=: jhjevids't1 t2'

ev_flip_click=: 3 : 0
't1 t2'=. getvs't1 t2' NB. t1 and t2 values
jhrcmds ('set t1 value *',8 u:|.7 u: t1);'set t2 value *',8 u:|.7 u: t2
)

ev_t1_enter=: ev_flip_click

desc=: 0 : 0
<hr>All events are passed to J handlers and no javascript code is required.<br><br>
Close with red button or Esc-q as this informs J server.
Tab/browser close does not inform J server.
)

NB. cascading style sheets - the look and feel of the document
CSS=: 0 : 0
form{margin:0px 2px 2px 2px;}
)

JS=: JEVIDS,0 : 0
// add custom javascript code and event handlers here
)
