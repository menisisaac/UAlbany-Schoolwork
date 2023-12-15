CC?=gcc
CFLAGS?=-g
BINARIES=map limits

default:
	make server
	make client

server: server.c
	$(CC) $(CFLAGS) server.c -o server

client: client.c
	$(CC) $(CFLAGS) client.c -o client

clean:
	find . -type f -executable -exec rm '{}' \;
	rm *.binf || true
	rm *.outf || true
	rm *.chck || true
	rm *.done || true
