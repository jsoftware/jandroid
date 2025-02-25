NB. html templates and utilities
coclass'jhs'

jsdata=: '"uninitialized"'   NB. jsdata not set by app - must be legal javascript

INC=: CSS=: JS=: HBS=: ''  NB. overidden in app locale
NOCACHE=: 0                NB. use cached js files

INC_jquery=: 0 : 0
~addons/ide/jhs/js/jquery/smoothness/jquery-ui.custom.css
~addons/ide/jhs/js/jquery/jquery-2.0.3.min.js
~addons/ide/jhs/js/jquery/jquery-ui.min.js
~addons/ide/jhs/js/jquery/jquery-ui.css"
)

INC_d3=: INC_jquery,0 : 0
~addons/ide/jhs/js/d3/d3.css
~addons/ide/jhs/js/d3/d3.min.js
)

INC_d3_basic=: INC_d3,0 : 0
~addons/ide/jhs/js/jsoftware/d3_basic.js
)

INC_handsontable=: INC_jquery, 0 : 0
~addons/ide/jhs/js/handsontable/handsontable.full.min.js
~addons/ide/jhs/js/handsontable/handsontable.full.min.css
)

INC_handsontable_basic=: INC_handsontable NB. no jsoftware stuff yet

INC_chartjs=: 0 : 0
~addons/ide/jhs/js/chartjs/chart.js
~addons/ide/jhs/js/chartjs/defaults.js
)

NB. extra html - e.g. <script .... src=...> - included after CSS and before JSCORE,JS
HEXTRA=: '' 

NB. core plus page styles with config replaces
NB. css from y has legacy <PC_FONTFIXED> and may have PC_FONTFIXED
NB. csscore.css has only PC_FONTFIXED
css=: 3 : 0
core=. fread JSPATH,'csscore.css'
t=. 'PC_JICON PC_FONTFIXED PC_FONTVARIABLE PC_FM_COLOR PC_ER_COLOR PC_LOG_COLOR PC_SYS_COLOR PC_FILE_COLOR PC_BUTTON PC_MENU_HOVER PC_MENU_FOCUS'
d=. PC_JICON;PC_FONTFIXED;PC_FONTVARIABLE;PC_FM_COLOR;PC_ER_COLOR;PC_LOG_COLOR;PC_SYS_COLOR;PC_FILE_COLOR;PC_BUTTON;PC_MENU_HOVER;PC_MENU_FOCUS
page=. y hrplc t;d                NB. <PC_...>
page=. page rplc (;:t),.":each d  NB. PC_...>
core=. core rplc (;:t),.":each d  NB. PC_...
'<style type="text/css">',LF,core,page,'</style>',LF
)

seebox=: 3 : 0
1 seebox y
:
;((x+>./>#each y){.each "1 y),.<LF
)


NB. Lambert/Raul forum
literate=: 3 : 0
; (LF;~dtb)"1]1 1}._1 _1}.":<y
)

seehtml=: 3 : 0
y rplc '<';LF,'<'
)

NB. form template - form, hidden handler sentence, hidden button for form enter (ff)
formtmpl=: 0 : 0 -. LF
<form id="j" name="j" method="post" action="<LOCALE>">
<input type="hidden" id="jdo" name="jdo"     value="">
<input type="hidden" id="jlocale" name="jlocale" value="<LOCALE>">
<input type="hidden" id="jid" name="jid"     value="">
<input type="hidden" id="jtype" name="jtype"   value="">
<input type="hidden" id="jmid" name="jmid"    value="">
<input type="hidden" id="jsid" name="jsid"    value="">
<input type="hidden" id="jclass" name="jclass"    value="">
<input type="submit" value="" onclick="return false;" style="display:none;width:0px;height:0px;border:none">
)

NB. form urlencoded has + for blank
jurldecodeplus=: 3 : 0
jurldecode y rplc '+';' '
)

jurldecode=: 3 : 0
t=. ~.<"1 (1 2 +"1 0 (y='%')#i.#y){y
d=. ".each(<'16b'),each tolower each t
d=. d{each <a.
t=. '%',each t
,t,.d
y rplc ,t,.d
)

jurlencode=: 3 : 0
,'%',.(16 16#:a.i.,y){'0123456789ABCDEF'
)

jmarka=:     '<!-- j html output a -->'
jmarkz=:     '<!-- j html output z -->'
jmarkc=: #jmarka

