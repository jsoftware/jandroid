NB. J HTTP Server - jijx app
coclass'jijx'
coinsert'jhs'

HBS=: 0 : 0
jhclose''
jhma''
jhjmlink''
'tool'   jhmg'tool';1;16
 'app'     jhmab'app'
 'demo'    jhmab'demo'
 'chart'   jhmab'plot-chart'
 'jd3'     jhmab'plot-d3'
 'debug'   jhmab'debug'
 'debugjs' jhmab'debug javascript'
 'react'   jhmab'react'
 'node'    jhmab'https'
 'print'   jhmab'print'
 'sp'      jhmab'sp'
 'table'   jhmab'table'
 'watch'   jhmab'watch'
'tour'     jhmg'tour';1;9
 'overview'jhmab'overview'
 'charttour'jhmab'chart'
 'canvas'  jhmab'canvas'
 'plot'    jhmab'plot'
 'spx'     jhmab'spx'
 'labs'    jhmab'labs'
'help'               jhmg'help';1;16
 'welcome'           jhmab'welcome'
 'shortcuts'         jhmab'shortcuts' 
 'popups'            jhmab'pop-ups'
 'closing'           jhmab'close'
 'framework'         jhmab'framework'
 'helpwikijhs'       jhmab'JHS'
 'helpwikinuvoc'     jhmab'vocabulary'
 'helpwikiconstant'  jhmab'constant'
 'helpwikicontrol'   jhmab'control'
 'helpwikiforeign'   jhmab'foreign'
 'helpwikiancillary' jhmab'ancillary'
 'helpwikistdlib'    jhmab'standard library'
 'helpwikirelnotes'  jhmab'release notes'
 'helphelp'          jhmab'807 html legacy'
 'wiki'              jhmab'wiki look up'
 'about'             jhmab'about'
'adv'jhmg '>';0;10
jhmz''
jhresize''
'log' jhec'<LOG>'
'ijs' jhhidden'<IJS>'
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
t=. '<div id="prompt" class="log">',t,'</div>'
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

NB. next ijs temp file number
nexttemp=: 3 : 0
>./;0".each _4}.each{."1 [1!:0 jpath '~temp\*.ijs'
)

NB. refresh response - not jajax
create=: 3 : 0
uplog''
'jijx' jhr 'LOG IJS';LOG;":nexttemp''
)

ev_advance_click=: 3 : 0
select. ADVANCE
case. 'spx' do. spx__''
case. 'lab' do. lab 0
case.       do. echo 'no open lab/spx to advance'
end.
)

jloadnoun_z_=: 0!:100

