NB. pen styles demo

coclass 'demopenstyle'
coinsert 'jgl2'

onStart=: penstyle_run

NB. =========================================================
PenStyles=: cutopen 0 : 0
Solid Line
Dash Line
Dot Line
Dash Dot Line
Dash Dot Dot Line
No Pen
)

NB. =========================================================
penstyle_run=: 3 : 0
if. -. checkrequire_jademo_ 'gl2';'graphics/gl2' do. return. end.
require 'gl2'
coinsert 'jgl2'
wd 'pc penstyle escclose closeok'
wd 'pn Pen Styles'
wd 'wh _1 _1'
wd 'cc g isigraph flush'
wd 'pshow'
)

penstyle_g_paint=: 3 : 0
off=. <.-:{:glqextent'X'
glfill 255 255 255 255
glrgb 0 0 255
for_i. i.#PenStyles do.
  y=. 30+40*i
  glpen 3,i
  gllines 25,y,200,y
  gltextxy 230,y-off
  gltext (":i),' ',i pick PenStyles
end.
glpaintx ''  NB. asyncj
)

wd 'activity ', >coname''
