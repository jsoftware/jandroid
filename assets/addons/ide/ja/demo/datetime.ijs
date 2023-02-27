NB. datetime demo
NB.
NB. cc date datepicker [c] [numeric options]
NB.
NB. style: c  show calendar view
NB.
NB. numeric options are:
NB. minimum
NB. maximum
NB. value   (yyyyMMdd)
NB.
NB. Note: c minimum maximum are ignored before API 11 (Android 3)
NB.
NB. cc time timeedit [am] [numeric options]
NB.
NB. numeric options are:
NB. value   (hhmmss.zzz)

coclass 'demodatetime'

onStart=: datetimedemo_run

NB. =========================================================
DTdemo=: 0 : 0
pc datetimedemo;
bin v;
cc showtime switch;cn "date/time" "date" "time";
binx date v;
wh _1 _2;cc date1 datepicker c 20100113 20201231 20150731;
wh _1 _2;cc date2 datepicker;
bin z;
binx time v;
wh _1 _2;cc time1 timepicker am 231432;
wh _1 _2;cc time2 timepicker;
bin z;
bin z;
)

NB. =========================================================
datetimedemo_close=: 3 : 0
wd 'pclose'
)

NB. =========================================================
datetimedemo_run=: 3 : 0
wd DTdemo
wd 'pshow'
showtab 0
)

NB. =========================================================
datetimedemo_showtime_button=: 3 : 0
showtab ". wd 'get showtime value'
)

NB. =========================================================
showtab=: 3 : 0
if. 0=y do.
  wd 'setp viewshow date 1'
  wd 'setp viewshow time 0'
else.
  wd 'setp viewshow date 0'
  wd 'setp viewshow time 1'
end.
)

NB. =========================================================
datetimedemo_date1_changed=: 3 : 0
wd 'set date2 value ', wd 'get date1 value'
)

NB. =========================================================
datetimedemo_time1_changed=: 3 : 0
wd 'set time2 value ', wd 'get time1 value'
)

NB. =========================================================
wd 'activity ', >coname''
