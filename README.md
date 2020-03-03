# Cactus

Simple app to recognize cactuses on pictures, based on Firebase Machine Learning kit using AutoML Vision Edge.
ML model is exported as application resource. Works with pre-existing pictures or just taken from camera.

# Learning set
Learning set was trained an hour. Contains the set of ~900 pictures:
* 500x cactus
* 140x tree
* 100x flower
* 100x leaf
* 60x grass

# Potential improvements
* using remote fetching model will decrease the app size by 50%~ but increase app init time
