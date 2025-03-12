NB. J HTTP Server - ijs app - textarea version

coclass'jijs'
coinsert'jhs'

HBS=: 0 : 0
'<script type="module" src="~addons/ide/jhs/js/jsoftware/editor.js"></script>'

'saveasdlg'    jhdiva''
 'saveasdo'    jhb'save as'
 'saveasx'     jhtext'';40
  'saveasclose'jhb'X'
'<hr></div>'

'rep'         jhdiv'<REP>'

'filename'    jhhidden'<FILENAME>'
'filenamed'   jhdiv'<FILENAME>'

jhresize''

'cm6_editor'   jhdiv''
'ijs'         jhtextarea'<DATA>';20;10
'textarea'    jhhidden''


NB. menu must come after codemirror
jhmenu''
'menu0'  jhmenugroup ''
         jhmpage''
'save'   jhmenuitem 'save';'^s'
'saveas' jhmenuitem 'save as ...'
'runw'   jhmenuitem 'load';'^r'
'runwd'  jhmenuitem 'loadd'

NB. 'lineadv' jhmenuitem 'lineadv';'^.'
NB. 'line'    jhmenuitem 'line';'^*'
NB. 'sel'     jhmenuitem 'selection';'^/'
'chelp'   jhmenuitem 'context sensitive';'h'

          jhmenulink 'edit';'edit'
 'ro'      jhmenuitem 'readonly';'t'
 'numbers' jhmenuitem 'numbers'
 'theme' jhmenuitem 'theme'
'close'     jhmenuitem 'close';'q'
jhmenugroupz''

jhmpagez''

'edit' jhmenugroup''
NB. cut/copy/paste do not have cm.commands - only ctrl+xcv
NB. cut/copy/paste for touch - not supported in codemirror
'undo'    jhmenuitem 'undo';'^z'
'redo'    jhmenuitem 'redo';'^y'

'find'     jhmenuitem 'find';'^f'
'next'     jhmenuitem 'next';'^g'
'previous' jhmenuitem 'previous';'^G'
'replace'  jhmenuitem 'replace';'^F'
'repall'   jhmenuitem 'replaceall';'^R'
jhmenugroupz''


)

NB. y file
create=: 3 : 0
y=. jshortname y
rep=.''
try.
 d=. (1!:1<jpath y) rplc '&';'&amp;';'<';'&lt;'
 addrecent_jsp_ y
catch.
 d=. ''
 rep=. 'file read failed ',(ftype y){::'(does not exist)';'';'(it is a folder)'
end.
(jgetfile y) jhr 'FILENAME REP DATA';y;rep;d
)

NB. new way - jwid=~temp/foo.ijs
NB. old way - mid=open&path=...
jev_get=: 3 : 0
if. #getv'jwid' do.
 create getv'jwid'
elseif. 'open'-:getv'mid' do.
 create getv'path' 
elseif. 1 do.
 create jnew''
end.
)

0 : 0
line/lineadv/selection support removed
conflict with spa
spa save/load/loaded need immediate error report in spa
)

NB. save only if dirty
ev_save_click=: 3 : 0
'dirty line'=. <;._2 getv'jdata'
line=. 0".line
ln=. 2{line NB. line with caret
line=. ,/:~2 2$line NB. sorted selection
f=. getv'filename'
ta=. getv'textarea'
bta=. <;._2 ta,LF,LF NB. ensure trailing LF and extra one for emtpy last line
if. 'chelp'-:getv'jmid' do.
 'a b'=. 2{.line
 t=. dltb;{.;:b}.;a{bta
 t=. ;(t-:''){t;'voc'
 s=. 'jhswiki''',t,''''
 jhrajax JASEP,s,JASEP,":0
 return.
end.

if. dirty-:'dirty' do.
 mkdir_j_ (f i:'/'){.f
 r=. (toHOST ta)fwrite f
 if. r<0 do. jhrajax'file save failed' end.
end. 

caret=. >:ln
s=. ''
select. getv'jmid'
case. 'runw'           do. s=. 'load ''',f,''''
case. 'runwd'          do. s=. 'loadd ''',f,''''
case. 'line';'lineadv' do.
 s=. 'tell_jhs_ ',ln{::bta
 if. iscolon s  do. NB. collect : lines
  c=. >:(ln}.bta) i. <,')'
  caret=. ln+c
  d=. c{.ln}.bta
  s=. ;d,each LF
 end.
case. 'sel'    do.
 n=. >:--/0 2{ line NB. lines to select
 d=. n{.({.,line)}.bta
 i=. <:#d
 d=. (<(3{line){.i{:: d) i}d NB. drop trailing chars not in sel
 d=. (<(1{line)}.0{::d)  0}d     NB. drop leading chars not in sel
 s=. ;d,each LF
end.

e=. ''
try. do__ s catch. e=. 13!:12'' end. 
 jhrajax e,JASEP,s,JASEP,":caret
)

ev_close_click=: ev_sel_click=: ev_line_click=: ev_lineadv_click=: ev_runw_click=: ev_save_click
ev_runwd_click=: ev_chelp_click=: ev_save_click

ev_saveasdo_click=:ev_saveasx_enter

NB. should have replace/cancel option if file exists
ev_saveasx_enter=: 3 : 0
f=. jpath getv'saveasx'
if. f-:jpath getv'filename' do. jhrajax'same name' end.
if. fexist f do. jhrajax'already exists' return. end.
if. '~'={.f do. jhrajax'~ bad name' return. end.
try.
 mkdir_j_ (f i:'/'){.f
 r=. (toHOST getv'textarea')fwrite f
 addrecent_jsp_ f
 jhrajax JASEP,jshortname f
catch.
 jhrajax 'save failed'
end.
)

NB. new ijs temp filename
jnew=: 3 : 0
d=. 1!:0 jpath '~temp\*.ijs'
a=. ":>:>./0, {.@:(0&".)@> _4 }. each {."1 d
NB. a=. ": {. (i. >: #a) -. a
f=. <jpath'~temp\',a,'.ijs'
'' 1!:2 f
>f
)

NB. 1+ >./0,;0 ". each _4}.each {."1 d

NB. jdoajax load/loadd need response - mimic jijx
urlresponse=: 3 : 0
jhrajax''
)

NB. from spx.ijs
iscolon=: 3 : 0
t=. ;:y
if. (<'define')e.t do. 1 return. end.
i=. t i. <,':'
(,each':';'0')-:(i+0 1){t,'';''
)

NB. p{} klduge because IE inserts <p> instead of <br> for enter
NB. codemirror needs jresizeb without scroll
NB. codemirror requires no div padding (line number vs caret) so set padding-left:0
NB. see activeline-background in util/jheme.4.2.css
CSS=: 0 : 0
#rep{color:red}
#filenamed{color:blue;background-color:white;}
#saveasdlg{display:none;}
*{font-family:<PC_FONTFIXED>;font-weight:550;}
#jresizeb{overflow:visible;border:solid;border-width:1px;clear:left;}
#ijs { display:none; }
div{padding-left:0;}
#cm6_editor { height: 100vh; }
)

JS=: fread JSPATH,'jijs.js'
