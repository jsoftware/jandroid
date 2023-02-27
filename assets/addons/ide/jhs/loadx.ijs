0 : 0
vscode extension ctrl+; shortcut saves file and appends name to ~loadx.txt file

J session runs: loadx 0 to load scripts as indicated by the editor

JHS ctrl+; shortcut runs: loadx__ 0
)

loadx_z_=: 3 : 0
f=. '~/loadx.txt'
d=. <;._2 fread f
''fwrite f 
d=. ~.d 
b=. (<'.ijs')=_4{.each d
if. +/-.b do. echo LF,~;(d#~-.b),each LF end.
d=. b#d 
if. 0=#d do. echo 'no scripts to load' return. end.
for_n. d do.
 n=. >n
 p=. (10{.(>:n i:'/')}.n),' ',n
 try.
  load n
  echo p 
catch. echo LF,LF,~p,LF,13!:12'' end.
end.
i.0 0
)

NB. gitbuild 'data/jmf'
gitbuild_z_=: 3 : 0
require'~system/util/project.ijs' NB. writesourcex
load'~Addons/',y,'/source/build.ijs'
)

