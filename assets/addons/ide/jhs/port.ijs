NB. port utilties - used with Jd/JHS node server
NB. should probably be an addons - for now it can live here
NB. Jd currently loads this script

coclass'jport'

killport=: 3 : 0 
'must be single port'assert 1=#y
pid=: pidfromport y
if. _1~:pid do. 
 select. UNAME
  case. 'Win' do. shell_jtask_'taskkill /f /pid ',":pid
  case. do. shell_jtask_'kill ',":pid
 end.
end. 
i.0 0
)

NB. pids,.ports
pidport=: 3 : 0
if. UNAME-:'Win' do.
NB.! may have same problem as lsof with multiple pid ports
 d=.  CR-.~each deb each <;._2 shell'netstat -ano -p tcp'
 b=. d#~;(<'TCP')-:each 3{.each d
 d=. ><;._2 each d,each' '
 d=. d#~(<'LISTENING')=3{"1 d
 a=. 1{"1 d
 a=. ;0".each(>:;a i: each':')}.each a
 d=. ;0".each 4{"1 d
 d,:a
else.
 NB. lsof reporting no ports gets interface error
 NB. lsof - some always reports f field and some only if requested - always request
 NB. p field can be followed by multiple f and n fields
 d=. shell_jtask_ :: 0: 'lsof -F pfn -s TCP:LISTEN -i TCP'
 if. d-:0 do. i.2 0 return. end.
 d=. <;._2 shell_jtask_'lsof -F pfn -s TCP:LISTEN -i TCP'
 r=. ''
 while. #d do.
  if. 'p'={.;{.d do.
   p=. {.d
   r=. r,3{.d
   d=. 3}.d
  else.
   r=. r,p,2{.d
   d=. 2}.d
  end.
 end.
 'unexpected lsof result'assert 0=3|#r
 d=. (3,~<.3%~#r)$r
 pids=. ;_1".each}.each{."1 d
 a=. {:"1 d
 ports=. ;_1".each a}.~each >:;a i: each':'
 pids,:ports
end.
)

NB. delays to allow task to start
pidfromport=: 3 : 0
'pid port'=. pidport''
r=. (port i. y){pid,_1
if. r=_1 do.
 6!:3[0.1
 'pid port'=. pidport''
 r=. (port i. y){pid,_1
 if. r=_1 do.
  6!:3[0.4
  'pid port'=. pidport''
  r=. (port i. y){pid,_1
 end. 
end.
r
)
