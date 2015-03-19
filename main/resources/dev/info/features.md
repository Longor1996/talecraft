<!-- Funky header with HTML! -->
<div style="
            padding: 0; margin: 0;
            width: 100%; height: 80px; border-radius: 16px;
            background-color: black; color: #DDD;
            font-size: 50px; text-antialias: on;
            text-align: center; vertical-align: middle;
"> TaleCraft - Features </div>

# Items

### Wand
Also called the `Magic Wand`, this item lets one select regions of blocks.
After selecting a region of blocks, other tools can be used to do something
with that region of blocks (like, filling it with another kind of block).

--------------------------------------------------------------------------------
# Blocks

### Clock Block
### Redstone Trigger


--------------------------------------------------------------------------------
# Meta (overlays, keys, visuals)

### InfoBar
The infobar is in the upper part of the screen when in CreativeMode.
This bar displays various bits of information that are useful when
building and/or debugging. Following is a (incomplete) list of
what the InfoBar displays when active.

* Current TaleCraft version and FPS
* Information about the item you are holding.
* Information about the block you are looking at.
  * Position, Type, State
* Information about the entity you are looking at.
  * Position, Type, Health
* Information about your 3D cursor.
  * Currently Selected Block (Position)
  * Currently Selected Region (Bounds, Volume)

### Keybinding: key.toggleBuildMode
Default Key: `b`

This keybinding toggles between CreativeMode (AKA: BuildMode) and AdventureMode.

### Keybinding: key.toggleWireframe
Default Key: `.`

This keybinding flips trough different visualizations of the world.
This is for debugging maps and can *only* be used when in creative-mode.
Following is a list of the visualization modes...

* Normal (Mode 0)
* Wireframe: Renders only the edges of all surfaces.
* Backface: Renders only the backside of all surfaces.
* Lighting: Disables textures, but keeps lighting active.







--------------------------------------------------------------------------------
# :mod internals:
Everything in this section is either *hidden*
or *not yet implemented* as an actual feature.

### Custom Sky Rendering
### Temporary Renderables
### Screen Fade
### TC Network Channel
### TC Timed Executor
