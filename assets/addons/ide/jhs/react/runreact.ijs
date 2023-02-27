coclass'jhs'

NB. create ~temp folder with all files from src/dist/
NB.  and then all files (possibly replacing) from src/adj/
runreact=: 3 : 0
name=. y
'does not exist'assert fexist'~addons/ide/jhs/react/',y,'/addj/',name,'.ijs'
src=: '~addons/ide/jhs/react/',name,'/'
snk=: '~temp/jhs/react/',name,'/'

mkdir_j_ snk

n=. {."1 [1!:0 <jpath src,'dist/*'
(fread each (<src,'dist/'),each n) fwrite each (<snk),each n

n=. {."1 [1!:0 <jpath src,'addj/*'
(fread each (<src,'addj/'),each n) fwrite each (<snk),each n
(fread src,'addj/',name,'.js') fwrite snk,'script.js'

echo'   load''~temp/jhs/react/',name,'/',name,'.ijs'''
echo'   ''',name,';10 10 500 200'' jpage '''''
i.0 0
)

