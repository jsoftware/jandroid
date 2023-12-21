NB. J HTTP Server - ijs app - textarea version

coclass'jijs'
coinsert'jhs'

HBS=: 0 : 0 rplc 'CMV';'4.2'
'<script src="~addons/ide/jhs/js/codemirror/codemirror.min.CMV.js"></script>'
'<script src="~addons/ide/jhs/js/codemirror/util/search.CMV.js"></script>'
'<script src="~addons/ide/jhs/js/codemirror/util/searchcursor.CMV.js"></script>'
'<script src="~addons/ide/jhs/js/codemirror/util/dialog.CMV.js"></script>'
'<script src="~addons/ide/jhs/js/codemirror/util/active-line.CMV.js"></script>'
'<link rel="stylesheet" href="~addons/ide/jhs/js/codemirror/codemirror.CMV.css">'
'<link rel="stylesheet" href="~addons/ide/jhs/js/codemirror/util/dialog.CMV.css">'
'<link rel="stylesheet" href="~addons/ide/jhs/js/codemirror/j/jtheme.CMV.css">'
'<script src="~addons/ide/jhs/js/codemirror/j/j.CMV.js"></script>'
jhclose''
jhma''
'action'   jhmg'action';1;11
 'save'     jhmab'save    s*'
 'saveas'   jhmab'save as...'
 'close'     jhmab'quit q^'
'run'      jhmg'run';1;11
 'runw'     jhmab'load     r*'
 'runwd'    jhmab'loadd'
 'lineadv'  jhmab'lineadv   .*'
 'line'     jhmab'line      ,*'
 'sel'      jhmab'selection /*'
'edit'     jhmg'edit';1;11
 'undo'     jhmab'undo    z*'
 'redo'     jhmab'redo    y*'
 'find'     jhmab'find    f*'
 'next'     jhmab'next    g*'
 'previous' jhmab'previous G*'
 'replace'  jhmab'replace F*'
 'repall'   jhmab'replaceall R*'
'option'    jhmg'option';1;8
 'ro'       jhmab'readonly    t^'
 'numbers'  jhmab'numbers'
jhmz''

'saveasdlg'    jhdivadlg''
 'saveasdo'    jhb'save as'
 'saveasx'     jhtext'';40
  'saveasclose'jhb'X'
'<hr></div>'

'rep'         jhdiv'<REP>'

'filename'    jhhidden'<FILENAME>'
'filenamed'   jhdiv'<FILENAME>'

jhresize''

'ijs'         jhtextarea'<DATA>';20;10

'textarea'    jhhidden''
)

NB. y file
create=: 3 : 0
rep=.''
try.
 d=. (1!:1<jpath y) rplc '&';'&amp;';'<';'&lt;'
 addrecent_jsp_ jshortname y
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

NB. save only if dirty
ev_save_click=: 3 : 0
'dirty line'=. <;._2 getv'jdata'
line=. 0".line
ln=. 2{line NB. line with caret
line=. ,/:~2 2$line NB. sorted selection
f=. getv'filename'
ta=. getv'textarea'
bta=. <;._2 ta,LF,LF NB. ensure trailing LF and extra one for emtpy last line
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
jhrajax JASEP,s,JASEP,":caret
)

ev_close_click=: ev_sel_click=: ev_line_click=: ev_lineadv_click=: ev_runw_click=: ev_runwd_click=: ev_save_click

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
#filenamed{color:blue;}
*{font-family:<PC_FONTFIXED>;}
#jresizeb{overflow:visible;border:solid;border-width:1px;clear:left;}
div{padding-left:0;}
/*.menu {margin-left:40px;}*/
)

JS=: 0 : 0
var fubar,ta,rep,readonly,saveasx,cm;

function ev_body_load()
{
 ce= jbyid("ijs");
 rep= jbyid("rep");
 ta= jbyid("textarea");
 saveasx=jbyid("saveasx");
 ce.focus();
 cm = CodeMirror.fromTextArea(ce,
  {lineNumbers: true,
   mode:  "j",
   tabSize: 1,
   gutter: false,
   styleActiveLine: {nonEmpty: true},
   extraKeys: {
    "Ctrl-S": function(instance){setTimeout(TOsave,1);},
    "Ctrl-R": function(instance){setTimeout(TOrunw,1);}
   }
  }
 );
 cm.on("change",setdirty);
 ro(0!=ce.innerHTML.length);
 dresize();
}

