# Summit Arcade Game

BPA project by Aadi Joshi, Adithya Bhattacharya, Sanjay Raghav, Aadithya Ramikrishnan Sriram. 1st place National Winner. 

## How to play:

The goal of this action arcade game is to defeat the Ice King as quickly as possible by finding the
blue portal that leads to the dungeons, obtaining the Red, Green, and Blue keys, and fighting the Ice King
in the final boss room. The Green and Blue keys can be found in randomly placed loot chests in the
dungeons, and the Red key can be obtained by a zombie or skeleton in the dungeons, with a 10% chance
of getting the key. Along the way, players can equip their sword, bow, or snowballs, eat apples from trees
to regain health, and craft arrows to defeat enemies (see below for controls).

## Controls:

**- ESC** to go back (navigate) in the user interface
**- F11** to enable fullscreen mode
- **W** , **A** , **S** , **D** to move the player up, down left or right
- You can **CLICK** on doors or portals with your mouseto enter them, HOWEVER you must
    standing underneath them
- **E** to open inventory and see which items you have
- **Q** to eat any apples in your inventory to regain health
- **R** to equip your bow
- **C** to equip your sword
- **F** to equip any snowballs in your inventory
- **T** to craft arrow from any available Bones and Sticksin your inventory
- To use a weapon / projectile you must be holding down **SHIFT**. An attack area will appear,
    pointing to where your mouse is. The weapon will be used when you **CLICK** your mouse, and
    **SHIFT** is being held down

## See below for Game Features ...


## Technical Features:

*This project was done from scratch, written purely in java. All source code is original and created by us,
EXCEPT the noise generation algorithm.

**Rendering engine and Graphics:**

- Uses a 256x144 2D pixel array for storing the current frame data
    - A low resolution means that expensive graphical operations can be performed with
       minimal loss in performance. Also, texture files are small, and easy to create.
    - After everything is done rendering to the 256x144 pixel array, it is upscaled to the screen
       resolution, and gives the game a pixelated style. However, this upscaling process can be
       slow on some machines, so to combat this, there is an option in settings to use multiple
       threads that work asynchronously to upscale the frame to the desired resolution. FPS can
       sometimes double if the computer supports several threads, or the FPS can lower, so this
       option is left for the user.
- All sprites/textures are original and created by us
- The renderer has several methods for rendering and altering the current frame or any sprites that
    will be drawn
       - Control the brightness or alter the RGB values of a given area on the screen, or a game
          texture (ex: Player, Tree, Grass sprites/textures)
       - Colored Lighting effects, shadows, as well as soft shadows around edges (Ambient
          Occlusion)
       - Outline or transform rendered sprites (Flip horizontally, vertically, or rotate) to avoid
          redundant texture files.

**Map Generation:**

- The “Main Map” of the game uses a perlin noise algorithm to procedurally generate the map, and
    have it be unique for every new game. This is implemented by the OpenSimplexNoise library by
    Kurt Spencer (more info in works cited).
- The game map for the dungeons uses a DFS recursive algorithm to draw out a connected cave
    network, for a hard to navigate and puzzling cave system.

**Game Difficulty:**

- There are 3 gameplay difficulties, Easy, Medium, and Hard. These dictate game mechanics such
    as damage from enemy mobs, like zombies, as well as attack range, and mob spawning. This is all
    stored on a settings file, which is also uploaded to the database (see below for more info).

**Runtime Settings:**

- Within the settings file, there are runtime settings for the number of graphical threads that the
    game should use, ambient occlusion which is a graphical effect, and an option for v-sync (vertical
    sync), to prevent any screen tearing.

**MySQL, Database, and Flow of Data:**

- This project features serialization of game save data to a personal or remote MySQL server. The
    program will automatically create a personal database on the server with a specialized unique key 
when you first open the game, so only you have access to the database and game saves. This key
is stored in the db_config file, in the db.serial field, with the final name of the database being
“summitdata_YourSerialKey”.

- Serialization is done through serializing the current **“GameWorld”** into a temp file located in
    **cache/temp.txt** , which can then be uploaded to thedatabase as a blob. This temp file however,
    can be used as a safe, intermediary between the user and server, so in case an issue occurs when
    uploading a save to the database, or the file gets corrupted, the serialized data on the temp file can
    be used instead. Essentially, this file acts as a backup. Within the game home screen, in the
    “Saved Games” tab, there is an option called “Cache” which will load from the temp file.
- Loading games from the database is simple. The option to do that within the game is in the
    “Saved Games” tab, which has a list of all game saves stored on the database. There is also an
    option to refresh the connection and retrieve saves from the database again, as well as an option
    to delete game saves from the database. Also, any game saves in which the player has died are
    automatically deleted from the database, and the cache file is cleared to prevent cheating.
- The game settings are also stored on the database, except on a different table with just the
    singular settings file. This settings file contains game graphics information, as well as **gameplay**
    **difficulty (Easy, Medium, Hard).**

**Logging and Crash Reporting:**

- All system events, database operations, server connections, and game retrievals are time stamped
    and logged on a server log file in the **server_logs** folder. Any exceptions that are thrown and
    caught by the system are also logged here to be debugged by the user, or emailed to the project
    team for debugging (see “Contact Through Email” section below)
- If a game session were to crash for an unknown reason, a crash report would be generated in the
    **crash_reports** folder. This crash report file willcontain the exception that caused the crash as
    well as vital game session information like the positions of all the game entities at the time of the
    crash, the position of the player, as well as the seed of the world so it can be replicated and
    debugged by the project team.

**Contact Through Email:**

- Within the games home screen, there is an option to email a crash report and server logs to one of
    the project team members. It will prompt the user on what to send to us within the email.


## Sample Images:
![lightingdemogif](https://user-images.githubusercontent.com/98367091/230689933-10cd619e-1009-4d09-b025-820915eb78ca.gif)
![breakingsnowgif](https://user-images.githubusercontent.com/98367091/230689978-c1194839-8b90-46de-8578-462aa37bc68f.gif)
![throwingsnowballsgif](https://user-images.githubusercontent.com/98367091/230690004-cd680c53-726f-4111-8828-ac91b8a5800d.gif)
![snowparticlesgif](https://user-images.githubusercontent.com/98367091/230690016-8b1d222b-1dfd-4d2f-96af-38afb6b940f4.gif)

