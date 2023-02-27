coclass'app6'
coinsert'jhs'

NB. sentences that define html elements
HBS=: 0 : 0
NB. base div implicity opened
      jhclose''
      'title'  jhtitle 'app6 - css flex - ta/tb - ta above tb'
      'hbs'jhb'show HBS'
      'css'jhb'show CSS'

jhdivz NB. base div close - flex active

NB. elements not in main div share remaining space
'tatitle'jhtitle'tb textarea'
'ta'jhtextarea'';10;10
jhdiva''                       NB. reopen main div

'hr'jhline''

jhdivz
'tbtitle'jhtitle'tb textarea'
'tb'jhtextarea'';10;10

jhdiva''                       NB. reopen main div
'footer'jhhn 3;'adsf'
)

CSS=: 0 : 0
#ta{font-family:<PC_FONTFIXED>;resize:none;} /* id ta - fixed font - no resize handle */
#tb{font-family:<PC_FONTFIXED>;resize:none;} /* id tb - fixed font - no resize handle */
#ta{width: 100%;height:70%;}                 /* id ta - fill available space          */
#tb{width: 100%;height:30%;resize:none;}
#hr{height: 10px; background-color: red;}
)

NB. J code - initialize and handle events
create=: 3 : 0 NB. called by page or b;'defaultrowser to initialize locale
t=. y jpagedefault ,LF,.~20 20$'some text '
'must be text1'assert 2=3!:0 t
jsdata=: 'ta';t;'tb';|.t
)

jev_get=: jpageget         NB. called by browser to load page

ev_hbs_click=: 3 : 0  NB. called by javascript function ev_hbs_click()
jhrjson 'ta';HBS
)

ev_css_click=: 3 : 0
jhrjson 'ta';CSS
)

NB. javascript code
JS=: 0 : 0

function ev_body_load(){jset('ta',jsdata.ta);jset('tb',jsdata.tb);}

function ev_ta_enter(){return true;} // return true allows enter in text
function ev_tb_enter(){return true;} // return true allows enter in text

function ev_hbs_click(){jdoj('');}
function ev_hbs_click_ajax_json(t){jset('ta',t.ta)};

function ev_css_click(){jdoj('');}
function ev_css_click_ajax_json(t){jset('tb',t.ta)};

)