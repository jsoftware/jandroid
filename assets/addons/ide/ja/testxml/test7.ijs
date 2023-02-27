NB. create view and menu

load 'gui/android'

coclass 'test'
coinsert 'jni jaresu'

jniImport ::0: (0 : 0)
android.content.Context
android.view.View
)

test=: 3 : 0
'rc strings sid'=: expat_parse_xml_javalues_ 1!:1 < jpath '~Addons/gui/android/test/res/values/strings.xml'
assert. 0=rc
'rc layouts lid'=: 1 expat_parse_xml_jalayout_ 1!:1 <jpath '~Addons/gui/android/test/res/layout/layout2.xml'
assert. 0=rc
'rc menus mid gp'=: (1,~ 1+#lid) expat_parse_xml_jamenu_ 1!:1 <jpath '~Addons/gui/android/test/res/menu/main.xml'
assert. 0=rc
((<'R_id_') ,&.> ids)=: >:@i.#ids=: lid,mid
assert. (#@~. = #) ids  NB. unique id names
StartActivity_ja_ (>18!:5'');'onDestroy onCreateOptionsMenu onOptionsItemSelected'
)

NB. =========================================================
NB. android activity callback

onStart=: 3 : 0
activity=: >@{. NewGlobalRef <2{y   NB. context/activity
vw=. (activity;18!:5'') mkview_jamkview_ layouts;ids;strings;''
jniCheck activity ('setContentView (LView;)V' jniMethod)~ vw
jniCheck DeleteLocalRef <vw
0
)

onDestroy=: 3 : 0
jniCheck DeleteGlobalRef <activity
0
)

NB. =========================================================
NB. button callback

button_onClick=: 3 : 0
jniCheck view=. GetObjectArrayElement (3{y);0
jniCheck id=. view ('getId ()I' jniMethod)~ ''
activity MakeToast_ja_ 'button: ', ids&id2name id
jniCheck DeleteLocalRef <view

jniCheck vw=. activity ('findViewById (I)LView;' jniMethod)~ R_id_editText1
jniCheck ts=. '' ('toString ()LString;' jniMethod) cs=. vw ('getText ()LCharSequence;' jniMethod)~ ''
activity MakeToast_ja_ jniToJString ts
jniCheck DeleteLocalRef"0 vw;cs;ts
1 return.
)

NB. =========================================================
NB. menu callback

onCreateOptionsMenu=: 3 : 0
jniCheck menu=. GetObjectArrayElement (3{y);0
(activity;menu;18!:5'') mkmenu_jamkmenu_ menus;ids;<strings
jniCheck DeleteLocalRef <menu
1
)

onOptionsItemSelected=: 3 : 0
jniCheck item=. GetObjectArrayElement (3{y);0
jniCheck id=. item ('getItemId ()I' jniMethod)~ ''
if. id = R_id_exit do.
  jniCheck activity ('finish ()V' jniMethod)~ ''
  jniCheck DeleteLocalRef <item
  1 return.
elseif. id>0 do.
  activity MakeToast_ja_ 'menu item: ', ids&id2name id
  jniCheck DeleteLocalRef <item
  1 return.
end.
activity MakeToast_ja_ 'menu id: ', ":id
jniCheck DeleteLocalRef <item
0
)

NB. =========================================================
NB. supercede onOptionsItemSelected

save_onClick=: 3 : 0
jniCheck item=. GetObjectArrayElement (3{y);0
jniCheck id=. item ('getItemId ()I' jniMethod)~ ''
activity MakeToast_ja_ 'save menu item: ', ids&id2name id
jniCheck DeleteLocalRef <item
1 return.
)

clear_onClick=: 3 : 0
activity MakeToast_ja_ 'clear menu item: '
1 return.
)

test''
