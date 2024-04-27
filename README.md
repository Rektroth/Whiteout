# Whiteout

Whiteout is an attempt to implement the gameplay and mechanic consistency patches included in the [Paper](https://github.com/PaperMC/Paper) project in Fabric.

## Patches

### Present

| Bug                                                   | Lazy? | Mojang Prioritization | Name                                                                                        |
|-------------------------------------------------------|-------|-----------------------|---------------------------------------------------------------------------------------------|
| [MC-4](https://bugs.mojang.com/browse/MC-4)           | Yes   | Low                   | Item drops sometimes appear at the wrong location                                           |
| [MC-27056](https://bugs.mojang.com/browse/MC-27056)   | Yes   | Low                   | You can blow the extension off a piston                                                     |
| [MC-84789](https://bugs.mojang.com/browse/MC-84789)   | No    | Low                   | Tamed wolves beg for bones while wild wolves don't                                          |
| [MC-108513](https://bugs.mojang.com/browse/MC-108513) | Yes   | *None*                | end crystal did not spawn/despawn correct after enderdragon respawned                       |
| [MC-123450](https://bugs.mojang.com/browse/MC-123450) | No    | Low                   | Item frames play sounds when the item within them is read from NBT                          |
| [MC-123848](https://bugs.mojang.com/browse/MC-123848) | No    | Normal                | Item frames (and items within) when removed from a ceiling, drop atop, not under, the block |
| [MC-157464](https://bugs.mojang.com/browse/MC-157464) | No    | Normal                | Villagers move around in bed or even leave the bed when food is thrown at them              |
| [MC-158900](https://bugs.mojang.com/browse/MC-158900) | No    | *None*                | "bad packet id 26" upon connecting after tempban expire                                     |
| [MC-167279](https://bugs.mojang.com/browse/MC-167279) | No    | Normal                | Bees get stuck in the void                                                                  |
| [MC-224454](https://bugs.mojang.com/browse/MC-224454) | No    | Normal                | Entities don't recognize azaleas or flowering azaleas as obstacles when pathfinding         |
| [MC-252817](https://bugs.mojang.com/browse/MC-252817) | No    | Normal                | Placing a map into an item frame and removing it does not remove the green player marker    |
| [MC-253721](https://bugs.mojang.com/browse/MC-253721) | No    | Important             | Wrong logs when running /op @a                                                              |
| [MC-257487](https://bugs.mojang.com/browse/MC-257487) | No    | Low                   | The ender dragon's name is not reset when it is respawned in the end                        |
| [MC-259571](https://bugs.mojang.com/browse/MC-259571) | No    | Normal                | Last player game mode not saved after player dies or the game is reloaded                   |
| [MC-262422](https://bugs.mojang.com/browse/MC-262422) | No    | Normal                | Lightning bolts during thunderstorms can strike players in spectator game mode              |

Patches that are marked as "lazy" generally meet at least one of the following criteria:

- The new behavior created by the patch is (possibly) not what's actually intended by Mojang
- The patch *circumvents* the issue but doesn't truly correct it
- The manner in which the patch is applied is not technically elegant and might cause unforeseen issues and/or performance degradation

Mojang prioritization is also noted here. If a ticket has *not* been assigned a priority by Mojang, this potentially means that a Mojang employee has not yet personally reviewed the ticket and confirmed that the behavior is unintentional.

Any patches that are considered "lazy" or are for a bug ticket which does not have a Mojang-assigned priority will be disabled by default when configurability is implemented.

### Planned

| Bug                                                   | Name                                                                                                                                                                                            |
|-------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [MC-11193](https://bugs.mojang.com/browse/MC-11193)   | The order in which powerable blocks (e.g. redstone dust blocks) along a wire are powered or de-powered is not clearly defined and causes a non-deterministic behavior for redstone contraptions |
| [MC-145260](https://bugs.mojang.com/browse/MC-145260) | Whitelist on state inconsistency                                                                                                                                                                |
| [MC-145656](https://bugs.mojang.com/browse/MC-145656) | Attribute "follow_range" is not working to hostile mobs before they find the target                                                                                                             |
| [MC-153086](https://bugs.mojang.com/browse/MC-153086) | Beacons always play deactivating sound when broken, even when not powered                                                                                                                       |
| [MC-159283](https://bugs.mojang.com/browse/MC-159283) | The End terrain does not generate in multiple rings centered around the world center                                                                                                            |
| [MC-163962](https://bugs.mojang.com/browse/MC-163962) | Villager demand values decrease indefinitely                                                                                                                                                    |
| [MC-171420](https://bugs.mojang.com/browse/MC-171420) | OP players get kicked for not being on the whitelist (enforce = on)                                                                                                                             |
| [MC-174630](https://bugs.mojang.com/browse/MC-174630) | Secondary beacon effect remains when switching primary effect                                                                                                                                   |
| [MC-188840](https://bugs.mojang.com/browse/MC-188840) | *This ticket covered piston-based block duplication. It is unknown why the ticket was privated/deleted.*                                                                                        |
| [MC-191591](https://bugs.mojang.com/browse/MC-191591) | Saddles lose their NBT data when equipped on horses, zombie horses, skeleton horses, mules or donkeys via right-clicking                                                                        |
| [MC-200092](https://bugs.mojang.com/browse/MC-200092) | /setworldspawn seems to ignore the 'angle' parameter                                                                                                                                            |
| [MC-210802](https://bugs.mojang.com/browse/MC-210802) | Inactive sheep eat grass                                                                                                                                                                        |
| [MC-235045](https://bugs.mojang.com/browse/MC-235045) | Entity type tags suggestions don't work in target selectors                                                                                                                                     |
| [MC-243057](https://bugs.mojang.com/browse/MC-243057) | Furnace recipes don't consider alternatives when first option of recipe is used for fuel                                                                                                        |
| [MC-244739](https://bugs.mojang.com/browse/MC-244739) | Goat eating sounds aren't played when feeding them the last item of wheat within a stack                                                                                                        |
| [MC-248588](https://bugs.mojang.com/browse/MC-248588) | The "mobGriefing" gamerule doesn't prevent burning entities from being able to decrease the levels of water or powder snow cauldrons                                                            |
| [MC-253884](https://bugs.mojang.com/browse/MC-253884) | Particles produced from the bad omen effect being consumed cannot be seen by other player                                                                                                       |
| [MC-253721](https://bugs.mojang.com/browse/MC-253721) | Wrong logs when running /op @a                                                                                                                                                                  |
| [MC-263999](https://bugs.mojang.com/browse/MC-263999) | Zombies breaking doors do not show break particles                                                                                                                                              |

Although MC-27056 covers the most common permanent block breaking exploit, Paper's general patch for breaking permanent blocks is also planned, as well as the general patch for gravity block duplication.

### Upstream

| Bug                                                 | Source    | Name                                                                          |
|-----------------------------------------------------|-----------|-------------------------------------------------------------------------------|
| [MC-33041](https://bugs.mojang.com/browse/MC-33041) | Spigot    | Dedicated server logs "java.io.IOException: The handle is invalid" on startup |
| [MC-99075](https://bugs.mojang.com/browse/MC-99075) | *Unknown* | Cancelled block place (spawn protection) causes inventory desync              |

These patches are from upstream projects. Although these patches would ideally be included at some point, they are not currently a priority.

### Not Planned

| Bug                                                   | Name                                                                      | Alternative                                                             |
|-------------------------------------------------------|---------------------------------------------------------------------------|-------------------------------------------------------------------------|
| [MC-81098](https://bugs.mojang.com/browse/MC-81098)   | Redstone dust updates cause lag                                           | [alternate-current](https://github.com/SpaceWalkerRS/alternate-current) |
| [MC-179072](https://bugs.mojang.com/browse/MC-179072) | Creepers do not defuse when switching from Survival to Creative/Spectator | [Debugify](https://github.com/isXander/Debugify)                        |

These patches are not planned for any future release as they are already implemented in other mods.

## Disclaimers

Whiteout is in no way associated with the Paper project. Please do not contact the maintainers of the Paper project about Whiteout.

### License

Since all bug fixes provided by this mod are ported from Paper, said fixes inherit their licensing from the Paper project, which is licensed under the [GPLv3](./LICENSE).

[While some contributors to the Paper project chose to release their code under the more permissive MIT License](https://github.com/PaperMC/Paper/blob/master/LICENSE.md), it is easier, and legally permissible, to release this entire project under the GPLv3. Patches are however still annotated to attribute appropriate credit to their original authors.
