coclass'jdemo16'
coinsert'jhs'
NB. override create, jev_get, saveonclose verbs to customize app

NB. html form definition
NB. html element id can be one part (pswd) or two parts (gn*up)
NB. gn*up (mid*sid) has gn main id and up secondary id
NB. J event handler is ev_mid_type
NB. easy to have a single handler for different events
HBS=:  0 : 0
        jhclose''                      NB. redbar close    
        jhhn 3;'pswd app - no javascript' NB. header size 3
'pswd'  jhspan '&nbsp;'                NB. pswd text - non-breaking space
        jhhr                           NB. html horizonatal rule
'len'   jhspan'length: ',":length
'gn*up' jhb'▲'                         NB. button to increase length
'gn*dn' jhb'▼'
'epy'   jhspan'entropy: '
        jhbr                           NB. html line break
'gn*sp' jhcheckbox'%^& etc';0
'gn*uc' jhcheckbox'uppercase';0
'gn*di' jhcheckbox'digits';0
        jhbr
'gn'    jhb'generate'
'copy'  jhb'copy to clipboard'
        jhhr
'gn*sim'jhradio'simple css';1;'cssset' NB. csset radio button group
'gn*fan'jhradio'fancy css' ;0;'cssset'
        jhbr
        desc                         NB. desriptive text
        jhdemo''                     NB. link to open source script
)

NB. ids for id/value pairs for J handler with no javascript handler
JEVIDS=: jhjevids'pswd gn*sp gn*uc gn*di gn*sim gn*fan'

length=: 10 NB. chars in password

NB. return pswd;entropy
gen=: 3 : 0
'lc uc di sp'=. (26}.Alpha_j_);(26{.Alpha_j_);'0123456789';'~!@#$%^&*()-=_+'
sud=. ;0".each getvs'gn*sp gn*uc gn*di' NB. sp uc di values from event
a=. lc,;sud#sp;uc;di                    NB. valid pswd chars
(a {~ ? length##a);<<.length*2^.#a
)

NB. jhrcmds finishes event handler with 0 or more commands
NB. that will be run in the browser
ev_gn_click=: 3 : 0
lastevent__=: NV NB. name/value pairs in event
length=: 10>.30<.length+('up'-:getv'jsid')-'dn'-:getv'jsid'
'p e'=. gen''
len=. 'set len  *length: ',":length
psw=. 'set pswd *',p
epy=. 'set epy  *entropy: ',":e
css=. 'css      *',(1=0".getv'gn*fan')#fancycss
jhrcmds  len;psw;epy;css NB. len;psw;css
)

ev_copy_click=: 3 : 0
jhrcmds'copy *',getv'pswd'
)

desc=: 0 : 0
<hr>Modeled after J602 app documented in
J wiki (User:Andrew_Nikitin/Literate).<br><br>
All events are passed to J handlers and no javascript code is required.<br><br>
Close with red button or Esc-q as this informs J server.
Tab/browser close does not inform J server.
)

NB. html cascading style sheet - document look and feel
CSS=: 0 : 0
form{margin:0px 2px 2px 2px;}
*.jhtext{font-family:"monospace";}
)

fancycss=: 0 : 0
form{margin:0px 40px;}
#pswd{background-color:salmon;font-size:20px}
)

JS=: JEVIDS,0 : 0
// add custom javascript code and event handlers here
)
