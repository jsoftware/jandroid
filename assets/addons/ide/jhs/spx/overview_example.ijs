NB. example script used in overview tour

NB. define verb fcrsum to add row and col sums to matrix
NB. fcrsum ?2 3$10
NB. 3 defines a verb and 0 uses lines up to ) for definition
fcrsum=: 3 : 0 
t=. y,+/y
t,.+/"1 t
)