jmarkjsa        =: '<!-- j js a --><!-- ' NB. next char ; is for refresh+ajax and blank for ajax only
jmarkjsz        =: ' --><!-- j js z -->'
jmarkremove     =: jmarka,jmarkjsa,' '
jmarkrcnt       =: #jmarkremove

NB. unique query string - avoid cache
uqs=: 3 : 0
canvasnum=: >:canvasnum
'?',((":6!:0'')rplc' ';'_';'.';'_'),'_',":canvasnum
)

NB. output starting with jmarka and ending with jmarkz,LF
NB.  is assumed to be html and is not touched
jhtmlfroma=: 3 : 0
if. (jmarka-:jmarkc{.y)*.jmarkz-:(-jmarkc){.}:y do. y return. end.
jhfroma y
)

bad=: 1{a. NB. this character hangs the browser

NB. &amp is a problem - '&amp;&nbsp;'
jhfroma=: 3 : 0
y rplc '<';'&lt;';'>';'&gt;';'&';'&amp;';'"';'&quot;';CRLF;'<br>';LF;'<br>';CR;'<br>';' ';'&nbsp;';bad;''
)

NB. app did not send response - send one now
jbad=: 3 : 0
if. METHOD-:'get' do.
 htmlresponse html409 NB. conflict - not working properly - reload
 echo 'html409 response for ',URL
else.
 echo NV
 e=. LF,'J event handler ev_',(getv'jmid'),'_',(getv'jtype'),' ran but did not provide ajax response'
 echo e
 jhrajax ({.a.),jsencode jcmds 'alert *',e NB. ajax jhrcmds
end.  
)

htmlresponse=: 3 : 0
logapp'htmlresponse'
NB. y 1!:2<jpath'~temp/lastreponse.txt'
LASTTS=: 6!:1''
putdata LASTRESPONSE=: y
shutdownJ_jsocket_ SKSERVER ; 2
sdclose_jsocket_ ::0: SKSERVER
SKSERVER_jhs_=: _1
i.0 0 NB. nothing to display if final J result
)

NB. x hrplc 'aa bb cb';daa;dbb;dcc
NB. aa treated as <aa>
NB. numbers converted to string
hrplc=: 4 : 0
x rplc ('<',each (;:>{.y),each'>'),. ": each }.y
)

NB. grid stuff

NB. template gridgen mid;vals
NB. mid*row*col
NB. template has <ID> and <VALUE> (other repaces already done)
jgridgen=: 4 : 0
'mid v'=. y
'r c'=. $v
d=. ''
for_i. i.r do.
 for_j. i.c do.
  id=. mid,'*',(":i),'*',":j
  d=. d,<x hrplc 'ID VALUE';id;j{i{v
 end.
end.
($v)$d
)

NB. create grid for editing named numeric matrix
NB. y is gid;colheads;rowheads;name
NB. gid makes up family of mids - gid_dd gid_ch ...
NB. colheads/rowheads'' gets defaults
NB. rowheads must be column
NB. edit events are gid_dd_enter gid_dd_change
NB. gid_hh contains the edited noun name for the event
NB. gid_vv contains new cell value for the event
jgridnumedit=: 3 : 0
'gid colh rowh name'=. y
try.
 assert. 0=nc <name
 data=. ".name
 assert. (2=$$data)*.(3!:0 data) e. 4 8 16
catch.
 data=. i.2 3
 ".name,'=: data'
end.
'r c'=. $data
if. ''-:colh do. colh=.   ,:(<'C'),each ":each i.c end.
if. ''-:rowh do. rowh=. |:,:(<'R'),each ":each i.r end.
assert. c={:$colh
assert. r=#rowh

mid=. gid,'_dd'
t=. jhtx'<ID>';'<VALUE>';10;mid;' onchange="return jev(event)"'
dd=. t jgridgen mid;<data
mid=. gid,'_ch'
t=. jhtx'<ID>';'<VALUE>';10;'<MID>';'readonly="readonly" tabindex="-1" '
ch=. (t rplc '<MID>';mid) jgridgen mid;<colh
mid=. gid,'_cf'
cf=. (t rplc '<MID>';mid) jgridgen mid;<,:+/data
mid=. gid,'_rh'
rh=. (t rplc '<MID>';mid) jgridgen mid;<rowh
mid=. gid,'_rf'
rf=. (t rplc '<MID>';mid) jgridgen mid;<|:,:+/"1 data
mid=. gid,'_xx'
co=. (t rplc '<MID>';mid) jgridgen mid;<,:,<''
co=. <name
cx=. <'+/'
t=. co,.ch,.cx
m=. rh,.dd,.rf
b=. cx,.cf,.<''
d=. t,m,b
d=. (<'<td>'),each(<'</td>'),~each d
d=. (<'<tr>'),.(<'</tr>'),.~d
d=. ('</table>'),~,('<table id="',gid,'" cellpadding="0" cellspacing="0">'),;d
d=. ((gid,'_hh') jhhidden name),d NB. name of data for this grid
d=. ((gid,'_vv') jhhidden''),d   NB. new value for this grid
)

NB. gid;80px
jgridnumeditcss=: 3 : 0
'gid width'=. y
t=.   '.',gid,'_dd{text-align:right;width:',width,';}',LF 
t=. t,'.',gid,'_ch{text-align:left; width:',width,';}',LF
t=. t,'.',gid,'_cf{text-align:right;width:',width,';}',LF
t=. t,'.',gid,'_rh{text-align:left; width:',width,';}',LF
t=. t,'.',gid,'_rf{text-align:right;width:',width,';}',LF
t=. t,'.',gid,'_xx{text-align:right;width:',width,';}',LF
)

NB. html template <...>
NB. TITLE
NB. CSS   - styles
NB. JS    - javascript
NB. BODY  - body
hrtemplate=: toCRLF 0 : 0
HTTP/1.1 200 OK
Content-Type: text/html; charset=utf-8
Connection: close
Cache-Control: no-cache

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><TITLE></title>
<CSS>
<HEXTRA>
<JS>
</head>
<BODY>
</html>
)

