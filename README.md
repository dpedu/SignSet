SignSet
=======

Updates in-game signs from the content of HTTP requests

Example config file is included.

Installation & Configuration
============================

- Place SignSet.jar and the SignSet directory (with contained config.yml) in your server's plugin directory

- For each set of signs, add a child node of signsets in the config file

- x1 must be smaller than x2, y1 than y2, z1 than z2.

- freq is how often the signs will be updated, in ticks. So 20 = one second.

- flipx and flipz is used to change what order the signs are updated in.
