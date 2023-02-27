NB. webview
NB.
NB. !!! this is experimental and *will* change...
NB.
NB. cover for the WebView
NB. cmd so far:
NB.  set c url url
NB.  set c baseurl url
NB.  set c html text

coclass 'demowebview'

onStart=: webview

NB. =========================================================
webview=: 3 : 0
wd 'pc webview; pn webview'
wd 'bin v'
wd 'cc e edit'
wd 'wwh 1 _1 0;cc w1 webview'
wd 'wwh 1 _1 0;cc w2 webview'
wd 'bin z'
wd 'pshow'
wd 'set e text http://news.yahoo.com'
wd 'set w1 url http://news.yahoo.com'
wd 'set w2 baseurl http://www.jsoftware.com'
wd 'set w2 html <html><body><img src=''./zippy.gif'' /></body></html>'
)

NB. =========================================================
webview_e_button=: 3 : 0
smoutput e
wd 'set w1 url *',e
)

NB. =========================================================
webview_w1_curl=: 3 : 0
wd 'set e text *',w1_curl
)

NB. =========================================================
webview_close=: 3 : 0
wd 'pclose'
)

NB. =========================================================
wd 'activity ', >coname''