hrxtemplate=: toCRLF 0 : 0
HTTP/1.1 200 OK
Content-Type: text/html; charset=utf-8
Connection: close

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><TITLE></title>
)

NB. html 204 response (leave the page as is)
html204=: toCRLF 0 : 0
HTTP/1.1 204 OK
Content-Type: text/html; charset=utf-8
Connection: close

)

NB. html 301 response (redirect to another url)
html301=: toCRLF 0 : 0
HTTP/1.1 301 Permanently moved
Location: <NEWURL>
Cache-Control: no-cache
Pragma: no-cache
Expires: 0
Connection: close


)

NB. html 409 Conflict response (J code didn't provide result)
html409=: toCRLF 0 : 0
HTTP/1.1 409 Conflict
Content-Type: text/html; charset=utf-8
Connection: close


get/post request failed<br>
response code 409<br>
application did not produce result<br>
try browsing to url again<br>
additional info in jijx<br/><br/>
<button onclick="if('undefined'==typeof window.parent.spaclose) return window.close(); return window.parent.spaclose(window);" >close and return to term</button>

)

gsrchead=: toCRLF 0 : 0
HTTP/1.1 200 OK
Server: JHS
Content-Length: <LENGTH>
Content-Type: <TYPE>
Cache-Control: no-cache
Connection: Keep-Alive

)


gsrcf=: 4 : 0
htmlresponse y,~gsrchead rplc '<TYPE>';x;'<LENGTH>';":#y
)

fsrchead=: toCRLF 0 : 0
HTTP/1.1 200 OK
Server: JHS
Content-Type: <TYPE>

)

NB. html for jajax response
NB. no-cache critical - otherwise we get old
NB. answers to the same question!
hajax=: toCRLF 0 : 0
HTTP/1.1 200 OK
Content-Type: text/html; charset=utf-8
Cache-Control: no-cache
Content-Length: <LENGTH>

)

NB. html for ajax response in chunks
hajax_chunk=: toCRLF 0 : 0
HTTP/1.1 200 OK
Content-Type: text/html; charset=utf-8
Cache-Control: no-cache
Transfer-Encoding: chunked

)

hajaxlogoff=: toCRLF 0 : 0
HTTP/1.1 403 OK
Content-Type: text/html; charset=utf-8
Cache-Control: no-cach
Content-Length: <LENGTH>
Set-Cookie: jcookie=0

)

NB. jhbs body builders

HBSX=: ' id="<ID>" name="<ID>" class="<CLASS>" '

NB. HBS is LF delimited list of sentences
NB. jhbs returns list of sentence results
jhbs=: 3 : 0
t=. <;._2 y
t=. LF,~LF,~LF,;jhbsex each t
i=. 1 i.~'</div><div id="jresizeb">'E.t
if. i~:#t do.
 t=. '<div id="jresizea">',t,'</div>'
end.
t=. '<div id="status-busy"><br>server busy<br>event ignored<br><br></div>',t
t=. '<div>',t,'</div>' NB. all in a div - used by flex
'<body onload="jevload();" onunload="jevunload();" onfocus="jevfocus();">',LF,(jhform''),LF,t,LF,'</form></body>'
)

