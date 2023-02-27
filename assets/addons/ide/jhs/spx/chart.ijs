jhslinkurl'www.chartjs.org' NB. link to home page

jcjs'reset'
jcjs'data';>:5?20
jcjs'labels';5 NB. a0,a1,a2,a3,a4
jcjs'plot';10 10 600 600
   
jcjs'data';5?5
jcjs'update'

jcjs'legend';'legend'
jcjs'titles';'xaxis';'yaxis';'top of the chart'
jcjs'update'
   
jcjs'type';'bar'
jcjs'update'

jcjs'type';'pie'
jcjs'update'

jcjs'type';'doughnut'
jcjs'update'

jcjs'type';'polarArea' NB. note uppercase A
jcjs'update'

jcjs'type';'line'
jcjs'update'

jcjs'data';(<5?15),<5?15
jcjs'update'

jcjs'add';'data.datasets.1.label "bardata"'
jcjs'add';'data.datasets.1.type "bar"'
jcjs'update'

jcjs'add';'options.animation.duration 2000'
jcjs'update'


0 : 0
press the 'show chart definition' button
 scroll down to the line with "grid"
this line is in the dictionary at options.scales.x.grid
the "grid":"color" value "rgba(0,0,0,0.1)" is not very visible
the next step sets the value to "red"
)

jcjs'add';'options.scales.x.grid.color "red"'
jcjs'add';'options.scales.x.grid.lineWidth 5'
jcjs'update'

0 : 0
you can inspect the 'show chart config' display
 for values you can change

or you can study the chartjs online documentation
 for far more customizations

most settings are simple values such as a string in "s or a number
you can also use more complicated values (dictionary or array)

note: values set explicity override those set with a dictionary
)

jcjs'add';'options.scales.y.grid {"color":"green","lineWidth":5}'
jcjs'update'

jcjs'get' NB. chart defition changes







jcjs'close'