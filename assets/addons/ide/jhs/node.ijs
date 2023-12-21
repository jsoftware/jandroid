NB. JHS node server

coclass'jhs'
require'~addons/ide/jhs/port.ijs'

node_man=: 0 : 0
*** summary
1. start JHS
   load'~addons/ide/jhs/node.ijs'
   node_config_jhs_'' 
   node_jhs_'start'
2. browse to url in start report   
 
*** https server requires certificate
self-signed certificate is provided for a quick start
this requires exception permission when you first browse the page
https://nodejs.org/en/knowledge/HTTP/servers/how-to-create-a-HTTPS-server/

*** jijx menu ide>break signals break to J from node proxy client

*** static ip - port forwarding - externalip
your machine ip address changes occasionaly if configured as dynamic
search www for how to set a static ip

access from a remote location requires router port forwarding
e.g., forward 65101 -> 65101 192.168.0.123
search www for how to configure port forwarding on your router

your router ip (used by remote users) will change occasionaly
make note of the remote address before you leave home

*** security - remote access to J on your machine!
1. have a good logonkey and protect it
2. don't leave a machine connected to your JHS server unattended
3. always logofff (jijx menu ide>logoff) when finished
)

pjhsnode=: '~temp/jhs/node'
mkdir_j_ pjhsnode

3 : 0''
if. _1=nc<'NODEPORT' do. NODEPORT=: 0 end.
)

config_validate=: 3 : 0
'node pem nodeport key'=. y
node=. jpath node
pem=. jpath pem
(' is not node binary',~node)assert 1=ftype node
(' is not pem folder',~pem)assert 1=ftype pem,'/cert.pem'
(' is not valid nodeport',~":nodeport) assert (3000<:nodeport)*.nodeport<:65536
'key too short' assert 5<#key
)

node_config_man=: 0 : 0
for initial config or for changes run one of the following:
   node_config_jhs_ node ; logonkey    NB. default pem and nodeport
   node_config_jhs_ node ; pem ; nodeport ; logonkey
   node_config_jhs_ logonkey           NB. change logonkey
   
where:   
 node     - node executable  - e.g. C:/Program Files/nodejs/node.exe
 logonkey - key required to logon
 pem      - folder with cert.pem file - default ~addons/ide/jhs/node
 nodeport - port served by node proxy server - default 100+PORT_jhs_
 
if node is not installed: download and install from: https://nodejs.org
)

node_config=: 3 : 0
if. (''-:y) +. -.1 2 4 e.~ #boxopen y do. echo node_config_man return. end.
if. 1=#boxopen y do. y=. (fread pjhsnode,'/argbin');y end.
if. 2=#y do. y=. ({.y),'~addons/ide/jhs/node';(100+PORT);{:y end.
config_validate y
'bin pem nodeport key'=: y
(hostpathsep jpath bin)fwrite pjhsnode,'/argbin'
(hostpathsep jpath pem)fwrite pjhsnode,'/argpem'
((":nodeport),' ',key)fwrite pjhsnode,'/arg'
i.0 0
)

node_config_get=: 3 : 0
'config_node_jhs_ must be run first' assert fexist pjhsnode,'/arg'
(fread pjhsnode,'/argbin');(fread pjhsnode,'/argpem');;:fread pjhsnode,'/arg'
)

NB. start or stop node server for current JHS server
node=: 3 : 0
if. -.(<,y)e.;:'? start stop' do. echo node_man return. end.
'node pem port key'=. node_config_get''
nport=. 0".port
killport_jport_ nport
if. 'stop'-:y do. i.0 0 return. end.
breakfile=. hostpathsep setbreak'node'
pem=. fread pjhsnode,'/argpem'
arg=. port,' ',key,' ',(":PORT),' "',breakfile,'" "',pem,'"'
t=. '"<BIN>" "<FILE>" <ARG> > "<OUT>" 2>&1'
bin=. hostpathsep fread pjhsnode,'/argbin'
file=. jpath'~addons/ide/jhs/node/server'
nodeout=: hostpathsep jpath pjhsnode,'/std.log'
t=. t rplc '<BIN>';bin;'<FILE>';file;'<ARG>';arg;'<OUT>';nodeout

echo t

fork_jtask_ t
'server failed to start' assert _1~:pidfromport_jport_ nport NB. pidfromport has delays
node_status''
)

nodetemplate=: 0 : 0
node port <NODEPORT> is proxy server for JHS port <PORT>
firewall must be configured to allow access to <NODEPORT>

local:  https://localhost:<NODEPORT>/jijx'
lan:    https://<LAN>:<NODEPORT>/jijx'
remote: https://<REMOTE>:<NODEPORT>/jijx'

set static ip address to avoid <LAN> last field changing
remote requires router port forwarding: <NODEPORT> -> <NODEPORT> <LAN>

   node_std_jhs_'' NB. node stdout/stderr
)

NB. value error PORT
NB. report how to access node JHS proxy server
node_status=: 3 : 0
'node pem port key'=. node_config_get''
if. _1=pidfromport_jport_ 0".port do. 'node server is not running' return. end.
nodetemplate rplc '<NODEPORT>';(":port);'<PORT>';(":PORT);'<LAN>';(getlanip_jhs_'');'<REMOTE>';getexternalip_jhs_''
)

node_std=: 3 : 'fread nodeout'

NB. start JHS server to serve NODE proxy server
NB. y - '' uses ~addons/ide/jhs/config/jhs-node.cfg
NB. y - 'file to use '
startJHS=: 3 : 0
'not from JHS'assert -.IFJHS
port=. 65001 NB. work required for other ports
killport_jport_ port
f=. ;(y-:''){y;'~addons/ide/jhs/config/jhs-node.cfg'
mkdir_j_ '~temp/jhsnode'
jhsout=: jpath'~temp/jhsnode/jhs.log'
t=. '"<BIN>" "<FILE>" > "<OUT>" 2>&1'
a=. t rplc '<BIN>';(hostpathsep jpath'~bin/jconsole');'<FILE>';f;'<OUT>';jhsout
fork_jtask_ a
if. _1=pidfromport_jport_ port do. NB. pidfromport has delays
 echo a,LF,fread jhsout_jhs_
 'JHS server failed to start' assert 0
end.
)

NB. nodeport ; pem ; key - https:// required
startNODE=: 3 : 0
'not from JHS'assert -.IFJHS
'nodeport pem key'=. y
'nodeport must be 65101'assert nodeport-:65101
mkdir_j_ '~temp/jhsnode'
nodeout=: jpath'~temp/jhsnode/node.log'
killport_jport_ nodeport
port=. nodeport-100

NB. breakfile needs pid from the JHS server
breakfile=. (jpath '~break/'),(":pidfromport_jport_ port),'.node'

pem=. ;(pem-:''){pem;jpath'~addons/ide/jhs/node'

arg=. (":nodeport),' ',key,' ',(":port),' "',breakfile,'" "',pem,'"'
file=. jpath'~addons/ide/jhs/node/server'
bin=. LF-.~fread'nodebin'

t=. '"<BIN>" "<FILE>" <ARG> > "<OUT>" 2>&1' 
a=. t rplc '<BIN>';bin;'<FILE>';file;'<ARG>';arg;'<OUT>';nodeout
fork_jtask_ a
if. _1=pidfromport_jport_ nodeport do. NB. pidfromport has delays
 echo a,LF,fread nodeout_jhs_
 'NODE server failed to start' assert 0
end. 
)
