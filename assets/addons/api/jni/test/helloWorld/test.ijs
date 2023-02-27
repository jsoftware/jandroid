load 'api/jni'

coinsert 'jni'

NB. first export env variable LIBJVM_PATH to point the path containing libjvm.so, eg.
NB. export LIBJVM_PATH=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server

test1=: 3 : 0
'vm env'=. CreateJavaVM 6;'-Djava.class.path=',jpath '~addons/api/jni/test/helloWorld'
assert. 0~:env
JNIENV_z_=: env
number=. 20
exponent=. 3
jniCheck 'helloWorld' ('main ([Ljava/lang/String;)V' jniStaticMethod)~ <0
jniCheck echo 'helloWorld' ('square (I)I' jniStaticMethod)~ <number
jniCheck echo 'helloWorld' ('power (II)I' jniStaticMethod)~ number;exponent
EMPTY
)

test1 ''
