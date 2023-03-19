all: test test-inst stackviz
test: 
			gcc -c test.c
test-inst: inst.o test
			gcc -finstrument-functions test.c inst.o -o test-inst
inst.o:
			gcc -c inst.c
stackviz:
			gcc -o stackviz stackviz.c
clean:
			rm *.o test-inst stack.bin



