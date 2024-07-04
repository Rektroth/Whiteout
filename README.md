# Whiteout

Whiteout is an attempt to implement the gameplay and mechanic consistency patches included in the [Paper](https://github.com/PaperMC/Paper) project stream in Fabric.

## Patches

### Present

| Bug                                                   | Source | Lazy? | Mojang Prioritization | Name                                                                                                                                              |
|-------------------------------------------------------|--------|-------|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| [MC-4](https://bugs.mojang.com/browse/MC-4)           | Paper  | Yes   | Low                   | Item drops sometimes appear at the wrong location                                                                                                 |
| [MC-27056](https://bugs.mojang.com/browse/MC-27056)   | Paper  | Yes   | Low                   | You can blow the extension off a piston                                                                                                           |
| [MC-84789](https://bugs.mojang.com/browse/MC-84789)   | Paper  | No    | Low                   | Tamed wolves beg for bones while wild wolves don't *(this has been partially fixed in the vanilla game, so this fix is no longer included Paper)* |
| [MC-108513](https://bugs.mojang.com/browse/MC-108513) | Paper  | Yes   | *None*                | end crystal did not spawn/despawn correct after enderdragon respawned                                                                             |
| [MC-123450](https://bugs.mojang.com/browse/MC-123450) | Paper  | No    | Low                   | Item frames play sounds when the item within them is read from NBT                                                                                |
| [MC-123848](https://bugs.mojang.com/browse/MC-123848) | Paper  | No    | Normal                | Item frames (and items within) when removed from a ceiling, drop atop, not under, the block                                                       |
| [MC-153086](https://bugs.mojang.com/browse/MC-153086) | Paper  | No    | Low                   | Beacons always play deactivating sound when broken, even when not powered                                                                         |
| [MC-157464](https://bugs.mojang.com/browse/MC-157464) | Paper  | No    | Normal                | Villagers move around in bed or even leave the bed when food is thrown at them                                                                    |
| [MC-158900](https://bugs.mojang.com/browse/MC-158900) | Paper  | No    | *None*                | "bad packet id 26" upon connecting after tempban expire                                                                                           |
| [MC-163962](https://bugs.mojang.com/browse/MC-163962) | Paper  | No    | *None*                | Villager demand values decrease indefinitely                                                                                                      |
| [MC-167279](https://bugs.mojang.com/browse/MC-167279) | Paper  | No    | Normal                | Bees get stuck in the void                                                                                                                        |
| [MC-188840](https://bugs.mojang.com/browse/MC-188840) | Paper  | Yes   | *N/A*                 | *This ticket covered piston-based block duplication. It is unknown why the ticket was privated/deleted.*                                          |
| [MC-210802](https://bugs.mojang.com/browse/MC-210802) | Paper  | No    | *None*                | Inactive sheep eat grass                                                                                                                          |
| [MC-224454](https://bugs.mojang.com/browse/MC-224454) | Paper  | No    | Normal                | Entities don't recognize azaleas or flowering azaleas as obstacles when pathfinding                                                               |
| [MC-248588](https://bugs.mojang.com/browse/MC-248588) | Paper  | No    | Low                   | The "mobGriefing" gamerule doesn't prevent burning entities from being able to decrease the levels of water or powder snow cauldrons              |
| [MC-253721](https://bugs.mojang.com/browse/MC-253721) | Paper  | No    | Important             | Wrong logs when running /op @a                                                                                                                    |
| [MC-257487](https://bugs.mojang.com/browse/MC-257487) | Paper  | No    | Low                   | The ender dragon's name is not reset when it is respawned in the end                                                                              |
| [MC-259571](https://bugs.mojang.com/browse/MC-259571) | Paper  | No    | Normal                | Last player game mode not saved after player dies or the game is reloaded                                                                         |
| [MC-262422](https://bugs.mojang.com/browse/MC-262422) | Paper  | No    | Normal                | Lightning bolts during thunderstorms can strike players in spectator game mode                                                                    |
| *N/A*                                                 | Paper  | Yes   | *N/A*                 | Accessing the nether roof                                                                                                                         |
| *N/A*                                                 | Paper  | Yes   | *N/A*                 | Breaking permanent blocks                                                                                                                         |

Patches that are marked as "lazy" generally meet at least one of the following criteria:

- The new behavior created by the patch is (possibly) not what's actually intended by Mojang
- The patch *circumvents* the issue but doesn't truly correct it
- The manner in which the patch is applied is not technically elegant and might cause unforeseen issues and/or performance degradation

Mojang prioritization is also noted here. If a ticket has *not* been assigned a priority by Mojang, this potentially means that a Mojang employee has not yet personally reviewed the ticket and confirmed that the behavior is unintentional.

Patches that are considered "lazy" or are for a bug ticket which does not have a Mojang-assigned priority are disabled by default. You will have to enable them in `.minecraft/configs/whiteout.properties` if you wish to have them enabled.

### Planned

| Bug                                                   | Source    | Name                                                                                                                     |
|-------------------------------------------------------|-----------|--------------------------------------------------------------------------------------------------------------------------|
| [MC-33041](https://bugs.mojang.com/browse/MC-33041)   | Spigot    | Dedicated server logs "java.io.IOException: The handle is invalid" on startup                                            |
| [MC-92282](https://bugs.mojang.com/browse/MC-33041)   | Spigot    | Mob spawner block_entity_data does not copy SpawnData data to missing SpawnPotentials                                    |
| [MC-121706](https://bugs.mojang.com/browse/MC-121706) | Purpur    | Skeletons and illusioners aren't looking up / down at their target while strafing                                        |
| [MC-145260](https://bugs.mojang.com/browse/MC-145260) | Paper     | Whitelist on state inconsistency                                                                                         |
| [MC-145656](https://bugs.mojang.com/browse/MC-145656) | Paper     | Attribute "follow_range" is not working to hostile mobs before they find the target                                      |
| [MC-147659](https://bugs.mojang.com/browse/MC-147659) | Paper     | Some witch huts spawn the incorrect cat                                                                                  |
| [MC-157395](https://bugs.mojang.com/browse/MC-157395) | Spigot    | Small armor stands drop no loot table                                                                                    |
| [MC-159283](https://bugs.mojang.com/browse/MC-159283) | Paper     | The End terrain does not generate in multiple rings centered around the world center                                     |
| [MC-171420](https://bugs.mojang.com/browse/MC-171420) | Paper     | OP players get kicked for not being on the whitelist (enforce = on)                                                      |
| [MC-174630](https://bugs.mojang.com/browse/MC-174630) | Paper     | Secondary beacon effect remains when switching primary effect                                                            |
| [MC-191591](https://bugs.mojang.com/browse/MC-191591) | Paper     | Saddles lose their NBT data when equipped on horses, zombie horses, skeleton horses, mules or donkeys via right-clicking |
| [MC-200092](https://bugs.mojang.com/browse/MC-200092) | Paper     | /setworldspawn seems to ignore the 'angle' parameter                                                                     |
| [MC-235045](https://bugs.mojang.com/browse/MC-235045) | Paper     | Entity type tags suggestions don't work in target selectors                                                              |
| [MC-243057](https://bugs.mojang.com/browse/MC-243057) | Paper     | Furnace recipes don't consider alternatives when first option of recipe is used for fuel                                 |
| [MC-244739](https://bugs.mojang.com/browse/MC-244739) | Paper     | Goat eating sounds aren't played when feeding them the last item of wheat within a stack                                 |
| [MC-253884](https://bugs.mojang.com/browse/MC-253884) | Paper     | Particles produced from the bad omen effect being consumed cannot be seen by other player                                |
| [MC-263999](https://bugs.mojang.com/browse/MC-263999) | Paper     | Zombies breaking doors do not show break particles                                                                       |
| [MC-264285](https://bugs.mojang.com/browse/MC-264285) | Spigot    | Unbreakable flint and steels are completely consumed when igniting a creeper                                             |
| [MC-264979](https://bugs.mojang.com/browse/MC-264979) | Spigot    | Fresh installations print NoSuchFileException for server.properties                                                      |

### Not Planned

| Bug                                                   | Name                                                                                                                                                                                            | Alternative                                                             |
|-------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------|
| [MC-11193](https://bugs.mojang.com/browse/MC-11193)   | The order in which powerable blocks (e.g. redstone dust blocks) along a wire are powered or de-powered is not clearly defined and causes a non-deterministic behavior for redstone contraptions | [alternate-current](https://github.com/SpaceWalkerRS/alternate-current) |
| [MC-81098](https://bugs.mojang.com/browse/MC-81098)   | Redstone dust updates cause lag                                                                                                                                                                 | [alternate-current](https://github.com/SpaceWalkerRS/alternate-current) |
| [MC-179072](https://bugs.mojang.com/browse/MC-179072) | Creepers do not defuse when switching from Survival to Creative/Spectator                                                                                                                       | [Debugify](https://github.com/isXander/Debugify)                        |

These patches are not planned for any future release as they are already implemented in other mods.

## Disclaimers

Whiteout is in no way associated with the Paper project. Please do not contact the maintainers of the Paper project about Whiteout.

### License

Since all bug fixes provided by this mod are ported from Paper, said fixes inherit their licensing from the Paper project, which is licensed under the [GPLv3](./LICENSE).

[While some contributors to the Paper project chose to release their code under the more permissive MIT License](https://github.com/PaperMC/Paper/blob/master/LICENSE.md), it is easier, and legally permissible, to release this entire project under the GPLv3. Patches are however still annotated to attribute appropriate credit to their original authors.

### Compatibility

Best effort is made to keep Whiteout compatible with [Lithium](https://github.com/caffeinemc/lithium-fabric). Reporting of issues with compatibility is appreciated.

Compatibility with other mods is not guaranteed, but if you make an issue ticket I may get around to it. Attempts at compatibility are at my discretion. I generally will not make an effort maintain compatibility with very niche mods or large mod-packs, and will make no attempt at compatibility with mods that are fundamentally incompatible with the goals of Whiteout.

Compatibility with Sinytra Connector and similar projects is **not supported at all**. If you have issues using Whiteout with such layers, that is your problem.

### Backporting

Deprecated versions of Minecraft will **never** be supported. ~~This is a hobby project.~~ *Actually, it's more accurate to say that I made this mod - and even learned how to mod in the first place - because <u>I personally</u> needed these fixes in Fabric. I only made this project public because I figured there might be at least a handful of others that want/need the same.* I do not care nor have the time to maintain backports. Please don't bother asking.
