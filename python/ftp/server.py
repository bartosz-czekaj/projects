import socket
import threading
import socketserver

def recvuntil(socket, signature, szlimit):
    data = b""
    while True:
        d = socket.recv(1)
        if d == "":
            raise Exception("disconnected")
        data += d
        if signature in data:
            return data
        if len(data) > szlimit:
            raise Exception("too much data")

class ThreadedTCPRequestHandler(socketserver.BaseRequestHandler):

    def handle_USER(self, args):
        if args == 'anonymous':
            #print("Logging in user anonymous")
            self.request.sendall(b"331 User name OK, give me e-mail.\r\n")
        else:
            #print("Log in incorrect (%s)" % args)
            self.request.sendall(b"530 Only anonymous exists.\r\n")

    def handle_PASS(self, args):
        self.request.sendall(b"230 User logged in, proceed.\r\n")

    def handle_SYST(self, args):
        self.request.sendall(b"215 UNIX Type: L8\r\n")

    def handle_PWD(self, args):
        reply = "257 %s\r\n" % (self.get_pwd())
        #print("# reply: %s" % reply)
        self.request.sendall(bytes(reply, 'utf-8'))

    def handle_TYPE(self, args):
        self.type = args
        self.request.sendall(b'200 Command okay.\r\n')

    def handle_SIZE(self, argss):
        file = self.get_file_by_fname(argss)
        #print("SIZE file %s" % file)
        #print("SIZE argss %s" % argss)
        if file is None:
            #print("SIZE File not found")
            self.request.sendall(b"550 File not found.\r\n")
            return
        #print("SIZE len %i" % len(file))    
        self.request.sendall(b"213 %i\r\n" % len(file)) 

    def handle_QUIT(self, args):
        self.request.sendall(b'221 Bye.\r\n')
        self.end_connection = True

    def handle_PASV(self, argss):
        ip = [127,0,0,1]
        port = self.data_port
        reply = b"227 Entering Passive Mode (%i,%i,%i,%i,%i,%i).\r\n" % (
            ip[0],ip[1],ip[2],ip[3],
            port / 256, port % 256
        )
        print(reply)
        self.request.sendall(reply)


    def handle_LIST(self, arg):
        self.request.sendall(b'150 Opening IMAGE mode data connection for LIST\r\n')
        s = self.data_s.accept()[0]
        reply = []
        files = self.get_file_by_namelist(self.cwd)
        if type(files) is not dict:
            # TODO send error
            return
        for el, el_data in files.items():
            if type(el_data) is dict:
                reply.append("drwxrwx--- 1 root vboxsf 4096 Oct  4 21:58 %s" % el)
            else:
                reply.append("-rwxrwx--- 1 root vboxsf 1234 Oct  4 21:58 %s" % el)

        s.sendall(bytes("%s%s" % ('\r\n'.join(reply),'\r\n'), "utf-8"))
        s.shutdown(socket.SHUT_RDWR)
        s.close()

        self.request.sendall(b'226 Transfer complete\r\n')

    def handle_RETR(self, args):
        file = self.get_file_by_fname(args)
        if type(file) is dict:
            self.request.sendall(b'550 File not found.\r\n')
            return

        assert type(file) is bytes

        self.request.sendall(b'150 Opening IMAGE mode data connection for RETR\r\n')
        s = self.data_s.accept()[0]
        s.sendall(file)
        s.shutdown(socket.SHUT_RDWR)
        s.close()

        self.request.sendall(b'226 Transfer complete\r\n')

    def handle_CWD(self, args):
        file = self.get_file_by_fname(args)
        if file is None or type(file) is not dict:
            self.request.sendall(b'550 File not found.\r\n')
            return

        self.cwd = self.fname_to_namelist(args)
        #print("--> Current Directory: %s" % repr(self.cwd))

        self.request.sendall(b'250 OK.\r\n')

    def handle_FEAT(self, args):
        self.request.sendall(
        b'211-Features:\r\n'
        b'211 End\r\n')

    def handle_MDTM(self, args):
        file = self.get_file_by_fname(args)
        if file is None:
            self.request.sendall(b'550 File not found.\r\n')
            return

        self.request.sendall(b'213 21421105000001.123\r\n')

    def handle_HELP(self, args):
        self.request.sendall(b'500 Nope\r\n')

    def handle_OPTS(self, args):
        self.request.sendall(b'500 Nope\r\n')

    def handle_opts(self, args):
        self.request.sendall(b'500 Nope\r\n')    

    def handle_CDUP(self, args):
        self.cwd.pop()
        self.request.sendall(b'200 OK\r\n')

    ########################
    def get_file_by_fname(self, fname):
        #print("# get_file_by_fname fname % s" % fname)
        namelist = self.fname_to_namelist(fname)
        result = self.get_file_by_namelist(namelist)
        #print("# get_file_by_fname result % s" % result)
        return result

    def get_file_by_namelist(self, namelist):
        if namelist == [''] or not namelist:
            return self.files

        d = self.files
        sz = len(namelist)
        for i, el in enumerate(namelist):
            if el not in d:
                return None

            el = d[el]

            if i == sz-1:
                return el

            if type(el) is dict:
                d = el
            continue

        return None

    def fname_to_namelist(self, fname):
        if fname.startswith('/ /'):
            fname = fname[3:].strip()
        elif fname.startswith('/'):
            fname = fname[1:].strip()
        else:
            fname = self.namelist_to_fname(self.cwd) + '/' + fname
        splitted = list(filter(None,fname.split('/')))
        #print("splitted 1: %s" % splitted) 
        #if splitted == [''] or not splitted:
        #   splitted = []
        #print("splitted 2: %s" % splitted)    
        return splitted 

    def namelist_to_fname(self, namelist):
        return '/'+'/'.join(namelist)    

    def get_pwd(self):
        return self.namelist_to_fname(self.cwd)

    def handle(self):
        self.request.sendall(b"220 Livestream FTP Server.\r\n")
        self.request.settimeout(60)
        self.end_connection = False
        self.cwd = []

        self.data_s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.data_s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.data_s.bind(("0.0.0.0", 0))
        self.data_s.listen(1)
        self.data_port = self.data_s.getsockname()[1]

        print("self.data_port: %s" % self.data_s)
 
        methods = {}

        self.files = {
            "xyz/../sasza" : b"szasza",
            "asd" : b"<h1>asd</h1><u>asd</u>",
            "dir" : {
                "abc" : b"abc"
                }
        }

        for method_name in dir(self):
            if method_name.startswith("handle_"):
                cmd = method_name.replace("handle_", "")
                methods[cmd] = getattr(self, method_name)

        '''methods = {
        "USER" : self.handle_USER,
        "PASS" : self.handle_PASS,
        "SYST" : self.handle_SYST,
        "PWD"  : self.handle_PWD,
        }
        '''
        print("######## NEW FTP CONNECTION ###############")
        while not self.end_connection:
            ln = str(recvuntil(self.request, b"\r\n", 4096), 'utf-8').strip()
            print("#ln-->%s" %ln)

            command, argss = (ln.split(" ", 1) + [""])[:2]

            if command in methods:
                #print("# command %s" % command)
                #print("# argss %s" % argss)  
                methods[command](argss)
                continue
            else:
                print("------------> command unknown %s" % command)
            break
    #data = str(self.request.recv(1024), 'ascii')
    #cur_thread = threading.current_thread()
    #response = bytes("{}: {}".format(cur_thread.name, data), 'ascii')
    #self.request.sendall(response)
    ##print(self.request.recv(4096))

class ThreadedTCPServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    pass

if __name__ == "__main__":
    # Port 0 means to select an arbitrary unused port
    HOST, PORT = "0.0.0.0", 1021

    server = ThreadedTCPServer((HOST, PORT), ThreadedTCPRequestHandler)
    ip, port = server.server_address

    # Start a thread with the server -- that thread will then start one
    # more thread for each request
    server_thread = threading.Thread(target=server.serve_forever)
    # Exit the server thread when the main thread terminates
    server_thread.daemon = True
    server_thread.start()
    print("Server loop running in thread:", server_thread.name)

    #client(ip, port, "Hello World 1")
    #client(ip, port, "Hello World 2")
    #client(ip, port, "Hello World 3")

    server_thread.join()
    server.shutdown()
    server.server_close()
