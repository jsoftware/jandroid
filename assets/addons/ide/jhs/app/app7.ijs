coclass'app7'
coinsert'jhs'

NB. sentences to create html elements
NB. jhbshtml_jdemoxx_'' shows HBS html
HBS=: 0 : 0
jhclose''
'title'  jhtitle 'app7 - chartjs - 4 chart types'
'top'jhdiv'same data - 4 chart types'
jhbr
'run'jhb'run'
'sentence'jhtext'';15
jhbr
'cjsa'jhchart''
'cjsb'jhchart''
jhbr
'cjsc'jhchart''
'cjsd'jhchart''
)

NB. style the html elements
CSS=: 0 : 0
#top{text-align:center;font-size:20px;}
#sentence{margin-bottom:5px;}
.jhchart_parent{border:solid;height:200px;width:50%;float:left;}
#cjsd_parent{background-color:pink}
hr{clear:both} /* so it doesn't appear under elements that float */
)

get=: 3 : 0
jcjs'type';y
jcjs'get'
)

NB. validate sentence
validate=: 3 : 0
try.
 t=. ".y 
 assert (1=$$t)*.2~:3!:0 t
catch.
 echo 'bad sentence: ',y
 t=. 1
end. 
t
)

create=: 3 : 0
t=. y jpagedefault '5?5'
v=. validate t
jcjs'reset'
jcjs'labels';#v
jcjs'data';v
jcjs'legend';'mydata'
jcjs'add';'options.animation.duration 2000'
jsdata=: 'cjsa';(get'line');'cjsb';(get'bar');'cjsc';(get'pie');'cjsd';(get'doughnut');'sentence';t
)

NB. ajax result has same json string form as initial create
ev_run_click=: 3 : 0
jhrjson create getv'sentence'
)

jev_get=: jpageget

INC=: INC_chartjs NB. include chart js code

NB. javaacript
JS=: 0 : 0
function ev_body_load(){
 cjs_init('cjsa');
 cjs_init('cjsb');
 cjs_init('cjsc');
 cjs_init('cjsd');
 jset('sentence',jsdata.sentence);
}

function ev_run_click(){jdoj('sentence');}
function ev_sentence_enter(){jscdo('run');}

function ev_run_click_ajax_json(t){
cjs_update('cjsa',t['cjsa']);
cjs_update('cjsb',t['cjsb']);
cjs_update('cjsc',t['cjsc']);
cjs_update('cjsd',t['cjsd']);
}

)
