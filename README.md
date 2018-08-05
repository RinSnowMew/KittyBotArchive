# KittyBot
Kittybot is a discord bot that can handle everything from general searches, post tracking, dice rolling, code compilation, and more!

## Commands
The highest tier of command is a developer command. Each tier can do everything below it as well as what's in it.

##### Dev Commands
- `save`: Force-save the state of kittybot's state. This is a potentially expensive command.
- `invitelink`: Creates an invite link allowing anyone with the link to add kittybot to a server.

##### Mod Commands
- `stopList`: Disables the blacklist feature.
- `startList`: Enables the blacklist feature which trims messages containing inapporpraite words.
- `mute`: Mutes kittybot.
- `unmute`: Unmutes kittybot.
- `addpoints <@user> <number>`: Adds points.
- `removepoints <@user> <number>`: Removes points from the specified user.
- `ignore <@user>`: Kittybot will ignore commands from a specified user (Overrides role ignores).
- `unignore <@user>`: Kittybot will no longer ignore commands from specified user (Does not override role ignores).
- `ignorerole <#role>`: Kittybot will ignore commands from the entire specified role.
- `unignorerole <#role>`: Kittybot will no longer ignore commands from the entire specified role (Overridden by per-user ignores).
- `auth <@user>`: Promotes the specified user to a kittybot mod, allowing access to mod commands.
- `deauth <@user>`: Demotes the specified user from a kittybot mod role.
- `authrole <#role>`: Promotes role to be kittybot mods, allowing access to mod commands.
- `deauthrole <#role>`: Demotes role from kittybot mod.
- `changeTrigger <new trigger character>`: Changes the character you use to issue commands to kittybot. By default, `!`.
- `startPoll <option 1, ..., option n>`: Creates a poll with a series of comma separated options.
- `addchoice <option n + 1>`: Adds a choice to the poll after initial creation.
- `endPoll`: Ends the poll and displays the results in chat.
- `E621Limit <#>`: Sets the maximum number of images that are grabbed when searching E621. By default, `1`.

##### General Commands
- `help`: Shows general help information about kittybot, displays the patreon 
- `info`: Developer info for kittybot
- `boop <@user 1 ... @user n>`: Serves as a ping command of sorts! Also boops all `@users` specified in the command line.
- `roll <number of dice>d<dice sides>`: Rolls n s-sided dice in `n`d`s` format. Limit of 100 dice, and 100 sides.
- `choose <option 1, ..., option n>`: Kittybot will randomly choose an entry in a comma-separated list of choices.
- `points`: Tells the person issuing the command how many points they have.
- `bet`: Try your luck with slots based betting to get more points! Each play costs 100 points.
- `highlow`: Try your luck guessing a number of points! Each play costs 100 points.
- `rpstart`: Begins logging messages in the channel the command was issued in.
- `rpend [optional name]`: Ends logging messages in the specified channel, and hands back the rp log file in `RP.txt`, or `[optional name].txt`.
- `vote <number>`: Vote on a specified option number in the currently active poll.
- `getPoll`: Gets the current poll and displays the options.
- `getResults`: Gets the current results of the current poll, but doesn't end the poll.
- `wolfram <query>`: Asks wolfram about the specified query, and posts the short response.
- `<c++ | g++> <code>`: Compiles specified C/C++ code with gcc (Coliru) compiler. Either c++ or g++ are both valid compilation commands for a snippet of code that follows. If `c` or `C` are specified as the syntax highlited colors, they are ignored. Code snippet denoting characters are ignored.
- `jdoodle <code>`: Compiles specified Java with java 1.8+ (JDoodle) compiler. Highlighting and code snippet characters may not be ignored.
- `e621 <tag1 ... tagN>`: Searches E621 with the specified tags and posts a random number of pictures, default `1`, specified by `E621Limit`.
- `derp`: Searches derpibooru using the specified search parameters. Provides `1` random image result.
- `givefishy`: Ping command in which Kittybot thanks the user for providing kittybot with a fishy.
- `praise`: Ping command in which Kittybot praises the concept of beans.