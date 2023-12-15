import snw_transport
import tcp_transport
import socket

SERVER_PORT = 10000
CACHE_PORT = 20000
TYPE = 'TCP'
FILES = {}


def main():
    if TYPE == 'UDP':
        cache = snw_transport.initServer(CACHE_PORT)
    else:
        cache = tcp_transport.initServer(CACHE_PORT)
    cache.listen(1)

    while True:
        connection, client_address = cache.accept()
        while True:
            data = tcp_transport.recieve(connection)
            if data == None:
                connection.close()
                continue
            filename = data.split(' ')[1]
            if filename not in FILES:
                server = tcp_transport.initClient(socket.gethostname(), 10000)
                tcp_transport.send(server, data)
                tcp_transport.send(connection, tcp_transport.recieve(server))
                FILES[filename] = tcp_transport.recieve(server)
                server.close()
                tcp_transport.send(connection, FILES[filename])
            else:
                tcp_transport.send(connection, 'File delivered from cache.')
                tcp_transport.send(connection, FILES[filename])


main()