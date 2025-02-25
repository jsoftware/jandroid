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
'jfif' jpage''
'jfile'jpage''
testdata=: i.3 4
echo testdata
)

splist'' NB. echo project sentences