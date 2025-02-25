NB. handsontable table editor - includes jhot iframe
NB. 'jtable'jpage'n'[n=. i.3 9

coclass'jtable'
coinsert'jhs'

HBS=: 0 : 0
jhclose''
'head'jhdiv'table'
'<div id="hots">'
'<iframe id="hot" name="hot" src="',(;hot),'"  ></iframe>'
'</div>'
)

NB. cojhs boilerplate from util.ijs

ev_create=: 3 : 0
if. ''-:y do.
 temp__=: 2 2$'aa';'b';'c';'dd'
 y=. 'temp'
end.

try.
 header=: dltb y
 header=: header,>('_'={:header){'__';''
 assert 0=nc<header
 d=. header~
 assert 2>:$$d
 assert 2>L.d
 (header)=: (,:^:({.2-$$d)) d
 hot=: 'jhot;_'jpage'EMPTY_z_'
 setdata__hot header~
 qa__=: JS__hot
 fixjs__hot'' NB. apply options and data to JS
 qz__=: JS__hot
 NB.! show__hot 'tab' NB.!!!
 jhcmds 'set head *table: ',header
catchd.
 ('create failed:',LF,13!:12'') assert 0
end.
)

NB. need better way to destroy embedded objects
ev_close_click=: 3 : 0
destroy__hot'' NB. jhot object
jhrajax''
shown=: 0 NB. already closed
destroy''
i.0 0
)

NB. currently gets all the data - could get and apply just changes
jev_change=: 3 : 0
(header)=: data__hot=: tablefromjs getv'jdata'
jhrajax''
)

CSS=: 0 : 0
body{margin:5px;}
#hots{height:90vh;}
#hot{width:100%;height:100%;}
)

JS=: 0 : 0 NB. javascript
function ev_body_load()
{
 window.frames[0].hot.addHook('afterChange', function(change,source)
 {
  if(source!="edit")return;
  var s=  "jev_change_"+jform.jlocale.value+"_ 0"
  jdoajax([],JSON.stringify(window.frames[0].data),s);
 }
 );
 jhrcmds(jsdata['jhrcmds']); // must do explicitly
}

function ajax(ts){;}

)