jhbsex=: 3 : 0
try.
 t=. LF,}.' ',,".y NB. need lit list
catch.
 echo t=.'HBS error: locale: ',(>coname''),' line: ',y,LF,13!:12''
 t=.'<div>',(jhfroma t),'</div>'
end.
t
) 

NB.? autocapitalize="none"
jeditatts=: ' autocomplete="off" autocapitalize="off" autocorrect="off" spellcheck="false" '

jhform=: 3 : 0
formtmpl hrplc 'LOCALE';>coname''
)

JASEP=: 1{a. NB. delimit substrings in ajax response

jgetfile=: 3 : '(>:y i: PS)}.y=.jshortname y'
jgetpath=: 3 : '(>:y i: PS){.y=.jshortname y'

NB.* button and ev handler for edit of defining script
jhijs=: 3 : 0
c=. ;coname''
c=. '.ijs',~;(_~:_".c){c;{.copath coname''
t=. 4!:3''
s=. (>:;t i:each '/')}.each t
p=. ;(s i: <c){t NB. get last script
JSCRIPT=: p
ev_jscript_click=: jhijshandler
'jscript'jhb'edit source script'
)

jhijshandler=: 3 : 0
t=. pageopenargs'jijs?jwid=',JSCRIPT
jhrcmds'pageopen *',}:;t,each','
)

NB.* html link to defining script
jhdemo=: 3 : 'jhbr,jhijs y'

NB. jgrid - special jht for grid
jhtx=: 3 : 0
t=. '<input type="text" id="<ID>" name="<ID>" class="<CLASS>" <EXTRAS>',jeditatts,'value="<VALUE>" size="<SIZE>" onkeydown="return jev(event)" >'
t hrplc 'ID VALUE SIZE CLASS EXTRAS';5{.y,(#y)}.'';'';'';'ht';''
)

NB.* see ~addons/ide/jhs/utilh.ijs for complete information
NB.* 

NB.*
NB.* HBS verbs with id

NB.* jhab*id jhab text - anchor like button with dblclick
jhab=: 4 : 0
(x jhb (boxopen y),<'jhab') jhaddatts ' ondblclick="return jev(event)"'
)

NB. add defaults
addd=: 4 : 0
t=. boxopen x
t,(<:#t)}.boxopen y
)

NB. validate id - id can have blank and anything else
vid=: 3 : 0
id=. dltb y
NB. ('bad id: ',id)assert -.' 'e.id
id
)

voptions=: 3 :0
('invalid options: ',y)assert 0=#y-.' ~='
y
)

NB. get default - '' replaced by default
gdef=: 3 : 0
;(''-:;{.y){y
)

NB.* jhb*id jhb value [;class [;options ]] - button
NB.* id jhb 'value'
NB.* id jhb 'value';'';'='
NB.* id jhb 'value';'myb'
NB.* class '' is 'jhb'
NB.* option '~' does not run j handler from default js handler
NB.* option '=' value is already html and avoids jhfroma
jhb=: 4 : 0
id=. vid x
'value class options'=. y addd 'jhb';''
class=. gdef class;'jhb'
options=. voptions options
if. -.'='e.options do. value=. jhfroma value end.
t=. '<button id="<ID>" name="<ID>" class="<CLASS>" onclick="return jev(event)">'
t=. t hrplc 'ID VALUE CLASS';id;value;class
t=. t,value,'</button>'
t jhaddatts ('~'e.options)#'data-jhsnojdefault="1"'
)

NB.* jhchart*id jhchart '' - chartjs
jhchart=: 4 : 0
'<div id="<id>_parent" class="jhchart_parent"><canvas id="<id>"></canvas></div>'hrplc 'id';x
)

NB.* jhclose*jhclose'' - menu with > term pages and close
jhclose=: 3 : 0
t=. jhmenu''
t=. t,'menu0'  jhmenugroup ''
t=. t,         jhmpage''
t=. t,'close'  jhmenuitem 'close';'q'
t=. t,         jhmenugroupz''
t=. t,         jhmpagez''
)

NB.* jhdiv*id jhdiv text- <div ...>text</div>
jhdiv=: 4 : 0
'<div id="',x,'" class="jhdiv" >',y,'</div>'
)

NB.* jhdiva*[id] jhdiva text - <div id...>text
jhdiva=: 3 : 0
''jhdiva y
:
'<div id="',x,'" class="jhdiv" >',y
)

NB. #saveasdlg{display:none;} - dialog

