CC?=gcc
CFLAGS?=-g
BINARIES=map limits

default:
	make services
	make producer
	make pipetools
	make consumer
	make main

main: main.c
	$(CC) $(CFLAGS) main.c -o main

producer: producer.c
	$(CC) $(CFLAGS) producer.c -o producer

consumer: consumer.c
	$(CC) $(CFLAGS) consumer.c -o consumer

pipetools: pipeTools.c
	$(CC) $(CFLAGS) -o pipeTools pipeTools.c

.PHONY services: services/physical/charToBits.c services/physical/addParity.c services/application/outputToFile.c services/application/toUpperCase.c services/application/readInput.c services/physical/bitsToChar.c services/physical/validateRemoveParity.c services/datalink/errorSimulation.c services/datalink/toFrame.c services/datalink/fromFrame.c
	$(CC) $(CFLAGS) services/physical/charToBits.c -o charToBits
	$(CC) $(CFLAGS) services/physical/addParity.c -o addParity 
	$(CC) $(CFLAGS) services/physical/validateRemoveParity.c -o validateRemoveParity
	$(CC) $(CFLAGS) services/physical/bitsToChar.c -o bitsToChar
	$(CC) $(CFLAGS) services/datalink/toFrame.c -o toFrame
	$(CC) $(CFLAGS) services/datalink/fromFrame.c -o fromFrame
	$(CC) $(CFLAGS) services/application/readInput.c -o readInput
	$(CC) $(CFLAGS) services/application/toUpperCase.c -o toUpperCase
	$(CC) $(CFLAGS) services/application/outputToFile.c -o outputToFile
	$(CC) $(CFLAGS) services/datalink/errorSimulation.c -o errorSimulation
	mv charToBits services/bin/charToBits
	mv addParity services/bin/addParity
	mv readInput services/bin/readInput
	mv bitsToChar services/bin/bitsToChar
	mv validateRemoveParity services/bin/validateRemoveParity
	mv fromFrame services/bin/fromFrame
	mv toFrame services/bin/toFrame
	mv toUpperCase services/bin/toUpperCase
	mv outputToFile services/bin/outputToFile
	mv errorSimulation services/bin/errorSimulation

clean:
	find . -type f -executable -exec rm '{}' \;
	rm *.binf || true
	rm *.outf || true
	rm *.chck || true
	rm *.done || true
