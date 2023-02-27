coclass'jni'
GetJNIENV=: 3 : 0
if. 'Android'-:UNAME do.
  try.
    'JNIVM_z_ JNIENV_z_'=: ; }. 'libj.so GetJavaVM i *x *x'&cd (,_1);,_1
    JNIENV_z_
  catch.
    0 return.
  end.
else.
  0
end.
)
JNIInvalidRefType=: 0
JNILocalRefType=: 1
JNIGlobalRefType=: 2
JNIWeakGlobalRefType=: 3
JNI_FALSE=: 0
JNI_TRUE=: 1
JNI_OK=: 0
JNI_ERR=: _1
JNI_EDETACHED=: _2
JNI_EVERSION=: _3
JNI_ENOMEM=: _4
JNI_EEXIST=: _5
JNI_EINVAL=: _6
JNI_COMMIT=: 1
JNI_ABORT=: 2

JNI_VERSION_1_1=: 16b00010001
JNI_VERSION_1_2=: 16b00010002
JNI_VERSION_1_4=: 16b00010004
JNI_VERSION_1_6=: 16b00010006
JNI_FUNCTION=: <;._2 ] 0 : 0
NULL
NULL
NULL
NULL
GetVersion                    i x
DefineClass                   x x *c x *c i
FindClass                     x x *c
FromReflectedMethod           x x x
FromReflectedField            x x x
ToReflectedMethod             x x x x c
GetSuperclass                 x x x
IsAssignableFrom              c x x x
ToReflectedField              x x x x c
Throw                         i x x
ThrowNew                      i x x *c
ExceptionOccurred             x x
ExceptionDescribe             n x
ExceptionClear                n x
FatalError                    n x *c
PushLocalFrame                i x i
PopLocalFrame                 x x x
NewGlobalRef                  x x x
DeleteGlobalRef               n x x
DeleteLocalRef                n x x
IsSameObject                  c x x x
NewLocalRef                   x x x
EnsureLocalCapacity           i x i
AllocObject                   x x x
NewObject                     x x x x
NewObjectV                    x x x x x
NewObjectA                    x x x x *x
GetObjectClass                x x x
IsInstanceOf                  c x x x
GetMethodID                   x x x *c *c
CallObjectMethod              x x x x
CallObjectMethodV             x x x x x
CallObjectMethodA             x x x x *x
CallBooleanMethod             c x x x
CallBooleanMethodV            c x x x x
CallBooleanMethodA            c x x x *x
CallByteMethod                c x x x
CallByteMethodV               c x x x x
CallByteMethodA               c x x x *x
CallCharMethod                w x x x
CallCharMethodV               w x x x x
CallCharMethodA               w x x x *x
CallShortMethod               s x x x
CallShortMethodV              s x x x x
CallShortMethodA              s x x x *x
CallIntMethod                 i x x x
CallIntMethodV                i x x x x
CallIntMethodA                i x x x *x
CallLongMethod                l x x x
CallLongMethodV               l x x x x
CallLongMethodA               l x x x *x
CallFloatMethod               f x x x
CallFloatMethodV              f x x x x
CallFloatMethodA              f x x x *x
CallDoubleMethod              d x x x
CallDoubleMethodV             d x x x x
CallDoubleMethodA             d x x x *x
CallVoidMethod                n x x x
CallVoidMethodV               n x x x x
CallVoidMethodA               n x x x *x
CallNonvirtualObjectMethod    x x x x x
CallNonvirtualObjectMethodV   x x x x x x
CallNonvirtualObjectMethodA   x x x x x *x
CallNonvirtualBooleanMethod   c x x x x
CallNonvirtualBooleanMethodV  c x x x x x
CallNonvirtualBooleanMethodA  c x x x x *x
CallNonvirtualByteMethod      c x x x x
CallNonvirtualByteMethodV     c x x x x x
CallNonvirtualByteMethodA     c x x x x *x
CallNonvirtualCharMethod      w x x x x
CallNonvirtualCharMethodV     w x x x x x
CallNonvirtualCharMethodA     w x x x x *x
CallNonvirtualShortMethod     s x x x x
CallNonvirtualShortMethodV    s x x x x x
CallNonvirtualShortMethodA    s x x x x *x
CallNonvirtualIntMethod       i x x x x
CallNonvirtualIntMethodV      i x x x x x
CallNonvirtualIntMethodA      i x x x x *x
CallNonvirtualLongMethod      l x x x x
CallNonvirtualLongMethodV     l x x x x x
CallNonvirtualLongMethodA     l x x x x *x
CallNonvirtualFloatMethod     f x x x x
CallNonvirtualFloatMethodV    f x x x x x
CallNonvirtualFloatMethodA    f x x x x *x
CallNonvirtualDoubleMethod    d x x x x
CallNonvirtualDoubleMethodV   d x x x x x
CallNonvirtualDoubleMethodA   d x x x x *x
CallNonvirtualVoidMethod      n x x x x
CallNonvirtualVoidMethodV     n x x x x x
CallNonvirtualVoidMethodA     n x x x x *x
GetFieldID                    x x x *c *c
GetObjectField                x x x x
GetBooleanField               c x x x
GetByteField                  c x x x
GetCharField                  w x x x
GetShortField                 s x x x
GetIntField                   i x x x
GetLongField                  l x x x
GetFloatField                 f x x x
GetDoubleField                d x x x
SetObjectField                n x x x x
SetBooleanField               n x x x c
SetByteField                  n x x x c
SetCharField                  n x x x w
SetShortField                 n x x x s
SetIntField                   n x x x i
SetLongField                  n x x x l
SetFloatField                 n x x x f
SetDoubleField                n x x x d
GetStaticMethodID             x x x *c *c
CallStaticObjectMethod        x x x x
CallStaticObjectMethodV       x x x x x
CallStaticObjectMethodA       x x x x *x
CallStaticBooleanMethod       c x x x
CallStaticBooleanMethodV      c x x x x
CallStaticBooleanMethodA      c x x x *x
CallStaticByteMethod          c x x x
CallStaticByteMethodV         c x x x x
CallStaticByteMethodA         c x x x *x
CallStaticCharMethod          w x x x
CallStaticCharMethodV         w x x x x
CallStaticCharMethodA         w x x x *x
CallStaticShortMethod         s x x x
CallStaticShortMethodV        s x x x x
CallStaticShortMethodA        s x x x *x
CallStaticIntMethod           i x x x
CallStaticIntMethodV          i x x x x
CallStaticIntMethodA          i x x x *x
CallStaticLongMethod          l x x x
CallStaticLongMethodV         x x x x
CallStaticLongMethodA         l x x x *x
CallStaticFloatMethod         f x x x
CallStaticFloatMethodV        f x x x x
CallStaticFloatMethodA        f x x x *x
CallStaticDoubleMethod        d x x x
CallStaticDoubleMethodV       d x x x x
CallStaticDoubleMethodA       d x x x *x
CallStaticVoidMethod          n x x x
CallStaticVoidMethodV         n x x x x
CallStaticVoidMethodA         n x x x *x
GetStaticFieldID              x x x *c *c
GetStaticObjectField          x x x x
GetStaticBooleanField         c x x x
GetStaticByteField            c x x x
GetStaticCharField            w x x x
GetStaticShortField           s x x x
GetStaticIntField             i x x x
GetStaticLongField            l x x x
GetStaticFloatField           f x x x
GetStaticDoubleField          d x x x
SetStaticObjectField          n x x x x
SetStaticBooleanField         n x x x c
SetStaticByteField            n x x x c
SetStaticCharField            n x x x w
SetStaticShortField           n x x x s
SetStaticIntField             n x x x i
SetStaticLongField            n x x x l
SetStaticFloatField           n x x x f
SetStaticDoubleField          n x x x d
NewString                     x x *w i
GetStringLength               i x x
GetStringChars                x x x *c
ReleaseStringChars            n x x *w
NewStringUTF                  x x *c
GetStringUTFLength            i x x
GetStringUTFChars             x x x *c
ReleaseStringUTFChars         n x x *c
GetArrayLength                i x x
NewObjectArray                x x i x x
GetObjectArrayElement         x x x i
SetObjectArrayElement         n x x i x
NewBooleanArray               x x i
NewByteArray                  x x i
NewCharArray                  x x i
NewShortArray                 x x i
NewIntArray                   x x i
NewLongArray                  x x i
NewFloatArray                 x x i
NewDoubleArray                x x i
GetBooleanArrayElements       x x x *c
GetByteArrayElements          x x x *c
GetCharArrayElements          x x x *c
GetShortArrayElements         x x x *c
GetIntArrayElements           x x x *c
GetLongArrayElements          x x x *c
GetFloatArrayElements         x x x *c
GetDoubleArrayElements        x x x *c
ReleaseBooleanArrayElements   n x x *c i
ReleaseByteArrayElements      n x x *c i
ReleaseCharArrayElements      n x x *w i
ReleaseShortArrayElements     n x x *s i
ReleaseIntArrayElements       n x x *i i
ReleaseLongArrayElements      n x x *l i
ReleaseFloatArrayElements     n x x *f i
ReleaseDoubleArrayElements    n x x *d i
GetBooleanArrayRegion         n x x i i *c
GetByteArrayRegion            n x x i i *c
GetCharArrayRegion            n x x i i *w
GetShortArrayRegion           n x x i i *s
GetIntArrayRegion             n x x i i *i
GetLongArrayRegion            n x x i i *l
GetFloatArrayRegion           n x x i i *f
GetDoubleArrayRegion          n x x i i *d
SetBooleanArrayRegion         n x x i i *c
SetByteArrayRegion            n x x i i *c
SetCharArrayRegion            n x x i i *w
SetShortArrayRegion           n x x i i *s
SetIntArrayRegion             n x x i i *i
SetLongArrayRegion            n x x i i *l
SetFloatArrayRegion           n x x i i *f
SetDoubleArrayRegion          n x x i i *d
RegisterNatives               i x x x i
UnregisterNatives             i x x
MonitorEnter                  i x x
MonitorExit                   i x x
GetJavaVM                     i x *x
GetStringRegion               n x x i i *w
GetStringUTFRegion            n x x i i *c
GetPrimitiveArrayCritical     x x x *c
ReleasePrimitiveArrayCritical n x x x i
GetStringCritical             x x x *c
ReleaseStringCritical         n x x *w
NewWeakGlobalRef              x x x
DeleteWeakGlobalRef           n x x
ExceptionCheck                c x
NewDirectByteBuffer           x x x l
GetDirectBufferAddress        x x x
GetDirectBufferCapacity       l x x
GetObjectRefType              x x x
)

