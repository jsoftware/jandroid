coclass'app1'
coinsert'jhs'

0 : 0
best practice for building JHS app (page)
page is numbered locale with class in its path
close button (red box x) closes page and locale
4 parts: HBS, CSS, J code, JS javascript code
J passes data to javascript as a JSON string
javascript passes data to J in a dictionary (accessed by getv) 
)

NB. sentences that define html elements
HBS=: 0 : 0
jhclose''
'title'    jhtitle 'app1 - overview'
'flipone'  jhb     'flipone'
'fliptwo'  jhb     'fliptwo'
'flipboth' jhb     'flip one and two'
'one'      jhtext  '';5
'two'      jhtext  '';5
)

NB. cascading style sheet statments - how html elements look
CSS=: 0 : 0
.jhb{background-color:lightgreen;} /* buttons are green */
)

NB. J code - initialize and handle events
create=: 3 : 0 NB. called by page or browser to initialize locale
t=. y jpagedefault 'test';'default'
'must be text1;text2'assert (1=L. t)*.(2=#t)*.2~:3!:0 each t
jsdata=: 'one';({.t);'two';{:t NB. javascript uses jsdata initialize page
)

jev_get=: jpageget         NB. called by browser to load page

NB. user clicks button one
NB.  -> browser calls javascript function ev_click_one()
NB.   -> javascript calls J verb ev_flipone_click
NB.    -> jhrjson returns json result to ev_click_one_ajax_json()
NB.     -> jset sets value t.one in text field one

ev_flipone_click=: {{ jhrjson 'one';|.getv'one' }} NB. return {'one': flipped-value }
ev_fliptwo_click=: {{ jhrjson 'two';|.getv'two' }}
ev_flipboth_click=: {{ jhrjson 'one';(|.getv 'one');'two';|.getv 'two' }}

NB. javascript code - initialize page and handle events
JS=: 0 : 0
function ev_body_load(){ // initialize page when loaded
 jset('one',jsdata.one); // set text field 'one' with value from jsdata
 jset('two',jsdata.two);
}

function ev_flipone_click(){jdoj('one');} // event handler - calls J event handler
function ev_flipone_click_ajax_json(t){jset('one',t.one);} // called when J finishes

function ev_fliptwo_click(){jdoj('two');} // html element 'two' value passed to J
function ev_fliptwo_click_ajax_json(t){jset('two',t.two);}

function ev_flipboth_click(){jdoj('one two');} // html values 'one' and 'two' passed
function ev_flipboth_click_ajax_json(t){jset('one',t.one);jset('two',t.two);}

function ev_one_enter(){jscdo('flipone');} // enter key calls ev_flipone_click()
function ev_two_enter(){jscdo('fliptwo');}

)
