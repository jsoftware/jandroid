NB. parse android layout xml file
load 'gui/android'

offset=. 1 1
'rc el id gp'=. offset expat_parse_xml_jamenu_ 1!:1 < jpath '~Addons/gui/android/test/res/menu/help.xml'
smoutput el
smoutput id
smoutput gp

offset=. offset + (#id),(#gp)
'rc el id gp'=. offset expat_parse_xml_jamenu_ 1!:1 < jpath '~Addons/gui/android/test/res/menu/main.xml'
smoutput el
smoutput id
smoutput gp