NB.* jhd3_basic*id jhd3_basic''
jhd3_basic=: 4 : 0
r=. 'redspacer'jhdiv'&nbsp;'
r=. r,LF,(x,'_error')jhdiv''
r=. r,LF,(x,'_header')jhdiv''
r=. r,LF,(x,'_title')jhdiv''
r=. r=. r,LF,x jhdiv''
r=. r,LF,(x,'_legend')jhdiv''
r=. r,LF,(x,'_footer')jhdiv''
(x,'_box')jhdiv LF,r,LF
)

NB.* jhec*id jhec html - contenteditable div
jhec=: 4 : 0
t=. '<div id="<ID>" contenteditable="true"',jeditatts
NB. t=. t,' style="white-space:nowrap;" ' - CSS
t=. t,' onkeydown="return jev(event)"'
t=. t,' onkeypress="return jev(event)"'
t=. t,' onfocus="jecfocus();"'
t=. t,' onblur="jecblur();"'
t=. t,'>',y,'</div>'
t hrplc 'ID';x
)

NB.* jhecwrap*id jhecwrap html - contenteditable div that wraps
jhecwrap=: 4 : 0
y=. y rplc '&nbsp;';'&nbsp;&ZeroWidthSpace;' NB. breaking space
'<div  class="html" style="overflow-wrap: break-word; white-space: normal;">',y,'</div>'
)

NB.* jhhidden*id jhhidden value - <input type="hidden"....>
jhhidden=: 4 : 0
t=. '<input type="hidden" id="<ID>" name="<ID>" value="<VALUE>">'
t hrplc 'ID VALUE';x;y
)

NB.* jhh1*id jhh1 text - <h1>text</h1>
jhh1=: 3 : 0
''jhh1 y
:
('<h1',HBSX,'><TEXT></h1>')hrplc 'ID CLASS TEXT';x;'jhh1';y
)

NB.* jhhn*id jhhn n;text - <hn>text</hn>
jhhn=: 4 : 0
''jhhn y
:
n=. ":;{.y
a=. '<h',n
b=. '</h',n,'>'
(a,HBSX,'><TEXT>',b)hrplc 'ID CLASS TEXT';x;'jhh1';;{:y
)

NB.* jhline*id jhline '' - jhhr with an id
jhline=: 4 : 0
t=. '<hr id="<ID>" name="<ID>" class="jhline" />'
t hrplc 'ID';x
)

jhpassword=: 4 : 0
id=. vid x
'value size class options'=. y addd 10;'jhtext';''
size=. gdef size;10
class=. gdef class;'jhpassword'
value=. jhfroma value
t=. '<input type="password" id="<ID>" name="<ID>" class="<CLASS>" ',jeditatts,'placeholder="<VALUE>" '
t=. t,'size="<SIZE>" onkeydown="return jev(event)" >'
t=. t hrplc 'ID CLASS VALUE SIZE';id;class;value;size
t jhaddatts ('~'e.options)#'data-jhsnojdefault="1"'
)

NB.* jhaddatts*html jhaddatts attributes
jhaddatts=: 4 : 0
i=. x i. '>'
(i{.x),' ',y,i}.x
)

