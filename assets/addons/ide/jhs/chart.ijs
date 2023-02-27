coclass'jhs'

jcjs_z_=: jcjs_jhs_

jcjs=: 3 : 0
a=. boxopen y
data=. }.a
select. ;{.a
fcase.'' do.
case.'help'     do. cjshelp return.
case.'tutorial' do. cjstutorial'' return.
case.'reset'    do. cjsreset'' return.
case.'get'    do. cjsdata return.
case.'set'    do. i.0 0[cjsdata=: data return.
case.'type'   do. t=. 'type "','"',~;data
case.'data'   do. t=. cjssetdata data
case.'legend' do. t=. cjssetlegend data
case.'labels' do. t=. cjssetlabels data
case.'titles' do. t=. cjssettitles data
case. 'add'   do. t=. ;data
case.'plot'   do. cjsplot ":;data return.
case.'update' do. cjsupdate'' return.
case.'close'  do. cjscmd 'w.jscdo("close")' return.
case.         do. ('jcjs: ',(;{.a),' unknown op')assert 0
end.
cjsdata=: cjsfix cjsdata,t,(LF~:{:t)#LF
i.0 0
)

cjsreset=: 3 : 'i.0 0[cjsdata=: cjsdefaults'

cjshelp=: 0 : 0
help
tutorial

reset                    - clear chart definition
get                      - return defn
set      ; defn          - set defn

type     ; line/bar/pie/...

data     ; numbers       - add dataset
legend   ; boxed legends
labels   ; integer       - labels a0,a1,...
labels   ; boxed labels

titles   ; boxed titles  - xaxis,yaxis,chart

add      ; 'options.animation.duration 2000'

plot     ; [x y [w h]]   - cjsloc_jhs_ set as chart locale

update                   - updates cjsloc chart

close                    - closes cjsloc chart
   
)

cjscmd=: 3 : 0
jjs 'w=window.open("","jcjs?jlocale=',(;cjsloc),'");',y,';'
)

cjstutorial=: 3 : 'spx''~addons/ide/jhs/spx/chart.ijs'''

cjsplot=: 3 : 0
cjsloc=: ('jcjs;',y) jpage 'charta';cjsdata
)

cjsupdate=: 3 : 0
r=. cjsdata rplc '"';'\"';LF;'\n'
cjscmd'w.cjs_update("charta","',r,'")'
)

NB. remove earlier settings and sort
cjsfix=: 3 : 0
b=.  |.<;.2 y
pb=. (b i. each ' '){.each b
;/:~(pb i. ~.pb){b
)

cjssetlabels=: 3 : 0
a=. y
if. 2~:3!:0 ;{.a do. a=. 'a',each ":each <"0 [i.;y end.
'data.labels ','[',']',~}.;',',each '"',each'"',~each a
)

cjssetdata=: 3 : 0
d=. boxopen y
r=. ''
for_i. i.#d do.
 r=. r,LF,~'data.datasets.',(":i),'.data ','[',']',~(":;i{d)rplc'_';'-';' ';','
end. 
)

cjssetlegend=: 3 : 0
d=. boxopen y
r=. ''
for_i. i.#d do.
 r=. r,LF,~'data.datasets.',(":i),'.label ','"','"',~>i{d
end. 
)

cjssettitles=: 3 : 0
'tx ty tc'=. 3{.boxopen y
r=.   (0~:#tx)#LF,~'options.scales.x.title {"display":1,"text":"<tx>"}'
r=. r,(0~:#ty)#LF,~'options.scales.y.title {"display":1,"text":"<ty>"}'
r=. r,(0~:#tc)#LF,~'options.plugins.title {"display":1,"text":"<tc>"}'
r=. r hrplc_jhs_ 'tx ty tc';tx;ty;tc
)

cjsdefaults=: 0 : 0
data.datasets []
options.animation.duration     0
options.responsive             1
options.maintainAspectRatio    0
type "line"
)