function TOsave(){jscdo("save");} // firefox needs ajax outside of event
function TOrunw(){jscdo("runw");}

window.onresize= dresize;

function dresize()
{
 // IE resize multiple frames sometimes gets id as null
 if(jbyid("jresizea")==null||jbyid("jresizeb")==null)return;
 var a= jgpwindowh(); // window height
 a-= jgpbodymh();     // body margin h (top+bottom)
 a-= jgpdivh("jresizea"); // header height
 a-= 5               // fudge extra
 a=  a<0?0:a;        // negative causes problems
 cm.setSize(jgpwindoww()-10,a);
}

// should be in jscore.js
function jgpwindoww()
{
 if(window.innerWidth)
  return window.innerWidth; // not IE
 else
  return document.documentElement.clientWidth;
}

function setdirty(){jbyid("filenamed").style.color="red";dirty=true;}
function setclean(){jbyid("filenamed").style.color="blue";dirty=false;}

function setnamed(){jbyid("filenamed").innerHTML=jbyid("filename").value;}

function ro(only)
{
 readonly= only;
 cm.setOption('readOnly', readonly?true:false)
 cm.getWrapperElement().style.background= readonly?"lightgray":"#fff";
 ce.focus();
}

function click(){
 ta.value= cm.getValue().replace(/\t/g,' ');
 s= cm.doc.listSelections();
 t= (dirty?"dirty":"clean")+JASEP;
 t= t+s[0].anchor.line+' '+s[0].anchor.ch+' '+s[0].head.line+' '+s[0].head.ch+JASEP;
 jdoajax(["filename","textarea","saveasx"],t);
}

function ev_save_click()    {click();}
function ev_runw_click()    {click();}
function ev_runwd_click()   {click();}
function ev_line_click()    {click();}
function ev_lineadv_click() {click();}
function ev_sel_click()     {click();}

function ev_undo_click(){cm.undo();}
function ev_redo_click(){cm.redo();}
function ev_find_click(){cm.execCommand("find");}
function ev_next_click(){cm.execCommand("findNext");}
function ev_previous_click(){cm.execCommand("findPrev");}
function ev_replace_click(){cm.execCommand("replace");}
function ev_repall_click(){cm.execCommand("replaceAll");}

function ev_saveasdo_click(){click();}
function ev_saveasx_enter() {click();}

function ev_saveas_click(){
 saveasx.value= jbyid("filename").value;
 jdlgshow("saveasdlg","saveasx");
 dresize();
}

function ev_saveasclose_click(){jhide("saveasdlg");dresize();}

function ev_ro_click(){ro(readonly= !readonly);}
function ev_numbers_click(){cm.setOption('lineNumbers',cm.getOption('lineNumbers')?false:true);}

// ajax response - ts[0] error ; ts[1] sentence ; advance_line ts[2]
function ajax(ts)
{
 rep.innerHTML= ts[0];
 jhide("saveasdlg");
 if(0!=ts[0].length)return;
 setclean(); // no report means save was done
 switch(jform.jmid.value){

 case 'close':
  window.close();
  break; 

 case 'saveasx':
 case 'saveasdo':
  jbyid("filename").value=ts[1];
  setnamed();
  var t=ts[1].split('/');
  document.title= t[t.length-1];
  break; 
 case 'lineadv':
  i= parseInt(ts[2]);
  if(i<cm.doc.lineCount()) cm.doc.setCursor(i,cm.doc.getLine(i).length);
 default:
  jijxrun(ts[1]); // run sentence in jijx
 }

 dresize();
}

function ev_ijs_enter(){return true;}

function ev_comma_ctrl(){jscdo("line");}
function ev_dot_ctrl()  {jscdo("lineadv");}
function ev_slash_ctrl(){jscdo("sel");}

function ev_z_shortcut(){cm.undo();}
function ev_y_shortcut(){cm.redo();}

function ev_t_shortcut(){jscdo("ro");}
function ev_r_shortcut(){jscdo("runw");}
function ev_s_shortcut(){jscdo("save");}
function ev_2_shortcut(){ce.focus();}

// override jscore.js defs
function ev_close_click(){if(dirty) click(); else window.close();}
function ev_close_click_ajax(){setclean();window.close();}

)
