0 : 0
drawing on canvas
   spx'~addons/ide/jhs/spx/canvas.ijs'
)

require'~addons/graphics/color/hues.ijs'

coclass'z'

3 : 0''
if. _1=nc<'jsxbuf' do. jsxbuf=: '' end.
)

jsxnew_z_=: 3 : 'r[jsxbuf_z_=:''''[r=. jsxbuf_z_'

jsxucp_z_=: 3 u: 7 u: ]              NB. int codepoints from utf8 string
jsxradian_z_=: 3 : '<.1e7*y'         NB. ints from fractional radians
jsxarg_z_=: 3 : '(":y)rplc'' '';'',''' NB. javascript string from int list

NB. y is number of colors required
jsxpalette_z_=: 3 : 0
t=. jsxarg each ":each<"1[255<.<.0.5+hues 5r6*(i.%<:)y
(<'rgb('),each t,each')'
)

jsxlines_z_=: 3 : 0
assert 0=2|#y
jscmoveTo 2{.y
i=. 2 
while. i<#y do.
 jsclineTo (i+0 1){y
 i=. i+2
end.
jscstroke''
)

NB. simple viewmat
jsxvm_z_=: 3 :0
'a b'=. $y
s=. >./jsxwh
d=. >./$y
r=. <.s%d
offset=. <.2%~jsxwh-r*$y
data=.  (~.,y) i. y
pal=. jsxpalette >:>./,data
for_i. i.a do.
 for_j. i.b do.
  jscbeginPath''
  jscfillStyle jsxucp ;(i{j{data){pal
  jscrect (offset+r*i,j),r,r
  jscfill''
 end.
end.
)

coclass'jcanvas'
coinsert'jhs'

NB. cojhs boilerplate from util.ijs

create=: 3 : 0
'width height'=: y
)

NB. jsc... commands and asserts
t=. <;._2 [ 0 : 0
fillStyle     0<#
strokeStyle   0<#
rect          4=#
fillText      2<#
font          0<#
lineWidth     1=#
beginPath     0=#
fill          0=#
stroke        0=#
clearRect     4=#
moveTo        2=#
lineTo        2=#
closePath     0=#
ellipse       8=#
strokeText    2<#
arc           6=#
)

ncmds=:    (t i.each' '){.each t
nasserts=: (>:each t i:each' ')}.each t

bld=: 3 : 0''
for_i. i.#ncmds do.
 a=. ;i{nasserts
 ('jsc',(;i{ncmds),'_z_')=: 3 : ('i.0 0[jsxbuf_z_=: jsxbuf_z_,',(":i),',(#d),d[''',(;i{ncmds),'''assert ',a,' d=. <. y' )
end.
i.0 0
)

run=: 3 : 0
jjs_jhs_ q=: 'w=window.open("","',(;coname''),'");w.doit("',(jsxarg y),'");'
)

markmouse=: 3 : 0
'a b c'=. 0".getv_jhs_'jdata'
jsxnew''
jscbeginPath''
jscfillStyle jsxucp y
jscarc a,b,5,0,(jsxradian 2*o.1),1
jscfill''
jsxarg jsxnew''
)

ev_mouse_down=: 3 : 0
jhrajax markmouse'green'
i.0 0
)

ev_mouse_up=: 3 : 0
jhrajax markmouse'blue'
i.0 0
)

NB. ev_mouse_move=: 3 : 0

HBS=: 0 : 0
jhclose''
jhbr
'<canvas id="can" width="<WIDTH>" height="<HEIGHT>"></canvas>'
)

CSS=: 0 : 0
form{margin:0px 2px 2px 2px;}
canvas {border: 1px solid black;}
)

fixcmds=: 3 : 0
a=. (<': '),~each (<'case '),each   ":each<"0 i.#y
;LF,~each a,each (<'(d);break;'),~each   (<'jsc'),each y
)

CMDS=: fixcmds ncmds

jev_get=: 3 : 0
title jhrx (getcss''),(getjs'BUFFER CMDS';(jsxarg refresh);CMDS),gethbs'WIDTH HEIGHT';width;height
)

JS=: 0 : 0

var x= 0, y= 0, down= 0, rfi= 10000000, can, context;
buffer="<BUFFER>"; // refresh buffer

function ev_body_load(){init();}

function init(){
 can = document.getElementById('can');
 context = can.getContext('2d');

 // ajax calls are slow and mouse events are lost - especially mousmove
 // handle only mousedown and mouseup
 can.addEventListener('mousedown', e => {down= 1;mevent("down",e);});
 can.addEventListener('mouseup', e =>   {down= 0;mevent("up",e);});
 // can.addEventListener('mousemove', e => {mevent("move",e);});

doit(buffer);
}

// jsc... cmds map directly to canvas commands
function jscclearRect(a){context.clearRect(a[0],a[1],a[2],a[3]);}
function jscrect(a){context.rect(a[0],a[1],a[2],a[3]);}
function jscfillStyle(a){context.fillStyle= stringfints(a);}
function jscstrokeStyle(a){context.strokeStyle= stringfints(a);}
function jscbeginPath(){context.beginPath();}
function jscclosePath(){context.closePath();}
function jscfill(){context.fill();}
function jscstroke(){context.stroke();}
function jsclineWidth(a){context.lineWidth= a[0];}
function jscfont(a){context.font= stringfints(a);}
function jscfillText(a){context.fillText(stringfints(a.slice(2,a.length)),a[0],a[1]);}
function jscstrokeText(a){context.strokeText(stringfints(a.slice(2,a.length)),a[0],a[1]);}
function jscmoveTo(a){context.moveTo(a[0],a[1]);}
function jsclineTo(a){context.lineTo(a[0],a[1]);}
function jscellipse(a){context.ellipse(a[0],a[1],a[2],a[3],a[4]/rfi,a[5]/rfi,a[6]/rfi,a[7]);}
function jscarc(a){context.arc(a[0],a[1],a[2],a[3]/rfi,a[4]/rfi,a[5]);}

// convert UTF-16 array to string
function stringfints(a)
{
 var str = "";
 for (var i=0;i<a.length;i++ ) 
  str += String.fromCharCode(a[i]);
 return str;
}

// a is string of , separated numbers - command,#,args ...
function doit(a)
{
 if(0==a.length)return; // avoid empty string -> 0
 a= a.split(",").map(Number) // convert string to int array
 for(var i=0;i<a.length;i=i+2+a[i+1])
 {
  var d= a.slice(i+2,i+2+a[i+1]);
  switch(a[i])
  {
<CMDS>
    break;
   default:
    break;
  }
 }
}

// call J mouse event handler
function mevent(type,e)
{
  jform.jid.value= "mouse";
  jform.jmid.value="mouse";
  jform.jtype.value=type;
  jform.jsid.value="";
  jdoajax([],e.offsetX+" "+e.offsetY+" "+down);
}

// process J mouse event handler result
function ev_mouse_down_ajax(){doit(rq.responseText.substr(rqoffset));}
function ev_mouse_up_ajax(){ev_mouse_down_ajax();}
function ev_mouse_move_ajax(){ev_mouse_down_ajax();}

)
