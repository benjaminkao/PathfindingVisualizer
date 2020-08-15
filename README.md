# PathfindingVisualizer
Pathfinding Visualizer shows pathfinding algorithms such as Dijkstra's, A*, and Depth First Search (DFS).


## Motivation
I wanted to prove that I understood the concepts of these pathfinding algorithms well enough to be able to code them. I also thought this project was a good opportunity to work on improving my UI creation skills.


## Screenshots
<img src="https://user-images.githubusercontent.com/54568364/90321568-d92f3d80-deff-11ea-9f9c-00182248e4b5.PNG" alt="Pathfinding Visualizer Screenshot" width="500"/>

## Framework Used
#### Built with
   - JavaFX


## Latest Update:


### August 10th, 2020
Added Algorithm Descriptions; Minor Updates

- Added an algorithm descriptions tabe to Settings
- Disabled all buttons and grid visualizer after visualizer is started so that the start and end nodes can not be moved during the pathfinding anymore


## Previous Updates:


### August 9th, 2020
Updated UI; Fixed Pathfinding Algorithm Issues

- Fixed window resize when resizing from north, northwest, and west directions so that it actually works
- Added menu bar, giving users another option to close application
- Users can also access a controls menu from the menu bar that shows how to use the application
- Fixed issue with placing nodes where application would crash due to NullPointerException, now users can run the pathfinding visualizer
- Fixed issue where Dijkstra's Algorithm would find a longer path to the end node even though a shorter path exists
- Now when the end node is added to the checking queue, all other nodes are disregarded, which will slightly increase the speed of Breadth First Search (BFS) and Dijkstra's


Updated UI; Added Erase Ability

- Got rid of terrible looking default Windows border
- Created new dark theme border, with icons made from SVGPaths
- Added window resize and drag to new border
- Added ability to "erase" nodes on right click


### August 6th, 2020
Updated UI

- Now using styling sheet for UI
- Dark theme
- Created custom checkmark for checkbox using SVG path


### August 5th, 2020
Added Breadth First Search and Fixed Some Bugs

- Added Breadth First Search (BFS) as an algorithm option
- Fixed issue where A* algorithm wouldn't work correctly when
allowDiagonal checkbox was checked.
- Fixed issue where after user pressed reset button and tried starting
the visualizer again, nothing would happen. Now something happens.


### August 4th, 2020
Finished creating basic JavaFX version:
- Added Depth First Search (DFS) algorithm
- Improved A* algorithm to be faster
- New updated UI
- Implemented reset functionality
