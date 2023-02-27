NB. controls demos
NB. also:
NB. pc controls closeok
NB. pc controls escclose

NB. layout width and height
NB. _1 match_patent
NB. _2 wrap_content

coclass 'democontrols'

onStart=: controls_run

Controls=: 0 : 0
pc controls;
rem make nested vertical, horizontal, vertical bins:;
bin vh;

bin v;
wh 200 _2;
cc s1 static;cn Display;
cc linear radiobutton;
cn "view linear";
cc boxed radiobutton group;
cn "view boxed";
cc tree radiobutton group;
cn "view tree";
bin z;

bin v;
wh 200 _2;
cc s2 static;cn "Expense Type";
cc gross radiobutton;
cc net radiobutton group;
cc paid checkbox;
set boxed value 1;
set net value 1;
bin z;

bin z s1;

bin h;
bin v;
cc names combobox;
set names items Bressoud Frye Rosen Wagon;
set names select 2;
cc entry edit;
set entry text 盛大 abc 大嘴鳥;
cc ted editm readonly;
rem demonstrate bin and child stretch:;
bin z;

wh _1 200;
cc list listbox;
set list items one "two turtle doves" three "four colly birds" five six seven;

bin z;

bin h s2;
cc iconbutton button;cn "";
cc ok button;cn "Push Me";
cc cancel button;cn "Cancel";
bin zz;
set ok stretch 1;
)

NB. =========================================================
controls_run=: 3 : 0
wd 'verbose 2'
wd Controls
wd 'set ted text *How grand to be a Toucan',LF,'Just think what Toucan do.'
wd 'set iconbutton icon ', (dquote jpath '~addons/graphics/bmp/toucan.bmp')
wd 'set tree icon ', dquote jpath '~addons/ide/ja/images/about.png'
wd 'set paid icon ', dquote jpath '~addons/ide/ja/images/clear.png'
wd 'pshow'
wd 'verbose 0'
)

NB. =========================================================
controls_close=: 3 : 0
wd 'pclose'
)

controls_cancel_button=: controls_close

wd 'activity ', >coname''
