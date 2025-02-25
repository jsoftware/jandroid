coclass'jhs'

0 : 0
   jhswiki'voc'  NB. NuVoc vocabulary
   jhswiki'i.'   NB. edit i. for others - click Dyad for x i. y
   jhswiki'if.'  NB. control words
   jhswiki'!:'   NB. foreigns
   jhswiki'12x'  NB. constants
   jhswiki'a'    NB. ancilliary 
   jhswiki'std'  NB. standard library
   jhswiki'rel'  NB. J release notes
   jhswiki'JHS'  NB. JHS info
   jhswiki'807'  NB. 807 legacy html
   jhswiki'main' NB. main page
)   

NB. add label for each link
wwwlinks=: <;._2 [ 0 : 0
vocabulary         https://code.jsoftware.com/wiki/NuVoc
control structures https://code.jsoftware.com/wiki/Vocabulary/ControlStructures
foreigns !:        https://code.jsoftware.com/wiki/Vocabulary/Foreigns
constants          https://code.jsoftware.com/wiki/Vocabulary/Constants
ancilliary         https://code.jsoftware.com/wiki/NuVoc#bottomrefs
standard library   https://code.jsoftware.com/wiki/Standard_Library/Overview
release notes      https://code.jsoftware.com/wiki/System/ReleaseNotes
JHS                https://code.jsoftware.com/wiki/Guides/JHS
807 legacy html    https://www.jsoftware.com/help/index.htm
main page          https://code.jsoftware.com/wiki/Main_Page
)

links=: 3 : 0
t=. '<div class="transient">Jsoftware www/wiki links:<br>'
for_n. wwwlinks do.
 a=. ;n
 i=. a i: ' '
 b=. deb i}.a
 a=. deb i{.a
 t=. t,LF,'<div contenteditable="false"><a href="',b,'" target="_blank">',a,'</a></div>'
end.
t=. t,LF,'</div>'
)

NB. borrows from ide/qt/help.ijs

jhsvocab=: 3 : 0
top=. {.;:y
first=. {.;top
last=. {:;top
b=. 'https://code.jsoftware.com/wiki/Vocabulary'
a=. ;top
if.     top e. DICTNV_jhs_              do. t=. b,'/',>DICTNVX {~ DICTNV i.top
elseif. (first e. Alpha_j_) *. '.'=last do. t=. b,'/ControlStructures'
elseif. '!:'-:a                         do. t=. 'https://code.jsoftware.com/wiki/Vocabulary/Foreigns'
elseif. (first='_')+.first e. Num_j_    do. t=. b,'/Constants'
elseif. 'JHS'-:a                        do. t=. 'https://code.jsoftware.com/wiki/Guides/JHS'
elseif. a-:,'a'                         do. t=. 'https://code.jsoftware.com/wiki/NuVoc','#bottomrefs'
elseif. a-:'807'                        do. t=. 'https://www.jsoftware.com/help/index.htm'
elseif. a-:'std'                        do. t=. 'https://code.jsoftware.com/wiki/Standard_Library/Overview'
elseif. a-:'rel'                        do. t=. 'https://code.jsoftware.com/wiki/System/ReleaseNotes'
elseif. a-:'main'                       do. t=. 'https://code.jsoftware.com/wiki/Main_Page'
elseif. 1                               do. t=. b
end.
jjs_jhs_  'urlopen("',t,'");'
i.0 0
)

wiki_z_=: jhsvocab_jhs_

NB. banco replaced by  !:
j=. <;._2 (0 : 0)
= eq
=. eqdot
=: eqco
< lt
<. ltdot
<: ltco
> gt
>. gtdot
>: gtco
_ under
_. underdot
_: underco
+ plus
+. plusdot
+: plusco
* star
*. stardot
*: starco
- minus
-. minusdot
-: minusco
% percent
%. percentdot
%: percentco
^ hat
^. hatdot
^: hatco
$ dollar
$. dollardot
$: dollarco
~ tilde
~. tildedot
~: tildeco
| bar
|. bardot
|: barco
. dot
.. dotdot
.: dotco
: co
:. codot
:: coco
, comma
,. commadot
,: commaco
; semi
;. semidot
;: semico
# number
#. numberdot
#: numberco
! bang
!. bangdot
!: Foreigns
/ slash
/. slashdot
/: slashco
\ bslash
\. bslashdot
\: bslashco
[ squarelf
[: squarelfco
] squarert
{ curlylf
{. curlylfdot
{: curlylfco
{:: curlylfcoco
} curlyrt
}. curlyrtdot
}: curlyrtco
" quote
". quotedot
": quoteco
` grave
`: graveco
@ at
@. atdot
@: atco
& ampm
&. ampdot
&.: ampdotco
&: ampco
? query
?. querydot
0: zeroco
1: zeroco
2: zeroco
3: zeroco
4: zeroco
5: zeroco
6: zeroco
7: zeroco
8: zeroco
9: zeroco
_1: zeroco
_2: zeroco
_3: zeroco
_4: zeroco
_5: zeroco
_6: zeroco
_7: zeroco
_8: zeroco
_9: zeroco
a. adot
a: aco
A. acapdot
b. bdot
C. ccapdot
d. ddot
D. dcapdot
D: dcapco
e. edot
E. ecapdot
f. fdot
H. hcapdot
i. idot
i: ico
I. icapdot
j. jdot
L. lcapdot
L: lcapco
M. mcapdot
o. odot
p. pdot
p.. pdotdot
p: pco
q: qco
r. rdot
s: sco
S: scapco
t. tdot
t: tco
T. tcapdot
u: uco
x: xco
)

n=. j i.&> ' '
DICTNV=: n {.each j
DICTNVX=: (n+1) }.each j