NB.* jhrematts*html jhrematts 'data-...'
jhrematts=: 4 : 0
'currently must be a single name  with no blanks or quotes'assert -.+/' "'e.y
a=. ' ',y,'="'
i=. 1 i.~a E. x
(i{.x),(i+#a)}.x
)

NB.* jhchk*id jhchk text [;check [;marks [;class [;options]]]]
NB.* id jhchk 'text'
NB.* id jhchk 'text';1
NB.* id jhchk 'text';0;'yn'
NB.* id jhchk 'text';0;'';'';'~'
NB.* not input tag type so is not included with submit
NB. marks '' default '□▣'
NB. class '' default is 'jhchk'
NB. see jhb for more info
jhchk=: 4 : 0
'value check marks class options'=. y addd 0;'□▣';'jhchk';''
class=. gdef class;'jhchk'
marks=. gdef marks;'□▣'
value=. (8 u: check{7 u: marks),' ',value
t=. x jhb value;class;options
t jhaddatts 'data-jhscheck="<CHECK>" data-jhsmarks="<MARKS>"'hrplc 'CHECK MARKS';check;marks
)

NB.* jhrad*id jhrad text [;check [;set [;marks [;class [;options]]]]]
NB.* prefered order:text;check;set
NB.*  but old style is converted: set;text;check
NB.* set used to change button state in same set
NB.* not input tag type so is not included with submit
NB. see jhchck for more info
jhrad=: 4 : 0
t=. y addd 0;'rad0';'◯⬤';'jhrad';''
NB. adjust set;text;check to be text;check;set
if. 2~:3!:0>2{t do. t=. (1 2 0{t),3}.t end.
'value check set marks class options'=. t
class=. gdef class;'jhrad'
marks=. gdef marks;'◯⬤'
set=.   gdef set;'rad0'
mark=. 8 u: check{7 u: marks
value=. (8 u: check{7 u: marks),' ',value
t=. x jhb value;class;options
t=. t jhaddatts 'data-jhsset="<SET>" data-jhscheck="<CHECK>" data-jhsmarks="<MARKS>"'hrplc 'SET CHECK MARKS';set;check;marks
)

NB.deprecated * jhref*jhref page;target;text
NB.deprecated * jhref*id jhref text - <a href="id">text</a> (deprecated - use monadic form)
jhref=: 3 : 0
'page target text'=. y
t=. '<a href="<REF>?jwid=<TARGET>" target="<TARGET>" class="jhref" ><TEXT></a>'
t hrplc 'REF TARGET TEXT';page;target;text
:
y=. boxopen y
t=. '<a href="<REF>" target="',TARGET,'" class="jhref" ><VALUE></a>'
t hrplc 'REF VALUE';x;y
)

NB.* jhselect*id jhselect texts [;size [;sel [;class [;options ]]]]
jhselect=: 4 : 0
id=. vid x
'first arg must be list of boxed texts'assert 2=L.{.y
'values size sel class options'=. y addd '';0;'jhselect';''
size=.  gdef size;0
sel=.   gdef sel;0
class=. gdef class;'jhselect'
t=. '<select id="<ID>" name="<ID>" class="<CLASS>" size="<SIZE>" onchange="return jev(event)" >'
t=. t hrplc 'ID CLASS SIZE';id;class;size
opt=. '<option value="<VALUE>" label="<VALUE>" <SELECTED>><VALUE></option>'
for_i. i.#values do.
 t=. t,LF,opt hrplc'VALUE SELECTED';(i{values),(i=sel){'';'selected="selected"'
end.
t=. t,'</select>'
t jhaddatts ('~'e.options)#'data-jhsnojdefault="1"'
)



NB.* jhspan*id jhspan text - <span id...>text</span>
jhspan=: 4 : 0
'<span id="',x,'" class="jhspan">',y,'</span>'
)

NB.* jhtable*id jhtable '' - jhtable with id
jhtable=: 4 : 0
('<table',HBSX,'>')hrplc 'ID CLASS ';x;'jhtable'
)

NB.* jhtext*id jhtext text [;cols [;class [;options ]]]
jhtext=: 4 : 0
id=. vid x
'value size class options'=. y addd 10;'jhtext';''
size=. gdef size;10
class=. gdef class;'jhtext'
value=. jhfroma value
t=. '<input type="text" id="<ID>" name="<ID>" class="<CLASS>" ',jeditatts,'value="<VALUE>" '
t=. t,'size="<SIZE>" onkeydown="return jev(event)" >'
t=. t hrplc 'ID CLASS VALUE SIZE';id;class;value;size
t jhaddatts ('~'e.options)#'data-jhsnojdefault="1"'
)

NB.* jhtextarea*id jhtextarea text [;rows-3;ccols-10]
NB.* no onkeydown handler
jhtextarea=: 4 : 0
t=.   '<textarea id="<ID>" name="<ID>" class="jhtextarea" wrap="off" rows="<ROWS>" cols="<COLS>" '
t=. t,jeditatts,'><DATA></textarea>'
t hrplc 'ID DATA ROWS COLS';x;3{.(boxopen y),3;10
)

NB.* jhtitle*id jhtitle text
jhtitle=: 4 : 0
'<span id="',x,'" class="jhtitle">',y,'</span><br/>'
)

NB.* jhurl*id jhurl url [;target [;text] ]
jhurl=: 4 : 0
t=. boxopen y
if. 1=#t do. t=. t,<'_target' end.
if. 2=#t do. t=. t,{.t        end.
'page target text'=. t
t=. '<a id="<ID>" name="<ID>" href="<REF>" target="<TARGET>" class="jhref" ><TEXT></a>'
t hrplc 'ID REF TARGET TEXT';x;page;target;text
)

NB.* 
NB.* HBS verbs without id

NB.* jhresize*jhresize'' - separate fixed div from resizable div
jhresize=: 3 : '''</div><div id="jresizeb">'''

