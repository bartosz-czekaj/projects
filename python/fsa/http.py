import string
import pprint
import socketserver
import socket
from enum import Enum

packet = """GET / HTTP/1.1
host: localhost:3000
connection: keep-alive
cache-control: max-age=0
upgrade-insecure-requests: 1
user-agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36
accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
accept-encoding: gzip, deflate, br
accept-language: pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7
cookie: Idea-89d5e830=0956428d-cd71-475a-b5f4-16420704a575 

"""
#packet.replace('\r','').replace('\n', '\r\n')

class State(Enum):
     S_START = 0
     S_VERB = 1
     S_WHITESPACE1 = 2
     S_QUERY = 3
     S_WHITESPACE2 = 4
     S_VER = 5
     S_NEWLINER0 = 6
     S_HEADERS_START = 7
     S_HEADER = 8
     S_WHITESPACE3 = 9
     S_VALUE = 10
     S_NEWLINER1 = 11

     S_BAD = -1

def parser_http_packet(p):
     verb = None
     query = None
     ver = None
     headers = []
     header_name = None
     header_value = None

     current_state = State.S_START

     CH_CONTROL = set([chr(i) for i in range(0,32)])
     CH_CONTROL.add(127)
     CH_HEADER = string.ascii_uppercase + string.ascii_lowercase + string.digits +'-'

     it = iter(p)

     while True:
          ch = next(it)
          
          if(current_state == State.S_START):
               if ch.isupper():
                    current_state = State.S_VERB
                    verb=ch
                    continue;
               
          if current_state == State.S_VERB:
               if ch.isupper():
                    #current_state = State.S_VER
                    verb+=ch
                    continue;
               if ch == " ":    
                    current_state = State.S_WHITESPACE1
                    continue

          if current_state == State.S_WHITESPACE1:
               if ch == " ":
                    continue
               if(ch not in CH_CONTROL):
                    query = ch
                    current_state = State.S_QUERY
                    continue

          if current_state == State.S_QUERY:
               if ch == " ":    
                    current_state = State.S_WHITESPACE2
                    continue
               if(ch not in CH_CONTROL):
                    query+=ch
                    continue;

          if current_state == State.S_WHITESPACE2:
               if ch == " ":
                    continue
               if(ch not in CH_CONTROL):
                    ver = ch
                    current_state = State.S_VER
                    continue

          if current_state == State.S_VER:
               if ch == '\r':
                    current_state = State.S_NEWLINER0
                    continue
               if ch == '\n':
                    current_state = State.S_HEADERS_START
                    continue
               if(ch not in CH_CONTROL and ch != " "):
                    ver += ch
                    continue

          if current_state == State.S_NEWLINER0: 
               if ch == "\n":
                    current_state = State.S_HEADERS_START
                    continue

          if current_state == State.S_HEADERS_START: 
               if ch == "\r":
                    current_state = State.S_NEWLINER1
                    continue
               if ch == "\n":
                    break;
               if ch in CH_HEADER:
                    header_name = ch
                    current_state = State.S_HEADER
                    continue

          if current_state == State.S_HEADER:
               if ch in CH_HEADER:
                    header_name += ch
                    continue
               if ch == ":":
                    current_state = State.S_WHITESPACE3
                    continue

          if current_state == State.S_WHITESPACE3:
               if ch == ' ':
                    continue
               if ch not in CH_CONTROL:
                    header_value = ch
                    current_state = State.S_VALUE
                    continue

          if current_state == State.S_VALUE:
               if ch == '\r':
                    headers.append((header_name, header_value))
                    current_state = State.S_NEWLINER1
                    continue
               if ch == '\n':
                    headers.append((header_name, header_value))
                    current_state = State.S_HEADERS_START
                    continue
               if ch not in CH_CONTROL:
                    header_value += ch
                    continue     

          if current_state == State.S_NEWLINER1: 
               if ch == "\n":
                    break;

          print(current_state)

          current_state = State.S_BAD
          if current_state == State.S_BAD:
               return

     return {
          "verb" : verb,
          "query" : query,
          "ver" : ver,
          "headers" : headers
     }

#pprint.pprint(parser_http_packet(packet))

def recvuntil(sock, txt):
     d = ""
     while d.find(txt) == -1:
          try:
               dnow = sock.recv(1)
               if len(dnow) == 0:
                    return ("DISCONNECTED", d)
          except socket.timeout:
               return ("TIMEOUT", d)
          except socket.error as msg:
               return ("ERROR", d)     
          d += dnow.decode("utf-8")

     return ("OK", d)     


class MyTCPHandler(socketserver.BaseRequestHandler):
     def handle(self):
          # self.request is the TCP socket connected to the client
          data = recvuntil(self.request, "\r\n\r\n")
          print(data[1])
          
          http = parser_http_packet(data[1])

          if(http["query"] == "/"):
               self.request.sendall(
                    b"""HTTP/1.1 200 OK
Content-Type: text/html; charset=utf-8

it works! Try <a href="/asdf">/asdf</a>"""
                    )
               return
          if(http["query"] == "/asdf"):     
               self.request.sendall(
                    b"""HTTP/1.1 200 OK
                    Content-Type: text/html; charset=utf-8

                    it works AGAIN"""
                    )
               return
          self.request.sendall(
                    b"""HTTP/1.1 404 Not Found
                    Content-Type: text/html;charset=utf-8

                   NOT FOUND! Seriously"""
                    )
          return     


if __name__ == "__main__":
    HOST, PORT = "localhost", 9999

    # Create the server, binding to localhost on port 9999
    server = socketserver.TCPServer((HOST, PORT), MyTCPHandler)

    # Activate the server; this will keep running until you
    # interrupt the program with Ctrl-C
    server.serve_forever()