require 'pacman'
coclass 'jpacman'

onStart=: pmview_open

Status=: cutopen 0 : 0
All
Upgrades
Not installed
Installed
)
create=: 3 : 0
log=: loggui
window=: 0
written=: matched=: 0
DATAMASK=: 0
DATAX=: _1
IFSECTION=: 0
ISGUI=: 1
LASTMSG=: ''
LOGTXT=: ''
MSGX=: 0
SECTION=: ,<'All'
SELNDX=: 0 0
if. -. pmview_init'' do. destroy'' end.
)
destroy=: 3 : 0
codestroy''
)
toDEL=: [: ; (DEL&, @ (,&DEL)) each
info=: 3 : 0
wd 'mb info "',SYSNAME,'" *',y
)
runpacman=: 3 : 0
if. (<'jpacman') e. {.2}.|:(<;._2);._2 wd'qpx' do. EMPTY return. end.
empty '' conew 'jpacman'
)
pm_apply_button=: 3 : 0
dat=. pmview_selected''
if. 0 = #dat do.
  info 'No packages selected for installation.' return.
end.
if. -. ONLINE do.
  if. -. getonline 'Install Packages';CHECKONLINE do. return. end.
end.
install dat
pmview_show''
)
pm_bclear_button=: 3 : 0
setshowall 0
pmview_show''
)
pm_bnotins_button=: 3 : 0
setshownew 1
pmview_show''
)
pm_bsection_button=: 3 : 0
IFSECTION=: '1'={.bsection
pmview_show''
)
pm_bselall_button=: 3 : 0
setshowall 1
pmview_show''
)
pm_bupdate_button=: 3 : 0
setshowups 1
pmview_show''
)
pm_close=: pm_exit_button=: 3 : 0
wd 'pclose'
if. 'Android'-:UNAME do.
  smact''
end.
destroy''
)
pm_remove_button=: 3 : 0
dat=. pmview_selected''
if. 0 = #dat do.
  info 'No packages selected for removal.' return.
end.
remove_console 1{"1 pmview_selected''
pmview_show''
)
pm_msg=: 3 : 0
MSGX=: y
pmview_showlog''
)
pm_pac_select=: 3 : 0
DATAX=: {. 0 ". pac_click
)
pm_prebuild_button=: 3 : 0
if. -. ONLINE do.
  getonline 'Read Catalog from Server';CHECKASK
end.
if. ONLINE do.
  log 'Updating server catalog...'
  refreshjal''
end.
log 'Rebuilding local file list...'
refreshaddins''
readlocal''
log 'Done.'
pacman_init''
pmview_show''
)
pm_pupcat_button=: 3 : 0
if. -. ONLINE do.
  if. 0 = getonline 'Read Catalog from Server';CHECKASK do. return. end.
end.
log 'Updating server catalog...'
if. refreshweb'' do.
  log 'Done.'
end.
pacman_init''
pmview_show''
)
pm_sel_select=: 3 : 0
SELNDX=: (0 ". sel_select) IFSECTION } SELNDX
pmview_sel''
)
pm_bsummary_button=: pm_msg bind 0
pm_bhistory_button=: pm_msg bind 1
pm_bmanifest_button=: pm_msg bind 2
pm_blog_button=: pm_msg bind 3

