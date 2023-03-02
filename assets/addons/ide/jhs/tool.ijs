coclass'jijx'
coinsert'jhs'

tool=: 3 : 0
jhtml'<hr>'
echo y
jhtml'<hr>'
)

ev_node_click=:    3 : 'tool tool_node'
ev_react_click=:   3 : 'tool tool_react'
ev_table_click=:   3 : 'tool tool_table'
ev_jd3_click=:     3 : 'tool tool_jd3'
ev_chart_click=:   3 : 'tool tool_chart'
ev_app_click=:     3 : 'tool tool_app'
ev_print_click=:   3 : 'tool tool_print'
ev_demo_click=:    3 : 'tool tool_demos'
ev_watch_click=:   3 : 'tool tool_watch'
ev_debug_click=:   3 : 'tool tool_debug'
ev_debugjs_click=: 3 : 'tool tool_debugjs'
ev_sp_click=:      3 : 'tool sphelp'
ev_labs_click=:    3 : 'tool tool_labs 0'

ev_welcome_click=:   3 :'tool tool_welcome'
ev_shortcuts_click=: 3 :'tool tool_shortcuts'
ev_popups_click=:    3 :'tool tool_popups'
ev_closing_click=:   3 :'tool tool_closing'

tool_debug=: 0 : 0
debug facilities: suspend execution at stop or error
 examine/modify values and definitions and continue

NOTE: jdebug into button requires j903 to work properly!

practice before you need it for real!

jijx ide>jdebug - open jdebug
 move tab so you can see jijx and jdebug at same time if possible
    
   dbr 1       NB. enable debug
   calendar'a' NB. will stop on error on dyadic line 0
   y=. 0       NB. fix error
   
press jdebug over button to step through verb
 check values as you move along
 ctrl+quote is same as over button - may be more convenient
 step until the verb is finished
 
   dbxsm'calendar 0:0' NB. stop on monadic/dyadic line 0
   calendar 0

press jdebug run button to run to stop on dyadic line 0

press jdebug over button to step through
)

tool_debugjs=: 0 : 0
browser html/css/javascript debuggers are excellent!
spend time getting familiar with your favorite browser
online docs are good, but mostly you just have to play
ctrl+shift+i might open the dbugger sub window

detect syntax errors with visual studio code (jshint extension)
 or install jshint in nodejs and run jshint cli
 ~/.jshintrc - {"esversion": 6,"multistr": true}
)

tool_react=: 0 : 0
react - popular javascript framework for building apps
you can take an existing react app and integrate it with J
tictactoe is a simple example where J is added to play O

run following sentence to setup tictactoe example:
   runreact_jhs_'tictactoe'
)

tool_node=: 0 : 0
node - commercial server - https://nodejs.org
node https proxy server sits between JHS and client
securely access your JHS server from any device on lan or remote

   load'~addons/ide/jhs/node.ijs'
   node_jhs_''
)

NB. jhslinkurl'www.d3js.org' NB. link to D3 home page
tool_jd3=: 0 : 0
plot d3 uses D3 javascript library
see menu wiki>JHS>help>libraries for more info
   jd3''
)

tool_chart=: 0 : 0
   jcjs'tutorial'
   jcjs'help'
)

tool_table=: 0 : 0
table (spreadsheet) uses Handsontable javascript library
see menu wiki>JHS>help>libraries for more info
   'jtable;0 0'jpage'n' [ n=. i.3 4
n immediately reflects any changes
edit cells and add new rows/cols
initial data was numeric, so non-numeric is red
   'jtable;20 20'jpage's' [ s=: 2 2$'aa';'b';'c';'dd'
close with red button or Esc-q as this informs J server
)

tool_app=: 0 : 0 
how to build an app
apps are built with J, JHS framework,
 HTML, CSS, DOM, and javascript
learning curve is long, but not steep
 significant rewards along the way
what you learn is applicable not just to J,
 but to every aspect of web programming
JHS IDE is built with the same facilities
run and study each script/app in order
   runapp_jhs_ N [;xywh]
    - copies appN.ijs to ~temp/app
    - runs and opens script in a tab
    - opens app in a tab or at xywh
move script and app so you can study them

<RUNAPP>
   runapp_jhs_ 1 NB. ; 10 10 400 400
)

tool_app=: tool_app rplc'<RUNAPP>';runapp''

tool_print=: 0 : 0
simple printing
     print_jhs_       'abc';i.2 3 4
   0 print_jhs_       'display without print dialog'
     printscript_jhs_ '~addons/ide/jhs/config/jhs.cfg'
   0 printscript_jhs_ '~addons/ide/jhs/config/jhs.cfg'
   printwidth_jhs_=: 80 NB. truncate longer lines with ... 
   printstyle_jhs_=: 'font-family:"courier new";font-size:16px;'
)

