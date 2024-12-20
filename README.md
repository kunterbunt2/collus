# collus

# Issues that need fixing soon

1. implement fadeout to white
1. level text must be removed after level is won
1. Add level description
1. add level menu using images that scroll horizontally.<br>
   all levels after the current are grayed out.
1. fix shadows
1. Reset timer for main menu
1. Show level step target and current steps
1. harmonize menu color with level color
1. Add visual buttons to rotate the cube.
1. Do we need a main menu?

# Issues fixed

1. Rename back to main menu to quit level
1. Add reset level menu
1. allow menu keyboard navigation

[Pluvia Web site](https://pluvia.bushnaq.de/)  
[Pluvia github site](https://github.com/kunterbunt2/pluvia)

Pluvia is written in Java using
[libgdx]( https://libgdx.com/) game library and the pbr extension [gdx-gltf]( https://github.com/mgsx-dev/gdx-gltf).

![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/05/pluvia-marble-1.png)

![alt tag](https://pluvia.bushnaq.de/wp-content/uploads/2022/05/pluvia-1.png)

# Installation

## How to install Collus on Windows

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
