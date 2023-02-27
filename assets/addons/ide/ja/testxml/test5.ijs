NB. create view

load 'gui/android'

coclass 'test'
coinsert 'jni jaresu'

jniImport ::0: (0 : 0)
android.content.Context
android.view.View
)


test=: 3 : 0
'rc layouts lid'=: 1 expat_parse_xml_jalayout_ 1!:1 <jpath '~Addons/gui/android/test/res/layout/layout.xml'
assert. 0=rc
ids=: lid
assert. (#@~. = #) ids  NB. unique id names
StartActivity_ja_ 18!:5''
)

NB. =========================================================
NB. android activity callback

onStart=: 3 : 0
activity=: 2{y   NB. context/activity
vw=. (activity;18!:5'') mkview_jamkview_ layouts;ids;'';''
jniCheck activity ('setContentView (LView;)V' jniMethod)~ vw
jniCheck DeleteLocalRef <vw
0
)

test''