pm_binfo_button=: addon_info
pm_bstatus_button=: pm_bsection_button
pmview_applycounts=: 3 : 0
dat=. y
if. 0=#dat do. 0 0 return. end.
'lib dat'=. splitlib dat
cnt=. 0 < #lib
siz=. cnt * 2 pick LIB
ind=. ({."1 ZIPS) i. 1 {"1 dat
(cnt + #ind),siz + +/>(<ind;4){ZIPS
)
pmview_getmask=: 3 : 0
ndx=. IFSECTION { SELNDX
if. IFSECTION do.
  sel=. ndx pick SECTION
  select. sel
  case. 'All' do.
    DATAMASK=: (#PKGDATA) $ 1
  case. BASELIB do.
    DATAMASK=: (1 {"1 PKGDATA) = <BASELIB
  case. do.
    DATAMASK=: (<sel,'/') = (1+#sel) {.each 1 {"1 PKGDATA
  end.
else.
  select. ndx pick Status
  case. 'All' do.
    DATAMASK=: (#PKGDATA) $ 1
  case. 'Not installed' do.
    DATAMASK=: pkgnew''
  case. 'Upgrades' do.
    DATAMASK=: pkgups''
  case. 'Installed' do.
    DATAMASK=: -. pkgnew''
  end.
end.
DATAX=: DATAX <. <: +/DATAMASK
)
pmview_init=: 3 : 0
if. -. checkaccess'' do. 0 return. end.
if. -. checkaddonsdir'' do. 0 return. end.
setfiles ''
readlocal''
if. -. checkonline'' do. 0 return. end.
pacman_init 1
wd 'activity ', >coname''
1
)
pmview_selected=: 3 : 0
if. _1= {. selection=. 0 ". wd 'get pac select' do.
  ''
else.
  PKGDATA {~ selection{(I.DATAMASK)
end.
)
pmview_show=: 3 : 0
wd 'psel pm'
pmview_getmask ''
sel=. IFSECTION pick Status;<SECTION
wd 'set sel items ',toDEL sel
wd 'set sel select ',":IFSECTION { SELNDX
wd 'set apply enable ',":ONLINE
pmview_showdata DATAMASK # PKGDATA
)
pmview_sel=: 3 : 0
wd 'psel pm'
pmview_getmask ''
wd 'set apply enable ',":ONLINE
pmview_showdata DATAMASK # PKGDATA
)
pmview_showdata=: 3 : 0
VIEWDATA=: y
n=. 1{"1 VIEWDATA
v1=. 2{"1 VIEWDATA
v2=. 3{"1 VIEWDATA
wd 'set pac items *',toDEL n ,&.> (<LF) ,&.> v1 ,&.> (<' / ') ,&.> v2
if. #selection=. I. ; {."1 VIEWDATA  do.
  wd 'set pac select ', ":selection
end.
)
pmview_showlog=: 3 : 0
if. window=0 do. return. end.
if. MSGX=3 do.
  t=. ; LOGTXT
else.
  ndx=. DATAX{(I.DATAMASK),_1
  if. ndx<0 do.
    t=. _1
  else.
    p=. '~addons/',(<ndx;1){:: PKGDATA
    select. MSGX
    case. 0 do.
      t=. ndx pick 5{"1 PKGDATA
    case. 1 do.
      t=. fread jpath p,'/history.txt'
    case. 2 do.
      t=. fread jpath p,'/manifest.ijs'
    end.
  end.
end.
if. 2 = 3!:0 t do.
  textview (MSGX{::'Summary';'History';'Manifest';'Log');'';t
end.
)
addon_info=: 3 : 0
if. 0=#PKGDATA do. return.end.
row=. DATAX{(I.DATAMASK),_1
if. row<0 do. return. end.
a=. (<row;1){:: PKGDATA
if. 'base library'-:a do. a=. 'JAL' else. a=. 'Addons/',a end.
browse_j_ 'http://code.jsoftware.com/wiki/',a
)
loggui=: 3 : 0
LOGTXT=: LOGTXT, t=. <;.2 y,LF -. {: y
if. window=0 do. return. end.
wd 'mb toast *', ;t
)
pkglater=: 3 : 0
if. 0=#PKGDATA do. $0 return. end.
loc=. fixvers > 2 {"1 PKGDATA
srv=. fixvers > 3 {"1 PKGDATA
{."1 /:"2 srv ,:"1 loc
)
pkgnew=: 3 : 0
0 = # &> 2 {"1 PKGDATA
)
PMVIEW=: 0 : 0
pc pm;
menupop "&File";
menu exit "&Quit" "Ctrl+Q";
menupopz;
menupop "&Tools";
menu pupcat "&Update Catalog from Server";
menusep;
menu prebuild "&Rebuild all Repository Catalogs";
menusep;
menu remove "Re&move Selected Packages";
menupopz;
bin v;
wh _1 _2;cc sel combolist;
bin h;
cc bstatus radiobutton horizontal;cn "Status";
cc bsection radiobutton group;cn "Category";
bin s;
cc binfo button;cn "Wiki";
bin z;
bin vh;
cc apply button;cn "Install";
bin s;
cc selection static;cn "Selections";
cc bselall button;cn "Select All";
bin zh;
cc bclear button flush;cn "Clear All";
cc bupdate button;cn "Updates";
cc bnotins button;cn "Not Installed";
bin szz;
bin v;
wwh 1 _1 _2;cc pac listbox multiple;
bin h;
cc bsummary button;cn "Summary";
cc bhistory button;cn "History";
cc bmanifest button;cn "Manifest";
cc blog button;cn "Log";
bin z;
bin z;
bin z;
)
pmview_postinit=: 3 : 0
pmview_setmenu''
pmview_show''
logstatus''
)
pmview_read=: 3 : 0
IFSECTION=: 0 ". bsection
SELNDX=: (0 ". sel_select) IFSECTION } SELNDX
)
pmview_open=: 3 : 0
wd PMVIEW
wd 'set bstatus value 1'
wd 'pn *',SYSNAME
wd 'pshow'
window=: 1
pmview_show''
)
