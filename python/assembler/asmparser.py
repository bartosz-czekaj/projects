import string
from enum import Enum

ASM_x86_REGS_32 =[
'EAX','ECX','EDX','EBX','ESP','EBP','ESI','EDI'
]
ASM_x86_REGS_16 =[
'AX','CX','DX','BX','SP','BP','SI','DI'
]
ASM_x86_REGS_8 =[
'AL','CL','DL','BL','AH','CH','DH','BH'
]

class RegType(Enum):
    RM = 1
    REG = 2

class TokenTypes(Enum):
    LABEL_INDICATOR = 1
    LITERAL_NUMBER = 2
    LITERAL_STR = 3
    IDENTIFIER = 4
    DIRECTIVE = 5

class OutputElement(Enum):
    LABEL = 1

class StreamException(Exception):
    pass

class AsmException(Exception):
    pass

class Stream():
    def __init__(self, data, ln_no):
        self.data = data
        self.stack = []
        self.reset()
        self.ln_no = ln_no

    def push(self):
        self.stack.append(self.i)

    def pop(self):
        self.i = self.stack.pop()

    def reset(self):
        self.i = 0

    def peek(self, size = 1, quiet = False):
        if(not quiet and self.i + (size - 1) >= len(self.data)):
            raise StreamException("out of chars")
        return self.data[self.i:self.i+size]

    def dis(self):
        self.stack.pop()    

    def get(self):
        ch = self.peek()
        self.i += 1
        return ch

    def unget(self):
        if(self.i == 0):
            raise StreamException("tried to unget on first char")
        self.i -= 1    

    def empty(self):
        return self.i == len(self.data)

    def gellnno(self):
        return self.ln_no


def s_qstring(s, t):
    if s.peek() != '"':
        return False
    s.get()
    strData = ""
    while not s.empty() and s.peek() != '"':
        strData += s.get()
    t.append((TokenTypes.LITERAL_STR, strData))

    return True

def s_declit(s, t):
    lit = ""
    while not s.empty() and s.peek() in string.digits:
        lit += s.get()

    if not lit:
        return False

    t.append((TokenTypes.LITERAL_NUMBER, (int(lit))))
    return True

def s_hexlit(s, t):
    if s.peek(2, True) != '0x':
        return False
    s.get()
    s.get()    
    lit = ""    
    while not s.empty() and s.peek() in string.hexdigits:
        lit += s.get()
    if not lit:
        return False
    t.append((TokenTypes.LITERAL_NUMBER, (int(lit, 16))))    
    return True

def s_arg(s, t):
    s.push()
    if s_qstring(s, t):
        s.dis()
        return True
    s.pop() 

    s.push()
    if s_cname(s, t):
        s.dis()
        return True
    s.pop() 

    s.push()   
    if s_hexlit(s, t):
        s.dis()
        return True
    s.pop() 
    
    s.push()   
    if s_declit(s, t):
        s.dis()
        return True    
    s.pop()
    return False    

def s_anything(data):
    return True

def s_wscutter(strm):
    while not strm.empty() and strm.peek() in " \t":
        strm.get()

def s_comment(strm):
    strm.reset()
    s_wscutter(strm)
    if(strm.peek() == '#'):
        return True
    return False

def s_comma(strm):
    try:
        if strm.peek() == ',':
            strm.get()
            return True
    except StreamException:
        return True        

    return False    

def s_stmt(strm, t):
    strm.reset()
    s_wscutter(strm)
    if not s_cname(strm, t):
        return False

    s_wscutter(strm)
    if not strm.empty():
        while(s_arg(strm,t)):
            if strm.empty() or not s_comma(strm):
                break
            s_wscutter(strm)            
    s_wscutter(strm)
    try:
        res = s_comment(strm)
        if res:
            return True
    except StreamException:
        return True    

    return False

def s_label(strm, t):
    strm.reset()
    s_wscutter(strm)

    if not strm.empty():
        if strm.peek() != ':':
            return False
        strm.get()
        if not s_cname(strm, t):
            t.pop()
            raise AsmException("Missing label name in line %i " % strm.gellnno())

        t.insert(0,(TokenTypes.LABEL_INDICATOR,":"))
        s_wscutter(strm)
        if strm.empty():
            return True
        try:
            res = s_comment(strm)
            if res:
                return True
        except StreamException:
            return True    
    return False

def s_cname(strm, t):
    CNAME0 = string.ascii_letters +  "_.!:"
    CNAME1 = string.ascii_letters + string.digits + "_."

    token = ""
    if(strm.peek() not in CNAME0):
        return False

    token = strm.get()
    try:
        while(strm.peek() in CNAME1):
            token += strm.get()
    except StreamException:
        pass

    t.append((TokenTypes.IDENTIFIER, token))
    return True

def s_parse(strm):
    t = []
    if s_comment(strm):
        return t
    if s_label(strm, t):   
        return t
    if s_stmt(strm, t):
        pass

    return t    


SYNTAX = {
    "comment" : ["#", s_anything]
}