NB. J HTTP Server - jpacman
coclass'jpacman'
coinsert'jhs'
require'pacman'

HBS=: 0 : 0

jhmenu''
'menu0'  jhmenugroup ''
         jhmpage''
'close'  jhmenuitem 'close';'q'
         jhmenugroupz''

jhmpagez''

'J Package Manager <a href="http://code.jsoftware.com/wiki/addons">code.jsoftware.com/wiki/addons</a>'
'<hr>'
'upable' jhb'Upgradeable'
'remable'jhb'Removeable'
'inst'   jhb'Installed'
'notin'  jhb'Not Installed'
'desc'   jhb'Descriptions'
'all'    jhb'Upgrade/Install All'
'buttons'jhdiv'<BUTTONS>'
jhresize''
'result' jhdiv'<RESULT>'
)

checkers=: ('check'jhb'Check all'),'uncheck'jhb'Uncheck all'

maketable=: 3 : 0
b=. ((<'c_'),each{."1 y)jhchk each <'';0;'';'';'~'
NB. b=. ,.(('c'),each ":each <"0 i.#y)jhchk each <'';0
t=. jhtablea
t=. t,,jhtr"1 b,.y
t,jhtablez
)

create=: 3 : 0 NB. create - y replaces <RESULT> in body
'jpacman'jhr'BUTTONS RESULT';y
)

jev_get=: 3 : 0
create '';('update'jpkg'')rplc LF;'<br>'
)

ev_all_click=: 3 : 0
'update'jpkg'' NB. update to make current
'upgrade'jpkg''
'install'jpkg,<'all'
jev_get''
)

ev_upable_click=: 3 : 0
'update'jpkg'' NB. update to make current
d=. 'showupgrade'jpkg''
 b=. checkers,~'upgrade'jhb'Upgrade Selected'
 t=. maketable d
 create b;t
)

ev_remable_click=: 3 : 0
'update'jpkg'' NB. update to make current
d=. 'showinstalled'jpkg''
d=. d#~-.({."1 d) e. 'base library';'ide/jhs'
b=. checkers,~'remove'jhb'Remove Selected'
t=. maketable d
create b;t
)

ev_notin_click=: 3 : 0
'update'jpkg'' NB. update to make current
b=. checkers,~'install'jhb'Install Selected'
t=. maketable'shownotinstalled'jpkg''
create b;t
)

ev_inst_click=: 3 : 0
'update'jpkg'' NB. update to make current
b=. checkers,~'upgrade'jhb'Upgrade Selected'
t=. maketable'showinstalled'jpkg''
create b;t
)

doselect=: 3 : 0
d=. {."1 NV
b=. (<'c_')=2{.each d
d=. b#{."1 NV
n=. ;0".each b#{:"1 NV
d=. 2}.each n#d
r=. y jpkg d
jhrcmds'set result *',r
)

ev_install_click=: 3 : 0
doselect'install'
)

ev_upgrade_click=: 3 : 0
doselect'upgrade'
)

ev_remove_click=: 3 : 0
doselect'remove'
)

descfix=: 3 : 0
i=. y i.LF
'<span style="color:red">',(i{.y),'</span>',}.i}.y
)

ev_desc_click=: 3 : 0
d=. ('showinstalled'jpkg''),'shownotinstalled'jpkg''
d=. /:~{."1 d
d=. 'show'jpkg d
d=. d rplc '== ';0{a.
d=. <;._1 d
d=. descfix each d
r=. ;d
create '';r rplc LF;'<br>'
)

JS=: 0 : 0
function ev_body_load(){jresize();}

function ev_upable_click(){jsubmit();}
function ev_remable_click(){jsubmit();}
function ev_inst_click(){jsubmit();}
function ev_notin_click(){jsubmit();}
function ev_desc_click(){jsubmit();}
function ev_all_click()
{
 jbyid("result").innerHTML= 'this may take a few minutes ...';
 jsubmit();
}

//function ev_remove_click(){jdojax();}
//function ev_upgrade_click(){jsubmit();}
//function ev_install_click(){jsubmit();}


function check(v)
{
 var marks= "□▣";
 var n=document.getElementsByClassName("jhchk");
 for(var i=0;i<n.length;++i)
 {
  n[i].innerHTML= marks[v]+'&nbsp;';
  n[i].setAttribute("data-jhscheck",v);
  //("checkbox"==n[i].getAttribute("type")){n[i].checked=v;}
 }
}

function ev_check_click(){check("1");}
function ev_uncheck_click(){check("0");}

function ev_close_click(){winclose();}

)
