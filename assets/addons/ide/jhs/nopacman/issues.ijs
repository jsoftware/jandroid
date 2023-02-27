NB. browser/javascript issues
NB. no J code here - .ijs so it shows up in jfif

0 : 0

*** function ev_close_click
 file/file/files/pacman/...
  no call to server - no beforeunload - just window.close - 

 jijx - dirty - allwins.length

 jijs - dirty - setdirty/setclean

 cojhs
   call server - beforeunload
   firefox requires user action for beforeunload to run

 4 cases
  simple - no server call
  jijx   - dirty - allwins.length - server call
  jijs   - dirty - setdirty/clean - server call
  cojhs  - server call

window.close does not work if window not opened by javascript

*** doc for next release
esc-? - escape char then next char (unlike shift or ctrl)

esc-1 activate menu

esc-q replaces ctrl+\ (conflict with contenteditable in jijx)

jijx esc-9 open jlog, esc-9 log allwins

ctrl+shift+up/dn - recalls lines or state - works in jfile

*** open or reopen page
window.open pageopen
function pageopen is used to open and reopen pages
function pageopen is used by edit'...' jfif jfile jfiles

*** opener

jijs (and jdebug) use opener to run sentences in jijx
 but if its opener jijx has closed, it would like to use
 another one - but it always opens a blank page
 this is unfortunate!
 
it does not seem to follow the same origin policy rules correctly

jijx - window.open('','jfif') -> opens existing jfif if it has that jijx as its opener
 but it opens a blank page if jfif was opened by a different jijx page

they are same origin but it seems to require opener as well
 this is the case for localhost/127.0.0.1 and firefox/chrome

*** no-cache
uqs nocache

