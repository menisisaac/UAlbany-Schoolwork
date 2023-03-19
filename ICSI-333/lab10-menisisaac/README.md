# Lab 10 - Networking
- Link to create your repository: https://classroom.github.com/a/7aiFEDMm
- Due date: **Nov. 21, 2022 (extended)**
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `client_server.c`
  - `.gitignore` (optional)

## Reading Materials and Resources
- Lecture Slides and readings
- `inclass-network` repository

## Task 1 - TCP Client/Server Socket Communication
1. Write a program that optionally accepts an address and a port from the command line. 
2. If there is no address and port on the command line, it should create a TCP socket and print the address (i.e. server mode). You can choose any port number for your server (>1024).
3. If there is an address and port, it should connect to it (i.e., client mode). 
4. Once the connections are set up, each side should enter a loop of receive, print what it received, then send a message. The `ping` from the client should be sent before entering that loop to start the process. Otherwise both sides will sit and listen without getting anything.
5. The message should be `ping` from the client and a `pong` from the server.
6. In order to test this on one machine, you will need to run the same program twice (in two separate terminals).
7. Run first in server mode, then run in client mode using the information printed from the server as your command line arguments.

## Example run
Running server mode first `./client_server`, it should print
```
TCP Server Socket Created with address 127.0.0.1 at port 3333
ping
ping
```
> Note: "ping" messages should print multiple times.

Running client mode after server `./client_server 127.0.0.1 3333` considering 127.0.0.1 and 3333 are ipaddress and port numbers for server. It should print
```
pong
pong

```
> Note: "pong" messages should print multiple times.

> **Hint**:
> - You will need to pick a port for the server - something over 1024.
> - Ensure that you open your firewall if needed to let the packets through.

## Rubric:
- Create server socket (1pt.)
- Bind server socket (1pt.)
- Listen for server connection (1pt.)
- Receive on server and print (1pt.)
- Send from server to client (1pt.)
- Create client socket (1pt.)
- Connect client to server (1pt.)
- Receive on client and print (1pt.)
- Send from client to server (1pt.)
- Close sockets (1pt.)
