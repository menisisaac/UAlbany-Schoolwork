Time: 4 Hours for part 1 and 2 mostly debugging compact, 2 hours for part 3 prob would take another couple of hours to perfect part 3
One suggestion would be to add the slottedpage.save method to the methods we need to do since you need to really understand it for compact 
For my buffer I went with a least recently used replacement strategy which is a bit iffy, for which I went 
down a rabbit hole of bugs and ran out of time to fully fix. The iterator is especially tricky as you can't reuse any code and it resets the buffer with the last x pages of the file using this strategy
