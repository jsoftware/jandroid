coclass'app4'
coinsert'jhs'

NB. sentences that define html elements
HBS=: 0 : 0
jhclose''
k
)

k=: 0 : 0
<div class="nav">
  <h1>Nav</h1>
</div>

<div class="area1">
  <h4>Area1</h4>
</div>

<div class="fill-height">
  <div class="area-2">
    <div class="area-2-a">
      <p>Area-2a</p>
    </div>
  <div class="area-2-b">
      <p1>Area-2b</p1>
      <input type="submit" id="ads" name="ads" value="adsf" class="jhb" onclick="return jev(event)">
      <input type="submit" id="adsf" name="adsf" value="qwer" class="jhb" onclick="return jev(event)">
    </div>
  </div>
</div>
)

CSS=: 0 : 0
body {
  margin:0;
  min-height: 100vh;
  display: flex;
  flex-flow: column;
}

form {
  margin:0;
  min-height: 100vh;
  display: flex;
  flex-flow: column;
}

.nav {
  height: 5rem;
  background-color: aqua;
}

/*
.nav-spacer {
  margin-top: 5rem;
}
*/

.area1 {
  height: 10rem;
  background-color: brown;
}

.fill-height {
  display: flex;
  flex-direction: column;
  flex: 1
}

/*
.fill-height>div {
  flex: 1;
}
*/

.area-2{flex:1;}
.area-2-b{flex:1;}


.area-2 {
  display: flex;
  flex-direction: row;
}

.area-2-a {
  width: 20%;
  background-color: #4f90ff;
}

.area-2-b {
  width: 80%;
  background-color: #2b41ff;
}
)


NB. J code - initialize and handle events
create=: 3 : 0 NB. called by page or browser to initialize locale
t=. y pagedefault 'test';'default'
'must be text1;text2'assert (1=L. t)*.(2=#t)*.2~:3!:0 each t
jsdata=: 'ta';'textarea',LF,'more'
)

jev_get=: jpageget         NB. called by browser to load page

NB. javascript code
JS=: 0 : 0

function ev_body_load(){;} // initialize page when loaded

function show(){jbyid("which").innerHTML= JEV+'()';}

function ev_button_click(){show();}
function ev_anchor_click(){show();}
function ev_anchor_dblclick(){show();}

function ev_rad0_click(){show();return true;} // return true to allow normal state change
function ev_rad1_click(){show();return true;}
function ev_rad2_click(){show();return true;}

function ev_cb0_click(){show();return true;}
function ev_cb1_click(){show();return true;}

function ev_text_enter(){show();}
function ev_pswd_enter(){show();}
function ev_ta_enter(){show();return true;}

)