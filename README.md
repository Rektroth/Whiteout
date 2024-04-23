# Whiteout

Whiteout is an attempt to implement the bug fixes offered by the [Paper](https://github.com/PaperMC/Paper) project in Fabric.

## Patches
### Present

| Bug       | Lazy? | Name                                                                                                |
|-----------|-------|-----------------------------------------------------------------------------------------------------|
| [MC-4](https://bugs.mojang.com/browse/MC-4)           | Yes   | Item drops sometimes appear at the wrong location       |
| [MC-158900](https://bugs.mojang.com/browse/MC-158900) | No    | "bad packet id 26" upon connecting after tempban expire |

Some of the patches are "lazy". This means that the Paper maintainers are not incredibly pleased with the manner in which the bug was patched, but the patch works nonetheless. These patches will be disabled by default when configurability is implemented.

### Planned

| Bug       | Name                                                                                                                                                                                            |
|-----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [MC-11193](https://bugs.mojang.com/browse/MC-11193)   | The order in which powerable blocks (e.g. redstone dust blocks) along a wire are powered or de-powered is not clearly defined and causes a non-deterministic behavior for redstone contraptions |
| [MC-27056](https://bugs.mojang.com/browse/MC-27056)   | You can blow the extension off a piston                                                                                                                                                         |
| [MC-33041](https://bugs.mojang.com/browse/MC-33041)   | Dedicated server logs "java.io.IOException: The handle is invalid" on startup                                                                                                                   |
| [MC-81098](https://bugs.mojang.com/browse/MC-81098)   | Redstone dust updates cause lag                                                                                                                                                                 |
| [MC-99075](https://bugs.mojang.com/browse/MC-99075)   | Cancelled block place (spawn protection) causes inventory desync                                                                                                                                |
| [MC-108513](https://bugs.mojang.com/browse/MC-108513) | end crystal did not spawn/despawn correct after enderdragon respawned                                                                                                                           |
| [MC-123450](https://bugs.mojang.com/browse/MC-123450) | Item frames play sounds when the item within them is read from NBT                                                                                                                              |
| [MC-145260](https://bugs.mojang.com/browse/MC-145260) | Whitelist on state inconsistency                                                                                                                                                                |
| [MC-145656](https://bugs.mojang.com/browse/MC-145656) | Attribute "follow_range" is not working to hostile mobs before they find the target                                                                                                             |
| [MC-157464](https://bugs.mojang.com/browse/MC-157464) | Villagers move around in bed or even leave the bed when food is thrown at them                                                                                                                  |
| [MC-159283](https://bugs.mojang.com/browse/MC-159283) | The End terrain does not generate in multiple rings centered around the world center                                                                                                            |
| [MC-163962](https://bugs.mojang.com/browse/MC-163962) | Villager demand values descrease indefinitely                                                                                                                                                   |
| [MC-167279](https://bugs.mojang.com/browse/MC-167279) | Bees get stuck in the void                                                                                                                                                                      |
| [MC-171420](https://bugs.mojang.com/browse/MC-171420) | OP players get kicked for not being on the whitelist (enforce = on)                                                                                                                             |
| [MC-179072](https://bugs.mojang.com/browse/MC-179072) | Creepers do not defuse when switching from Survival to Creative/Spectator                                                                                                                       |
| [MC-188840](https://bugs.mojang.com/browse/MC-188840) | *This ticket covered piston-based block duplication. It is unknown why the ticket was privated/deleted.*                                                                                        |
| [MC-191591](https://bugs.mojang.com/browse/MC-191591) | Saddles lose their NBT data when equipped on horses, zombie horses, skeleton horses, mules or donkeys via right-clicking                                                                        |
| [MC-235045](https://bugs.mojang.com/browse/MC-235045) | Entity type tags suggestions don't work in target selectors                                                                                                                                     |
| [MC-243057](https://bugs.mojang.com/browse/MC-243057) | Furnace recipes don't consider alternatives when first option of recipe is used for fuel                                                                                                        |
| [MC-244739](https://bugs.mojang.com/browse/MC-244739) | Goat eating sounds aren't played when feeding them the last item of wheat within a stack                                                                                                        |
| [MC-248588](https://bugs.mojang.com/browse/MC-248588) | The "mobGriefing" gamerule doesn't prevent burning entities from being able to decrease the levels of water or powder snow cauldrons                                                            |
| [MC-252817](https://bugs.mojang.com/browse/MC-252817) | Placing a map into an item frame and removing it does not remove the green player marker                                                                                                        |
| [MC-253884](https://bugs.mojang.com/browse/MC-253884) | Particles produced from the bad omen effect being consumed cannot be seen by other player                                                                                                       |
| [MC-253721](https://bugs.mojang.com/browse/MC-253721) | Wrong logs when running /op @a                                                                                                                                                                  |
| [MC-257487](https://bugs.mojang.com/browse/MC-257487) | The ender dragon's name is not reset when it is respawned in the end                                                                                                                            |

Although MC-27056 covers the most common permanent block breaking exploit, Paper's general patch for breaking permanent blocks is also planned, as well as the general patch for gravity block duplication.

## Disclaimers

Whiteout is in no way associated with the Paper project. Please do not contact the maintainers of the Paper project about Whiteout.

### License

Since all bug fixes provided by this mod are ported from Paper, said fixes inherit their licensing from the Paper project, which is licensed under the [GPLv3](./LICENSE).

[While some contributors to the Paper project chose to release their code under the more permissive MIT License](https://github.com/PaperMC/Paper/blob/master/LICENSE.md), it is easier, and legally permissible, to release this entire project under the GPLv3. Patches are however still annotated to attribute appropriate credit to their original authors.