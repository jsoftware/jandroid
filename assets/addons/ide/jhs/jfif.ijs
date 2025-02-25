coclass'jfif'
coinsert'jhs'

MAXFILES=: 100000 NB. fails if too many files in search path

HBS=: 0 : 0
jhmenu''
'menu0'  jhmenugroup ''
         jhmpage''
'close'  jhmenuitem 'close';'q'
         jhmenugroupz''
jhmpagez''

'find'     jhb'Find'
'context'  jhselect(<;._2 FIFCONTEXT);1;0
'mcase'    jhb 'Aa'
'mfolders' jhb '/...'
'nameonly' jhb 'lines'
jhbr
'what'     jhtext '<what>';50
jhbr
'where'    jhtext '<where>';50
jhbr
jhresize''
'area'     jhdiv''
)

CSS=: 0 : 0
*{font-family:PC_FONTFIXED;}
)

jev_get=: 3 : 0
'jfif'jhr''
)

ev_find_click=: 3 : 0
t=. <;._2 getv'jdata'
'FIFWHAT FIFCONTEXTNDX FIFTYPE FIFDIR FIFCASE FIFSUBDIR FIFNAMEONLY'=: t

FIFTYPE=: '*.ijs'
if. 0~:#FIFDIR do.
 if. '/'~:{:FIFDIR do.
  i=. FIFDIR i:'/'
  t=. }.i}.FIFDIR
  if. '*'={.t do.
   FIFTYPE=: t
   FIFDIR=: i{.FIFDIR
  end.
  FIFDIR=: FIFDIR,'/'
 end.
end. 

FIFCONTEXTNDX=: ".FIFCONTEXTNDX
FIFCASE=: ".FIFCASE
FIFSUBDIR=: ".FIFSUBDIR
FIFNAMEONLY=: ".FIFNAMEONLY
FIFFOUND=: ''
FIFINFO=: ''
JHSFOUNDFILES=: ''
fiff_find_button''

t=. FIFINFO
if. 0~:#t do. t=. t,'<br><br>find WHAT WHERE<br>find abcd ~addons<br>find abcd ~addons/*.txt' end. 
 
jhrajax t,>FIFNAMEONLY{JHSFOUNDFILES;FIFFOUND
)

fiff_find_button=: 3 : 0
if. 0=#FIFWHAT    do. finfo 'WHAT is empty - nothing to find' return. end.
if. 0=#FIFDIR     do. finfo 'WHERE is empty' return. end.
if. 0=#dir FIFDIR do. finfo 'WHERE is not a folder' return. end.
dat=. ffss''
if. ''-:dat  do. return. end. NB. finfo called
if. 0=# 0 pick dat do. finfo'no match' return. end.
'FIFFOUND FIFLINES FIFHITS FIFMSK FIFFILES'=: dat
)

termLF=: , (0: < #) # LF"_ -. _1&{.
toLF=: (10{a.)&(I. @(e.&(13{a.))@]})

fifplain=: ;@(,~&.> e.&'[](){}$^.*+?|\' #&.> (<'\')"_) NB. escape NOT /

3 : 0''
if. IFUNIX do. filecase=: [ else. filecase=: tolower end.
0
)

ffssinit=: 3 : 0
p=. y
nna=. '(^|[^[:alnum:]_])'
nnz=. '($|[^[:alnum:]_.:])'
ass=. '[[:space:]]*='
select. FIFCONTEXTNDX
case. 0 do. p=. fifplain y
case. 1 do. p=. nna,p,nnz              
case. 2 do. p=. nna,p,ass,':'    
case. 3 do. p=. nna,p,ass,'\.'        
case. 4 do. p=. nna,p,ass,'[:.]'        
case. 5 do. p=. y NB. regex          
end.
FIFCOMP=: rxcomp_jregex_ :: _1: p
)

NB. return '' if finfo called
ffss=: 3 : 0
JHSFOUNDFILES=: ''

if. FIFCASE=0 do.
  what=. tolower FIFWHAT
else.
  what=. FIFWHAT
end.
fls=. ffgetfiles''
if.  ''-:fls         do. '' return. end.
if. _1=ffssinit what do. finfo'regex failed'       return. end.
fnd=. ''
lns=. ''
msk=. ''
hit=. ''
dr=. fls
read=. (1!:1 :: _1:) @ <
read=. toLF @ (read f.)
while. #dr do.
  dat=. read fl=. >{.dr
  dr=. }.dr
  if. dat -: _1 do. msk=. msk,0 continue. end.
  dat=. termLF dat
  if. FIFCASE do. txt=. dat else. txt=. tolower dat end.
  ndx=. FIFCOMP rxmatches_jregex_ txt
  ndx=. +/"1 {."2 ndx
  if. rws=. #ndx do.
    msk=. msk,1
    t=. jhsfixfl fl
    fnd=. fnd,t,' (',(":#ndx),')'
    JHSFOUNDFILES=: JHSFOUNDFILES,t
    txt=. dat
    ind=. +/ ndx >"1 0 I.txt=LF NB. display found line just from LF
    b=. ~: ind
    ind=. b#ind
    hit=. hit, <b#ndx
    lns=. lns, <ind
    txt=. ; ind { <;.2 txt
    fnd=. fnd,jhsfixtxt txt
  else.
    msk=. msk,0
  end.
end.

rxfree_jregex_ FIFCOMP
fnd;lns;hit;msk;<fls
)

NB. return list of files or finfo
ffgetfiles=: 3 : 0
dirs=. <FIFDIR
r=. ''
dirs=. fullname_j_ each dirs

while. #dirs do.
  if. MAXFILES<#r do. finfo 'too many files to search' return. end.
  fpath=. (>{.dirs) &,
  dirs=. }.dirs
  dat=. a: -.~ 1!:0 each fpath each <FIFTYPE

  if. #dat do.
    dat=.('d'~:;4{each 4{"1 ;dat)#{."1 ;dat
    r=. r, fpath each /:~ dat
  end.

  if. FIFSUBDIR do.
    if. #j=. 1!:0 fpath '*' do.
      if. #j=. ({."1 ffgetsubdir j) do.
        dirs=. ((fpath @ (,&'/')) each j),dirs
      end.
    end.
  end.
end.
r=. ~.jpath each ( ;@:((# {. 1:)&.>) <;.1 filecase@;) r
if. 0=#r do. finfo'no files to search' return. end.
r
)

ffgetsubdir=: #~ '-d'&-:"1 @ (1 4&{"1) @ > @ (4&{"1)

finfo=: 3 : 0
FIFINFO=: y
''
)

FIFCONTEXT=: 0 : 0
any
name
=:
=.
=: =.
regex
)

jhsfixfl=: 3 : 0
t=. jshortname y
'<br>',('file*',jurlencode t)jhab t
)

jhsfixtxt=: 3 : 0
'<br>',jhfroma y
)

JS=: 0 : 0
function ev_body_load()
{
 setlast("what");
 setlast("where");
 jform.context.selectedIndex= getls("context");
 setchkstate('mcase',   getls('mcase'));
 setchkstate('mfolders',getls('mfolders'));
 setchkstate('nameonly',getls('nameonly'));
 jbyid("what").focus();
 jresize();
}

function ev_what_enter(){jscdo("find");}
function ev_where_enter(){jscdo("find");}
function ev_find_click_ajax(ts){jbyid("area").innerHTML=ts[0];}

function ev_find_click()
{
 jbyid('area').innerHTML= 'searching...';
 adrecall("what",jform.what.value,"0");
 adrecall("where",jform.where.value,"0");
 setls("context",   jform.context.selectedIndex);
 setls('mcase', getchkstate('mcase'));
 setls('mfolders', getchkstate('mfolders'));
 setls('nameonly', getchkstate('nameonly'));
 var t=jform.what.value+JASEP;
 t+=jform.context.selectedIndex+JASEP;
 t+=0+JASEP; // type is part of where - jform.type.value+JASEP;
 t+=jform.where.value+JASEP;
 t+=(getchkstate('mcase')?1:0)+JASEP;
 t+=(getchkstate('mfolders')?1:0)+JASEP;
 t+=(getchkstate('nameonly')?1:0)+JASEP;
 jdoajax([],t);
}

function ev_file_click(){
 t= 'jijs?jwid='+jsid.value;
 jijxwindow.pageopen(t,t);
}

function ev_file_click(){
  if(null!=window.frameElement){
    var a= 'jifr-jijs?jwid='+decodeURIComponent(jsid.value);
    var b= 'jijs?jwid='+(jsid.value);
    jijxwindow.newpage(a,'jifr',b);
  }else{
    var t= 'jijs?jwid='+jsid.value;
    jijxwindow.pageopen(t,t);
  } 
}


function ev_mcase_click(){flipchkstate('mcase');jscdo('find');}
function ev_mfolders_click(){flipchkstate('mfolders');jscdo('find');}
function ev_nameonly_click(){flipchkstate('nameonly');jscdo('find');}

function ev_context_change()
{
 jscdo('find');
 //jbyid("which").innerHTML= JEV+" : "+jbyid("sel").selectedIndex;
}

// early support for chkbutton - normal button with 2 color backgroundcolors
// move eventually to utiljs
var chk1='lightgrey';chk0= 'white';

function getchkstate(id){
 return (jbyid(id).style.backgroundColor==chk1)?1:0;
}

function setchkstate(id,v){jbyid(id).style.backgroundColor= (v==0)?chk0:chk1;}

function flipchkstate(id){setchkstate(id,getchkstate(id)!=1)};

function ev_close_click(){winclose();}
)
