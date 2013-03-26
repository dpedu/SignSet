SignSet
=======

Updates in-game signs from the content of HTTP requests

Example config file is included.

Installation & Configuration
============================

- Place SignSet.jar and the SignSet directory (with contained config.yml) in your server's plugin directory

- For each set of signs, add a child node of signsets in the config file

- x1 must be smaller than x2, y1 than y2, z1 than z2. These coordinates create an inclusive cube region. Non-sign blocks will simply be ignored, so having your signs in irregular shapes is OK.

- freq is how often the signs will be updated, in ticks. So 20 = one second.

- flipx and flipz is used to change what order the signs are updated in.

- Colors may be used on signs. "&&" (two ampersands) will be replaced by the color symbol, so &&4 would result in red text. See more color ids here: http://www.minecraftwiki.net/wiki/Formatting_codes#Color_codes

- If an HTTP Request fails, the signs will simply not be updated, and an error logged to your server console.

Tips
====

- Try to keep the cube regions as small as possible. Every single block within them has to be scanned for signs when updating.

- Is your text out of order? Try adjusting flipx or flipz.

- "Blank" state not being applied to signs? Add lines that only contain a space to the end of your file.
