coclass'app3'
coinsert'jhs'

NB. study CSS=: lines to see how they change the html look and feel
NB. CSS can be complicated
NB.  online validators or visual studio code can help
NB. CSS is powerful - cut/paste can get you a long ways
NB.  but serious use requires study of the exensive online resources

NB. sentences that define html elements
HBS=: 0 : 0
jhclose''
      'title'  jhtitle 'app3 - css custmizing look and feel'
      'which'  jhspan  'report javascript event here'
      'hr'     jhline''
      'show'   jhb    'showNV'     NB. button show with text showNV
      'text'   jhtext 'text';10
      'anchor' jhab   'anchor'
      'pswd'   jhpassword '<password>';10
      'url'    jhurl  'https://www.jsoftware.com/' NB. default _blank and text same as url
      jhbr
      'r0'     jhrad  'r0 Aa'   ;1;'rg1'
      'r1'     jhrad  'r1 /...' ;0;'rg1'
      'r2'     jhrad  'r2 lines';0;'rg1'
      jhbr
      'c0'     jhchk  'c0 foo';1
      'c1'     jhchk  'c1 boo'
      jhbr
      'bg11'jhb'bg11 first'
      'bg12'jhb'bg12 second one'
      jhbr
      'bg21'jhb'bg21 longer than'
      'bg22'jhb'bg22 short'
      jhbr      
      'ta'     jhtextarea '';25;25
)

CSS=: 0 : 0
#ta{font-family:<PC_FONTFIXED>;}         /* id ta - fixed font for display of seebox NV */
.jhb[id^="bg"]{width: 150px; color:red;} /* jhb class with id prefix bg - wide and red  */
#text{background-color: lightblue;}      /* id text - lightblue                         */
#text:focus{background-color: yellow;}   /* id text with focus - yellow                 */
#pswd:hover{background-color: pink;}     /* id pswd with mouse - pink                   */
)

NB. J code - initialize and handle events
create=: 3 : 0 NB. called by page or browser to initialize locale
t=. y jpagedefault  ,LF,.~20 20$'silly text '
'must be text'assert 2=3!:0 t
jsdata=: 'ta';t
)

jev_get=: jpageget NB. called by browser to load page

ev_show_click=: 3 : 0  NB. called by javascript function ev_show_click()
jhrjson 'ta';seebox NV NB. NV is dictionary of values from javascript event
)

NB. javascript code
JS=: 0 : 0

function ev_body_load(){jset('ta',jsdata.ta);} // init from jsdata dictionary

function sw(){jseth('which',JEV+'()');} // set which with event info

function ev_show_click(){sw();jdoj('text pswd r0 r1 r2 c0 c1')};
function ev_show_click_ajax_json(t){jset('ta',t.ta)}

function ev_anchor_click(){sw();}
function ev_anchor_dblclick(){sw();}

function ev_text_enter(){sw();}
function ev_pswd_enter(){sw();}
function ev_ta_enter(){sw();return true;} // return true allows enter in text

function ev_r0_click(){sw();jflipchk(jform.jid.value);} /* jform.jid.value is r0 */
function ev_r1_click(){sw();jflipchk(jform.jid.value);}
function ev_r2_click(){sw();jflipchk(jform.jid.value);}

function ev_c0_click(){sw();jflipchk(jform.jid.value);}
function ev_c1_click(){sw();jflipchk(jform.jid.value);}

function ev_bg11_click(){sw();}
function ev_bg12_click(){sw();}
function ev_bg21_click(){sw();}
function ev_bg22_click(){sw();}

)