ev_clearrefresh_click=: 3 : 'LOG_jhs_=: '''''

wikilu=: 0 : 0
look up things in the wiki
   wiki''    NB. vocabulary
   wiki'i.'  NB. i. y (click Dyad for x i. y)
   wiki'if.' NB. control words
   wiki'12x' NB. constants
   wiki'a'   NB. ancilliary 
)

ev_wiki_click=: 3 : 0
jhtml'<hr/>'
echo wikilu
jhtml'<hr/>'
)

ev_about_click=: 3 : 0
jhtml'<hr/>'
echo JVERSION
echo' '
echo'Copyright 1994-2022 Jsoftware Inc.'
jhtml'<hr/>'
)

tour=: 4 : 0
jhtml'<hr>'
echo x
spx '~addons/ide/jhs/spx/',y
jhtml'<hr/>'
)

ev_plot_click=:  3 : 0
'plot tour'tour'plot.ijs'
)

ev_overview_click=: 3 : 0
'overview tour'tour'overview.ijs'
)

ev_charttour_click=: 3 : 0
'chart tour'tour'chart.ijs'
)

ev_canvas_click=: 3 : 0
'canvas tour'tour'canvas.ijs'
)

ev_spx_click=:  3 : 0
'spx tour'tour'spx.ijs'
)

NB. default ctrl+,./ handlers
ADVANCE=: 'none'
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

jhjmlink=: 3 : 0
t=.   'jmlink' jhmg 'ide';1;13
t=. t,'jfile'  jhmab'jfile    f^'
t=. t,JIJSAPP  jhmab'jijs     J^'
t=. t,'jpacman'jhmab'jpacman'
t=. t,'jdebug' jhmab'jdebug'

if. 1=#gethv'node-jhs:' do.
 t=. t,'jlogoff' jhmab'logoff'
 t=. t,'jbreak'  jhmab'break'
end.

t=. t,'clearwindow'jhmab'clear window'
t=. t,'clearrefresh'jhmab'clear refresh'
t=. t,'clearLS'jhmab'clear LS'
t=. t,'close'jhmab'quit q^'
t
)

CSS=: 0 : 0
*{font-family:<PC_FONTFIXED>;}
form{margin-top:0;margin-bottom:0;}
*.fm   {color:<PC_FM_COLOR>;}
*.er   {color:<PC_ER_COLOR>;}
*.log  {color:<PC_LOG_COLOR>;}
*.sys  {color:<PC_SYS_COLOR>;}
*.file {color:<PC_FILE_COLOR>;}
)

NB. *#log:focus{border:1px solid red;}
NB. *#log:focus{outline: none;} /* no focus mark in chrome */

JS=: 0 : 0
var allwins= []; // all windows created by jijx
var phead= '<div id="prompt" class="log">';
var ptail= '</div>';
var globalajax; // sentence for enter setTimeout ajax
var TOT= 1;     // timeout time to let DOM settle before change
//var TOT= 100; // might need more time on slow devices???
var wjdebug= null; // jdebug window object

function ev_body_focus(){if(!jisiX)setTimeout(ev_2_shortcut,TOT);}

function ev_body_load()
{
 jijxwindow= window;
 window.name= "jijx";
 jseval(false,jbyid("log").innerHTML); // redraw canvas elements
 newpline("   ");
 jresize();
}

function isdirty(){return 0!=allwins.length;}

var setvkb = function()
{
 var sx = document.body.scrollLeft, sy = document.body.scrollTop;
 var naturalHeight = window.innerHeight;
 window.scrollTo(sx, document.body.scrollHeight);
 VKB= naturalHeight - window.innerHeight;
 window.scrollTo(sx, sy);
 jresize();
}

function setfocus(){jbyid("log").focus();}

// iX must get VKB and resize when log gets focus
// iX does not call onfocus the first time - so we also do setvkb in enter
function jecfocus()
{
 if(jisiX)
 { 
  setTimeout(setvkb(),TOT);
  setTimeout(scrollz(),TOT);
 }
}

// iX must get VKB and resize when log loses focus
function jecblur(){if(jisiX)setTimeout(setvkb(),TOT);}

// remove id - normally 1 but could be none or multiples
// remove id parent if removal of id makes it empty
function removeid(id)
{
 var parnt;
 while(1)
 {
  p= jbyid(id);
  if(null==p) break;
  parnt= p.parentNode;
  parnt.removeChild(p);
  if(0==parnt.childNodes.length) parnt.parentNode.removeChild(parnt);
 }
}

function updatelog(t)
{
 var n= document.createElement("div");
 n.innerHTML= jjsremove(t);
 removeid("prompt");
 jbyid("log").appendChild(n);
 setTimeout(scrollz,TOT); // allow doc to update
}

function scrollz()
{
 setfocus(); // required by ff
 if(null==jbyid("prompt"))return;
 jsetcaret("prompt",1);
 jbyid("prompt").scrollIntoView(false);
}

function scrollchunk(){jbyid("chunk").scrollIntoView(false);}

// ajax update window with new output
function ajax(ts)
{
 var a= ts[0];
 updatelog(a);
 jseval(true,a);
}

// ajax update window with new output
function ev_log_enter_ajax()
{
 a= rq.responseText.substr(rqoffset);
 updatelog(a);
 jseval(true,a);
}


function ev_log_enter_ajax_chunk()
{
 // jijx echo marks end of chunk with <!-- chunk --> 
 jbyid("log").blur();
 var i=rq.responseText.lastIndexOf("<!-- chunk -->");
 if(i>rqoffset) // have a chunk
 {
  if(rqoffset==0) removeid("prompt"); // 1st chunk contains input line so must remove original
  rqchunk= rq.responseText.substr(rqoffset,i-rqoffset);
  
  //var a= jssplit(rqchunk);
  //alert("chunk");
  //alert(a[0]);
  //alert(a[1]);
  
  
  rqoffset= i+14; // skip <!-- chunk -->
  var n= document.createElement("div");
  removeid("chunk");
  n.innerHTML= jjsremove(rqchunk)+'<div id="chunk"></div>';
  jbyid("log").appendChild(n);
  jseval(true,rqchunk);
  setTimeout(scrollchunk,1);
 } 
}

function ev_2_shortcut(){scrollz();}

function newpline(t)
{
 t= t.replace(/&/g,"&amp;");
 t= t.replace(/</g,"&lt;");
 t= t.replace(/>/g,"&gt;");
 t= t.replace(/ /g,"&nbsp;");
 t= t.replace(/-/g,"&#45;");
 t= t.replace(/\"/g,"&quot;");
 updatelog(phead+t+ptail);
}

// function keyp(){jbyid("kbsp").style.display= "block";scrollz();return true;} // space for screen kb

function ev_up_click(){uarrow();}
function ev_dn_click(){darrow();}

// log enter - contenteditable
// run or recall line with caret
// ignore multiple-line selection for now
function ev_log_enter()
{
 var t,sel,rng,tst,n,i,j,k,p,q,recall=0,name;
 if(jisiX)setvkb();
 if(window.getSelection)
 {
  sel= window.getSelection();
  rng= sel.getRangeAt(0);
  if(0!=rng.toString().length)return;
  jdominit(jbyid("log"));
  q=rng.commonAncestorContainer;

  for(i=0;i<jdwn.length;++i) // find selection in dom
   if(q==jdwn[i])break;

  for(j=i;j>=0;--j)   // backup find start DIV/P/BR
  {name=jdwn[j].nodeName;if(name=="DIV"||name=="BR"||name=="P")break;}

  for(k=i+1;k<jdwn.length;++k) // forward to find end DIV/P/BR or end
  {name=jdwn[k].nodeName;if(name=="DIV"||name=="BR"||name=="P")break;}

  rng.setStart(jdwn[j],0);
  recall=!(k==jdwn.length||k==jdwn.length-1);
  if(!k==jdwn.length)
    rng.setEnd(jdwn[k],0);
  else
    rng.setEndAfter(jdwn[k-1],0)
  t= rng.toString();
  t= t.replace(/\u00A0/g," "); // &nbsp;
 }
 else
 {
  p= jbyid("prompt");
  rng= document.selection.createRange();
  t= rng.text;
  if(0!=t.length) return; // IE selection has LF, but ignore for now
  // IE -  move left until CR or NaN - move right til no change
  while(1)
  {
    rng.moveStart('character',-1);
    t= rng.text;
    if(t.charAt(0)=='\r'||isNaN(t.charCodeAt(0))){rng.moveStart('character',1); break;}
  }
  while(1)
  {
    n= rng.text.length; // no size change for CRLF
    rng.moveEnd('character',1);
    if(n==rng.text.length){rng.moveEnd('character',-1); break;}
  }
  t= rng.text;
  if(null!=p)// last line exec vs old line recall
  {
   tst= document.selection.createRange();
   tst.moveToElementText(jbyid("prompt"));
   tst.collapse(true);
   recall= -1!=tst.compareEndPoints("EndToEnd",rng);
  }
 }
 if(recall)
 {
  t= t.replace(/ /g,"\u00A0"); // space -> &nbsp;
  newpline(t);
 }
 else
 {
  adrecall("document",t,"-1");
  globalajax= t;
  setTimeout(TOajax,TOT);
 }
}

// firefox can't do ajax call withint event handler (default action runs)
function TOajax(){jdoajax([],"",globalajax,true);}

function document_recall(v){newpline(v);}

function ev_advance_click(){jdoajax([]);}

function ev_print_click() {jdoajax([]);}
function ev_app_click() {jdoajax([]);}
function ev_demo_click(){jdoajax([]);}
function ev_j1_click(){jdoajax([]);}
function ev_j2_click(){jdoajax([]);}
function ev_j3_click(){jdoajax([]);}
function ev_plot_click(){jdoajax([]);}
function ev_overview_click(){jdoajax([]);}
function ev_canvas_click(){jdoajax([]);}
function ev_table_click(){jdoajax([]);}
function ev_node_click(){jdoajax([]);}
function ev_jd3_click(){jdoj('');}
function ev_chart_click(){jdoj('');}
function ev_react_click(){jdoj('');}
function ev_charttour_click(){jdoj('');}
function ev_spx_click(){jdoajax([]);}
function ev_watch_click(){jdoajax([]);}
function ev_debug_click(){jdoajax([]);}
function ev_debugjs_click(){jdoajax([]);}
function ev_sp_click(){jdoajax([]);}
function ev_spx_click(){jdoajax([]);}
function ev_labs_click(){jdoajax([]);}
function ev_about_click(){jdoajax([]);}
function ev_wiki_click(){jdoajax([]);}

function ev_welcome_click(){jdoajax([]);}
function ev_shortcuts_click(){jdoajax([]);}
function ev_popups_click(){jdoajax([]);}
function ev_closing_click(){jdoajax([]);}

function ev_clearwindow_click(){jbyid("log").innerHTML= "";newpline("   ");}
function ev_clearrefresh_click(){jdoajax([]);}
function ev_clearLS_click(){localStorage.clear();};

function linkclick(a){pageopen(a,a);return false;} // open new tab or old - cache

function ev_jfile_click(){linkclick("jfile");}
function ev_jfiles_click(){linkclick("jfiles");}
function ev_jfif_click(){linkclick("jfif");}
function ev_jpacman_click(){linkclick("jpacman");}
function ev_jijx_click(){linkclick("jijx");}
function ev_framework_click(){linkclick("jdoc");}

function ev_jijs_click(){
 id= jbyid('ijs');
 id.value= 1+parseInt(id.value)+'';
 linkclick("jijs?jwid=~temp/"+id.value+".ijs")
} 

function ev_f_shortcut(){ev_jfile_click();}
function ev_k_shortcut(){ev_jfiles_click();}
function ev_F_shortcut(){ev_jfif_click();}
function ev_J_shortcut(){ev_jijs_click();}

function ev_9_shortcut(){jlogwindow= pageopen('jijs?jwid=~temp/jlog.ijs','jijs?'+encodeURIComponent('jwid=~temp/jlog.ijs'));}

function ev_8_shortcut(){
 jlog('\nesc-8 '+allwins.length);
 allwins.forEach(function (el){
  try{t= el.name;}catch(e){t= 'error'};
  jlog(el.closed?'closed':t)
});
}

function ev_jdebug_click(){wjdebug= pageopen('jdebug','jdebug');return false;;}

function ev_jbreak_click()
{
 if(0==rqstate) return; // do not break if not busy
 if("http:"==window.location.protocol)
 {
  updatelog("<b>jbreak not supported - use ctrl+c in server terminal window</b>")
  newpline("");
  return;
 }
 let rq= newrq();
 rq.open("POST",jform.jlocale.value,0); // synch call
 rq.send("jbreak?");
}

function ev_jlogoff_click()
{
 let rq= newrq();
 rq.open("POST",jform.jlocale.value,0); // synch call
 rq.send("jlogoff?");
 window.location= "jlogoff";
}

function ev_helphelp_click(){urlopen("https://www.jsoftware.com/help/index.htm")};
function ev_helpwikinuvoc_click(){urlopen("https://code.jsoftware.com/wiki/NuVoc")};
function ev_helpwikiancillary_click(){urlopen("https://code.jsoftware.com/wiki/NuVoc#bottomrefs")};
function ev_helpwikirelnotes_click(){urlopen("https://code.jsoftware.com/wiki/System/ReleaseNotes")};
function ev_helpwikiconstant_click(){urlopen("https://code.jsoftware.com/wiki/Vocabulary/Constants")};
function ev_helpwikicontrol_click(){urlopen("https://code.jsoftware.com/wiki/Vocabulary/ControlStructures")};
function ev_helpwikiforeign_click(){urlopen("https://code.jsoftware.com/wiki/Vocabulary/Foreigns")};
function ev_helpwikijhs_click(){urlopen("https://code.jsoftware.com/wiki/Guides/JHS")};
function ev_helpwikistdlib_click(){urlopen("https://code.jsoftware.com/wiki/Standard_Library/Overview")};

function ev_comma_ctrl(){jdoajax([]);}
function ev_dot_ctrl(){jdoajax([]);}
function ev_slash_ctrl(){jdoajax([]);}
function ev_less_ctrl(){jdoajax([]);}
function ev_larger_ctrl(){jdoajax([]);}
function ev_query_ctrl(){jdoajax([]);}
function ev_semicolon_ctrl(){jdoajax([]);}
function ev_quote_ctrl(){jdoajax([]);}
function ev_colon_ctrl(){jdoajax([]);}
function ev_doublequote_ctrl(){jdoajax([]);}

function ev_close_click(){
 allwins_clean();
 for(let i = 0; i < allwins.length; i++) {allwins[i].jscdo("close");}
 allwins_clean();
 if(0==allwins.length)
 {
  updatelog('<div id="prompt" class="log"><b><font style="color:red;"><br>JHS server for this page has exited.</font></b></div>')
  jijxrun("exit''")
  // kill all page events
  document.addEventListener("click", deadhandler, true);
  document.addEventListener("keyup", deadhandler, true);
  document.addEventListener("keypress", deadhandler, true);
  document.addEventListener("keydown", deadhandler, true);
 }
} 

function deadhandler(e) {
  e.stopPropagation();
  e.preventDefault();
  scrollz();
}

// allwins stuff

function allwins_clean(){allwins= allwins.filter(el => !el.closed)} // remove closed

// return allwins window object for wid or null
function getwindow(wid){
 allwins_clean()
 for(let i = 0; i < allwins.length; i++) {
  w= allwins[i]
  if(wid==w.name) return w;
 }
 return null; 
}

)
