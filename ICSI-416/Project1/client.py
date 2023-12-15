import socket
import struct
import tcp_transport

SERVERPORT = 10000
CACHEPORT = 20000

def getFile(path):
    f = open(path)
    return f.read()


def main():
    host = socket.gethostname()
    client = tcp_transport.initClient(host, CACHEPORT)
    clientToServer = tcp_transport.initClient(host, SERVERPORT)
    while True:
        data = input("Enter Command: ")
        if data == 'quit':
            break
        if data.split(' ')[0] == 'get':
            tcp_transport.send(client, data)
            print(tcp_transport.recieve(client))
            print(tcp_transport.recieve(client))
        if data.split(' ')[0] == 'put':
            tcp_transport.send(clientToServer, data)
            tcp_transport.send(clientToServer, getFile(data.split(' ')[1]))
            print(tcp_transport.recieve(clientToServer))

    clientToServer.close()
    client.close()

main()