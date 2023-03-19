all: testfork testfork-inst testpipe testpipe-inst stackviz
testfork:
			gcc -g -Wall -c testfork.c
testpipe:
			gcc -g -Wall -c testpipe.c
inst.o:
			gcc -g -Wall -c inst.c
stackviz:
			gcc -g -Wall -o stackviz stackviz.c
testfork-inst: inst.o testfork
			gcc -g -Wall -finstrument-functions -pthread testfork.c inst.o -o testfork-inst
testpipe-inst: inst.o testpipe
			gcc -g -Wall -finstrument-functions -pthread testpipe.c inst.o -o testpipe-inst
clean:
			rm *.o testpipe-inst testfork-inst stack.bin stackviz