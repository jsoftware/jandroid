NB. JHS - recent files
coclass'jfiles'
coinsert'jhs'

HBS=: 0 : 0
jhfcommon''
jhresize''
files''
)

jev_get=: 3 : 0
'jfiles'jhr''
)

files=: 3 : 0
addrecent_jsp_''

if. 0=#SPFILES_jsp_ do.
 '</div>',~'<div>The recent files list is empty.'
else.
 '</div>',~'<div>',;fx each SPFILES_jsp_
end. 
)

fx=: 3 : 0
s=. ;shorts_jsp_ y
t=. jshortname y
(('file*',jurlencode t)jhab s),(;(1>.20-#s)#<'&nbsp;'),y,'<br>'
)

CSS=: 0 : 0
#jfiles{color:blue}
*{font-family:<PC_FONTFIXED>;}
)

JS=: jsfcommon,0 : 0
function ev_body_load(){jresize();}

function ev_file_click(){
 t= 'jijs?jwid='+jsid.value,jsid.value;
 pageopen(t,t); //? nocache???
}

)


