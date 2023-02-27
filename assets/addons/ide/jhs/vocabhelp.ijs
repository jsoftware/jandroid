coclass'jhs'

NB. borrows from ide/qt/help.ijs

jhsvocab=: 3 : 0
top=. {.;:y
first=. {.;top
last=. {:;top
b=. 'https://code.jsoftware.com/wiki/Vocabulary'
if.     ''-:;top                        do. t=. b
elseif. top e. DICTNV_jhs_              do. t=. b,'/',>DICTNVX {~ DICTNV i.top
elseif. (first e. Alpha_j_) *. '.'=last do. t=. b,'/ControlStructures'
elseif. (first='_')+.first e. Num_j_    do. t=. b,'/Constants'
elseif. 1                               do. t=. 'https://code.jsoftware.com/wiki/NuVoc','#bottomrefs'
end.
jjs_jhs_  'urlopen("',t,'");'
i.0 0
)

wiki_z_=: jhsvocab_jhs_

NB. !: changed from bangco to Foreigns
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
