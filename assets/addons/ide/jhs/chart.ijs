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
case.'set'    do. i.0 0[cjsdata=: ;data return.
case.'type'   do. t=. 'type "','"',~;data
case.'data'   do. t=. cjssetdata data
case.'legend' do. t=. cjssetlegend data
case.'labels' do. t=. cjssetlabels data
case.'titles' do. t=. cjssettitles data
case. 'add'   do. t=. ;data
case.'plot'   do. cjsplot ":;data return.
case.'term'   do. cjsterm data return.
case.'termcss'do. t=. 'jtermcss "',(;data),'"' NB. stored in cjsdata
case.'termclose'do. cjstermclose data return. 
case.'update' do. cjsupdate'' return.
case.'close'  do. cjscmd 'w.jscdo("close")' return.
case.         do. ('jcjs: ',(;{.a),' unknown op')assert 0
end.
cjsdata=: cjsfix cjsdata,t,(LF~:{:t)#LF
i.0 0
)

cjstermclose=: {{ for_i. i.0>.chartnum-;y do. jjs 'removeelement("jchart',(":i),'_parent");' end. }}

cjsreset=: 3 : 'i.0 0[cjsdata=: cjsdefaults'

cjshelp=: 0 : 0
help
tutorial

reset                    - clear chart definition
get                      - return defn
set      ; defn          - set defn

type     ; line/bar/pie/...

data     ; numbers       - replace datasets
legend   ; boxed legends - replace legends

labels   ; integer       - replace labels 0,1,...
labels   ; boxed labels

titles   ; boxed titles  - xaxis,yaxis,chart

add      ; 'options.animation.duration 2000'

plot     ; [x y [w h]]   - cjsloc_jhs_ set as chart locale

term     ; data          - jijx plot - cjsid_jhs_ set as term chart id
termcss  ; css           - styles for jijx plot - width:600px;height:400px;
termclose; n             - close all but last n charts

update                   - updates cjsloc chart

close                    - closes cjsloc chart

global defaults (font size, ...) are from js/chartjs/defaults.js
term can overide with jjs, for example:
   jjs_jhs_'Chart.defaults.font.size = 32;'

   ['reset'] chart data - [jcjs'reset'] jcjs'term';data
)

cjscmd=: 3 : 0
jjs 'w=window.open("","jcjs?jlocale=',(;cjsloc),'");',y,';'
)

cjstutorial=: 3 : 'spx''~addons/ide/jhs/spx/chart.ijt'''

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
if. 2~:3!:0 ;{.a do. a=. ":each <"0 [i.;y end.
'data.labels ','[',']',~}.;',',each '"',each'"',~each a
)

cjssetdata=: 3 : 0
if. 1=#y do. NB. each row of a single matrix is a dataset
 if. 2=$$>{.y do. y=. ,<"1 >y end.
end. 
d=. boxopen y
r=. ''
cjsdata=: cjsremove 'data.datasets..data' NB., remove previous
for_i. i.#d do.
 r=. r,LF,~'data.datasets.',(":i),'.data ','[',']',~(":;i{d)rplc'_';'-';' ';','
end. 
)

cjssetlegend=: 3 : 0
d=. boxopen y
r=. ''
cjsdata=: cjsremove 'data.datasets..label'   NB., remove previous
r=. r,LF,~'options.plugins.legend {"display":1}'
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
options.plugins.legend {"display":0}
options.animation.duration  2000
options.responsive             1
options.maintainAspectRatio    0
options.elements.point.radius  1
type "line"
jtermcss "width:400px;height:200px;background-color:lightblue;border:solid 1px black;"
)

cjsremove=: {{ ;(-.cjsmatch y)#<;.2 cjsdata }}

NB. mask for cjsdata rows with names that match
NB. options.animation.duration
NB. data.datasets.0.data where the 0 is ignored
cjsmatch=: {{
  a=. <;.2 cjsdata
  a=. (a i.each' '){.each a
  (<y)=a-.each <Num_j_
}}

NB. chart displayed in term window
cjsterm=: 3 : 0
id=. 'jchart',":chartnum
chartnum=: >:chartnum
cjsid=: id
data=. y

NB. get term style from cjsdata
s=. cjsmatch'jtermcss'
if. 0=+/s do. s=. '' else. s=. 9}.;s#<;._2 cjsdata end.

t=. id jhchart''
jhtml t jhaddatts'style=',s
jcjs'labels';{:$>data
jcjs'data';data
d=. jurlencode jsencode jcmds 'chartjs ',id,' *',jcjs'get'
jjs 'jsdata=[];runjhrcmds("',d,'");'
)

chart_z_=: {{
 jcjs'term';y
 :
 select. x
 case.'reset'  do. jcjs'reset'
 case.         do. 'invalid chart x argument'assert 0
 end.
 chart y
}}

NB. charts side by side
chart_sidebyside=: 3 : 0
jcjs'reset'
jcjs'termcss';'width:400px;height:200px;float:left;'
d=. 5?5
chart d
jcjs'type';'pie'
chart d
jcjs'termcss';''
jhtml_jhs_ '<div style="clear:left;"></div>'
)
3 : 0''
if. _1=nc<'cjsdata' do. jcjs'reset' end.
)