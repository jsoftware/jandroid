NB. JHS - core services
require 'socket'

JHSVERSION_z_=: ' '''-.~9}.(d i. LF){.d=. (1 i.~ 'VERSION=:' E. d)}.d=. fread'~addons/ide/jhs/manifest.ijs'

coclass'jhs'

JIJSAPP=: 'jijs' NB. 'jijsm' for simple jijs editor
PROMPT=: '   '
JZWSPU8=: 226 128 139{a. NB. empty prompt kludge - &#8203; \200B
JSPATH=: '~addons/ide/jhs/js/jsoftware/' NB. path to jsoftware js/css files

NB. prevent child inherit - critical with fork
cloexec=: 3 : 0
if. -.IFUNIX do. return. end.
((unxlib 'c'),' fcntl i i i i') cd y,F_SETFD_jsocket_,FD_CLOEXEC_jsocket_
)

NB. J needs input - y is prompt - '' '   ' '      '
input=: 3 : 0
logapp 'jhs input prompt: ',":#y
try.
if. _1~:SKSERVER do. try. ".'urlresponse_',URL,'_ y' catch. end. end. NB. jijx
if. _1~:SKSERVER do. jbad'' end.
getdata'' NB. get and parse http request
if. 1=NVDEBUG do. smoutput seebox HNV end. NB. HNV,NV
if. -. ((<URL)e.boxopen OKURL)+.(cookie-:gethv'Cookie:')+.PEER-:LOCALHOST
                       do. r=. 'jev_get_jlogin_ 0'