the way no-cache works (or doesn't work) seems to have changed over the years

it seems to work better now and there is less need for uqs or nocache=...

Cache-Control: no-cache vs no-store 
no-store and no-cache both seem to have no effect with firefox

and what about no-store 

*** older comments that may or may not still be relevant

*** Cache-Control: no-cache
Browser caching can be confusing and is quite different
from a desktop application.

Back/forward, switching tabs, switching browser apps, are
showing cached pages. A get (typed into the URL box or from
favorites) shows a cached page if possible. And exactly when
it shows a cached page and when it gets a fresh page varies
from browser to browser and the phase of the moon. This can
be confusing if you have the expectation of a new page with
current information.

Ajax requests (in particular JIJX) have no-cache as old
pages in this area are more confusing and than useful.

All other pages allow cache as the efficiency of mucking
around pages without dealing with the server is significant.
Sometimes this means that when you want a fresh page with
latest info you are in getting a cached version.

Some browsers have a transmission progress bar indicator.
No flash means you are getting a cached page and a flash
means you getting a new page.

Refresh (F5 on some browsers) gets a fresh page and is a
useful stab poke if confused.

*** timer - wd docs
timer i ; set interval timer to i milliseconds.
Event systimer occurs when time has elapsed.
The timer keeps triggering events until the timer is turned off.
An argument of 0 turns the timer off.
The systimer event may be delayed if J is busy,
and it is possible for several delayed events to be reported as a single event.

*** login/bind/cookie/security overview

listening socket binds to any
localhost is relatively secure
firewalls provide protection
localhost is relatively secure and would gain little from login
tunnel to localhost provides good security
non-localhost requires a login
login is provided by a cookie
cookie set in the response to providing a valid password
cookie is then included in the header of all requests - validated by server
cookie is non-persistent and is deleted when browser closes.
tabs do not need to login, but a new browser does.

*** app overview
URL == APP == LOCALE

Browser request runs first available sentence from:
 post          - jdo
 get URL has . - jev_get_jfilesrc_ URL_jhs_
 get           - jev_get_URL_''

Post can be submit (html for new page) or ajax (for page upates).

The sentence can send a response (closing SKSERVER).

urlresponse_URL_ run if response has not been sent
when new input required. jijx does this as the response
requires J output/prompt that are not available until then.

Use XMLHttpRequest() for AJAX style single page app.
Post request for new data to update page. jijx app does
this for significant benefit (faster and no flicker).

Form has hidden:
 button to absorb enter not in input text (required in FF)
 jdo="" submit sentence

Enter in input text field caught by element keydown event handler.

*** event overview
Html element id has main part and optional sub part mid[*sid].

<... id="mid[*sid]" ... ontype="return jev(e)"

jev(event)
{
 sets evid,evtype,evmid,evsid,evev
 onclick is type click etc
 try eval ev_mid_type()
 returns true or false
}

If ev_mid_type returns value, it is returned to the onevent caller,
otherwise a calculated value is returned.

ev_mid_type can ajax or submit J sentence.
Ajax has explicit nv pairs in post data and result.
Submit has normal form nv pairs in post data and result is new page

*** gotchas

Form elements use name="...". Submit of hidden element requires
name and the element will not be included in post data with just id.

Javascript works with id. In general a form input element should have
the same value for both id and name. The exception is radio where id
is unique and name is the same across a set of radio buttons.

***
1. depends on cross platform javascript and styles

2. 127.0.0.1 seems faster than localhost
   wonder if dot ip name is faster than www.jsoftware.com

3. Enter with only text has no button.
   Enter with buttons submits as if first button pressed.

4. html pattern
<!DOCTYPE html>
<html>
 <head>
  <meta...>
  <title>...</title>
  [<style type="text/css">...</style>...]
 </head>
 <body>
  ...
 </body>
 [<script>...</script>...]
</html>

5. autocomplete and wrap fail validator - but are necessary


*** menu ide>jfile/jijs/etc
browsers block popups if not 'directly' the result of a user action - click on link or button

*** jquery dialog stuff
 '<div id="dialog" title="Table Editor Error"></div>'
 $(function(){$("#dialog").dialog({autoOpen:false,modal:true});});
 $("#dialog").html(ts[0]);
 $("#dialog").dialog("open");

*** unload/beforeunload
april 2022 update:

onbeforeunload is OK to warn user they are about to leave a dirty page
the user gets a standard brower defined warning about leaving the page

ajax/alert/... can not be used in the handler - depends on user staying and using close button

firefox triggers the event on a new page

chrome does NOT trigger the event on a new page (no interactions)
 in that case the user is not warned about leaving
 and cojhs locale will be orphaned

----

Jan 2017 - conclusion again that unload/beforeunload are not useful
start to use standard close button on numbered locale pages
 this can free locales and resource properly

unload can free locales - but refresh is an unload and thed a reload from the locale that no longer exists
beforeunload does not ask permission to leave if page has not been touched by user (no good for freeing locale)
beforeunload can prevent unload of changed page - but gets confusing on if being replaced by J open_jhs_

----

onunload/onbeforeunload events would be nice if they worked 'properly'
would allow app to clean up client side and (ajax) server side
there are limitations (ajax, etc.) in the handlers
and nasty cross browser differences
problems mostly in the ajax call

chrome requires onbeforeunload with async false
firefox requires onbeforeunload but doesn't care about async
who knows about safari and other
and if the server is hung then there are other problems

the framework set the unload event the same way it set load event
but it doesn't trigger and something is wrong

the ajax calls with body are suspect - sid "" should probably be null to avoid body*

no current pages really require client/server side cleanup
the main requirement would be a page that required a locale to hold state
that needed to be properly released

this could probably be handled by the framework as follows:

window.onbeforeunload= xclose;
function xclose(){return dirty?""prefered way to leave this page is menu close or close button ×;null;

a page could manage the dirty flag and do proper shutdown in the close handler


*** contenteditable to/from text
IE:
 <BR>             <-> N (\n)
 </P>              -> N
 <P>&nbsp;</P      -> N (can not tell emtpy from 1 blank)
 can have \r\n !

Chrome:
 <br>            <-> N
 <div>            -> N
 <div><br></div>  -> N
 saw nested divs, but do not know how to get them
 starting div so break on div rather than /div
 
FF:
 <br>            <-> N
 has (and needs) <br> at end

Portable rules (all case insensitive):
 remove \r \n
 <p>&nbsp;</p>    -> N
 <div><br></div>  -> N
 <br>            <-> N
 </p>             -> N
 </div>           -> N
 always have <br> at end (read/write)
 &lt;...         <-> < > & space

)