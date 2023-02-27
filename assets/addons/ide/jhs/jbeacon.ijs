NB. jbeacon sendbeacon handler

coclass'jbeacon'
coinsert'jhs'

jev_post_raw=: 3 : 0
decho NV_jhs_
bnv__=: NV_jhs_
NV_jhs_=: parse NV_jhs_
op=. getv'op'
if. op-:'close' do.
 d=. getv'uri'
 c=. <,d}.~>:d i:'/'
 echo c
 echo names__c''
end.
)
   
urlresponse=: 3 : 0
shutdownJ_jsocket_ SKSERVER_jhs_ ; 2
sdclose_jsocket_ ::0: SKSERVER_jhs_
SKSERVER_jhs_=: _1
)

NB. jjs_jhs_'navigator.sendBeacon("jbeacon",log.innerHTML);'
NB. jjs_jhs_'w=window.open("","jfif");navigator.sendBeacon("jbeacon",w.document.title+"&state=123");'