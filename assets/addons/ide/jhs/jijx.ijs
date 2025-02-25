NB. J HTTP Server - jijx app
coclass'jijx'
coinsert'jhs'

HBS=: 0 : 0
jhmenu''

'menu0'  jhmenugroup ''
         jhmpage''
         jhmenulink 'devs';'dev tools'
         jhmenulink 'options';'options'         
         jhmenulink 'view';'view'
'jinputs'jhmenuitem  'inputs';'d'
'jbreak' jhmenuitem 'break';'c'
         jhmenulink 'build';'build apps'
         jhmenulink 'labs';'labs and tools'
         jhmenulink 'help';'help'
'closepages' jhmenuitem 'close term pages/tabs'
'close'     jhmenuitem 'quit';'q'
jhmenugroupz''

jhmpagez''

'devs'     jhmenugroup''
'jfile'    jhmenuitem 'file explorer';'e'
'jfif'     jhmenuitem 'find in files';'f'
'jijs'     jhmenuitem 'edit new temp file';'n'
'jlocale'  jhmenuitem 'locale explorer';'l'
'jcopy'    jhmenuitem 'copy files server/client'
'jpacman'  jhmenuitem 'package manager'
'jdebug'   jhmenuitem 'debug'
jhmenugroupz''

'options' jhmenugroup''
'wrap'    jhmenuitem 'NOWRAP ➜ wrap'
'jmtoggle'jhmenuitem 'TERM ➜ tab'
jhmenugroupz''

'build'  jhmenugroup''
'app'    jhmenuitem'app (build gui app)'
'page'   jhmenuitem'app examples'
'demo'   jhmenuitem'more app examples'
'react'  jhmenuitem'react'
jhmenugroupz''

'labs'    jhmenugroup''
'tour'         jhmenuitem'tour (JHS tutorial)'
'lab'          jhmenuitem'lab (tutorial)'
'tool'         jhmenuitem'tool'
jhmenugroupz''

'view'         jhmenugroup''
'cleartemps'   jhmenuitem 'remove red boxes';'s'
'clearwindow'  jhmenuitem 'clear window'
'clearrefresh' jhmenuitem 'clear refresh'
'clearLS'      jhmenuitem 'clear LS'
jhmenugroupz''

'help' jhmenugroup''
'welcome'            jhmenuitem 'welcome'
'guest_rules'        jhmenuitem 'guest rules'
'guest_files'        jhmenuitem 'guest files'
 'mobile'            jhmenuitem 'mobile'
 'wiki'              jhmenuitem 'wiki';'p'
 'shortcuts'         jhmenuitem 'shortcuts' 
 'popups'            jhmenuitem 'pop-ups'
 'closing'           jhmenuitem 'close'
 'framework'         jhmenuitem 'framework'
 'about'             jhmenuitem 'about' 
jhmenugroupz''

'quit' jhmenugroup''
jhhr
'exit'               jhmenuitem 'exit server';'z'
jhmenugroupz''

jhdivz NB. base div close - flex active
NB. elements not in main div share remaining space
'log' jhec'<LOG>'
NB. touch screen button on right side
'uarrow' jhb '';'jhtouch'
'darrow' jhb '';'jhtouch'
'advance'jhb '';'jhtouch'
jhdiva''                       NB. reopen main div
)

jev_get=: create

NB. move new transaction(s) to log
uplog=: 3 : 0
LOG_jhs_=: LOG,LOGN
LOGN_jhs_=: ''
)

NB. y is J prompt - '' '   ' or '      '
NB. called at start of input
NB. ff/safari/chrome collapse empty div (hence bull)
NB. empty prompt is &bull; which is removed if present from input
urlresponse=: 3 : 0
if. 0=#y do.
 t=. JZWSPU8
 PROMPT_jhs_=: JZWSPU8
else.
 t=. (6*#y)$'&nbsp;'
 PROMPT_jhs_=: y
end.
t=. '<div id="prompt" class="log"  onpaste="mypaste(event)">',t,'</div>'
d=. LOGN,t
uplog''
if. METHOD-:'post' do.
 if. CHUNKY do.
  CHUNKY_jhs_=: 0
  jhrajax_z d
 else.
  jhrajax d
 end. 
else.
 create''
end.
)

