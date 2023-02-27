NB. parse android layout xml file
load 'gui/android'

'rc el id'=. 1 expat_parse_xml_jalayout_ 1!:1 < jpath '~Addons/gui/android/test/res/layout/layout.xml'
smoutput el
smoutput id
