NB. simple project manager and related tools

require'~addons/ide/jhs/jfif.ijs'

coclass'jsp'

sp_z_       =: sp_jsp_
spf_z_      =: spf_jsp_
spr_z_      =: spr_jsp_
spx_z_      =: spx_jsp_
NB. sprunner_z_ =: 0!:111[ NB. tacit, but called from explicit so =. must still map to =:
NB. sprunnerx_z_=: 0!:110[
NB. tacit gets error: public assignment to a name with a private value
sprunner_z_ =: 3 : '0!:111 y'
sprunnerx_z_=: 3 : '0!:110 y'

sptable_z_  =: sptable_jsp_

sphelp_z_=: 0 : 0
sp (simple project) defined by ~addons/ide/jhs/sp.ijs
part of JHS and can be loaded in any front end

simple project:
 sp 0    init example SPFILE
 ctrl+,  load SPFILE (JHS)
 sp ''   load SPFILE

 sp f    set non-default SPFILE and load it

 SPFILE carried over sessions

SPFILES/shortname:
 sp use of an ijs file adds it to SPFILES
 SPFILES carried over sessions

SPFILES/shortname example:
 'echo 456'fwrite f=: '~temp/b.ijs'
 spf f   NB. sp use adds to SPFILES
 spf 'b' NB. shortname to name
 spr 3   NB. shortnames and 3{.SPFILES
 sp  'b' NB. set SPFILE from shortname and load it
 spf ''  NB. SPFILE
 
utilities:
 spr n    shortnames and n{.SPFILES
 spf f    file from filename or shortname or SPFILE
 sptable 3 2$'asdf';123;'def';(i.2 3);'q';23
)

spxhelp_z_=: 0 : 0
simple project managed execution:
 spx f        set SPXFILE
 crtl+.       JHS advance
 menu >       JHS advance
 ctrl+j       JQt advance
 spx''        advance
 spx 0        status
 spx n        run line n
 spx n m      run lines n through m
 spx':'       sections
 spx'~addons/ide/jhs/spx/spx.ijs' 
)

MAXRECENT=: 40 NB. max recent files 
sprecentf=: '~temp/sp/recent.txt'
spspf    =: '~temp/sp/sp.txt'

cfile=: 3 : 0
t=. fread y
>(_1-:t){t;''
)

sp=: 3 : 0
a=. '~temp/sp/spfile.ijs'
if. 0-:y do.
 SPFILE_z_=: a
 SPFILE fwrite spspf
 if. fexist SPFILE do.
  t=. (_4}.SPFILE),'_',((":6!:0'')rplc' ';'_';'.';'_'),'.ijs'
  echo 'SPFILE backed up as: ',t
 end. 
 'NB. script for simple project'fwrite '~temp/spexample.ijs'
 (fread'~addons/ide/jhs/spfile_template.ijs')fwrite SPFILE
 echo'ctrl+, loads SPFILE (',SPFILE,')'
 return.
end.
if. (''-:y) *. -.fexist a do. sp 0 end.
t=. spf y
assert. (fexist t)['must exist'
SPFILE_z_=: t
t fwrite spspf
load__ t
)

NB. only add .ijs and non temp files
addrecent=: 3 : 0
t=. >(('.ijs'-:_4{.y)*.-.*/(;shorts_jsp_ y)e.'0123456789'){'';y
t=. ~.SPFILES,~<t
t=. (;fexist t)#t
SPFILES_z_=: (MAXRECENT<.#t){.t
(;SPFILES,each LF) fwrite sprecentf
y
)

NB. filename / '' for project / spr shortname
NB. first spr shortname prefix that matches 
NB. error if file does not exist or is not .ijs
NB. file is added to head of of recent
spf=: 3 : 0
if. ''-:y do.
 assert. fexist SPFILE['project must exist'
 r=. SPFILE
elseif. +./y e.'~/.' do.
 r=. y
elseif. 1 do.
 c=. (shorts SPFILES)=<,y
 assert. 0~:+/c['not found in recent'
  r=. ,>{.c#SPFILES
end.
assert. fexist r['must exist'
if. '.ijs'-:_4{.r do. addrecent r end. NB. addrecent only for .ijs
r
)

shorts=: 3 : 0
t=. '/',each boxopen y
_4}.each(>:>t i:each '/')}.each t
)

spr=: 3 : 0
addrecent'' NB. brute force cleanup
t=. y{.SPFILES
sptable(shorts_jsp_ t),.t
)

spxinit=: 3 : 0
'only runs in JHS'assert IFJHS
'file must exist' assert fexist spf y
ADVANCE_jijx_=: 'spx'
SPXFILE_z_=: spf y
SEM=: get SPXFILE
SEMN=: 1
a=. SPXFILE,LF,'advance through lab:',LF
b=. 'keyboard: ctrl+. (ctrl+dot)<br>touchscreen: right <span style="color:green;">green</span> button'
echo a
echo jhtml'<div><font style="color:red;font-weight:bold">',b,'</font></div>'
NB. status''
i.0 0
)


spxqt=: 3 : 0
if. 0-:y do.
 spx''
else.
 labs_run_jqtide_=: qtsave 5!:0
 labs_run_jqtide_ y
end. 
)


SECTION=: 'NB.spxsection:'
SECTIONC=: #SECTION

spxsections=: 3 : 0
b=.(<SECTION)=SECTIONC{.each y
i=.b#i.#y
t=. (#SECTION)}.each i{y
;t=. (<'spx'':'),each t,each <'''',LF
)

spx=: 3 : 0
nsec=. -.':'={.y
if. nsec*.(0~:#y)*.2=3!:0 y do. spxinit y return. end.
if. -.fexist SPXFILE do. smoutput 'not initialized - do spxinit' return. end.
if. ''-:y do. spx SEMN return. end.
if. 0={.y do. status'' return. end.
d=. SEM
SEM=:get SPXFILE

if. -.nsec do.
 if. ':'-:y do. spxsections SEM return. end.
 a=. SECTION,deb}.y
 s=. (#a){.each SEM
 i=. s i.<a
 if. i<#s do. y=. >:i end.
end.

if. 2=#y do.
 if. -.(3!:0[4) e. 1 4 do. smoutput 'not integer line numbers' return. end.
 SEMN=: {.y
 b=. ({:y)<.#SEM
 while. SEMN<:b do.
  spx SEMN
  a=. SEMN
 end.
 i.0 0
 return.
end.

if. (0~:$$y)+.-.(3!:0[4) e. 1 4 do. smoutput 'not integer line number' return. end.
if. y<0 do. smoutput 'not valid line number' return. end.
SEMN=: y
label_top.
if. SEMN>#SEM do. 'end of script' return. end.
ot=. 0 NB. lines
ndx=. <:SEMN
d=. >ndx{SEM
if. 0=#d do. SEMN=:>:SEMN goto_top. end. NB. empty line ignored
if. 0=#d-.' ' do. SEMN=:>:SEMN[echo ;IFJHS{'';LF goto_top. end.

if. 'NB.spxhr:'-:9{.deb d do.
 SEMN=:>:SEMN
 if. IFJHS do. jhtml_jhs_'<hr/>' else. echo 80$'_'end.
 goto_top.
end.

if. 'NB.spxrun:'-:10{.deb d do.
 SEMN=:>:SEMN
 ".10}.d
 i.0 0
 return.
end. 

if. 'NB.spxaction:'-:13{.deb d do.
 SEMN=:>:SEMN
 d=. 13}.d
 if. IFJHS do.
  jhtml_jhs_ '<div><font style="color:red;font-style:bold;">action required: <TEXT></font></div>'rplc'<TEXT>';d
 else.
  echo 'action: ',d
 end.
 return.
 goto_top.
end.

if. 'NB.spxhtml:'-:11{.deb d do.
 SEMN=:>:SEMN
 d=. 11}.d
 if. IFJHS do.
  jhtml_jhs_ d
 else.
  echo 'html: ',d
 end.
 goto_top.
end.

if. 'NB.spxlatex:'-:12{.deb d do.
 SEMN=:>:SEMN
 d=. 12}.d
 if. IFJHS do.
  jhtml_jhs_'<img src="http://latex.codecogs.com/svg.latex?',d,'" border="0"/>'
 else.
  echo 'latex: ',d
 end.
 goto_top.
end.

if. iscolon d do. NB. collect : lines
 ot=. 1
 c=. (dltb each ndx}.SEM) i. <,')'
 d=. '   ',;LF,~each (ndx+i.>:c){SEM
 ndx=. ndx+c
end.
if. isnb d do. NB. collect comment lines
 ot=. 2
 c=. (>(3{.each dltb each ndx}.SEM) -: each <'NB.')i.0
 d=. ;LF,~each (ndx+i.c){SEM
 ndx=. ndx+<:c
end.
NB. kludge to convert =. tp =:
i=. d i.LF
t=. i{.d
if. (<'=.')e.;:t do.
 d=. (t rplc '=.';'=:'),i}.d
end.
 select. ot
 case. 0 do. NB. single line
   sprunner__ d
 case. 1 do. NB. :
  a=. <;.2 d
  b=. ;:}:;{.a
  if. b-:;:'0 : 0' do.
   jhtml_jhs_'<hr/>'
   echo ;}.}:a
  else.
   if. IFJHS do.
    jhtml_jhs_'<font color="blue">',(jhfroma_jhs_ ;a),'</font>'
   else.
    echo ;a
   end.
  end.
  sprunnerx__ d
  
 case. 2 do. NB. multiple NB. lines
  d=. 3}.each dltb each <;.2 d
  d=. ;(*./' '=;{.each d)}.each d
  if. IFJHS do. 
   jhtml_jhs_'<font color="green">',(jhfroma_jhs_ d),'</font>'
  else.
   echo d
  end. 
 end. 

SEMN=: 2+ndx
if. (SEMN<#SEMN)*.'NB.'-:3{.dlb d do. goto_top. end.
i.0 0
)

iscolon=: 3 : 0
t=. ;:y
if. (<'define')e.t do. 1 return. end.
i=. t i. <,':'
(,each':';'0')-:(i+0 1){t,'';''
)

isnb=: 3 : 0
t=. dltb y
('NB.'-:3{.t)*.~.'NB.spx'-:6{.t
)

get=: 3 : 0
d=. toJ fread y
d=. d,(LF~:{:d)#LF
<;._2 d
)

status=: 3 : 0
smoutput (":SEMN),' of ',(":#SEM),' in  ',SPXFILE
)

sptable_z_=: 3 : 0
t=. 9!:6''
9!:7[11$' '
d=. ":y
9!:7 t 
(-.*./"1 d="1 1[' '#~{:$d)#d
)

NB. line recall list of projects
splist=: 3 : 0
echo ;LF,~each dtb each(<'   edit_jhs_ SPFILE'),(<'   '),each(<'_jsp_'''''),~each 'p_'nl_jsp_ 3
)

3 : 0''
try.
if. _1=nc<'initialized' do. 
 1!:5 :: [ <jpath '~temp/sp'
 SPFILE_z_  =: cfile spspf
 SPFILES_z_ =: <;._2 cfile sprecentf
 SPXFILE_z_ =: ''
 SEMN       =: 0
end.
initialized=: 1
catch.
 smoutput'sp initialization failed'
end.
i.0 0
)

0 : 0
bind -s ^E "sp''\n"
bind -s ^R "spx''\n"
editrc fwrite '~home/.editrc'
)

NB.! run wiki page as spx style tutorial - proof of concept
NB. wikiinit_jsp_'CalorieCounting'

coclass'jsp'

require'pacman'

NB. wrap html text in jijx
htmla=: '<div  class="html" contenteditable="false" style="overflow-x: auto;white-space: normal;">'
htmlz=: '</div>'

prea=: '<div class="log">'
prez=: '</div>'

fn=: '~wiki.html'

getwiki=: 3 : 0
d=. fread 1{httpget_jpacman_ y

NB.! make https links work in new tab - should ensure href is in <a tag
d=. d rplc ' href="https:';' target="_blank"  href="https:'

NB. make wiki links work in new tab - not rigourous and could make false changes
d=. d rplc '<a href="/wiki/';'<a target="_blank" href="https:///code.jsoftware.com/wiki/'

NB. rid of cruft at start and end
d=. ('<p>'findfirst d)}.d              NB. discard stuff before first <p>
d=. (('</p>'findlast d)>.'</pre>'findlast d){.d  NB. discard stuff after last </p> or </pre?

NB. LFs to make it more readable
d=. d rplc '<div'  ; LF,'<div'
d=. d rplc '<p'    ; LF,'<p'
d=. d rplc '<code' ; LF,'<code'
d=. d rplc '<div'  ; LF,'<div'
d=. d rplc '<pre'  ; LF,'<pre'

d=. d rplc 'onaoclines';'   onaclines' NB. log line without 3 spaces

d fwrite 't.txt'

r=. ''

while. #d do.
 i=. 1 i.~ '<pre>' E. d
 if. i<#d do.
   NB. get html stuff upto pre
   r=. r,LF,htmla,LF,(i{.d),htmlz NB. html stuff
   d=. i}.d
   
   NB. process pre stuff
   i=. 1 i.~ '</pre>' E. d
   'pre without end'assert i<#d
   i=. 6+i
   pre=. i{.d
   b=. _6}.5}.pre
   z=. <;._2 b
   z=. ((<'   ')=3{.each z,each<'xxx')#z NB. assume non-empty line starting with 3 blanks is log input line
   z=. jhfroma_jhs_ 3}.each z
   z=. ;(<prea),each z,each <prez
   r=. r,LF,z
   d=. i}.d
 else.
  r=. r,LF,htmla,LF,d,htmlz
  d=. ''
 end.
end.
r fwrite fn
)

findfirst=: 4 : '(x E. y)i.1'
findlast=: 4 : '(#x)+(x E. y)i:1'

classhtml=: LF,'<div  class="html"'
classlog=:  LF,'<div class="log">'

wikiinit=: 3 : 0
'only CalorieCounting currently supported'assert y-:'CalorieCounting'
link=. 'https://code.jsoftware.com/wiki/ShareMyScreen/AdventOfCode/2022/01/CalorieCounting'
cb_jhs_=:cbfix_jhs_'1000\n2000\n3000\n\n4000\n\n5000\n6000\n\n7000\n8000\n9000\n\n10000'
clipboarddata__=: cb_jhs_  
  
ADVANCE_jijx_=: 'wiki'
echo'ctrl+. or menu > advances'
echo link
getwiki link
wikidata=: fread fn
i.0 0
)

