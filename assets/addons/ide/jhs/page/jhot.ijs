NB. handsontable - minimal
NB. 'jhot'cojhs'n__'[n__=: i.3 9

coclass'jhot'
coinsert'jhs'

NB. cojhs boilerplate from util.ijs

NB. create=: 3 : 'setdata y~'
create=: 3 : 0
setdata y~
)

jev_get=: 3 : 0
js=. getjs'CUSTOM';CUSTOM hrplc 'OPTIONS DATA';options;jsfromtable data
'jhot' jhrx (getcss''),js,gethbs''
)

options=: 0 : 0
data: data,
minSpareRows: 1,
minSpareCols: 1,
contextMenu: true,
undo: true,
)  

setdata=: 3 : 0
'bad data'assert (2=$$y)*.2>L.y
option'type: ',;((0~:L.y)+.2=3!:0 y){'"numeric"';'"text"'
data=: y
)

NB. option'type:'          - remove option
NB. option'type:"numeric"' - new option
option=: 3 : 0
d=. <;.2 options
a=. (>:;d i.each':'){.each d
i=. >:y i.':'
t=. dltb i{.y
p=. dltb i}.y
'trailing , not allowed'assert ','~:{:p
d=. ;(a~:<t)#d
if. #p do. d=. d,t,p,',',LF end.
options=: d
)

INC=: INC_handsontable_basic 

HBS=: 0 : 0
'hot'jhdiv''
)

CUSTOM=: 0 : 0
 data= <DATA>
 $('#hot').handsontable({<OPTIONS>});
 hot = $('#hot').handsontable("getInstance");
 // hot.addHook('afterChange', function(){dirty= true;});
) 

JS=: 0 : 0 NB. javascript
function ev_body_load(){<CUSTOM>}

)
