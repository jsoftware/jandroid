coclass'jd3'
coinsert'jhs'

NB. css/js library files to include
INC=: INC_d3_basic 

NB. J sentences - create html body
HBS=: 0 : 0
jhclose''
'ga'jhd3_basic'' NB. ga,ga_... divs for d3_basic plot
)

NB. cojhs boilerplate from util.ijs

create=: 3 : 0
try.
 'opt d'=. y
 data=: (opt rplc LF;'\n'),jd3data d
catchd.
 ('create failed:',LF,13!:12'') assert 0
end.
)

NB. J handlers for app events
jev_get=: 3 : 0
title jhrx(getcss''),(getjs'TABDATA';data),gethbs''
)

CSS=: 0 : 0
)

NB. javascript
JS=: 0 : 0

tabdata="<TABDATA>"; // set by J jev_get handler

function ev_body_load()
{
 if (window.frameElement){jhide("close");} // demo14.ijs - only 1 close button in iframe set
 resize();
 window.onresize= resize;
}

function resize()
{
 jbyid("ga_box").style.width=window.innerWidth-20+"px";
 jbyid("ga_box").style.height=window.innerHeight-20+"px";
 plot("ga",tabdata);
}

function ajax(ts){;}

)