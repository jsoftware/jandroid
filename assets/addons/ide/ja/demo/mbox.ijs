NB. mb (message box) demo
NB.
NB. all mb cmd must be called with a current form
NB.
NB. the message box syntax is:
NB.   wd 'mb type buttons title message'
NB.
NB. type specifies the icon and default behaviour:
NB.  info      [title] msg
NB.  about     identical to info
NB.  toast     msg [0|1]  (1=short duration)
NB.  query     callback [title] msg [positive negative neutral]
NB.
NB. query will call verbs in the form's locale
NB. form_callback_positive
NB. form_callback_negative
NB. form_callback_neutral
NB.
NB.  dir      callback title directory
NB.  open1    callback title directory [filter]
NB.
NB. file chooser will call verbs in the form's locale
NB. form_callback_dir
NB. form_callback_open1

coclass 'demombox'


onStart=: mboxdemo_run

mboxdemo_run=: 3 : 0
wd 'pc mbox'
wd 'bin v'
wd 'bin h'
wd 'cc b1 button;cn toast'
wd 'cc b2 button;cn toast long'
wd 'bin z'
wd 'bin h'
wd 'cc b3 button;cn info'
wd 'cc b4 button;cn about'
wd 'bin z'
wd 'bin h'
wd 'cc b5 button;cn query 2 buttons'
wd 'cc b6 button;cn query 3 buttons'
wd 'bin z'
wd 'bin h'
wd 'cc b7 button;cn wdquery 2 buttons'
wd 'cc b8 button;cn wdquery 3 buttons'
wd 'bin z'
wd 'bin h'
wd 'cc b9 button;cn open'
wd 'cc b10 button;cn dir'
wd 'bin z'
wd 'bin z'
wd 'pshow'
)

mbox_b1_button=: 3 : 0
wd 'mb toast *Job finished.',LF,LF,'Goodbye.'
)

mbox_b2_button=: 3 : 0
wd 'mb toast ',DEL,'Long job finished.',LF,LF,'Goodbye.',DEL,' 0'
)

mbox_b3_button=: 3 : 0
wd 'mb info *Job finished.',LF,LF,'Goodbye.'
)

mbox_b4_button=: 3 : 0
wd 'mb about "Model Run" "Job finished early."'
)

mbox_b5_button=: 3 : 0
wd 'mb query dialog "Model Run" "OK to save?" ok cancel'
)

mbox_b6_button=: 3 : 0
wd 'mb query dialog "Model Run" "OK to continue?" ok no cancel'
)

mbox_b7_button=: 3 : 0
'ok cancel' wdquery 'Model Run';'OK to save?'
)

mbox_b8_button=: 3 : 0
'ok no cancel' wdquery 'Model Run';'OK to continue?'
)

mbox_b9_button=: 3 : 0
wd 'mb open1 dialog "Open file" "/sdcard"'
)

mbox_b10_button=: 3 : 0
wd 'mb dir dialog "Open directory" "/sdcard/j805-user"'
)

mbox_dialog_positive=: 3 : 0
wd 'mb toast postive'
)

mbox_dialog_negative=: 3 : 0
wd 'mb toast negative'
)

mbox_dialog_neutral=: 3 : 0
wd 'mb toast neutral'
)

mbox_dialog_open1=: 3 : 0
wd 'mb toast *', sysdata
)

mbox_dialog_dir=: 3 : 0
wd 'mb toast *', sysdata
)

NB. =========================================================
wd 'activity ', >coname''
