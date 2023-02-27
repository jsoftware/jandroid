NB. editm demo

coclass 'demoeditm'

onStart=: editmdemo_run

Text=: topara 0 : 0
The King and Queen of Hearts were seated on their throne when
they arrived, with a great crowd assembled about them -- all
sorts of little birds and beasts, as well as the whole pack of
cards: the Knave was standing before them, in chains, with a
soldier on each side to guard him; and near the King was the
White Rabbit, with a trumpet in one hand, and a scroll of
parchment in the other.

In the very middle of the court was a table, with a large dish of
tarts upon it: they looked so good, that it made Alice quite
hungry to look at them -- `I wish they'd get the trial done,' she
thought, `and hand round the refreshments!' But there seemed to
be no chance of this, so she began looking at everything about
her, to pass away the time.

Alice had never been in a court of justice before, but she had
read about them in books, and she was quite pleased to find that
she knew the name of nearly everything there. `That's the judge,'
she said to herself, `because of his great wig.'

The judge, by the way, was the King; and as he wore his crown
over the wig, he did not look at all comfortable, and it was
certainly not becoming.

`And that's the jury-box,' thought Alice, `and those twelve
creatures,' (she was obliged to say `creatures,' you see, because
some of them were animals, and some were birds,) `I suppose they
are the jurors.' She said this last word two or three times over
to herself, being rather proud of it: for she thought, and
rightly too, that very few little girls of her age knew the
meaning of it at all. However, `jury-men' would have done just as
well.

The twelve jurors were all writing very busily on slates. `What
are they doing?' Alice whispered to the Gryphon. `They can't have
anything to put down yet, before the trial's begun.'

`They're putting down their names,' the Gryphon whispered in
reply, `for fear they should forget them before the end of the
trial.'

`Stupid things!' Alice began in a loud, indignant voice, but she
stopped hastily, for the White Rabbit cried out, `Silence in the
court!' and the King put on his spectacles and looked anxiously
round, to make out who was talking.

Alice could see, as well as if she were looking over their
shoulders, that all the jurors were writing down `stupid things!'
on their slates, and she could even make out that one of them
didn't know how to spell `stupid,' and that he had to ask his
neighbour to tell him. `A nice muddle their slates'll be in
before the trial's over!' thought Alice.

`Herald, read the accusation!' said the King.

On this the White Rabbit blew three blasts on the trumpet, and
then unrolled the parchment scroll, and read as follows:

    `The Queen of Hearts, she made some tarts,
       All on a summer day:
     The Knave of Hearts, he stole those tarts,
       And took them quite away!'

`Consider your verdict,' the King said to the jury.

`Not yet, not yet!' the Rabbit hastily interrupted. `There's a
great deal to come before that!'

`Call the first witness,' said the King; and the White Rabbit
blew three blasts on the trumpet, and called out, `First
witness!'

The first witness was the Hatter. He came in with a teacup in one
hand and a piece of bread-and-butter in the other. `I beg pardon,
your Majesty,' he began, `for bringing these in: but I hadn't
quite finished my tea when I was sent for.'
)

NB. =========================================================
editmdemo_run=: 3 : 0
wd 'pc editmdemo;pn "Editm Demo"'
wd 'cc ted editm'
wd 'set ted text *',Text
wd 'bin zhs'
wd 'cc ok button;cn OK'
wd 'cc close button;cn Close'
wd 'pshow'
NB. call these after the pshow:
wd 'set ted select 1580 1763'
)

NB. set line wrap off:
NB. wd 'set ted wrap 0'

NB. set readonly:
NB. wd 'set ted readonly 1'

NB. =========================================================
editmdemo_close=: 3 : 0
wd 'pclose'
)

editmdemo_close_button=: editmdemo_close

NB. =========================================================
wd 'activity ', >coname''
