NB. JAndroid

require 'project'

coclass 'ja'

load 'ide/ja/gl2'

finalize_jgl2_=: 3 : 0
load 'ide/ja/gl2'
)
boxj2utf8=: 3 : 0
if. 1 < #$y do. y return. end.
b=. (16+i.11) { a.
if. -. 1 e. b e. y do. y return. end.
y=. ucp y
a=. ucp '┌┬┐├┼┤└┴┘│─'
x=. I. y e. b
utf8 (a {~ b i. x { y) x } y
)
clipread=: wd@('clippaste'"_)
clipwrite=: 3 : 0
txt=. boxj2utf8 flatten ":y
wd 'clipcopy *',txt
#txt
)
dbjc=: 3 : 0
if. y do.
  if. _1 = 4!:0 <'jdb_open_jdebug_' do.
    0!:0 <jpath '~addons/ide/ja/debugs.ijs'
  end.
  jdb_open_jdebug_''
else.
  jdb_close_jdebug_ :: ] ''
end.
)
flatten=: 3 : 0
dat=. ": y
select. # $ dat
case. 1 do.
case. 2 do.
  }. , LF ,. dat
case. do.
  dat=. 1 1}. _1 _1}. ": < dat
  }: (,|."1 [ 1,.-. *./\"1 |."1 dat=' ')#,dat,.LF
end.
)
getsha1=: 3 : 0
gethash 'sha1';y
)
gethash=: 3 : 0
't m'=. y
t gethash m
:
m=. ,y
c=. '"',libjqt,'" gethash i *c *c i * *i'
'r t m w p n'=. c cd (tolower x);m;(#m);(,2);,0
res=. memr p,0,n
if. r do.
  res (13!:8) 3
end.
res
)
gridindex=: 3 : 0
'rws cls sel ndx shp'=. y
d=. (sel,rws,cls) |: i.shp
r=. (sel{shp),(*/rws{shp),*/cls{shp
,(<ndx) { r ($,) d
)
logcat=: empty @: (('"',libjqt,'" logcat n *c') cd <@,)
showevents=: 3 : 0
select. {. y,1
case. 0 do.
  4!:55 <'wdhandler_debug_z_'
case. 1 do.
  wdhandler_debug_z_=: 3 : 'smoutput sysevent'
case. 2 do.
  wdhandler_debug_z_=: 3 : 'smoutput wdq'
case. 3 do.
  wdhandler_debug_z_=: 3 : 'if. -. ''_mmove''-:_6{.sysevent do. smoutput sysevent end.'
case. 4 do.
  wdhandler_debug_z_=: 3 : 'if. -. ''_mmove''-:_6{.sysevent do. smoutput wdq end.'
end.
EMPTY
)
textview=: 3 : 0
if. 2=#p=. boxopen y do.
  p=. 1 0 1 #^:_1 p
end.
'title caption text'=. _3 {. p
wd 'textview *;',title,';',caption,';',flatten text
)
wdhandlerx=: 3 : 0
try.
  loc=. <,y
  if. 0 <: 18!:0 loc do. wdhandler__loc'' end.
catch.
  smoutput 'error in handler for event: ',sysevent__loc
end.
EMPTY
)
addons_msg=: 0 : 0
The XX are not yet installed.

To install, select menu Tools|Package Manager and install package YY.
)
addons_missing=: 3 : 0
'name addon script'=. y
if. fexist script do. 0 return. end.
sminfo name;addons_msg rplc 'XX';name;'YY';addon
1
)
demoja=: 3 : 0
p=. jpath '~addons/ide/ja/demo/jademo.ijs'
if. addons_missing 'jandroid demos';'ide/ja';p do. return. end.
load p
)
demowd=: 3 : 0
p=. jpath '~addons/demos/wd/demos.ijs'
if. addons_missing 'Showcase demos';'demos/wd';p do. return. end.
load p
)
labs_run=: 3 : 0
smfocus_jijs_=: 0:
closewindows_jijs_=: 0:
smclose_jijs_=: 0:
smopen_jijs_=: open
smsel_jijs_=: 0:
smselout_jijs_=: 0:
smsetsaved_jijs_=: 0:
smwrite_jijs_=: 0:
tile_jijs_=: 0:
coinsert_jijs_ (,copath) coname''

p=. jpath '~addons/labs/labs/lab.ijs'
if. addons_missing 'labs';'labs/labs';p do. return. end.
require p
if. 0 e. $y do.
  require '~addons/labs/labs/labs805.ijs'
  labselect_jlab805_'' return.
end.
if. y -: 1 do. y=. ':' end.
empty lab_jlab_ y
)
helpcontext0=: 3 : 0
require '~addons/ide/ja/help.ijs'
helpcontext y
)

