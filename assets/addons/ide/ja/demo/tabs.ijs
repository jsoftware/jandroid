NB. tabdemo
NB.
NB. this is the controls demo in 3 tabs
NB.
NB. tabs control is not implemented
NB. emulated using binx and viewshow

coclass 'demotabs'

onStart=: tabdemo_run

NB. =========================================================
Tabdemo=: 0 : 0
pc tabdemo closeok escclose;

bin v;
bin v;
cc tview radiobutton horizontal;cn View;
cc teditor radiobutton group;cn Editor;
cc tdummy radiobutton group;cn Dummy;
bin z;
line;
binx view v;
cc linear radiobutton;
cn "view linear";
cc boxed radiobutton group;
cn "view boxed";
cc tree radiobutton group;
cn "view tree";
bin z;

binx editor v;
bin vh;
cc gross radiobutton;
cc net radiobutton group;
cc paid checkbox;
bin z;
cc names combobox;
bin zz;

binx dummy v;
cc list listbox;
cc entry edit;
cc ted editm;
bin z;

bin h s2;
cc ok button;cn "Push Me";
cc cancel button;cn "Cancel";

set boxed value 1;
set net value 1;
set names items Bressoud Frye Rosen Wagon;
set names select 2;
set entry text 盛大 abc 大嘴鳥;
set list items one "two turtle doves" three "four colly birds" five six seven;

)

NB. =========================================================
tabdemo_run=: 3 : 0
wd Tabdemo
wd 'set ted text *How grand to be a Toucan',LF,'Just think what Toucan do.'
wd 'pshow'
wd 'set tview value 1'
NB. tabdemo_tview_button''
)

NB. =========================================================
tabdemo_close=: 3 : 0
wd 'pclose'
)

NB. =========================================================
tabdemo_tview_button=: 3 : 0
wd 'setp viewshow view 1'
wd 'setp viewshow editor 0'
wd 'setp viewshow dummy 0' 
)


NB. =========================================================
tabdemo_teditor_button=: 3 : 0
wd 'setp viewshow view 0'
wd 'setp viewshow editor 1'
wd 'setp viewshow dummy 0' 
)


NB. =========================================================
tabdemo_tdummy_button=: 3 : 0
wd 'setp viewshow view 0'
wd 'setp viewshow editor 0'
wd 'setp viewshow dummy 1' 
)

wd 'activity ', >coname''
