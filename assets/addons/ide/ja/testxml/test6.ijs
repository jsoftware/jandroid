NB. create view and image

load 'gui/android'

coclass 'test'
coinsert 'jni jaresu'

jniImport ::0: (0 : 0)
android.content.Context
android.view.View
android.widget.ArrayAdapter
android.widget.ListAdapter
android.widget.SpinnerAdapter
)


test=: 3 : 0
'rc layouts lid'=: 1 expat_parse_xml_jalayout_ 1!:1 <jpath '~Addons/gui/android/test/res/layout/layout1.xml'
if. rc do. smoutput layouts;lid end.
assert. 0=rc
((<'R_id_') ,&.> ids)=: >:@i.#ids=: lid
assert. (#@~. = #) ids  NB. unique id names
StartActivity_ja_ (>18!:5'');'onDestroy'
)

NB. =========================================================
NB. android activity callback

onStart=: 3 : 0
activity=: 2{y   NB. context/activity
draws=: crunch_jadrawable_ jpath '~Addons/gui/android/test/res'
vw=. (activity;18!:5'') mkview_jamkview_ layouts;ids;'';<draws

setadapter activity,vw

jniCheck activity ('setContentView (LView;)V' jniMethod)~ vw

jniCheck DeleteLocalRef <vw
0
)

onDestroy=: 3 : 0
jniCheck DeleteGlobalRef <activity
if. #draws do. jniCheck DeleteGlobalRef"0 {:"1 draws end.
0
)

NB. content fo listview and spinner
planets=: <;._2 (0 : 0)
Mercury
Venus
Earth
Mars
Jupiter
Saturn
Uranus
Neptune
)

NB. fill content for listview and spinner
setadapter=: 3 : 0
'activity view'=. y
ar=. jniToStringarr planets
jniCheck adapter1=. 'ArrayAdapter LContext;I[LObject;' jniNewObject~ activity ; R_layout_simple_list_item_1 ; ar
jniCheck adapter2=. 'ArrayAdapter LContext;I[LObject;' jniNewObject~ activity ; R_layout_simple_spinner_item ; ar

jniCheck listview=. view ('findViewById (I)LView;' jniMethod)~ R_id_listview
jniCheck listview ('setAdapter (LListAdapter;)V' jniMethod)~ adapter1

jniCheck spinner=. view ('findViewById (I)LView;' jniMethod)~ R_id_spinner
jniCheck adapter2 ('setDropDownViewResource (I)V' jniMethod)~ R_layout_simple_dropdown_item_1line
jniCheck spinner ('setAdapter (LSpinnerAdapter;)V' jniMethod)~ adapter2
jniCheck DeleteLocalRef"0 ar;adapter1;adapter2
EMPTY
)

test''
