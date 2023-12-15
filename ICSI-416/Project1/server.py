import snw_transport
import tcp_transport

SERVER_PORT = 10000
CACHE_PORT = 20000
TYPE = 'TCP'
FILES = {'hello': 'purple'}


def processData(sock, data):
    processed = data.split(' ')
    command = processed[0]
    if command == 'get':
        tcp_transport.send(sock, 'File delivered from origin')
        tcp_transport.send(sock, FILES[processed[1]])
    if command == 'put':
        FILES[processed[1]] = tcp_transport.recieve(sock)
        tcp_transport.send(sock, 'File successfully uploaded')

def main():
    if TYPE == 'UDP':
        server = snw_transport.initServer(SERVER_PORT)
    else:
        server = tcp_transport.initServer(SERVER_PORT)
    server.listen(1)

    while True:
        connection, client_address = server.accept()
        while True:
            data = tcp_transport.recieve(connection)
            if data == None:
                connection.close()
                break
            processData(connection, data)

main()