NB.* jhtr*jhtr rowdata - list of boxed row data html
jhtr=: 3 : 0
'<tr>','</tr>',~;(<'<td>'),each y,each<'</td>'
)

NB.* 
NB.* HBS nouns

NB.*jhdivz*jhdivz - </div>
jhdivz=: '</div>'

jhtablea=: '<table>' NB. deprecated - use jhtable

NB.* jhtablez*jhtablez - </table>
jhtablez=: '</table>'

NB.* jhbr*jhbr - <br/>
jhbr=: '<br/>'

jhhr=: '<hr/>' NB. deprecated - use jhline

NB.* jhbshtml*jhbshtml_jdemo1_'' -  show HBS sentences and html
jhbshtml=: 3 : 0
s=.<;._2 HBS
seebox s,.LF-.~each jhbsex each s
)

NB.* jnv*jnv 1 - toggle display of event name/value pairs
jnv=: 3 : 'NVDEBUG=:y' NB. toggle event name/value display

NB.* 
NB.* html response verbs

NB. build html response from page globals CSS JS HBS
NB. CSS or JS undefined allowed
NB.* jhr*title jhr names;values - names to replace with values
NB.* *send html response built from HBS CSS JS names values
jhr=: 4 : 0
if. _1=nc<'JS'  do. JS=:'' end.
if. _1=nc<'CSS' do. CSS=:'' end.
tmpl=. hrtemplate
if. SETCOOKIE do.
 SETCOOKIE_jhs_=: 0
 tmpl=. tmpl rplc (CRLF,CRLF);CRLF,'Set-Cookie: ',cookie,CRLF,CRLF
end.
htmlresponse tmpl hrplc 'TITLE CSS HEXTRA JS BODY';(TIPX,x);(css CSS);HEXTRA;(getjs'');(jhbs HBS)hrplc y
)

NB.* jhrx*title jhrx (getcss'...'),(getjs'...'),getbody'...'
NB.* *send html response built from INCLUDE, CSS, JS, HBS
jhrx=: 4 : 0
JTITLE=: x
htmlresponse (hrxtemplate hrplc 'TITLE';(TIPX,x)),y
)

NB.* jhrpage*title jhrpage (getcss'...'),(getjs'...'),getbody'...'
NB.* *page response to a get
jhrpage=: 4 : 0
(hrxtemplate hrplc 'TITLE';(TIPX,x)),y
)

getincs=: 3 : 0
t=. ~.<;._2 INC
t=. (;(<y)-:each (-#y){.each t)#t
if. 0=#t do. t return. end.
b=. ;fexist each t
('INC file not found: ',>{.(-.b)#t)assert b
t
)

getcss=: 3 : 0
'getcss arg not empty'assert ''-:y
t=. getincs'.css'
t=. ;(<'<link rel="stylesheet" href="'),each t,each<'" />',LF
t,css CSS hrplc 'PS_FONTCODE';PS_FONTCODE NB. PS_FONTCODE contains PC_... 
)

fixjsi=: 3 : 0
if. NOCACHE do.
 '<script type="text/javascript">',LF,'// NOCACHE: ',y,LF,(fread y),LF,'</script>'
else.
 '<script src="',y,'"></script>'
end. 
)

jsa=: LF,'<script type="text/javascript">',LF
jsz=: LF,'</script>',LF


getjs=: 3 : 0
t=. getincs'.js'
t=. ;LF,~each fixjsi each t 
t=. t,jsa,'function jevload(){alert("load javascript failed:\nsee jijx menu tool>debug javascript")};',jsz
t,jsa,(fread JSPATH,'jscore.js'),(JS hrplc y),'var jsdata= ',jsdata,';',jsz
)

NB. core plus y - used for special pages
js=: 3 : 0
jsa,(fread JSPATH,'jscore.js'),y,jsz
)

gethbs=: 3 : 0
'</html>',~'</head>',y hrplc~jhbs HBS
)

NB.* jhrajax*jhrajax data - JASEP delimited data
jhrajax=: 3 : 0
htmlresponse y,~hajax rplc '<LENGTH>';":#y
)

NB.* jcmds*jcmds cmds - 0 or more boxed list of browser commands
jcmds=: 3 : 0
t=. boxopen y
'jcmds arg is not 0 or more boxed strings'assert 2=;3!:0 each t
'jhrcmds';<t
)

