NB. parse android values xml file
load 'gui/android'

'rc el id'=. expat_parse_xml_javalues_ 1!:1 < jpath '~Addons/gui/android/test/res/values/colors.xml'
smoutput el

'rc el id'=. expat_parse_xml_javalues_ 1!:1 < jpath '~Addons/gui/android/test/res/values/dimens.xml'
smoutput el

'rc el id'=. expat_parse_xml_javalues_ 1!:1 < jpath '~Addons/gui/android/test/res/values/strings.xml'
smoutput el
