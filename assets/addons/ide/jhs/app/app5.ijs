coclass'app5'
coinsert'jhs'

NB. sentences that define html elements
HBS=: 0 : 0
NB. base div implicity opened
      jhclose''
      'title'  jhtitle 'app5 - css flex - ta|tb - side by side'
      'hbs'jhb'show HBS'
      'css'jhb'show CSS'

jhdivz NB. base div close - flex active

'jflexrow'jhdiva''
      'ta'jhtextarea'';10;10
      'tb'jhtextarea'';10;10
jhdivz

jhdiva'' NB. base div open - flex inactive
      'footer'jhhn 3;'page footer'
NB. base div implicity closed
)


CSS=: 0 : 0
#ta{font-family:<PC_FONTFIXED>;resize:none;} /* id ta - fixed font - no resize handle */
#tb{font-family:<PC_FONTFIXED>;resize:none;} /* id tb - fixed font - no resize handle */
#ta{width: 50%;height:100%;}                 /* id ta - fill available space          */
#tb{width: 50%;height:100%;}
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