NB.* jhrcmds*jhrcmds ajax cmds - 0 or more boxed cmds
NB.* run in event handler to return cmds to javascript ajax routine
NB.* *set id *value     - html elements (e.g. jhtext) with value
NB.* *set id *innerHTML - html elements with HTML (e.g. jhspan)
NB.* *css *css          - set new extra CSS
jhrcmds=: 3 : 0
jhrajax ({.a.),jsajaxdata=: jsencode jcmds y NB. ajax cmds
)

NB.* jhcmds*jhcmds - 0 or or more cmds to be run by ev_body_load
NB.* run in ev_create/create/jev_get to pass cmds to javascript in var jsdata
NB.* see jhrcmds 
jhcmds=: 3 : 0
jsdata=: jsencode jcmds y
)

NB.* jhrjson*jhrjson - 0 or more boxed name/value pairs
jhrjson=: 3 : 0
jhrajax ({.a.),jsencode y
)

chunk=: 3 : 0
if. 0=#y do.
 ''
else. 
 (hfd#y),CRLF,y,CRLF
end. 
)

NB.* jhrajax_a*jhrajax_a data - first chunk
jhrajax_a=: 3 : 0
putdata hajax_chunk,chunk y
)

NB.* jhrajax_b*jhrajax_b data - next chunk
jhrajax_b=: 3 : 0
putdata chunk y
)

NB.* jhrajax_b*jhrajax_z data - last chunk
jhrajax_z=: 3 : 0
putdata (chunk y),'0',CRLF,CRLF
shutdownJ_jsocket_ SKSERVER ; 2
sdclose_jsocket_ ::0: SKSERVER
SKSERVER_jhs_=: _1
)

jsencode=: 3 : 'enc_pjson_ (2,~-:$y)$y'

NB. standard menu for pages
jhmpage=: 3 : 0
'jmpage' jhmenulink  'jmpages';'term pages' NB. ;'4' ev_4_ shortcut
)

NB. standard menu page menu
jhmpagez=: 3 : 0
t=.   'jmpages' jhmenugroup ''
t=. t,'jmleft'  jhmenuitem 'left';'^<'
t=. t,'jmright' jhmenuitem 'right';'^>'
    t,jhmenugroupz''
)

NB. hamburger menu
jhmenu=: 3 : 0
menuids=:   <'menu0' 
menutexts=: <'☰'
menubacks=: <''
('menuburger'jhb'☰';'jmenuburger'),('menuclear'jhb'';'jmenuclear') NB. ,jhmenulink  'jmpages';'pages'
)

jhmenugroup=: 4 : 0
menuid=: x
i=. menuids i. <x
if. i=#menuids do.  i=. 0 end. NB. user not informed of failuer
value=. ;i{menutexts
backid=. ;i{menubacks
more=. '<span class="jmenuspanleft" >',(jhfroma'<      '),'</span>'
t=. '<a href="#" class="jmenuitem" onclick="return menushow(''<BACK>'')" ><VALUE></a>'
t=. t hrplc 'BACK VALUE';backid;more,jhfroma value
t,~'<div id="<ID>" class="jmenugroup">'rplc '<ID>';x
)

jhmenugroupz=: 3 : '''</div>'''


NB. id jhmenuitem 'test'[;esc] - esc is '' or single alphanumeric
jhmenuitem=: 4 : 0
'text esc'=. 2{.(boxopen y),<''

select. {.esc
case. ' ' do. t=. ''
case. '^' do. t=. 'ctrl+',1{esc
case.     do. t=. 'esc-',esc
end.

esc=.  '<span class="jmenuspanright">',t,'</span>'
t=. '<a id="<ID>" href="#" class="jmenuitem" onclick="mmhide();return jev(event)" ><VALUE></a>'
t hrplc 'ID VALUE';x;(jhfroma text),esc
)

NB. jhmenulink text;id[;esc]
jhmenulink=: 3 : 0
'' jhmenulink y
:
if. #x do. x=. 'id="',x,'"' end.
'to text esc'=. 3{.y,<''

select. {.esc
case. ' ' do. t=. ''
case. '^' do. t=. 'ctrl+',1{esc
case.     do. t=. 'esc-',esc
end.
esc=.  '<span class="jmenuspanright">',t,'</span>'

menuids=: menuids,<to
menutexts=: menutexts,<text
menubacks=: menubacks,<menuid
more=. '<span class="jmenuspanleft" >&gt&nbsp;</span>'
t=. '<a href="#" class="jmenuitem" <ID> onclick="return menushow(''<TO>'')" ><VALUE></a>'
t hrplc 'ID VALUE TO';x;(more,(jhfroma text),esc);to
)
