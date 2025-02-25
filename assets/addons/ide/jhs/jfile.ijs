NB. J HTTP Server - jfile app
coclass'jfile'
coinsert'jhs'
require'~addons/convert/misc/base64.ijs'

HBS=: 0 : 0

jhmenu''
'menu0'  jhmenugroup ''
         jhmpage''
'close'  jhmenuitem 'close';'q'
         jhmenugroupz''
jhmpagez''

'edit'     jhb 'edit'
'load'     jhb 'load'
'rename'   jhb 'rename'
'new'      jhb 'new'
'del'      jhb 'del'
'copy'     jhb 'copy'
'cut'      jhb 'cut'
'paste'    jhb 'paste'
'download' jhb 'copy to downloads'

'renamedlg'  jhdiva''
 jhbr
 'renamedo'  jhb'rename as'
 'renpx'    jhtext'';20
 'rennx'    jhtext'';10
 'rensx'    jhtext'';4
 'renameclose'jhb'X'
'<hr></div>'

'deletedlg'    jhdiva''
 jhbr
 'deletedo'    jhb'delete'
 'deletename'  jhspan''
 'deleteclose' jhb'X'
'<hr></div>'

'newdlg' jhdiva''
 jhbr
 'newfile'   jhb'newfile'
 'newname'   jhtext'';20
 'newfolder' jhb'newfolder'
 'newclose'  jhb'X'
'<hr></div>'

'report'    jhdiv'<R>'

jhhr
'recent'   jhrad 'Recent';0;'paths'
shorts''
'path'      jhhidden'<F>'
'pathd'    jhdiv'<F>'
jhresize''

'sel'       jhdiv'<FILES>'
)

LASTPATH=: jpath'~temp/'

