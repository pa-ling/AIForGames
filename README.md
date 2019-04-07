# AI For Games

These are the projects for the programme "AI For Games" at the University of Applied Sciences Berlin. The professor provided two server applications for two multiplayer games. The assignment was to create a client which can operate with these games without the need of user input. 

## Gawihs
The first Game "Gawihs" is a round based multiplayer board game for three players, where the board gets smaller after each move of a player. When a player has no moves left to make, he lost. The Client for this game used the following data structures and algorithm:
* Assessment of Moves
* Game Tree with Alpha-Beta-Search and Multithreading
* Evolutionary learning algorithm

## Zpifub
The second Game "Zpifub" is a realtime multiplayer strategy game where each player controls three bots. With these three bots the user has to paint as much ground as possible with the user's color. The one who has the most ground painted after a certain amount of time wins. The Client for this used the following data structures and algorithm:
* Quad tree
* A*

### Additional Information
For compilation you will need a copy of gson in version 2.8.4 (other versions might also work) and the gawihs.jar or zpifub.jar respectively.

Develop and tested on Ubuntu operating system.
