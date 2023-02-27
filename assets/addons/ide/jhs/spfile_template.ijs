coclass'jsp'

0 : 0
define project verbs

default SPFILE is: ~temp/sp/spfile.ijs
template is: ~addons/ide/jhs/spfile_template.ijs

edit/open do popups
see: jijx>wiki>JHS>>Help>pop-up

focus works in firefox, but may not work in other browsers
)

p_aaa=: 3 : 0
edit       '~temp/spexample.ijs'
open_jhs_  'jfif'
open_jhs_  'jfiles'
focus_jhs_ 'jijx'
)

p_bbb=: 3 : 0
open_jhs_ 'jfile'
10 10 open_jhs_ 'jdebug'
focus_jhs_ 'jijx'
load 'git/addons/data/jd/jd.ijs'
testdata=: i.3 4
echo testdata
)

splist'' NB. echo project sentences