elseif. 1=RAW          do. r=. 'jev_post_raw_',URL,'_'''''
elseif. 'post'-:METHOD do.
 r=. getv'jdo'
 t=. 0 i.~ r=' '
 NB. if. ')'={.t}.r do. r=. (t#' '),'jev_jcmd''',(}.t}.r),'''' end. NB.!
elseif. '.'e.URL       do. r=. 'jev_get_jfilesrc_ URL_jhs_'
elseif. 1              do. r=. 'jev_get_',URL,'_'''''
end.
logapp 'jhs sentence: ',r
if. JZWSPU8-:3{.r do. r=. 3}.r end. NB. empty prompt kludge
r,{.a. NB. J sentence to run

catch.
 logappx 'input error'
 exit'' NB. 2!:55[11 crashes
end.
)

CHUNKY_jhs_=: 0

NB. J has output - x is type, y is string
NB. MTYOFM  1 formatted result array
NB. MTYOER  2 error
NB. MTYOLOG  3 log
NB. MTYOSYS  4 system assertion failure
NB. MTYOEXIT 5 exit - not used
NB. MTYOFILE 6 output 1!:2[2
NB. x is type, y is string
output=: 4 : 0
logapp 'output type : ',":x
if. 5=x do. 'we should never get here'fappend 'log.txt' end.
try.
 s=. y NB. output string
 type=. x NB. MTYO type
 class=. >type{'';'fm';'er';'log';'sys';'';'file'
 
 if. (6=type)*.URL-:'jijx' do.
  t=. jhtmlfroma s
  if. '<br>'-:_4{.t do. t=. _4}.t end.
 
  a=. LOGN
  LOGN=: LOGN,'<div class="',class,'">',t,'</div><!-- chunk -->'
  if. jmarkremove-:jmarkrcnt{.s do.
   LOG_jhs_=: LOG,a NB. jjs ajax only not added to log
  else. 
   LOG_jhs_=: LOG,LOGN
  end.

  if. -.CHUNKY do.
   jhrajax_a LOGN
  else.
   jhrajax_b LOGN
  end.
  LOGN=: ''
  CHUNKY_jhs_=: 1
 
 elseif. (3~:type)+.-.'jev_'-:4{.dlb s do. NB. jev_... lines not logged
  NB. if. 3=type do. s=. PROMPT,dlb s end. NB. cleaning input is bad
  t=. jhtmlfroma s
  if. '<br>'-:_4{.t do. t=. _4}.t end.
  LOGN=: LOGN,'<div class="',class,'">',t,'</div>'
 end.
catch.
 logappx'output'
 exit''
end.
)

NB. event handler called by js event
NB. catch. changed to catchd. - december 2017 - for debug step with error
jev=: 3 : 0
try.
 ".t=. 'ev_',(getv'jmid'),'_',(getv'jtype'),' 0'
catchd.
 e=. LF,'error: J event handler',LF,'locale: ',;coname''
 e=. e,LF,t,LF,13!:12''
 echo e
 jhrcmds'alert *',e
end.
)

NB. get/post data - headers end with CRLF,CRLF
NB. post has Content-Length: bytes after the header
NB. listen and read until a complete request is ready
getdata=: 3 : 0
RAW=: 0
while. 1 do.
 logapp 'getdata loop'
 cloexec SKSERVER_jhs_=: 0 pick sdcheck_jsocket_ sdaccept_jsocket_ SKLISTEN

 NB. JHS runs blocking sockets and uses sdselect for timeouts
 NB. sdioctl_jsocket_ SKSERVER,FIONBIO_jsocket_,1

 try.
  PEER=: >2{sdgetpeername_jsocket_ SKSERVER
  d=. h=. ''
  while. 1 do.
   h=. h,  srecv''
   i=. (h E.~ CRLF,CRLF)i.1
   if. i<#h do. break. end.
  end.
  i=. 4+i
  d=. i}.h
  h=. i{.h
  parseheader h
  if. '100-continue'-:gethv'Expect:' do.
   hr=. 'HTTP/1.1 100 Continue',CRLF,CRLF    NB. inform client to send request body
   try.
    while. #hr do. hr=. (ssend hr)}.hr end.
   catch.
    logapp '100-continue error: ',13!:12''
   end.
  end.
  if. 'POST '-:5{.h do.
   len=.".gethv'Content-Length:'
   while. len>#d do. d=. d,srecv'' end.
   d=. len{.d
   METHOD=: 'post'
   seturl'POST'
   if. 3=nc<'jev_post_raw_',URL,'_' do.
    RAW=: 1
    NV=: d
   else.
    parse d
   end.
  else.
   METHOD=: 'get'
   seturl'GET'
   t=. (t i.' '){.t=. gethv 'GET'
   parse (>:t i.'?')}.t
  end.
  return.

 catch.
  NB. t=. 13!:12''
  NB. if. -.'|recv timeout:'-:14{.t do. NB. recv timeout expected
  NB.  smoutput '*** getdata error: ',t
  NB. end.
  NB. recv errors expected and are not displayed
  logapp 'getdata error: ',13!:12''
 end.
end.
)

NB. possibly interesting idea - urlmod - use modifier at end of URL to customize J/Javascript
seturl=: 3 : 0
URL=: jurldecode}.(<./t i.' ?'){.t=. gethv y
)

serror=: 4 : 0
if. y do.
 shutdownJ_jsocket_ SKSERVER ; 2
 sdclose_jsocket_ ::0: SKSERVER
 logapp x
 x 13!:8[3
end.
)

NB. return SKSERVER data (toJ)
NB. serror on
NB.  timeout, socket error, or no data (disconnect)
NB. PC_RECVSLOW 1 gets small chunks with time delay

srecv=: 3 : 0
z=. sdselect_jsocket_ SKSERVER;'';'';PC_RECVTIMEOUT
if. -.SKSERVER e.>1{z do.
 'recv timeout' serror 1  NB.0;'';'';'' is a timeout
end.

'recv not ready' serror SKSERVER~:>1{z
if. PC_RECVSLOW do.
 6!:3[1
 bs=. 100 NB. 100 byte chunks with 1 second delay
else.
 bs=. PC_RECVBUFSIZE
end.
'c r'=. sdrecv_jsocket_ SKSERVER,bs,0
('recv error: ',":c) serror 0~:c
'recv no data' serror 0=#r
r NB. used to do toJ here!
)

secs=: 3 : 0
":60#.4 5{6!:0''
)

NB. return count of bytes sent to SKSERVER
NB. serror on
NB.  timeout, socket error, no data sent (disconnect)
NB. PC_SENDSLOW 1 simulates slow connection
ssend=: 3 : 0
z=. sdselect_jsocket_ '';SKSERVER;'';PC_SENDTIMEOUT
'send not ready' serror SKSERVER~:>2{z
if. PC_SENDSLOW do.
 6!:3[0.2
 y=. (100<.#y){.y NB. 100 byte chunks with delay
end.
'c r'=. y sdsend_jsocket_ SKSERVER,0
('send error: ',":c) serror 0~:c
'send no data' serror 0=r
r NB. bytes sent
)

putdata=: 3 : 0
logapp'putdata'
try.
 while. #y do. y=. (ssend y)}.y end.
catch.
 logapp 'putdata error: ',13!:12''
end.
)

NB. set HNV from request headers
parseheader=: 3 : 0
y=. toJ y
a=. <;._2 y
i=. (y i.' '),>:}.>a i. each ':'
HNV=: (i{.each a),.dlb each i}.each a
)

NB. global NV set from get/post data
NB. name/values delimited by & but no trailing &
NB. namevalue is name[=[value]]
NB. name0value[&name1value1[&name2...]]
parse=: 3 : 0
try.
 y=. toJ y
 d=. <;._2 y,'&'#~0~:#y
 d=. ;d,each('='e.each d){'=&';'&'
 d=. <;._2 d rplc '&';'='
 NV=: jurldecodeplus each (2,~(2%~#d))$d
catch.
 smoutput '*** parse failed: ',y
 NV=: 0 2$''
end.
)

gethv=: 3 : 0
i=. (toupper&.>0{"1 HNV)i.<toupper y
>1{i{HNV,0;''
)

NB. get value for name y - '' for no value
getv=: 3 : 0
i=. (0{"1 NV)i.<,y
>1{i{NV,0;''
)

NB. get values for names
getvs=: 3 : 0
((0{"1 NV)i.<;._1 ' ',deb y){(1{"1 NV),<''
)

NB. shortest full name with ~.../
jshortname=: 3 : 0
p=. jpath y
if. -.'/'e.p do.
  p=. (1!:43''),'/',p NB. c:a.ijs ends up as a bad file name - what to do?
end.
p=. <p
'a b'=.<"1 |:SystemFolders_j_,UserFolders_j_
c=. #each b
f=. p=(jpath each b,each'/'),each (>:each c)}.each p
if.-.+./f do. >p return. end.
d=. >#each f#b
m=. >./d
f=. >{.(d=m)#f#a
('~',f,m}.>p) NB. rplc '~home/';'~/'
)

logclear=: 3 : ''''' 1!:2 logappfile'

NB. log timestamp
lts=: 3 : 0
20{.4 3 3 3 3 3":<.6!:0''
)

logapp=: 3 : 0
if. -.PC_LOG do. return. end.
((lts''),(>coname''),' : ',y,LF)1!:3 logappfile
)

NB. force log of this and following messages
logappx=: 3 : 0
PC_LOG=: 1
logapp y,' error : ',13!:12''
)

logstdout=: 3 : 'i.0 0[(y,LF) 1!:2[4'

audio=: 3 : 0
assert fexist y
jhtml'<audio controls="controls"><source src="',y,'" type="audio/mp3">not supported</audio>'
)

NB. z locale utilities


auto_welcome=: 0 : 0

if you don't see a new jijx tab in your browser,
 manually browse to: http://<LOCALHOST>:<PORT>/jijx

best practice:
 close server with shortcut Esc-q (Escape then q) in the jijx page

Ctrl+c here signals an interrupt to J.

)

noauto_welcome=: 0 : 0

manually browse to: http://<LOCALHOST>:<PORT>/jijx

best practice:
 close server with shortcut Esc-q (Escape then q) in the jijx page

Ctrl+c here signals an interrupt to J.
)

setsid=: 0 : 0

setsid not available for properly starting browser 
you can install setsid with something like:
...$ sudo apt-get install util-linux

**************************************************
)

bind_failed=: 0 : 0

bind to port <PORT> failed - <ERROR>

if another JHS server is already on the port, close this task,
 and use the already running server

if another service is on the port, close this task,
 and edit file ~addons/ide/jhs/config/jhs.cfg to use another port

to run a new JHS session on the next free port, run the following:
   nextport_jhs_''
)

NB. html/css/js config parameters
configdefault=: 3 : 0
PORT=:   65001       NB. private port range 49152 to 65535
USER=:   ''          NB. 'john' - login
PASS=:   ''          NB. 'abra' - login
TIPX=:   ''          NB. tab title prefix - distinguish sessions
GUEST=:  0           NB. not a guest 
AUTO=:   1           NB. start browser (if necessary) and browse to http:/localhost:PORT/jijx
NB. Esc-q rules
NB. 0 - server closed     - page disabled
NB. 1 - server not closed - page disabled
NB. 2 - confirm() close   - guest server
QRULES=: 0           NB. Esc-q - see ev_close_click in jijx.ijs

NB. following are options and css name values

PC_JICON=:         '#33D2F6'
PC_FONTFIXED=:     '"courier new","courier","monospace"'
PC_FONTVARIABLE=:  '"sans-serif"'
PC_FONTSIZES=:     '"640 48px 820 36px"' NB. w0 css0 w1 css1 ...
PC_BOXDRAW=:       0        NB. 0 utf8, 1 +-, 2 oem

PC_BUTTON=:        'lightgrey'
PC_MENU_FOCUS=:    'lightgrey'
PC_MENU_HOVER=:    'lightgrey'

PC_FM_COLOR=:      'black'  NB. formatted output
PC_ER_COLOR=:      'red'    NB. error
PC_LOG_COLOR=:     'blue'   NB. log user input
PC_SYS_COLOR=:     'purple' NB. system error
PC_FILE_COLOR=:    'green'  NB. 1!:! file output

PC_CHECK1_BACKGROUND=: 'darkgrey'
PC_CHECK0_BACKGROUND=: 'white'

NB. following are css chunks - PS_... PC_... values replaced in getcss''
PS_FONTCODE=:      'font-family:',PC_FONTFIXED,';font-weight:550;white-space:pre;'

)

NB. undocumneted config parameters
PC_RECVSLOW=:     0        NB. 1 simulates slow recv connection
PC_SENDSLOW=:     0        NB. 1 simulates slow send connection
PC_LOG=:          0        NB. 1 to log events
PC_RECVBUFSIZE=:  10000    NB. size of recv buffer
PC_RECVTIMEOUT=:  5000     NB. seconds for recv timeout
PC_SENDTIMEOUT=:  5000     NB. seconds for send timeout

USERNAME=: '' NB. JUM left over

NB. JUM - no longer used
NB. fix userfolders for username y
NB. adjust SystemFolders for multi-users in single account
fixuf=: 3 : 0
USERNAME=: y
if. 0=#y do. return. end.
t=. SystemFolders_j_
a=. 'break';'config';'temp';'user'
i=. ({."1 t)i.a
p=. <'~user/jhs/',y
n=. a,.jpath each p,each '/break';'/config';'/temp';''
SystemFolders_j_=: n i} t
(":2!:6'') 1!:2 <jpath'~user/.jhspid'
1!:44 jpath'~user' NB. cd
)

NB. similar to startup_console in boot.ijs
startupjhs=: 3 : 0
f=. jpath '~config/startup_jhs.ijs'
if. 1!:4 :: 0: <f do.
  try.
    load__ f
  catch.
    smoutput 'An error occurred when loading startup script: ',f
  end.
  cocurrent 'jhs'
end.
)

dobind=: 3 : 0
sdcleanup_jsocket_''
cloexec SKLISTEN=: 0 pick sdcheck_jsocket_ sdsocket_jsocket_''
if. IFUNIX do.  sdsetsockopt_jsocket_ SKLISTEN;SOL_SOCKET_jsocket_;SO_REUSEADDR_jsocket_;2-1 end.
sdbind_jsocket_ SKLISTEN;AF_INET_jsocket_;y;PORT
)

nextport=: 3 : 0
while.
 PORT=: >:PORT
 TIPX=: ":PORT
 r=.dobind y
 shutdownJ_jsocket_ SKLISTEN ; 2
 sdclose_jsocket_ ::0: SKLISTEN
 sdcleanup_jsocket_''
 erase'SKLISTEN_jhs_'
 0~:r
do. end.
init''
)

addOKURL=: 3 : 0
rmOKURL y
OKURL=: OKURL,<y
)

rmOKURL=: 3 : 0
OKURL=: OKURL-.<y
)

NB. simplified config
jhscfg=: 3 : 0
if. _1=nc<'PORT' do. NB. avoid if config already done or manually adjusted
 configdefault''
 if. 3=nc<'config' do. config'' end.
end. 
'PORT invalid' assert (PORT>49151)*.PORT<2^16
'PASS invalid' assert 2=3!:0 PASS
if. _1=nc<'USER' do. USER=: '' end. NB. not in JUM config
'USER invalid' assert 2=3!:0 USER
PASS=: ,PASS
USER=: ,USER
if. _1=nc<'TIPX' do. TIPX=: '' end.
TIPX=: ,TIPX
TIPX=: TIPX,(0~:#TIPX)#'/'
'TIPX invalid' assert 2=3!:0 TIPX
if. _1=nc<'TARGET' do. TARGET=: '_blank' end.
if. _1=nc<'OKURL' do. OKURL=: '' end. NB. URL allowed without login
)

NB. leading &nbsp; for Chrome delete all
welcome=: 0 : 0
<span><font style="font-size:2rem; color:blue; padding:0 8px 0 16px;" >J</font>
)

NB. SO_REUSEADDR allows server to kill/exit and restart immediately
init=: 3 : 0
echo'JHS - J HTTP Server'
'already initialized' assert _1=nc<'SKLISTEN'
IFJHS_z_=: 1
canvasnum=: 1
chartnum=: 1
jhscfg''
PATH=: jpath'~addons/ide/jhs/'
NB. IP=: getexternalip''
NB. LOCALHOST=: >2{sdgethostbyname_jsocket_'localhost'
LOCALHOST=: '127.0.0.1'
logappfile=: <jpath'~user/.applog.txt' NB. username
SETCOOKIE=: 0
NVDEBUG=: 0 NB. 1 shows NV on each input
LOG=: jmarka,('overview'jhb'J - click me'),jmarkz
LOGN=: ''
PDFOUTPUT=: 'output pdf "',(jpath'~temp\pdf\plot.pdf'),'" 480 360;'
DATAS=: ''
PS=: '/'
cfgfile=. jpath'~addons/ide/jhs/config/jhs_default.ijs'
r=. dobind''
if. 0~:r do.
 echo bind_failed hrplc 'PORT ERROR';(":PORT);sderror_jsocket_ r
 return.
end.
sdcheck_jsocket_ sdlisten_jsocket_ SKLISTEN,5 NB. queue length
SKSERVER_jhs_=: _1
boxdraw_j_ PC_BOXDRAW
startupjhs''
cookie=: 'jcookie=',":{:6!:0''
input_jfe_=: input_jhs_  NB. only use jfe locale to redirect input/output
output_jfe_=: output_jhs_

if. AUTO *. (<UNAME)e.'Linux';'FreeBSD';'OpenBSD' do. NB. require linux manual start if no setsid
 try. 2!:0'which setsid' catch. echo setsid[AUTO=: 0 end.
end. 

if. AUTO do.
 url=. 'http://<LOCALHOST>:<PORT>/jijx'hrplc'LOCALHOST PORT';LOCALHOST;":PORT
 select. UNAME
 case. 'Win'    do. shell_jtask_'start ',url
 case. 'Linux';'FreeBSD';'OpenBSD'  do.
  if. (0;'') -.@e.~ <2!:5 'DISPLAY' do.
   assert. *#t=. dfltbrowser_j_''
   2!:0 'setsid ',t,' ',url,' </dev/null >/dev/null 2>&1 &'
  end.
 case. 'Darwin' do. 2!:0'open ',url,' &'
 end.
 echo auto_welcome hrplc 'LOCALHOST PORT';LOCALHOST;":PORT
else.
 echo noauto_welcome hrplc 'LOCALHOST PORT';LOCALHOST;":PORT
end.
jfe 1
)

NB. load rest of JHS core
load__'~addons/ide/jhs/util.ijs'
load__'~addons/ide/jhs/utilh.ijs'
load__'~addons/ide/jhs/sp.ijs'
load__'~addons/ide/jhs/tool.ijs'
load__'~addons/ide/jhs/d3.ijs'
load__'~addons/ide/jhs/chart.ijs'
load__'~addons/ide/jhs/vocabhelp.ijs'
load__'~addons/ide/jhs/jdoc.ijs'
load__'~addons/ide/jhs/extra/man.ijs'
load__'~addons/ide/jhs/widget/jhot.ijs'

NB. load addons, but do not fail init if not found
load__ :: ['~addons/math/misc/trig.ijs' NB. used in overview.ijs chart
load__ :: ['~addons/convert/json/json.ijs'
load__ :: ['~addons/convert/pjson/pjson.ijs' NB. preferred - kill off json.ijs 

NB. jev_... inputs are not displayed in log
jev_run_z_=: 0!:111[ NB. tacit - run sentence in jijx

stub=: 3 : 0
'jev_get y[load''~addons/ide/jhs/',y,'.ijs'''
)

NB. app stubs to load app file
jev_get_jijx_=:    3 : (stub'jijx')
jev_get_jfile_=:   3 : (stub'jfile')
jev_get_jcopy_=:   3 : (stub'jcopy')
jev_get_jijs_=:    3 : (stub'jijs')
jev_get_jfif_=:    3 : (stub'jfif')
jev_get_jpacman_=: 3 : (stub'jpacman')
jev_get_jlogin_=:  3 : (stub'jlogin')
jev_get_jfilesrc_=:3 : (stub'jfilesrc')
jev_get_jdebug_=:  3 : (stub'jdebug')
jev_get_jlocale_=: 3 : (stub'jlocale')

NB. simple wget with sockets - used to get google charts png

NB. jwget 'host';'file'
NB. jwget 'chart.apis.google.com';'chart?&cht=p3....'
NB. simplistic - needs work to be robust and general
NB. JHS get/put and jwget should probably share code
wget=: 3 : 0
'host file'=. y
ip=. >2{sdgethostbyname_jsocket_ host
try.
 sk=. >0{sdcheck_jsocket_ sdsocket_jsocket_''
 sdcheck_jsocket_ sdconnect_jsocket_ sk;AF_INET_jsocket_;ip;80
 t=. gettemplate rplc 'FILE';file
 while. #t do. t=.(>sdcheck_jsocket_ t sdsend_jsocket_ sk,0)}.t end.
 h=. d=. ''
 cl=. 0
 while. (0=#h)+.cl>#d do. NB. read until we have header and all data
  z=. sdselect_jsocket_ sk;'';'';5000
  assert sk e.>1{z NB. timeout
  'c r'=. sdrecv_jsocket_ sk,10000,0
  assert 0=c
  assert 0~:#r
  d=. d,r
  if. 0=#h do. NB. get headers
   i=. (d E.~ CRLF,CRLF)i.1 NB. headers CRLF delimited with CRLF at end
   if. i<#d do. NB. have headers
    i=. 4+i
    h=. i{.d NB. headers
    d=. i}.d
    i=. ('Content-Length:'E. h)i.1
    assert i<#h
    t=. (15+i)}.h
    t=. (t i.CR){.t
    cl=. _1".t
    assert _1~:cl
   end.
  end.
 end.
catch.
 shutdownJ_jsocket_ sk ; 2
 sdclose_jsocket_ ::0: sk
 smoutput 13!:12''
 'get error' assert 0
end.
shutdownJ_jsocket_ sk ; 2
sdclose_jsocket_ ::0: sk
h;d
)

jwget_z_=: wget_jhs_

NB. jwget template
gettemplate=: toCRLF 0 : 0
GET /FILE HTTP/1.1
Host: 127.0.0.1
Accept: image/gif,image/png,*/*
Accept-Language: en-ca
UA-CPU: x86
Accept-Encoding: gzip, deflate
User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; WOW64; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729)
Connection: Keep-Alive

)

NB. return first ip address that is not localhost
getlanip=: 3 : 0
if. IFWIN do.
 r=. deb each<;._2 spawn_jtask_'ipconfig'
 r=. ((<'IPv4 Address')=12{.each r)#r
 r=. (>:;r i.each':')}.each r
else.
 r=. deb each <;._2[2!:0'ip address'
 r=. 5}.each((<'inet ')=5{.each r)#r
 r=. (r i. each '/'){.each r
end.
r=. deb each r
r=. deb each(r i.each' '){.each r
r=. r-.<'127.0.0.1'
'no lan ip' assert 0<#r
if. 1<#r do. echo 'multiple lan ips: ',LF,;LF,~each' ',each r end.
;{.r
)

0 : 0 NB. getlanip with old ifconfig
elseif. UNAME-:'Darwin' do.
 r=. <;._2[2!:0'ifconfig';
 r=. deb each r rplc each <TAB;' ' 
 r=. ((<'inet ')=5{.each r)#r
 r=. 5}.each r
elseif. 1 do.
 r=. deb each<;._2[2!:0'ifconfig'
 r=. ((<'inet addr:')=10{.each r)#r
 r=. 10}.each r
end.
)

getexternalip=: 3 : 0
z=. >2{sdgethostbyname_jsocket_ >1{sdgethostname_jsocket_''
if. ('255.255.255.255'-:z) +. (';.'-:6{.z) +. '192.168.'-:8{.z do.
 if. (<UNAME)e.'Linux';'FreeBSD';'OpenBSD' do.
  a=. , 2!:0 ::_1: 'wget -q --timeout=3 --waitretry=0 --tries=3 --retry-connrefused -O - http://www.checkip.org/'
 elseif. UNAME-:'Darwin' do.
  a=. , 2!:0 ::_1: 'curl -s --max-time 7 -o - http://www.checkip.org/'
 elseif. UNAME-:'Win' do.
  a=. , spawn_jtask_ '"',(jpath '~tools/ftp/wget.exe'),'" -q --timeout=3 --waitretry=0 --tries=3 --retry-connrefused -O - http://www.checkip.org/'
 elseif. do.
  a=. ,_1
 end.
 if. 1 e. r=. '<h1>Your IP Address:' E. a do.
   z=. ({.~ i.&'<') (}.~ [: >: i.&'>') (20+{.I.r)}.a
 else.
   z=. '?.?.?.?'
 end.
end.
z
)

NB. shutdownx JHS
NB. y: integer return code for 2!:55
NB.    ''  just shutdown JHS, J not exit
shutdownx=: 3 : 0
 shutdownJ_jsocket_ SKLISTEN ; 2
 sdclose_jsocket_ ::0: SKLISTEN
 sdcleanup_jsocket_''
 erase'SKLISTEN'
 IFJHS_z_=: 0
 jfe 0
 2!:55^:(''-.@-:y)y
) 

wd_z_=: 3 : '''wd not supported in JHS''assert 0'

jxsleep_z_=: 6!:3
