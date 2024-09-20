# pluvia

Pluvia, a computer game for rainy days.  
It is relaxing and does not impose time pressure.
Pluvia’s main target is to gain the highest score by keeping a steady growing pile of stones from reaching the top of
the screen.  
In a Pluvia game, stones of different color drop from above onto each other to form gradually a heap that will
eventually reach the top of the screen and end the game. Each stone that drops into the game screen increments the score
one point.  
Whenever two or more stones of the same colour land on top of each other, they vanish and leave space for more stones to
drop.  
Whenever two or more stones of the same colour land beside each other, they glue to each other and form a bar. Bars are
very annoying and you will end up trying to avoid them.  
Each time some stones have dropped into the game screen, you can interact with the heap; You can click on one of the
stones with the left or right mouse button to push it to the left or right. The stone will move into that direction as
long as there is room, pushing any other stones with along with it.  
This way you can heap stones of same colour on top of each other to make room.  
If you do not want to move any stone, you can press the space key to force new stones to drop.  
You can exit any game using the escape button and selecting ‘Exit’ in the ‘Game Menu’. You can always continue such a
game.

[Pluvia Web site](https://pluvia.bushnaq.de/)  
[Pluvia github site](https://github.com/kunterbunt2/pluvia)

Pluvia is written in Java using
[libgdx]( https://libgdx.com/) game library and the pbr extension [gdx-gltf]( https://github.com/mgsx-dev/gdx-gltf).

![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/05/pluvia-marble-1.png)

![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/05/pluvia-1.png)

# Installation

## How to install PLuvia on Windows

Pluvia is released as a msi installer for Windows platform. This installer is however not digitally signed. This means
that your Windows will warn you with a popup that the software could be harmful  
![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/06/windows-protected-your-pc-1.png)

You need to select More info to see the next popup  
![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/06/windows-protected-your-pc-2.png)  
Now you can seelect Run anyway to start the installer.

## How to install PLuvia on MacOS

Pluvia is released as a pkg installer for the MacOS platform. This installer is however not digitally signed. This means
that if you double click the file your MacOS will warn you with a popup that the software could be harmful  
![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/06/macos-cannot-be-opened.png)

You need to Control-click the pkg file and select open.  
Now another warning is shown  
![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/06/macos-cannot-verify-the-developer.png)  
If you select open, the installer will open and you can just click continue until it is finished installing Pluvia.  
You can find Pluvia in the finder under Applications.

# Controls

| Control            | Purpose                                                                        |
|--------------------|:-------------------------------------------------------------------------------|
| Space              | drops more stones into the level                                               |
| Left mouse button  | clicking a stone with the left mouse button will push the stone to the left    |
| Right mouse button | clicking a stone with the righjt mouse button will push the stone to the right |
| ESC                | open Pause Dialog                                                              |

# Additional Controls

These controls are only valid in debug mode

| Control             | Purpose                                                    |
|---------------------|:-----------------------------------------------------------|
| W/cursor up         | move camera towards the screen                             |
| A/cursor left       | move camera left                                           |
| D/cursor right      | move camera right                                          |
| S/cursor down       | move camera away from the screen                           |
| Print               | save screenshot of various fbos into the screenshot folder |
| I                   | open info pane including debug inforamtion                 |
| F5                  | toggle debug mode                                          |
| F6                  | show time graphs                                           |
| Middle mouse button | clicking and move mouse to tilt camera                     |

# Configuration

Pluvia can be configured in the Options dialog.

You can configure the performance of Pluvia to match your system, just slide the Graphics Quality slider to the positoin
that works on your computer. The difference of performance on my computer is factor 20.
![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/06/pluvia-options-1.png)

All other settings like multi monitor, full screen mode, etc. can be configured in the Graphics Tab of the Options
dialog.
![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/06/pluvia-options-2.png)

# How To Report a Bug

Please contact me to report a bug or just create a ticket at https://github.com/kunterbunt2/pluvia/issues  
A direct x diagnostic report is very helpfull, as every system is different.

# How to Genrate a DirectX Diagnostic Report

1. Open DirectX Diagnostic Tool by clicking the Start button Picture of the Start button, typing dxdiag in the Search
   box, and then pressing ENTER.
2. A window will pop up asking if you wish to check if your drivers are digitally signed.
3. Select No
4. On the next window, select Save All Information
5. Save the file to your desktop
6. attach the dxdiag.txt file to teh ticket

# Tested on Following Systems

https://github.com/kunterbunt2/pluvia/wiki/System-Tested
