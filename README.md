# DevTools
Modpack and/or developertools for MineCraft

Currently the mod adds only one tool, the Chunk Analyzer.

##Chunk Analyzer
This tool will when used on a block analyze all the blocks in that chunk and add the counts of white- or blacklisted blocks to the internal memory of the tool. Sneaking while activating the tool will print the current data (average counts per chunk if more than one chunk is analyzed).

Config settings allow you to toggle between white- and blacklist mode, change output verbosity, display name settings and maximum number of chunks allowed to be scanned at once.

### Technical limitations
The analyzer uses a 32-bit integer to store blockcounts. That means that it can't count higher than 2,147,483,647. Shouldn't be a problem unless you set the chunklimit to 33K-ish and whitelist stone. Don't do that.
