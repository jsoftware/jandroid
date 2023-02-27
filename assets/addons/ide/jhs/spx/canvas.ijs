NB. graphics - drawing on an html canvas
require'~addons/ide/jhs/cojhs/jcanvas.ijs'
'jsxw_z_ jsxh_z_'=: jsxwh_z_=: 300 300 NB. width,height 
p=. 'jcanvas;_' cojhs jsxwh NB. create locale - no show
title__p=: 'canvas-tour'
refresh__p=: '' NB. initial and refresh is blank canvas
NB. next step shows graph page in a new window
show__p 1 1 400 400
NB. position canvas-tour page so you can see it and jijx page

f1=: 3 : 0
jsxnew''              NB. clear jsxbuf buffer
jscbeginPath''        NB. start path that will be painted
jsclineWidth 4        NB. pen width
jscstrokeStyle jsxucp'red' NB. red pen
jscmoveTo 0 0         NB. upper left 
jsclineTo jsxwh       NB. lower right
jscstroke''           NB. draw line
jsxnew''              NB. return jsxbuf and clear
)

run__p b1=: f1'' NB. run commands to draw thick red line
b1 NB. list of ints - command count args ...
NB. 1st int is for command beginPath and it has 0 args
NB. 3rd int is for command lineWidth and it has 1 arg which is 1

reload_jhs_ ;p NB. same as pressing F5 on canvas page
refresh__p=: jsxarg b1 NB. 
reload_jhs_ ;p NB. same as pressing F5 on canvas page

NB. common setup: pen width; pen color ; brush color
common=: 3 : 0
'a b c'=. y
jsxnew''
jscbeginPath''
jsclineWidth a
jscstrokeStyle jsxucp b
jscfillStyle   jsxucp c
)

f2=: 3 : 0
common 2;'blue';'blue'
jscmoveTo 0,jsxh
jsclineTo jsxw,0
jscstroke''
jsxnew''
)
run__p b2=: f2'' NB. draw thin blue line from lower left to upper right

NB.spxaction: mouse event handlers - click mouse down, move, and mouse up

f3=: 3 : 0
common 3;'rgb(0,255,0)';'yellow' NB. any css color string is valid
jscrect 20 20 50 20
jscfill''
jscstroke''
jsxnew''
)
run__p b3=: f3'' NB. draw rectangle

f4=: 3 : 0
common 4;'pink';'aqua'
jscfont jsxucp '80px Arial'
jscfillText 20 150, jsxucp 'hello Ü'
jsxnew'' 
)
run__p b4=: f4'' NB. draw text

f5=: 3 : 0
common 3;'orange';'aqua'
jscellipse 150  75  50  75  ,(jsxradian 1 0 ,2*o.1), 1
jscstroke''
jscbeginPath''
jscellipse 60 200 50 75 ,(jsxradian 0 0 , o.1),0
jscfill''
jscstroke''
jsxnew''
)
run__p b5=: f5'' NB. ellipse

f6=: 3 : 0
jscbeginPath''
r=. 0 , 0.5*o.1

jscbeginPath''
jscmoveTo 200 200
jscarc 200 200 50 ,(jsxradian r),0
jscfillStyle jsxucp 'green'
jscfill''

jscbeginPath''
jscmoveTo 200 200
jscarc 200 200 50 ,(jsxradian r+0.5*o.1),0
jscfillStyle jsxucp 'red'
jscfill''

jscbeginPath''
jscmoveTo 200 200
jscarc 200 200 50 ,(jsxradian r+o.1),0
jscfillStyle jsxucp'yellow'
jscfill''

jscbeginPath''
jscmoveTo 200 200
jscarc 200 200 50 ,(jsxradian r+1.5*o.1),0
jscfillStyle jsxucp'blue'
jscfill''

jsxnew''
)
run__p b6=: f6'' NB. pie quarters

f7=: 3 : 0
jscbeginPath''
jsclineWidth 3
jscstrokeStyle jsxucp'forestgreen'
jsxlines ?20$300
jsxnew''
)
run__p b7=: f7'' NB. line connecting random points

fclear=: 3 : 0
jsxnew''
jscbeginPath
jscclearRect 0 0,jsxwh
jsxnew'' 
)
run__p bclear=: fclear''

run__p b1,b2,b3,b4,b5,b6,b7

f8=: 3 : 0 
d=. #:(~:/&.#:@, +:)^:(<32) 1
jsxnew''
jscclearRect 0 0,jsxwh
jscbeginPath''
jsxvm d
jsxnew''
)

run__p b8=: f8'' NB. simple viewmat - Sierpinski triangle