3 : 0''
(('ID_'&,)@:(-.&' ')@:(30&{.)&.> JNI_FUNCTION)=: i.#JNI_FUNCTION
func=: (-.&' ')@:(30&{.)&.> JNI_FUNCTION
func_sig=: dtb@:(30&}.)&.> JNI_FUNCTION
opt=. IFUNIX{::' + ';' '
". 4}. (>func),"1 ('=: 3 : (''''''1 ',"1 (":,.i.#JNI_FUNCTION),"1 opt,"1 (>func_sig),"1 '''''&(15!:0) (<JNIENV), y''',"1 ';'':'';',"1 '''''''1 ',"1 (":,.i.#JNI_FUNCTION),"1 opt,"1 (>func_sig),"1 '''''&(15!:0) (<x), y'')')
EMPTY
)
JNIVM_FUNCTION=: <;._2 ] 0 : 0
NULL
NULL
NULL
DestroyJavaVM                 i x
AttachCurrentThread           i x *x x
DetachCurrentThread           i x
GetEnv                        i x *x i
AttachCurrentThreadAsDaemon   i x *x x
)

3 : 0''
(('ID_'&,)@:(-.&' ')@:(30&{.)&.> JNIVM_FUNCTION)=: i.#JNIVM_FUNCTION
func2=: (-.&' ')@:(30&{.)&.> JNIVM_FUNCTION
func2_sig=: dtb@:(30&}.)&.> JNIVM_FUNCTION
opt=. IFUNIX{::' + ';' '
". 3}. (>func2),"1 ('=: 3 : (''''''1 ',"1 (":,.i.#JNIVM_FUNCTION),"1 opt,"1 (>func2_sig),"1 '''''&(15!:0) (<JNIVM), y''',"1 ';'':'';',"1 '''''''1 ',"1 (":,.i.#JNIVM_FUNCTION),"1 opt,"1 (>func2_sig),"1 '''''&(15!:0) (<x), y'')')
EMPTY
)
3 : 0''
if. 0 -: jvm=. 2!:5 'LIBJVM_PATH' do. jvm=. '' end.
if. #jvm do. jvm=. jvm, (-.({:jvm)e.'/\')#'/' end.
if. 0 -: jlib=. 2!:5 'JAVALD_PATH' do. jlib=. '' end.
if. #jlib do.
  if. IFUNIX do.
    ((unxlib 'c'),' setenv > i *c *c i')&(15!:0) (('Darwin'-:UNAME){::'LD_LIBRARY_PATH';'DYLD_LIBRARY_PATH');jlib;1
  else.
    'kernel32 SetDllDirectoryW > x *w'cd <uucp jlib
  end.
end.
libjvm=. jvm, (('Darwin'-:UNAME) + IFUNIX){::'jvm.dll';'libjvm.so';'libjvm.dylib'
opt=. IFUNIX{::' + ';' '
JNI_GetDefaultJavaVMInitArgs=: ('"', libjvm, '" JNI_GetDefaultJavaVMInitArgs', opt, 'i x')&(15!:0)
JNI_CreateJavaVM=: ('"', libjvm, '" JNI_CreateJavaVM', opt, 'i *x *x x')&(15!:0)
JNI_GetCreatedJavaVMs=: ('"', libjvm, '" JNI_GetCreatedJavaVMs', opt, 'i *x i *i')&(15!:0)
JNI_OnLoad=: ('"', libjvm, '" JNI_OnLoad', opt, 'i x x')&(15!:0)
JNI_OnUnload=: ('"', libjvm, '" JNI_OnUnload', opt, 'n x x')&(15!:0)
EMPTY
)
CreateJavaVM=: 3 : 0
if. 0= #y=. boxxopen y do.
  args=. '' [ version=. 4
else.
  version=. >{.y [ args=. }.y
end.
assert. version e. 1 2 4 6
noptions=. #args
version=. ". 'JNI_VERSION_1_', ":version
SZI=. IF64{4 8
initargs=. mema IF64{16 24
if. noptions do.
  options=. mema noptions * IF64{8 16
  pargs=. 15!:14 <'args'
  for_p. memr pargs,0,noptions,4 do.
    (p+memr p,0 1 4) memw (options + p_index*IF64{8 16),0 1 4
  end.
end.
(2 ic version) memw initargs,0 4 2
(2 ic noptions) memw initargs,4 4 2
if. noptions do. options memw initargs,8 1 4 end.
(0{a.) memw initargs,(IF64{12 16),1,2
vm=. ,2-2
env=. ,2-2
'rc vm env'=. 3{. JNI_CreateJavaVM vm;env;initargs
if. noptions do. memf options end.
memf initargs
(0>rc){::(vm,env);0 0
)
JNI_IMPORT=: (<;._1);._2 (0 : 0)
 Boolean Ljava/lang/Boolean
 Btye Ljava/lang/Btye
 Char Ljava/lang/Char
 Class Ljava/lang/Class
 CharSequence Ljava/lang/CharSequence
 Double Ljava/lang/Double
 Float Ljava/lang/Float
 Integer Ljava/lang/Integer
 Long Ljava/lang/Long
 Object Ljava/lang/Object
 Short Ljava/lang/Short
 String Ljava/lang/String
)

jniImport=: 3 : 0
if. (<'jni') -: 18!:5'' do. EMPTY return. end.
if. 0~:4!:0<'JNI_IMPORT' do. JNI_IMPORT=: JNI_IMPORT_jni_ end.
for_b. <;._2 y do.
  d=. }. (}.~ i:&'/') c=. './' charsub >b
  if. (<d) e. {."1 JNI_IMPORT do. continue. end.
  JNI_IMPORT=: JNI_IMPORT, d;'L',c
end.
EMPTY
)
jniResolve=: 0&$: : (4 : 0)
if. 'L'~:{.y do. y return. end.
if. 0~:4!:0<'JNI_IMPORT' do. }.^:x y return. end.
if. 0=#JNI_IMPORT do. }.^:x y return. end.
if. 1 e. './' e. y do. }.^:x y return. end.
y1=. }:^:(';'={:) }.y
a=. {."1 JNI_IMPORT
if. (i=. a i. <y1) < #a do.
  }.^:x ((';'={:y)#';'),~ (<i,1){::JNI_IMPORT
else.
  }.^:x y
end.
)
va_list=: 3 : 0
(3!:0 &> y) va_list y
:
assert. (#x)=#y [ 'va_list_jni'
z=. ''
for_i. i.#x do.
  select. i{x
  case. 1 do. z=. z, (IF64{2 3)&ic i{::y
  case. 2 do. z=. z, i{::y
  case. 4 do. z=. z, (IF64{2 3)&ic i{::y
  case. 8 do. z=. z, 2&fc i{::y
  case. 16 do. z=. z, 2&fc +. i{::y
  case. do. assert. 0 [ 'va_list_jni'
  end.
end.
z
)
jniVararg=: 1 : 0
:
opt=. IFUNIX{::' + ';' '
((":1,m), opt, x)&(15!:0) (<JNIENV), y
)
jniVarargs=: 2 : 0
:
opt=. IFUNIX{::' + ';' '
((":1,m), opt, x)&(15!:0) (<n), y
)
jniSigniture=: 3 : 0
if. '#' e. y=. deb y do. y return. end.
if. '('={.y do.
  w1=. }. ({.~ i.&')') y
  w2=. }. (}.~ i.&')') y
  '(', (jniSigniture w1), ')', jniSigniture w2 return.
end.
z=. ''
i=. 0 [ ar=. 0
while. i<#y do.
  if. '[' = (tp=. i{y) do.
    i=. >:i [ ar=. 1 continue.
  else.
    if. tp e. 'BSIJFDZCV' do.
      z=. z, '#', (ar#'['), i{y
      i=. >:i [ ar=. 0
    else.
      assert. 'L'=tp [ 'jniSigniture'
      if. (#k) = j=. ';' i.~ k=. i}.y do.
        z=. z, '#', (ar#'['), (jniResolve k), ';' break.
      else.
        z=. z, '#', (ar#'['), (jniResolve j{.k), ';'
        i=. i + j + 1 [ ar=. 0
      end.
    end.
  end.
end.
z
)
jniSigx15=: 1&$: : (4 : 0)
z=. ''
for_t. <;._1 y do.
  t1=. >t
  if. ar=. '['={.t1 do.
    t1=. }.t1
    if. (t2=. {.t1) e. 'BSIJFDZC' do.
      z=. z, ' x'
    else.
      z=. z, ' x'
    end.
  else.
    if. (t2=. {.t1) e. 'BSIJFDZC' do.
      z=. z, ' ', ('BSIJFDZC' i. t2){ x{::'csilfdic';'ciilddic'
    else.
      z=. z, ' x'
    end.
  end.
end.
}.z
)
jniSigarg=: 4 : 0
y=. boxxopen y
assert. (#y) = +/ '#'=x [ 'jniSigarg'
z=. 0$<''
for_t. <;._1 x do.
  a1=. t_index{::y
  t1=. >t
  if. ar=. '['={.t1 do.
    assert. 1 4 e.~ 3!:0 a1 [ 'jniSigarg array not object'
    z=. z, < a1
  else.
    a1=. {.a1
    if. (t2=. {.t1) e. 'BZ' do.
      if. 2=3!:0 a1 do.
        z=. z, < a.i.a1
      else.
        assert. 1 4 e.~ 3!:0 a1 [ 'jniSigarg'
        z=. z, < a1
      end.
    elseif. t2 e. 'C' do.
      assert. 2 131072 262144 e.~ 3!:0 a1 [ 'jniSigarg'
      z=. z, <{. uucp a1
    elseif. t2 e. 'SIJFD' do.
      assert. 1 4 8 e.~ 3!:0 a1 [ 'jniSigarg'
      z=. z, <a1
    elseif. t2 e. 'L' do.
      assert. 1 4 32 e.~ 3!:0 a1 [ 'jniSigarg'
      z=. z, <>a1
    elseif. do.
      assert. 0 [ 'jniSigarg'
    end.
  end.
end.
z
)
jniCheck=: 3 : 0
]`('JNI exception' (13!:8) 3:)@.(0 ~: a. i. >@{.@ExceptionCheck_jni_@(''"_)) y
:
]`('JNI exception' (13!:8) 3:)@.(0 ~: a. i. (x&(>@{.@ExceptionCheck_jni_)@(''"_)) y
)
jniToJString=: 3 : 0
if. 0=y do. '' return. end.
jniCheck str=. >@{. GetStringUTFChars y;<<0
z=. utf8@:ucp memr str,0,_1
jniCheck ReleaseStringUTFChars y;<<str
z
)
jniFromObjarr=: 3 : 0
jniCheck >@{.@GetObjectArrayElement"1 y;"0 i. GetArrayLength <y
)
jniToObjarr=: 3 : 0
jniCheck clz=. >@{. FindClass <'java/lang/Object'
jniCheck z=. >@{. NewObjectArray (#y);clz;0
jniCheck DeleteLocalRef <clz
for_i. i.#y do.
  jniCheck SetObjectArrayElement z;i;i{y
end.
z
)
jniToStringarr=: 3 : 0
jniCheck clz=. >@{. FindClass <'java/lang/String'
jniCheck z=. >@{. NewObjectArray (#y);clz;0
jniCheck DeleteLocalRef <clz
for_i. i.#y do.
  jniCheck SetObjectArrayElement z;i;s=. >@{. NewStringUTF utf8@:(3&u:)@:ucp@,&.> i{y
  jniCheck DeleteLocalRef <s
end.
z
)
jniException=: 3 : 0
try.
  ExceptionClear''
  jniCheck cls=. >@{. GetObjectClass <y
  jniCheck mid=. >@{. GetMethodID cls;'getMessage';'()Ljava/lang/String;'
  jniCheck jstr=. >@{. 'x x x x' (ID_CallObjectMethod jniVararg) y ; mid
  z=. jniToJString jstr
  jniCheck DeleteLocalRef <jstr
  jniCheck DeleteLocalRef <cls
  jniCheck DeleteLocalRef <y
  ExceptionClear''
  z
catch.
  jniCheck DeleteLocalRef <y
  ExceptionClear''
  '' return.
end.
)
jniClassName=: 0&$: : (4 : 0)
try.
  jniCheck ccls=. >@{. FindClass <'java/lang/Class'
  jniCheck mid=. >@{. GetMethodID ccls;'getName';'()Ljava/lang/String;'
  if. 0=x do.
    jniCheck ecls=. >@{. GetObjectClass <y
  else.
    ecls=. y
  end.
  jniCheck jstr=. >@{. 'x x x x' (ID_CallObjectMethod jniVararg) ecls ; mid
  z=. jniToJString jstr
  if. 0=x do. DeleteLocalRef <ecls end.
  jniCheck DeleteLocalRef <jstr
  jniCheck DeleteLocalRef <ccls
  ExceptionClear''
  z
catch.
  ExceptionClear''
  '' return.
end.
)
jniField=: 1 : 0
'field sig'=. <;._1 ' ', deb m
rt=. field,' ',jniSigniture sig
assert. 1 4 e.~ 3!:0 y [ 'jniField'
assert. 0-.@-:y [ 'jniField'
jniCheck cls=. >@{. GetObjectClass_jni_ <y
z=. rt jniCallField_jni_ 0;y;cls
jniCheck DeleteLocalRef_jni_ <cls
z
:
'field sig'=. <;._1 ' ', deb m
rt=. field,' ',jniSigniture sig
assert. 1 4 e.~ 3!:0 y [ 'jniField'
assert. 0-.@-:y [ 'jniField'
jniCheck cls=. >@{. GetObjectClass_jni_ <y
rt jniCallField_jni_ (0;y;cls), boxxopen x
jniCheck DeleteLocalRef_jni_ <cls
EMPTY
)
jniStaticField=: 1 : 0
'field sig'=. <;._1 ' ', deb m
rt=. field,' ',jniSigniture sig
newcls=. 1
if. 32 e.~ 3!:0 y do.
  assert. 1 4 e.~ 3!:0 >y [ 'jniStaticField'
  newcls=. 0
  cls=. {.>y
elseif. 1 4 e.~ 3!:0 y do.
  assert. 0-.@-:y [ 'jniStaticField'
  jniCheck cls=. >@{. GetObjectClass_jni_ <y
elseif. do.
  assert. ''-.@-:y [ 'jniStaticField'
  class=. './' charsub y -. ';'
  class=. 1&jniResolve 'L',class
  jniCheck cls=. >@{. FindClass_jni_ <class
end.
z=. rt jniCallField_jni_ 1;y;cls
if. newcls do.
  jniCheck DeleteLocalRef_jni_ <cls
end.
z
:
'field sig'=. <;._1 ' ', deb m
rt=. field,' ',jniSigniture sig
newcls=. 1
if. 32 e.~ 3!:0 y do.
  assert. 1 4 e.~ 3!:0 >y [ 'jniStaticField'
  newcls=. 0
  cls=. {.>y
elseif. 1 4 e.~ 3!:0 y do.
  assert. 0-.@-:y [ 'jniStaticField'
  jniCheck cls=. >@{. GetObjectClass_jni_ <y
elseif. do.
  assert. ''-.@-:y [ 'jniStaticField'
  class=. './' charsub y -. ';'
  class=. 1&jniResolve 'L',class
  jniCheck cls=. >@{. FindClass_jni_ <class
end.
rt jniCallField_jni_ (1;y;cls), boxxopen x
if. newcls do.
  jniCheck DeleteLocalRef_jni_ <cls
end.
EMPTY
)

jniCallField=: 4 : 0
'field sig'=. <;._1 ' ', deb x
'attr obj cls'=. 3{.y
static=. {.attr
y=. 3}.y

assert. 1=+/'#'=sig [ 'jniCallField'
rt=. sig-.'#'
jniCheck mid=. >@{. GetFieldID`GetStaticFieldID@.static cls;field;({.a.),~ rt
if. static do. obj=. cls end.
if. 0=#y do.
  if. '[' e. rt do.
    jniCheck rc=. >@{. ('x x x x ') ((static{ID_GetObjectField,ID_GetStaticObjectField) jniVararg) (obj ; mid)
  else.
    select. {.rt
    case. 'V' do. jniCheck rc=. >@{. ('x x x x ') ((static{ID_GetVoidField,ID_GetStaticVoidField) jniVararg) (obj ; mid)
    case. 'L' do. jniCheck rc=. >@{. ('x x x x ') ((static{ID_GetObjectField,ID_GetStaticObjectField) jniVararg) (obj ; mid)
    case. 'B' do. jniCheck rc=. >@{. ('c x x x ') ((static{ID_GetByteField,ID_GetStaticByteField) jniVararg) (obj ; mid)
    case. 'Z' do. jniCheck rc=. >@{. ('i x x x ') ((static{ID_GetBooleanField,ID_GetStaticBooleanField) jniVararg) (obj ; mid)
    case. 'I' do. jniCheck rc=. >@{. ('i x x x ') ((static{ID_GetIntField,ID_GetStaticIntField) jniVararg) (obj ; mid)
    case. 'J' do. jniCheck rc=. >@{. ('l x x x ') ((static{ID_GetLongField,ID_GetStaticLongField) jniVararg) (obj ; mid)
    case. 'S' do. jniCheck rc=. >@{. ('s x x x ') ((static{ID_GetShortField,ID_GetStaticShortField) jniVararg) (obj ; mid)
    case. 'F' do. jniCheck rc=. >@{. ('f x x x ') ((static{ID_GetFloatField,ID_GetStaticFloatField) jniVararg) (obj ; mid)
    case. 'D' do. jniCheck rc=. >@{. ('d x x x ') ((static{ID_GetDoubleField,ID_GetStaticDoubleField) jniVararg) (obj ; mid)
    case. 'C' do. jniCheck rc=. >@{. ('w x x x ') ((static{ID_GetCharField,ID_GetStaticCharField) jniVararg) (obj ; mid)
    case. do. assert. 0 [ 'jniCallField return type'
    end.
  end.
else.
  if. '[' e. rt do.
    jniCheck rc=. >@{. ('x x x x ', jniSigx15 sig) ((static{ID_SetObjectField,ID_SetStaticObjectField) jniVararg) (obj ; mid), sig jniSigarg y
  else.
    select. {.rt
    case. 'V' do. jniCheck rc=. >@{. ('x x x x ', jniSigx15 sig) ((static{ID_SetVoidField,ID_SetStaticVoidField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'L' do. jniCheck rc=. >@{. ('x x x x ', jniSigx15 sig) ((static{ID_SetObjectField,ID_SetStaticObjectField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'B' do. jniCheck rc=. >@{. ('c x x x ', jniSigx15 sig) ((static{ID_SetByteField,ID_SetStaticByteField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'Z' do. jniCheck rc=. >@{. ('i x x x ', jniSigx15 sig) ((static{ID_SetBooleanField,ID_SetStaticBooleanField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'I' do. jniCheck rc=. >@{. ('i x x x ', jniSigx15 sig) ((static{ID_SetIntField,ID_SetStaticIntField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'J' do. jniCheck rc=. >@{. ('l x x x ', jniSigx15 sig) ((static{ID_SetLongField,ID_SetStaticLongField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'S' do. jniCheck rc=. >@{. ('s x x x ', jniSigx15 sig) ((static{ID_SetShortField,ID_SetStaticShortField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'F' do. jniCheck rc=. >@{. ('f x x x ', jniSigx15 sig) ((static{ID_SetFloatField,ID_SetStaticFloatField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'D' do. jniCheck rc=. >@{. ('d x x x ', jniSigx15 sig) ((static{ID_SetDoubleField,ID_SetStaticDoubleField) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'C' do. jniCheck rc=. >@{. ('w x x x ', jniSigx15 sig) ((static{ID_SetCharField,ID_SetStaticCharField) jniVararg) (obj ; mid), sig jniSigarg y
    case. do. assert. 0 [ 'jniCallField return type'
    end.
  end.
end.
rc
)
jniMethod=: 1 : 0
:
if. (0 4 -.@e.~ 3!:0 y) +. 0-:y do.
  smoutput 'method signiture: ',m
end.
assert. 0 4 e.~ 3!:0 y [ 'jniMethod'
assert. 0-.@-:y [ 'jniMethod'
jniCheck cls=. >@{. GetObjectClass_jni_ <y
assert. 0~:cls [ 'jniMethod'

'method proto'=. <;._1 ' ', deb m
proto=. jniSigniture proto
sig=. }. ({.~ i.&')') proto
rt=. }. (}.~ i.&')') proto
m=. method,' ',proto

rc=. m jniCall_jni_ (boxxopen x),~ 2 0 0;y;cls
jniCheck DeleteLocalRef_jni_ <cls
rc
)
jniStaticMethod=: 1 : 0
:
if. 1 4 e.~ 3!:0 y do.
  assert. 0-.@-:y [ 'jniStaticMethod'
  jniCheck cls=. >@{. GetObjectClass_jni_ <y
else.
  assert. ''-.@-:y [ 'jniStaticMethod'
  class=. './' charsub }.^:('L'={.) y -. ';'
  jniCheck cls=. >@{. FindClass_jni_ <class
end.
assert. 0~:cls [ 'jniStaticMethod'

'method proto'=. <;._1 ' ', deb m
proto=. jniSigniture proto
sig=. }. ({.~ i.&')') proto
rt=. }. (}.~ i.&')') proto
m=. method,' ',proto

rc=. m jniCall_jni_ (boxxopen x),~ 2 1 0;y;cls
jniCheck DeleteLocalRef_jni_ <cls
rc
)
jniCall=: 4 : 0
'method proto'=. <;._1 ' ', deb x
sig=. }. ({.~ i.&')') proto
rt=. '#'-.~ }. (}.~ i.&')') proto
'attr obj cls'=. 3{.y
'member static nonvirtual'=. attr
y=. 3}.y
if. -. (+/'#'=sig) = #y do. smoutput 'jniCall : ',x end.
assert. (+/'#'=sig) = #y [ 'jniCall incorrect number of arguments'

str=. stri=. 0$0
jniCheck mid=. >@{. GetMethodID`GetStaticMethodID@.static cls;method;({.a.),~ proto-.'#'
if. 1 e. s1=. ((<'Ljava/lang/CharSequence;') = sig1) +. (<'Ljava/lang/String;') = sig1=. <;._1 sig do.
  for_i. I. s1 do.
    if. 2 131072 262144 e.~ 3!:0 y1=. i{::y do.
      str=. str, < >@{. NewStringUTF < utf8@:(3&u:)@:ucp ,y1
      stri=. stri, i
    end.
  end.
  if. #stri do. y=. str stri}y end.
end.
if. '<init>'-:method do.
  jniCheck rc=. >@{. ('x x x x ', jniSigx15 sig) (ID_NewObject jniVararg) (cls ; mid), sig jniSigarg y
else.
  if. static do. obj=. cls end.
  if. '[' e. rt do.
    jniCheck rc=. >@{. ('x x x x ', jniSigx15 sig) ((static{ID_CallObjectMethod,ID_CallStaticObjectMethod) jniVararg) (obj ; mid), sig jniSigarg y
  else.
    select. {.rt
    case. 'V' do. jniCheck rc=. >@{. ('x x x x ', jniSigx15 sig) ((static{ID_CallVoidMethod,ID_CallStaticVoidMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'L' do. jniCheck rc=. >@{. ('x x x x ', jniSigx15 sig) ((static{ID_CallObjectMethod,ID_CallStaticObjectMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'B' do. jniCheck rc=. >@{. ('c x x x ', jniSigx15 sig) ((static{ID_CallByteMethod,ID_CallStaticByteMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'Z' do. jniCheck rc=. >@{. ('i x x x ', jniSigx15 sig) ((static{ID_CallBooleanMethod,ID_CallStaticBooleanMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'I' do. jniCheck rc=. >@{. ('i x x x ', jniSigx15 sig) ((static{ID_CallIntMethod,ID_CallStaticIntMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'J' do. jniCheck rc=. >@{. ('x x x x ', jniSigx15 sig) ((static{ID_CallLongMethod,ID_CallStaticLongMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'S' do. jniCheck rc=. >@{. ('s x x x ', jniSigx15 sig) ((static{ID_CallShortMethod,ID_CallStaticShortMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'F' do. jniCheck rc=. >@{. ('f x x x ', jniSigx15 sig) ((static{ID_CallFloatMethod,ID_CallStaticFloatMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'D' do. jniCheck rc=. >@{. ('d x x x ', jniSigx15 sig) ((static{ID_CallDoubleMethod,ID_CallStaticDoubleMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. 'C' do. jniCheck rc=. >@{. ('w x x x ', jniSigx15 sig) ((static{ID_CallCharMethod,ID_CallStaticCharMethod) jniVararg) (obj ; mid), sig jniSigarg y
    case. do. assert. 0 [ 'jniCall return type'
    end.
  end.
end.
for_js. str do. DeleteLocalRef js end.
rc
)
jniNewObject=: 4 : 0
if. ' ' e. y do.
  'class sig'=. <;._1 ' ', deb y
else.
  class=. y [ sig=. '()V'
end.
if. ')' -.@e. sig do. sig=. '(', ')V',~ sig end.
sig=. jniSigniture sig
class=. './' charsub class -. ';'
class=. 1&jniResolve 'L',class
jniCheck cls=. >@{. FindClass_jni_ <class
assert. 0~:cls [ 'jniNewObjet'
rc=. ('<init> ',sig) jniCall_jni_ (boxxopen x),~ 1 0 0;0;cls
jniCheck DeleteLocalRef <cls
rc
)
jniOverride=: 4 : 0
x=. boxxopen x
assert. 32=3!:0 y [ 'jniOverride: y must be box'
assert. 2 3 4 e.~ #y [ 'jniOverride: y must be class_signiture ; locale [; childid [; override]]'
if. 0=#x do.
  class=. >@{.y
  sig=. 'Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;'
else.
  'class sig'=. <;._1 ' ', >@{.y
  if. ')' e. sig do.
    sig=. '(' -.~ ({.~ i.&')') sig
  end.
  sig=. sig, 'Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;'
end.
if. ')' -.@e. sig do. sig=. '(',sig,')V' end.
class=. './' charsub }.^:('L'={.) class -. ';'

obj=. ((3{.}.y),~ x) jniNewObject class, ' ', sig
)
jniProxy=: 4 : 0
cls=. >@{. GetObjectClass <x
mid=. >@{. GetMethodID cls;'CreateProxy';'(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;'
s1=. >@{. NewStringUTF utf8@:(3&u:)@:ucp , {.y
s2=. >@{. NewStringUTF utf8@:(3&u:)@:ucp , {:y
s=. >@{. 'x x x x x x' (ID_CallObjectMethod jniVararg) x;mid;s1;s2
jniCheck DeleteLocalRef <s1
jniCheck DeleteLocalRef <s2
jniCheck DeleteLocalRef <cls
s
)
jnhandler_z_=: 4 : 0
if. 3=4!:0<'jnhandler_debug' do.
  try. x jnhandler_debug y catch. end.
  EMPTY return.
end.
jn_fn=. x
if. 3~:(4!:0) <jn_fn do. EMPTY return. end.
if. 13!:17'' do.
  z=. jn_fn~ y
else.
  try. z=. jn_fn~ y
  catch.
    if. 0~:exc=. ExceptionOccurred_jni_'' do.
      if. ''-:jn_err=. jniException_jni_ exc do.
        jn_err=. 'JNI exception'
      end.
      jn_err=. '|', jn_err, LF
    else.
      jn_err=. 13!:12''
    end.
    if. 0=4!:0 <'ERM_j_' do.
      jn_erm=. ERM_j_
      ERM_j_=: ''
      if. jn_erm -: jn_err do. 0 return. end.
    end.
    jn_err=. LF,,LF,.}.;._2 jn_err
    smoutput 'jnhandler error in: ',jn_fn,jn_err
    0
  end.
end.
)