wikistep=: 3 : 0
if. 0=#wikidata do. echo'end of wiki page' end.
if. classhtml-:(#classhtml){.wikidata do.
 i=. 1 i.~  classlog E. wikidata
 t=. i{.wikidata
 wikidata=: i}.wikidata
 jhtml_jhs_ t
else.
 i=. 1 i.~  classhtml E. wikidata
 t=. i{.wikidata
 t=. t rplc '<div class="log">';'';'</div>';LF
 t=. t rplc 'wd ''clippaste''';'clipboarddata'
 
 t=. afromh t
 
 wikidata=: i}.wikidata
 wikilines__=: t
 9!:27'0!:111 wikilines'
 9!:29[1
end.
)

NB. ascii from html - remove <tags> and &...;
afromh=: 3 : 0
t=. y
r=. ''
while. #t do.
 i=. t i. '<'
 r=. r,i{.t
 t=. i}.t
 i=. t i. '>'
 t=. }.i}.t
end.
r=. r rplc '&nbsp;';' ';'&lt;';'<';'&gt;';'>';'&amp;';'&';'&#160;';' ' NB. non-breaking space
)

NB. x is total ms for animation
animate=: 4 : 0
a=. >.x%#y NB. delay time
n=. ":100
p=. >:0 i.~y=' '
t=. 'setTimeout(newinput,X,"D");'rplc 'X';n;'D';p{.y
for_i. i.(#y)-p do.
 t=. t,'setTimeout(newinput,X,"D");'rplc 'X';n;'D';(p+i){.y
 n=. ":a+".n
end.
t=. t,'setTimeout(colorinput,',n,',"red");'
n=. ":500+a+".n
t=. t,'setTimeout(runinput,',n,',"',y,'");'
)
