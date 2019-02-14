import sys
import os
import asmparser
import struct
from conf import dict_tuples
from asmparser import StreamException
from asmparser import AsmException
from asmparser import TokenTypes
from asmparser import OutputElement
from asmparser import RegType
from asmparser import ASM_x86_REGS_16, ASM_x86_REGS_8, ASM_x86_REGS_32

class Assembler():
    def __init__(self):
        self.output = []
        self.labels = {}
        self.output_size = 0
        self.inst_dict=dict_tuples 
        
    def get_output(self):
        return self.output

    def handle_output(self, index, el_args):     
        fmt = {
            1:"B",
            2:"H",
            3:"I",
            4:"Q"
        }[el_args[1]]

        label = el_args[0]
        if label not in self.labels:
            raise AsmException("Unknown label %s" % label)
        value = self.labels[label]    

        self.output[index] = struct.pack(fmt, value)
        return True


    def handle_data(self, tokens):
        fmt = {
            "db":("B",1),
            "dw":("H",2),
            "dd":("I",4),
            "dq":("Q",8)
        }[tokens[0][1]]
       
        for token in tokens[1:]:
            if (token[0] == TokenTypes.LITERAL_STR):
                self.output_size += len(token[1])
                self.output.append(token[1])
            elif (token[0] == TokenTypes.LITERAL_NUMBER):
                self.output_size += fmt[1]
                self.output.append(struct.pack(fmt[0], token[1]))
            elif (token[0] == TokenTypes.IDENTIFIER):
                self.output.append((OutputElement.LABEL, token[1], fmt[1]))
                self.output_size += fmt[1]
            else:
                raise Exception("handle data failed")

        return True

    def handle_org(self, tokens):    
        self.org = tokens[0][1]
        return True

    def _append_regs_data(self, token, reglabel, regset):
        if token in regset:
            return (reglabel, token, regset.index(token))
        return None 

    def _try_mach(self, candidate_element, argument_element):
        if argument_element[0].startswith("imm") and candidate_element.startswith("imm"):
            argument_ver = int(argument_element[0][3:])
            candidate_ver = int(candidate_element[3:])
            if(argument_ver <= candidate_ver):
                return True
        elif argument_element[0].startswith("reg") and (candidate_element.startswith("reg") or candidate_element.startswith("r/m")):        
            argument_ver = int(argument_element[0][3:])
            candidate_ver = int(candidate_element[3:])
            if(argument_ver == candidate_ver):
                return True
        return False         

    def _emit_opcodes(self, element, args):
        if element['Len'] % 8 != 0:
           element['Opcodes'] += args[0][2]
           element['Len'] += 3

        for i in range(element['Len'] / 8):
            o = (element['Opcodes']>> (element['Len'] - (i*8) - 8)) & 0xff
            self.output.append(chr(o))
            self.output_size+=1

    def _emit_imm(self, element, args):
        for single_arg in args:
            if not single_arg[0].startswith('imm'):
                continue

            imm_var = int(single_arg[0][3:])

            #TODO negative int
            #TODO labes

            sign = 'B'
            if(imm_var == 16):
                sign = 'H'
            else:   
                sign = 'I'

            #print(sign, imm_var)    

            output = struct.pack(sign, single_arg[1])
            self.output.append(output)
            self.output_size += len(output)

    def _emit_modrm(self, element, args):
        if element['ModRM']:
            mod = 0 # 2 bits
            reg = 0 # 3 bits
            rm = 0 # 3 bits

            if element['OpReg'] is not None:
                reg = element['OpReg']
            #TODO: add proper support for  addresses in []

            mod = 0b11

            reg_type = None    

            for single_arg in element['Args']:
                if(single_arg is None):
                    continue
                if(single_arg.startswith('r/m')):
                    reg_type = RegType.RM
                    break
                elif(single_arg.startswith('reg')):   
                    reg_type = RegType.REG
                    break

            if(reg_type == RegType.REG):
                reg = args[2]
            elif(reg_type == RegType.RM):            
                rm = args[2]
            
            self.output.append(chr(
                (mod << 6) | (reg<<3) | rm
            ))
            self.output_size += 1


    def handle_ins (self, tokens):   
        ins = tokens[0][1].upper()
        if ins not in self.inst_dict.keys():
            raise AsmException("Instruction found but not found wtf '%s'" % ins)

        #arg_count = len(tokens) - 1
        arg = []
        for t in tokens[1:]:
            if t[0] == TokenTypes.IDENTIFIER:
                ovveride_literal_int = False

                tupper = t[1].upper()
                res = self._append_regs_data(tupper, 'reg8', ASM_x86_REGS_8)
                if(res == None):
                    res = self._append_regs_data(tupper, 'reg16', ASM_x86_REGS_16)
                if(res == None):    
                    res = self._append_regs_data(tupper, 'reg32', ASM_x86_REGS_32)
                if(res == None):  
                    # TODO handle labels   
                    raise AsmException("Token not found '%s'" % tupper)

                arg.append(res)
            elif t[0] is TokenTypes.LITERAL_NUMBER or ovveride_literal_int:
                size = 32 
                if t[1] < 256:
                    size = 8
                elif t[1] < 65536: #0x10000
                    size = 16
                arg.append(("imm%i"%size, t[1]))
            else:
                raise AsmException("Incorrect token in instruction arguments '%s'" % tupper)    

        el = None
        list_el = self.inst_dict.get(ins)
        for el_candidate in list_el:
            current = 0
            matched = 0
            nones = 0
            for curr_element in el_candidate["Args"]:
                if(curr_element == None):
                    nones += 1
                    continue
                if(self._try_mach(curr_element, arg[current])):
                    matched += 1
                current += 1
            if(matched == len(arg)  or (len(arg) == 0 and nones==3)):
                el = el_candidate
                break
        else:
            raise AsmException("Element not found '%s' [%s] " % (ins, arg))    
        
        print(el)

        #EMIT OPCODE
        self._emit_opcodes(el, arg)
        
        #EMIT modr/m / sib ' disp if any

        #EMIT imm
        self._emit_imm(el, arg)

        return True


    def handle_label(self, tokens, ln_no):
        if len(tokens) != 2:
            raise AsmException("Unexpected extra cacharters in label name in line %i" % ln_no)

        if(tokens[1][0] != TokenTypes.IDENTIFIER):
            raise AsmException("Syntax error at line %i" % ln_no)

        label_name = tokens[1][1]
        if label_name in self.labels:
            raise AsmException("Label redeclared in line %i" % ln_no)

        self.labels[label_name] = self.output_size
        #print (label_name, self.output_size)
        return True

    def phase1(self, data):
        for ln_no, ln in data:
            res = self._phase1_worker(ln_no, ln)       
            #print(self.output)
            if not res:
                raise AsmException("Something went wrong in phase 1 at line %i [%s]" % (ln_no, ln))

    def _phase1_worker(self, ln_no, ln):
        s = asmparser.Stream(ln, ln_no)
        try:
            tokens = asmparser.s_parse(s)
        except StreamException:
            print (ln_no, ":", ln)    

        if(len(tokens) == 0):
            return True
        if self._parse_org(tokens):   
            return self.handle_org(tokens)
        elif self._parse_instructions(tokens):
            return self.handle_ins(tokens)    
        elif self._parse_keywords(tokens):
            return self.handle_data(tokens)
        elif self._parse_label(tokens):   
            return self.handle_label(tokens, ln_no)

        raise AsmException("Unknown directive/instruction/etc in line %i %s" % (ln_no , ln))   
      
    def phase2(self):
        for i in range(len(self.output)):
            if type(self.output[i]) is str:
                continue

            #print( self.output[i])
            el_type = self.output[i][0] 
            el_args = self.output[i][1:]

            if el_type == OutputElement.LABEL:
                self.handle_output(i, el_args)
                continue

            raise AsmException("Unsupported output element %s" % repr(self.output[i]))

    def _parse_keywords(self, tokens):
        keywords = {"db", "dw", "dd", "dq"}
        if  tokens[0][0] != TokenTypes.IDENTIFIER or tokens[0][1] not in keywords:
            return False
        return True    

    def _parse_org(self, tokens):
        keywords = {"!org"}
        if tokens[0][0] != TokenTypes.IDENTIFIER or tokens[0][1] not in keywords:
            return False
        return True    

    def _parse_label(self, tokens):
        if tokens[0][0] != TokenTypes.LABEL_INDICATOR:
            return False
        return True

    def _parse_instructions(self, tokens): 
        if tokens[0][0] != TokenTypes.IDENTIFIER or tokens[0][1].upper() not in self.inst_dict.keys():
            return False
        return True    

def main():
    if len(sys.argv) != 2:
        sys.exit("usage: casm.py <fname.asm>")

    infname = sys.argv[1]
    outfname = os.path.splitext(infname)[0]+".bin"

    with open(infname, "r") as f:
        data = f.read().splitlines()

    data = [(i+1, ln) for i, ln in enumerate(data) if len(ln.strip())]

    cmp = Assembler()

    cmp.phase1(data)
    cmp.phase2()

    with open(outfname, "wb") as f:
        f.write('.'.join(cmp.get_output()))

if __name__ == "__main__":
    main()