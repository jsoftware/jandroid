0 : 0
a few minutes on this interactive tour
 gives you a taste of J programming and
 JHS, a user interface to J

it covers the mechanics of programming
 with J/JHS and gives links to dive deeper

press ctrl+. or menu > to continue the tour
)

NB.spxhr:
0 : 0
experienced users can skim for recent changes:
 jijx menu>wiki>JHS>Release Notes
)

NB.spxhr:
0 : 0
read-eval-print loop
 this jijx page is your repl interface to J
)
+/1 2 3      NB. sum over - (Nota Bene) starts comment
a=: ?2 6$100 NB. a is random values from 2 by 6 array of 100s
a            NB. display a
+/a          NB. sum over columns (last dimension)
+/"1 a       NB. sum over rows (1st dimension)

NB.spxhr:
0 : 0
3 blanks prompt for your keyboard input
 and output is on the left

try a few inputs: e.g.,
   +/20 2 
   */20 2 3
)

NB.spxhr:
0 : 0
move the caret up to the line that starts
 with a=: and press enter to recall the line

press enter again to run the line again
)

NB.spxhr:
NB. type a and enter to display the new values in a

NB.spxhr:
0 : 0
recall lines with ctrl+shift+up arrow
 try it now to recall the a=: line
)

NB.spxhr:
0 : 0
definitions are defined in scripts
 scripts are text files with .ijs suffix
)

fin=: '~addons/ide/jhs/spx/overview_example.ijs'
jpath fin  NB.full path - ~addons expands to J addons folder
data=: fread fin NB. file read
data       NB. display data value
fout=: '~temp/overview.ijs'
jpath fout NB. ~temp expands to J temp folder file
data fwrite fout NB. data written to file

0 : 0
next step opens the script in a jijs edit page
 this is a pop-up (new tab/page)

JHS works best with authorization for pop-ups
 from localhost on port 65001

most browers show a warning at the top and 
 require your action to enable pop-ups

when the jijs page appears, if possible, drag
 it so you can see both the jijx jijs pages

focus jijx page and press ctrl+. to continue

jijx menu>wiki>JHS>Help>pop-up has more info
)

edit fout NB. open script file in jijs page

fcrsum a  NB. error as fcrsum is not defined

load fout NB. load script to define fcrsum
a         NB. display a
fcrsum a  NB. fcrsum adds row and col sums
fcrsum    NB. display fcrsum

0 : 0
name=: ... NB. global assignment
name=. ... NB. local assigment - only for current definition

the fcrsum sentence:
t=. y,+/y
assigns the local name t 
)

t=: 123
fcrsum a
t NB. global t is not changed by fcrsum local use

erase'fcrsum'
fcrsum a
load fout NB. load script again to define fcrsum
fcrsum a

fcrsum 'asdf' NB. text causes an error

0 : 0
many visulization tools are available
)

load'plot' NB. load plot package
plot fcrsum a

0 : 0
edit jijs page to add a new verb definition

jijs menu option>readonly to enable edits

copy fcrsum defn
 4 lines from fcrsum=: to closing ) line
 and paste at the end of the script

edit name of new defn to be fcrmax
 and change the 2 occurances of + to be >.
 >. is the J max verb
 
jijs menu action>save 
)

load fout NB. load script to define fcrmax 
a
fcrmax a  NB. max values for each col and row
fcrsum a

0 : 0
jijx menu wiki>NuVoc
 primary quick reference for J primitives

for info on >. primitive:
 go down the left col to > and then
 right to >. section and click Max
)

NB.spxhr:
0 : 0
although generic programming terms can be used
 J tends to use its own terms for clarity
 
    J       - generic
noun
verb
adverb/conjunction
)

NB.spxhr:
0 : 0
a typo or coding error can run a looong time

signal a break (ctrl+c) to take back control

ctrl+c in the jijx page does not signal break

you need to do ctrl+c in the terminal/console
 task that started JHS

find the terminal window (not a browser tab/page)
 that starts with the text:
J HTTP Server - init OK

a simple long running sentence is: sleep 20 seconds

it takes 2 breaks (ctrl+c) in the terminal window
 to break a sleep
 
the next advance will sleep with no output
 for 20 seconds unless you do ctrl+c twice
 in the terminal window
)
6!:3[20 NB. sleep for 20 seconds or until 2nd break

NB.spxhr:spx adf
0 : 0
there are several J books

jijx menu wiki>JHS
 press J in upper left to get to main page
 scan left column and click Beginners Books
)

NB.spxhr:
0 : 0
this is the end of this overview

explore jijx menu and run other tours
 
jijx menu>tour>plot
 
jijx menu>tour>labs
 the first 5 listed by running:
   lablist_jijx_ 'core'
 are interactive introductions to programming
 
run:  
   labrun_jijx_ 'Locales'
 to learn about locales - an important concept
 in advanced programming as they provide
 object-oriented and namespace faciliies

run:
   labrun_jijx_ 'Files'
 to learn about working with files   
)