NB. get valid path (based on folderinfo) ; folderinfo
getfandp=: 3 : 0
d=. folderinfo remlev y
if. '/'={:y do. y;<d return. end.
n=. <jgetfile y
i=. d i. n
if. i<#d do. y;<d return. end.
b=. ((;{:each d)i:'/')}.d 
i=. (<:#b)<.>:(nsort b,n)i.n 
if. '/'={:;i{b do. ('/',~remlev y);<d return. end.
((remlev y),'/',;i{b);<d 
)

NB. y - error;file
create=: 3 : 0
'r f'=. y
if. r-:'' do. r=. '&nbsp;' end. NB. so div does not collapse
'f d'=. getfandp f
d=. (<d),<(('/'=;{:each d){'';'▶'),each d
'jfile' jhr 'R F FILES';r;(jshortname f);(buttons 'files';d,<'<br>')
)

NB. get file with mid/path opens the file
jev_get=: 3 : 0
create '&nbsp;';LASTPATH
)

buttons=: 3 : 0
'mid sids values sep'=. y
p=. ''
for_i. i.#sids do.
 id=. mid,'*',>i{sids
 p=. p,sep,~id jhab i{values
end.
)

radios=: 3 : 0
'mid sids values sep'=. y
p=. ''
for_i. i.#sids do.
 t=. ,'/'
 id=. mid,'*',>i{sids
 b=. LASTPATH-:t,~jpath'~',;i{values
 b=. b+.(LASTPATH-:t)*.t-:;i{values
 p=. p,sep,~id jhrad mid;(;i{values);b
end.
)

shorts=: 3 : 0
radios 'paths';(2#<((roots''),~{."1 UserFolders_j_,SystemFolders_j_)-.;:'Demos User bin config system break snap tools'),<''
)

ev_paths_click=: 3 : 0
f=. getv'jsid'
if. -.f-:,'/' do.
 LASTPATH=: jpath '~',f,'/'
else.
 LASTPATH=: ,'/'
end.
create '';LASTPATH
)

NB. folder clicked (file click handled in js)
ev_files_click=: 3 : 0
sid=. getv'jsid'
path=. jpath getv'path'
sid=. sid-.PS
if. sid-:'..' do.
 f=. PS,~remlev remlev path 
else.
 f=. (remlev path),PS,sid,PS
end.
LASTPATH=: f
create '';f
)

nsort=: 3 : '/:~y'

NB. y path - result is folders,files
folderinfo=: 3 : 0
a=. 1!:0 <jpath y,'/*'
n=. {."1 a
d=. 'd'=;4{each 4{"1 a
('/',~each(<'..'),nsort d#n),nsort (-.d)#n
)

NB. y '' for file and '/' for folder
new=: 3 : 0
F=. jpath getv'path'
n=. getv'newname'
f=. (remlev F),PS,n
if. +./ '/\' e. n do.
 r=. n,' must not contain / or \'
elseif. fexist f do.
 r=. n,' already exists'
else.
 try. 
  if. ''-:y do. ''1!:2<f else. 1!:5<f end.
  r=. n,' created'
 catch.
  r=. n,' create failed'
 end.
end.
create r;f
)

ev_newfile_click=: 3 : 'new'''''

ev_newfolder_click=: 3 : 'new''/'''

ev_renamedo_click=: 3 : 0
F=. jpath getv'path'
s=. getv'rensx'
f=. jpath (getv'renpx'),(getv'rennx'),s
if. '/'={:F do. f=. f,('/'~:{:f)#'/' end.
if. F-:f do. 
 r=. 'to same name'
elseif. fexist f do.
 r=. 'failed - already exists'
else. 
 if. f frename F do.
  r=. 'ok'
  F=. f
  else.
  r=. 'failed'
 end.
end.
create F;~'Rename: ',r 
)

ev_deletedo_click=: 3 : 0
create delete getv'path'
)

delete=: 3 : 0
newf=. p=. y
F=. jpath p
try.
 t=. jpath'~temp/deleted/'
 if. PS={:F do. NB. delete folder 
  srcfolder=. F
  snkfolder=. t,p
  if. -.t-:(#t){.F do. NB. backup folder
   deletefolder snkfolder
   copyfiles (srcfolder,'*');snkfolder
   deletefolder srcfolder
   r=. 'deleted ',p,' (backup at ~temp/deleted/...)'
  else. 
   deletefolder srcfolder
   r=. 'deleted ',p
  end. 
 else. NB. delete file
  if. -.t-:(#t){.F do. NB. backup file
   mkdir_j_ t,remlev p
   (1!:1 <F) 1!:2 <jpath t,p
   1!:55 <F
   r=. 'deleted ',p,' (backup at ~temp/deleted/...)'
  else.
   1!:55 <F
   r=. 'deleted ',p
  end. 
 end.
catch.
  echo 13!:12''
  r=. 'delete ',p,' failed'
end.
r;newf
)

ev_load_click=: 3 : 0
f=. jgetfile F=. jpath getv'path'
if. f-:'' do.
 create'No file selected to load';F
else.
 try.
  load__ F
  create'file loaded';F
 catch.
  smoutput 13!:12''
  create ('load error: ',13!:12'');F
 end. 
end.
)

copy=: _1 NB. _1 not ready, 0 copy, 1 cut 

copycut=: 3 : 0
copy=: y
srcfile=: jpath getv'path'
create 'Ready for paste';srcfile
)

ev_copy_click=: 3 : 'copycut 0'
ev_cut_click=:  3 : 'copycut 1'

ev_paste_click=: 3 : 0
F=. jpath getv'path'
f=. jgetfile srcfile
if. ''-:f do.
 folder=. jgetfile remlev srcfile
 srcfolder=. srcfile
 snkfolder=. F,jgetfile remlev srcfile
 if. srcfile-:(#srcfile){.snkfolder do.
  create 'Paste: destination can not be in source';F
  return.
 end.
 try.
  1!:5<snkfolder
 catch.
  create 'Paste: folder already exists';F
  return.
 end.
 copyfiles (srcfile,'*');snkfolder
 if. copy=1 do. deletefolder }:srcfile end.
 create ('Paste: created folder ',jshortname snkfolder);F  
 return.
end.
d=. fread srcfile
i=. f i: '.'
a=. i{.f
z=. i}.f
c=. 0
while. fexist snkfile=. (remlev F),PS,f do.
 c=. >:c
 f=. a,'-Copy(',(":c),')',z
end.
if. _1=copy              do. create 'Paste: no copy or cut';F          return. end.
if. _1-:d                do. create 'Paste: read source file failed';F return. end.
if. _1-:d fwrite snkfile do. create 'Paste: write newfile failed';F    return. end.
if. copy=1 do. try. 1!:55 <srcfile catch. end. end.
create ('Paste: created file ',f);F
)

ev_download_click=: 3 : 0
f=. getv'path'
if. '/'={:f do.
 NB. tar it
 f=. }:jpath f
 n=. f}.~>:f i:'/'
 f=. (-#n)}.f
 shell q__=: 'tar -C "',f,'" -czf  ',n,'.tgz ',n
 n=. n,'.tgz'
 d=. fread n
else.
 d=. fread f
 n=. f}.~>:f i:'/'
end.
jhrjson qq__=: 'report';(jhfroma n,' copied to server Downloads');'data';(tobase64 d);'name';n
)

NB. copyfiles src;snk
NB. src ends with \fspec which can have wildcards
copyfiles=: 3 : 0
'src snk'=. y
src=. jpath src
snk=. jpath snk
i=. src i:PS
fspec=. (>:i)}.src
src=. i{.src
mkdir_j_ snk
ns=. 1!:0 <jpath src,'\',fspec
for_n. ns do.
 pn=. jpath'\',>{.n
 srcpn=.src,pn
 snkpn=.snk,pn
 if. 'd'~:4{>4{n do.
  t=. 1!:1<srcpn
  p=. 1!:7<srcpn
  t 1!:2<snkpn
  p 1!:7<snkpn NB. permissions
 else.
  if. -.srcpn-:snk do.
   1!:5 :: [ <snkpn
   copyfiles (srcpn,'\*');snkpn
  end.
 end.
end.
i.0 0
)

NB. deletefolder y
deletefolder=: 3 : 0
y=. (-PS={:y)}.y NB. remove trailing /
p=. <jpath y
if. 1=#1!:0 p do.
 if. 'd'=4{,>4{"1 (1!:0) p do.
  deleterecursive y
  1!:55 p
 end.
end.
i.0 0
)

NB. deletesub y
deleterecursive=: 3 : 0
assert. 3<+/PS=jpath y
ns=. 1!:0 <jpath y,'\*'
for_n. ns do.
 s=. jpath y,'\',>{.n
 if. 'd'=4{>4{n do.
  deleterecursive s
 end.
 1!:55<s
end.
)

remlev=: 3 : '(y i: PS){.y'    NB. remove level from path

roots=: 3 : 0
if. IFWIN do.
 a=. 26}.Alpha_j_
 b=. (<"0 a),each <':/'
 d=. a#~0~:;#each dir each b
 (<"0 d),each <':'
else.
 <'/'
end.
)

fx=: 3 : 0
s=. ;shorts_jsp_ y
t=. jshortname y
NB.!f=. ;(1>.10-#s)#<'&nbsp;'
f=. ;(1>.10-#s)#<' '
(('file*',jurlencode t)jhab s,f,t),jhbr
)

ev_recent_click=: 3 : 0
if. 0=#SPFILES_jsp_ do.
 t=. '</div>',~'<div>The recent files list is empty.'
else.
 t=. '</div>',~'<div>',;fx each SPFILES_jsp_
end.
jhrajax t 
)

CSS=: 0 : 0
*{font-family:<PC_FONTFIXED>;}
#report{color:red;}
#pathd{color:blue;}
#renamedlg{display:none;}
#deletedlg{display:none;}
#newdlg{display:none;}
)

JS=: 0 : 0
var anchor=null;

function ev_body_load(){jresize();setanchor(true);}

function setpath(t){jform.path.value= t;jbyid("pathd").innerHTML= t;}
function ev_paths_click(){jsubmit();}
function ev_paths_dblclick(){;}

//function ev_recall_click(){jsubmit();}
//function document_recall(v){jform.path.value= v;jbyid("pathd").innerHTML= v;jscdo("recall",v);}

function ev_x_shortcut(){jscdo("cut");}
function ev_c_shortcut(){jscdo("copy");}
function ev_v_shortcut(){jscdo("paste");}
//function ev_2_shortcut(){jbyid("sel").childNodes[0].focus();}

function setanchor(scroll){
 t= path.value;
 i= t.lastIndexOf('/');
 if(i==t.length-1) return;
 t= t.substring(i+1);
 if(anchor!=null) anchor.style.color=  jbyid('sel').firstElementChild.style.color;
 anchor= jbyid('files*'+t);  
 anchor.style.color='red';
 if(scroll) anchor.scrollIntoView({behavior: 'auto', block: 'nearest'});
}

//! kill off files/fif/copy
function ev_files_click() // file select
{
 clr();
 if('/'!=jform.jsid.value.charAt(jform.jsid.value.length-1))
 {
  var t= jform.path.value;
  var i= t.lastIndexOf('/');
  setpath(t.substring(0,++i)+jform.jsid.value);
  setanchor(false);
 }
 else
  jsubmit();
}

function ev_files_dblclick()
{
 if('/'!=jform.path.value[jform.path.value.length-1])
  ev_file_dblclick();
}

function clr(){
 jbyid('report').innerHTML = "&nbsp;";
 jhide("renamedlg");jhide("deletedlg");jhide('newdlg');
 }

function ev_rename_click()
{
 clr();
 t= jbyid("path").value;
 i= t.lastIndexOf('/');
 if(i==t.length-1)
 {
  t= t.substring(0,i); // remove trailing /
  i= t.lastIndexOf('/');
 }
  jbyid("renpx").value= t.substring(0,i+1);
  t= t.substring(i+1);
  i= t.lastIndexOf('.');
  if(i==-1) i= t.length;
  jbyid("rennx").value= t.substring(0,i);
  jbyid("rensx").value= t.substring(i);
  jdlgshow("renamedlg","rennx");
}
 
function ev_renameclose_click(){clr();}

function ev_new_click(){
 clr();
 jdlgshow('newdlg','newname');
}

function ev_newclose_click(){clr();}

function ev_del_click()
{
 clr();
 jbyid("deletename").innerHTML=jform.path.value;
 jdlgshow("deletedlg","deleteclose");
}

function ev_deleteclose_click(){clr();}

function ev_recent_click(){jdoajax();}

function ev_recent_click_ajax(t){jbyid('sel').innerHTML= t;}

// handler must be defined - no longer defaults to jsubmit if not defined
function ev_edit_click(){ev_files_dblclick();}
function ev_load_click(){jsubmit();}
function ev_copy_click(){jsubmit();}
function ev_cut_click(){jsubmit();}
function ev_paste_click(){jsubmit();}
function ev_newfile_click(){jsubmit();}
function ev_newfolder_click(){jsubmit();}
function ev_renamedo_click(){jsubmit();}
function ev_renpx_enter(){jscdo('renamedo');}
function ev_rennx_enter(){jscdo('renamedo');}
function ev_rensx_enter(){jscdo('renamedo');}
function ev_newname_enter(){jscdo('newfile');}
function ev_deletedo_click(){jsubmit();}
//function ev_adrecall_click(){adrecall("document",jbyid('path').value,"0");}

function ev_file_click(){
 setpath(decodeURIComponent(jsid.value));
 //! setanchor(false);
}

function ev_file_dblclick(){
  if(null!=window.frameElement){
    var a= 'jifr-jijs?jwid='+jform.path.value;
    var b= 'jijs?jwid='+encodeURIComponent(jform.path.value);
    jijxwindow.newpage(a,'jifr',b);
  }else{
    var t= 'jijs?jwid='+jform.path.value;
    jijxwindow.pageopen(t,t);
  }  
}

function ev_close_click(){winclose();}

function ev_download_click(){clr();jdoajax(['path']);}
function ev_download_click_ajax_json(t){
 if(t.data!=undefined){saveAs(base64ToArrayBuffer(t.data),t.name);}
 jbyid('report').innerHTML= t.report;
}

)