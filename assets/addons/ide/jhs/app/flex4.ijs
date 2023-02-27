coclass'flex4'
coinsert'jhs'

NB. sentences that define html elements
HBS=: 0 : 0
jhdiva''
      jhclose''
      'title'  jhtitle   'flex4 - css flex for textarea height'
      'which' jhspan  'report javascript event here'
      'hr' jhline''
      'button' jhb    'button'
      'text'   jhtext 'text';10
      'anchor' jhab   'anchor'
      'pswd'    jhpassword '<password>';10
      'url'    jhurl  'https://www.jsoftware.com/' NB. default _blank and text same as url
      jhbr
      'rad0'  jhradio'radio0';0;'rgroup'
      'rad1'jhradio'radio1';1;'rgroup'
      'rad2'jhradio'radio2';0;'rgroup'
      'cb0'    jhcheckbox'checkbox 0';0
      'cb1'    jhcheckbox'checkbox 1';1
jhdivz                                  NB. end main div 

'flexc'jhdiva''                         NB. start flex div
      'ta'jhtextarea'';10;10
jhdivz
)

NB. CSSFLEX is added to base CSS to support flex sizing
CSS=: CSSFLEX,0 : 0
#button{background-color:lightgreen;}
#text{color:red;} 
#ta{width:100%;height:100%;resize:none;}
)

NB. J code - initialize and handle events
create=: 3 : 0 NB. called by page or browser to initialize locale
t=. y jpagedefault 'test'
'must be text'assert 2=3!:0 t
jsdata=: 'ta';500$'textarea',LF,'more'
)

jev_get=: jpageget         NB. called by browser to load page

NB. javascript code
JS=: 0 : 0

function ev_body_load(){jset('ta',jsdata.ta);} // initialize page when loaded
//function ev_body_load(){} // initialize page when loaded

function show(){jbyid("which").innerHTML= JEV+'()';}

function ev_button_click(){show();}
function ev_anchor_click(){show();}
function ev_anchor_dblclick(){show();}

function ev_rad0_click(){show();return true;} // return true to allow normal state change
function ev_rad1_click(){show();return true;}
function ev_rad2_click(){show();return true;}

function ev_cb0_click(){show();return true;}
function ev_cb1_click(){show();return true;}

function ev_text_enter(){show();}
function ev_pswd_enter(){show();}
function ev_ta_enter(){show();return true;}

)