tool_demos=: 0 : 0
simple apps showing JHS gui programming
run demos to see some of the possibilities
study the source to see how it is done
study menu tool>app first as the source
will make more sense after that
1  Roll submit
2  Roll ajax
3  Flip ajax
4  Controls/JS/CSS
5  Plot
6  Grid editor
7  Table layout
8  Dynamic resize
9  frames
10 Ajax chunks
11 Ajax interval timer
12 WebGL 3d graphics
13 D3 line and bar plots
14 iframes - spreadsheet/graph
15 Flip - no javascript
16 pswd gen - no javascript
   rundemo_jhs_ 1
)

tool_watch=: 0 : 0
   'jwatch;0 0' jpage '?4 6$100' NB. watch an expression
)

tool_labs_txt=: 0 : 0
labs - interactive tutorials - a good way to learn J
labs are not always current and may run with errors
labs are organized into categories
run one of the following sentences:
)

tool_welcome=: 0 : 0
welcome to the browser interface to J
chartjs adds powerful new plot facility
 menu tool>plot-chart jcjs'tutorial'
building an app has changed over the years
 for the latest info, see menu tool>app
jijs script - run current line or selection
close pages with red box in upper right corner
)

tool_shortcuts=: 0 : 0
Esc-q (Escape key then q) - close page
 jijx close closes all pages and exits server
Esc-1 - focus menu - arrow keys
ctrl+shift+↑/↓ - recall previous line/input
ctrl shortcuts are supported for
 ,./<>? (comma,dot,slash,less,larger,query)
jijx ctrl shortcuts
 ctrl+. lab/spx advance
 ctrl+, load project (see sphelp)
 ctrl+' debug step
 ctrl+" debug stepin
custom jijx shortcut handler for ctrl+?
 ev_query_ctrl_jijx_=: 3 : 'i.5'
see menu>help>JHS for more info
)

tool_popups=: 0 : 0
if browser is set to block pop-ups, you will
 get an alert when JHS tries to create one
most browsers let you configure to allow them
 (if possible, just allow from localhost:65001)
see menu>help>JHS for more info
)

tool_closing=: 0 : 0
close JHS page with Esc-q
 or, if present, the page red close button
this lets JHS manage the close
 (save changes and free up resources)
browser close button misses these steps
see menu>help>JHS for more info
)

getlabs=: 3 : 0
LABFILES=: f=. excludes ,{."1 dirtree'~addons/*.ijt' NB. exclude inserted to remove excluded labs - see exlabs.txt
d=. (>:#jpath'~addons')}.each f
d=. (;d i: each '/'){.each d
b=. ;(<'labs/labs/')=10{.each d
d=. (b*10)}.each d
d=. (<'addons') ((-.b)#i.#d)}d
LABCATS=: d
t=. toJ each fread each f
t=. (t i.each LF){.each t
t=. (>:each t i.each ':')}.each t
t=. t-.each ''''
t=. deb each t
LABTITLES=: t
)

NB. could be used to exclude labs - see exlabs.txt
EXJHS=: 0 : 0
)

excludes=: 3 : 0  NB. based on excludes_jlab805_ with a different final line
t=. 'b' fread '~addons/labs/labs/exlabs.txt'
if. t-:_1 do. y return. end.
t=. t #~ '#' ~: {.&> t
0!:100 ; t ,each LF
r=. EXALL
if. IFJHS do.
 r=. r,EXJHS
elseif. IFQT do.
 r=. r,EXJQT
elseif. IFJNET do.
 r=. r,EXJNET
end.
r=. ((jpath '~addons/'),deb) each <;._2 r
y #~ -. y e. r                       NB. This line is different from the jlab805 version as LABFILES is a list
)

tool_labs=:3 : 0
getlabs''
if. 0=#LABCATS do.
 t=. 'No labs installed.',LF,'Do jal (pacman) labs/labs install and try again.'
else.
 t=. tool_labs_txt
 d=. /:~~.LABCATS
 t=. t,;LF,~each(<'   lablist_jijx_ '),each'''',~each'''',each d
end.
t
)

lablist=: 3 : 0
getlabs''
titles=. /:~(LABCATS = <dltb y)#LABTITLES
echo'run one of the following sentences:'
echo ;LF,~each (<'   labrun_jijx_ '),each'''',~each'''',each titles
)

labrun=: 3 : 0
f=. ;LABFILES{~LABTITLES i. <dltb y
echo ;f
ADVANCE=: 'lab'
require__'~addons/labs/labs/lab.ijs'

NB. restore spx
spx__=:    spx_jsp_
spx_jhs_=: spx_jsp_

ADVANCE_jlab_=: 'To advance, press ctrl+. or click menu > item.'
smselout_jijs_=: smfocus_jijs_=: [ NB. allow introcourse to run
echo'JHS lab advance - ctrl+. or menu >'
lab_jlab_ f
)
