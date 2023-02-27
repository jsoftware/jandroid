
18!:55 <'jademo'
coclass 'jademo'

onStart=: jademo_run

sububar=: I. @(e.&'_')@]}
maketitle=: ' '&sububar each @ cutopen ;._2
fexist=: (1:@(1!:4) :: 0:) @ (fboxname &>) @ boxopen

asyncj=: ". wd 'getj asyncj'

rundemo=: 1 : 0
load bind ('~addons/ide/ja/demo/','.ijs',~m)
)

SOH=: 1{a.
toSOH=: [:;(SOH,~":)each

TITLES=: maketitle 0 : 0
controls dcontrols
datetime ddatetime
edit dedit
editm deditm
gl2 dgl2
image dimage
mbox dmbox
menu dmenu
pen_styles dpenstyles
plot dplot
progressbar dprogressbar
seekbar dseekbar
shader dshader
tabs dtabs
timer dtimer
video dvideo
viewmat dviewmat
webd3 dwebd3
webview dwebview
)

NB. =========================================================
JCDEMO=: 0 : 0
pc jademo closeok;pn "Demos Select";
bin v;
cc static1 static;cn "static1";
bin h;
cc addons button;cn "Install addons";
bin z;
wh _1 _2;cc listbox listbox;
bin z;
rem form end;
)

NB. =========================================================
jademo_run=: 3 : 0
wd JCDEMO
t=. 'Select a jandroid demo from the list below.',LF2
t=. t,'Click "install addons" to install the addons',LF
t=. t,'needed for the demos.'
wd 'set static1 text *',t
wd 'set listbox items ',;DEL,each ({."1 TITLES),each DEL
wd 'set listbox select 0'
wd 'setfocus listbox'
wd 'pshow;'
)

NB. =========================================================
jademo_close=: 3 : 0
wd 'pclose'
)

NB. =========================================================
jademo_listbox_select=: 3 : 0
fn=. > {: (".listbox_select) { TITLES
fn~0
)

NB. =========================================================
jademo_listbox_button=: 3 : 0
f=. }. > {: (".listbox_select) { TITLES
textview f;1!:1 <jpath '~addons/ide/ja/demo/',f,'.ijs'
)

NB. =========================================================
dcontrols=: 'controls' rundemo
ddatetime=: 'datetime' rundemo
dedit=: 'edit' rundemo
deditm=: 'editm' rundemo
dgl2=: 'gl2' rundemo
dimage=: 'image' rundemo
dmbox=: 'mbox' rundemo
dmenu=: 'menu' rundemo
dpenstyles=: 'penstyles' rundemo
dplot=: 'plot' rundemo
dprogressbar=: 'progressbar' rundemo
dshader=: 'shader' rundemo`notsupport@.(asyncj)
dseekbar=: 'seekbar' rundemo
dtabs=: 'tabs' rundemo
dtimer=: 'timer' rundemo
dvideo=: 'video' rundemo
dviewmat=: 'viewmat' rundemo
dwebd3=: 'webd3' rundemo
dwebview=: 'webview' rundemo

NB. =========================================================
jademo_addons_button=: 3 : 0
require 'pacman'
'update' jpkg ''
'install' jpkg 'api/gles graphics/bmp graphics/gl2 graphics/plot graphics/viewmat'
wd 'mb toast *jandroid demo addons installed'
)

NB. =========================================================
checkrequire=: 3 : 0
'req install'=. y
if. ''-:getscripts_j_ req do. 1 return. end.
if. *./fexist getscripts_j_ req do. 1 return. end.
wd 'mb toast *', 'To run this demo, first install: ',install
0
)

NB. =========================================================
notsupport=: 3 : 0
wd 'mb toast *','This demo is not supported on ', UNAME, ' ', wd 'version'
)

NB. =========================================================
wd 'activity ', >coname''