NB. refresh response - not jajax
create=: 3 : 0
uplog''
'term' jhr 'LOG';LOG
)

ev_advance_click=: 3 : 0
select. ADVANCE
case. 'spx' do. spx__''
case. 'lab' do. lab 0
case. 'wiki'do. wikistep_jsp_''
case.       do. echo 'no open lab/spx to advance'
end.
)

jloadnoun_z_=: 0!:100

ev_clearrefresh_click=: 3 : 'LOG_jhs_=: '''''

ev_about_click=: 3 : 0
jhtml'<hr/>'
echo JVERSION
echo' '
echo'Copyright 1994-2024 Jsoftware Inc.'
jhtml'<hr/>'
)

NB. aws server window.close fails (depends on how started)
ev_close_click=: 3 : 0
select. QRULES
case. 0 do. NB. localhost    - close pages, exit server, close jterm
 jhrajax''
 exit''
case. 1 do. NB.server user   - close pages, no exit, window.location=juser
 jhrajax 'juser>' NB. causes set of window.location
case. 2 do. NB. server guest - close pages, exit server, window.location=jguest
 exit'' NB. no jhrajax triggers jguest page
end.
)

ev_comma_ctrl =: 3 : 'sp__'''''
ev_dot_ctrl=: ev_advance_click
ev_slash_ctrl  =: 3 : 'i.0 0'
ev_less_ctrl   =: 3 : 'i.0 0'
ev_larger_ctrl =: 3 : 'i.0 0'
ev_query_ctrl =: 3 : 'i.0 0'
ev_semicolon_ctrl =:   3 : 'loadx__ 0'
ev_colon_ctrl =:       3 : 'echo''colon'''
ev_quote_ctrl_jijx_=: 3 : 'dbover dbxup'''''
ev_doublequote_ctrl =: 3 : 'dbinto dbxup'''''

load'~addons/ide/jhs/loadx.ijs'

NB. #ijs{z-index:2;display:none;width:100%;height:100%;resize:vertical;overflow:auto}  /* display:none; */

CSS=: 0 : 0 

#log{width:100%;height:100%;resize:vertical;overflow:auto;}

*{font-family:<PC_FONTFIXED>;font-weight:550;}
form{margin-top:0;margin-bottom:0;}
*.fm   {color:<PC_FM_COLOR>;}
*.er   {color:<PC_ER_COLOR>;}
*.log  {color:<PC_LOG_COLOR>;}
*.sys  {color:<PC_SYS_COLOR>;}
*.file {color:<PC_FILE_COLOR>;}

.jhb#overview{background-color:<PC_JICON>;font-weight:bold;font-size:2rem;padding:0.2rem;}
#prompt{background-color:blanchedalmond;border:2px solid black;padding:8px 0 8px 0;}

/* touch screen right side buttons */
.jhtouch{background-color:rgba(255,255,255,0);position:fixed;right:0;margin:0px;border-radius:0;
 width:4rem;height:3rem;border-width:0 0.5rem 0 0;border-style:solid;} /* border-style ??? */

.jhtouch#advance{border-color:darkseagreen;}
.jhtouch#uarrow {border-color:blue;}
.jhtouch#darrow {border-color:red;}

/* all except mobile - assume no kb movement */
.jhtouch#advance{bottom:13rem;}
.jhtouch#uarrow {bottom:10rem;}
.jhtouch#darrow {bottom:7rem;}

/* tablet */
@media screen and (max-device-width: 992px){
#prompt{padding:10px 0 10px 0;}
.jhtouch#advance{bottom:26rem;}
.jhtouch#uarrow {bottom:23rem;}
.jhtouch#darrow {bottom:20rem;}
}

/* phone */
@media screen and (max-device-width: 640px){
#prompt{padding:36px 0 36px 0;}
.jhtouch#advance{bottom:14rem;}
.jhtouch#uarrow {bottom:11rem;}
.jhtouch#darrow {bottom: 8rem;}
}

)

INC=: INC_chartjs NB. include chart js code

JS=: ('var qrules= ',":QRULES),LF,fread JSPATH,'jijx.js'