helpcontext1=: 3 : 0
require '~addons/ide/ja/help.ijs'
helpcontext1 y
)
cocurrent IFJA{'ja';'z'
wd=: 11!:0
wdhandler=: 3 : 0
wdq=: wd 'q'
wd_val=. {:"1 wdq
({."1 wdq)=: wd_val
if. 3=4!:0<'wdhandler_debug' do.
  try. wdhandler_debug'' catch. end.
end.
wd_ndx=. 1 i.~ 3 = 4!:0 [ 3 {. wd_val
if. 3 > wd_ndx do.
  wd_fn=. > wd_ndx { wd_val
  if. 13!:17'' do.
    wd_fn~''
  else.
    try. wd_fn~''
    catch.
      wd_err=. 13!:12''
      if. 0=4!:0 <'ERM_j_' do.
        wd_erm=. ERM_j_
        ERM_j_=: ''
        if. wd_erm -: wd_err do. i.0 0 return. end.
      end.
      wd_err=. LF,,LF,.(}.^:(':|'e.~{.));._2 LF,~ wd_err
      wdinfo 'wdhandler';'error in: ',wd_fn,wd_err
    end.
  end.
end.
i.0 0
)
wdclippaste=: (wd bind 'clippaste') :: (''"_)
wdqq=: (wd bind 'q') :: (''"_)
wdqchildxywh=: (0 ". [: wd 'qchildxywh ' , ]) :: (0 0 0 0"_)
wdqcolor=: (0 ". [: wd 'qcolor ' , ":) :: ( 0 0 0"_)
wdqd=: (wd bind 'qd') :: (''"_)
wdqer=: (wd bind 'qer') :: (''"_)
wdqform=: (0 ". wd bind 'qform') :: (0 0 800 600"_)
wdqhinst=: (0 ". wd bind 'qhinst') :: 0:
wdqhwndc=: (0 ". [: wd 'qhwndc ' , ]) :: 0:
wdqhwndp=: (0 ". wd bind 'qhwndp') :: 0:
wdqhwndx=: (0 ". wd bind 'qhwndx') :: 0:
wdqm=: (0 ". wd bind 'qm') :: (800 600 8 16 1 1 3 3 4 4 19 19 0 0 800 570"_)
wdqp=: (wd bind 'qp') :: (''"_)
wdqpx=: (wd bind 'qpx') :: (''"_)
wdqscreen=: (0 ". wd bind 'qscreen') :: (264 211 800 600 96 96 32 1 _1 36 36 51"_)
wdqwd=: (wd bind 'qwd')
wdcenter=: 0:
wdfit=: 0:

wdreset=: wd bind 'reset'
wdforms=: <;._2;._2 @ wd bind 'qpx'
wdisparent=: (boxopen e. 0: {"1 wdforms) ::0:
wdishandle=: (boxopen e. 1: {"1 wdforms) ::0:
wdinfo=: 3 : 0
'a b'=. _2{. boxopen y
if. 2=#$b=. ":b do. b=. }.,LF,.b end.
f=. 8 u: DEL&, @ (,&DEL) @ -.&(0 127{a.)
empty wd 'mb info ',(f a),' ',(f b)
)
wdquery=: 3 : 0
'yes no' wdquery y
:
t=. x [ 'a b'=. _2{. boxopen y
if. 32~:(3!:0) t do. t=. ;:t end.
t=. DEL&, @ (,&DEL) &.>t
if. 2=#$b=. ":b do. b=. }.,LF,.b end.
f=. 8 u: DEL&, @ (,&DEL) @ -.&(0 127{a.)
m=. 'mb query dialog ',(f a),' ',(f b),' ', ;x
wd m
)
mbopen=: 3 : 0
jpathsep wd 8 u: 'mb open1 ',y
)
mbsave=: 3 : 0
jpathsep wd 8 u: 'mb save ',y
)
wdget=: 4 : 0
nms=. {."1 y
vls=. {:"1 y
if. L. x do. vls {~ nms i. ,&.>x
else. > vls {~ nms i. <,x
end.
)

wdpclose=: [: wd :: empty 'psel ' , ';pclose' ,~ ":
immexj_ja_=: [: wd 'immexj *'&,
wbsk_ja_=: 11!:3050
readimg_ja_=: 11!:3000
getimg_ja_=: 11!:3001
writeimg_ja_=: 11!:3002
putimg_ja_=: 11!:3003
clippasteimg_ja_=: 11!:3004
clipcopyimg_ja_=: 11!:3005
gethash_ja_=: 11!:3100
getripemd160_ja_=: 11!:3101
getmd4_ja_=: 11!:3103
getmd5_ja_=: 11!:3104
getsha1_ja_=: 11!:3105
getsha224_ja_=: 11!:3106
getsha256_ja_=: 11!:3107
getsha384_ja_=: 11!:3108
getsha512_ja_=: 11!:3109
getsha3_224_ja_=: 11!:3110
getsha3_256_ja_=: 11!:3111
getsha3_384_ja_=: 11!:3112
getsha3_512_ja_=: 11!:3113
3 : 0 IFJA

dirmatch=: 3 : 'wd ''dirmatch '', ; dquote&.> 2 {. boxopen y'
open=: 3 : 'wd ''openj *'' , > {. getscripts_j_ y'
smact=: wd bind 'smact'
immexj_z_=: immexj_ja_
wbsk_z_=: wbsk_ja_

getsha1_z_=: getsha1_ja_
gethash_z_=: gethash_ja_
textview_z_=: textview_ja_

EMPTY
)
coclass 'ja'
JAREQ=: '1.0.0'
checkjaversion=: 3 : 0
f=. 1000 #. 0 ". ' ' I.@('.'=])} ]
ver=. wd 'version'
act=. f (<./ ver i.'/s') {. ver
req=. f JAREQ
if. req <: act do. return. end.
msg=. 'The JAndroid application needs updating.',LF2
msg=. msg,'Please download and install the latest Android apk.'
sminfo 'JAndroid';msg
)
checkjaversion^:IFJA''

cocurrent 'base'
