coclass'jcjs'
coinsert'jhs'

INC=: INC_chartjs NB. include chart js code

NB. define html elements (button,...)
HBS=: 0 : 0
jhclose''
'dict'jhb'show chart config'
'defn'jhb'show chart defintion'
jhdivz NB. base div close - flex active

NB. share remaining vertical space
'charta'jhchart''
'show'jhdiv''

jhdiva'' NB. reopen main div
)

ev_create=: 3 : 0
if. ''-:y do.
 jcjs'reset'
 jcjs'data';>:5?20
 jcjs'labels';5
end. 
jhcmds 'chartjs charta *',cjsdata
)

NB. style html elements (color,size,border,..)
CSS=: 0 : 0
#charta_parent{background-color:pink;}
#charta_parent{height:50%;width:100%;}
#show{font-family:monospace;}
)

NB. javascript code
JS=: 0 : 0

function show(t){
 t= t.replace(/</g, '&lt;')
 t= t.replace(/>/g, '&gt;');
 t= t.replace(/\n/g, '<br/>');
 t= t.replace(/ /g,  '&nbsp;');
 t= t.replace(/\"/g, '&quot;');
 t= t.replace(/'/g, '&#39;');
 jbyid('show').innerHTML= t;
}

function ev_dict_click(){
 var c= cjs('charta').config;
 show(JSON.stringify(c._config,null,4));
}

function ev_defn_click(){show(jsdata['charta']);}

)