coclass'jdoc'
coinsert'jhs'

HBS=: 0 : 0
jhclose''
'title'jhtitle'JHS framework'
'jhs locale - utils - verbs/nouns/... for working with JHS'
jhbr
mfix    '';'general utils';'~addons/ide/jhs/util.ijs'
mfix'html';'html elements';'~addons/ide/jhs/utilh.ijs'
mfix  'js';'javascript utils';'~addons/ide/jhs/utiljs.ijs'
)

CSS=: 0 : 0
form{margin:10px;}
div{font-family:<PC_FONTFIXED>;white-space:pre;padding-left:10px}
)

intro=: 0 : 0
An app (browser page using a JHS server) is started by:
   'appname' jpage args
or
   browse to url JHS-server:port/appname

A new temp (numbered) locale is created for the browser page.

Script that defines locale appname must be run first.

An app is defined by a script that defines nouns HBS, CSS, JS and event handler verbs.

HBS is a set of J lines that create html and the result of each line is added together
to create the html for the page.

HBS line: 'id' jhb 'click me'
runs in the jhs locale and you can see the html by running:
   'id' jhb_jhs_ 'click me'

You can see the entire html result with:
    gethbs_jhs_'...' NB. where ... is the locale wtih the app defintions

You do not need to do any javascript programming to build an app. Advanced apps can benefit from
javascript programming, but that is beyond the scope of this document.

This document covers apps that do not require any javascript programming.

Event handler example:
  user clicks button with id 'one'
   -> browser calls j verb ev_one_click
    -> ev_one_click calls jhrcmds with list of browser commands
     -> browser runs commands

best practice for building JHS app (page)
page is numbered locale with class in its path
meny has close )esc-q) to close page and free locale
app has 4 parts: HBS, CSS, J code, JS javascript code
HBS (html build sentences) definess app elements (buttons etc)
CSS (cascading style sheet) defines how elements look
J code defines event handlers (what happens when a button is clicked)
JS (javascript code) is not required except in advanced apps

browser passes event data to J in a dictionary (accessed by getv)
J passes cmds back to javascript as a JSON string
)

fix=: 3 : 0
t=. doc y
t=. t rplc '<';'&lt;'
''jhdiv t
)

mfix=: 3 : 0
'doc header url'=. y
t=. ''jhline''
t=. t,''jhhn 3;header
t=. t,fix doc
)

jev_get=: 3 : 0
'jdoc'jhr''
)

