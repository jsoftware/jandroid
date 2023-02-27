coclass'app4'
coinsert'jhs'

NB. css flex allows dynamic sizing
NB.  you can do almost any layout you can imagine
NB.   but it can be complicated!
NB. you can do a lot with cut/paste from examples
NB.  serious use requires study of extensive online resources

NB. sentences that define html elements
HBS=: 0 : 0
NB. base div implicity opened
      jhclose''
      'title'  jhtitle 'app4 - css flex - ta textarea'
      'hbs'jhb'show HBS'
      'css'jhb'show CSS'

jhdivz NB. base div close - flex active

      'ta'     jhtextarea '';0;20 NB. flex gives element available space

jhdiva'' NB. base div open - flex inactive
      'footer'jhhn 3;'page footer'
NB. base div implicity closed
)

CSS=: 0 : 0
#ta{font-family:<PC_FONTFIXED>;resize:none;} /* id ta - fixed font - no resize handle */
#ta{width:100%;height:100%;}                 /* id ta - fill available space          */
)

NB. J code - initialize and handle events
create=: 3 : 0 NB. called by page or browser to initialize locale
t=. y jpagedefault  ,LF,.~20 20$'silly text '
'must be text'assert 2=3!:0 t
jsdata=: 'ta';t
)

jev_get=: jpageget NB. called by browser to load page

ev_hbs_click=: 3 : 0  NB. called by javascript function ev_hbs_click()
jhrjson 'ta';HBS
)

ev_css_click=: 3 : 0
jhrjson 'ta';CSS
)

NB. javascript code
JS=: 0 : 0

function ev_body_load(){jset('ta',jsdata.ta);} // init from jsdata dictionary

function ev_ta_enter(){return true;} // return true allows enter in text

function ev_hbs_click(){jdoj('');}
function ev_hbs_click_ajax_json(t){jset('ta',t.ta)};

function ev_css_click(){jdoj('');}
function ev_css_click_ajax_json(t){jset('ta',t.ta)};

)