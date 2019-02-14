

with open("opcodes.txt") as f:
    lines = f.read().splitlines()

labels = ('Mnem', 'Opcodes', 'Len',  'ModRM', 'OpReg',  'Arg1', 'Arg2', 'Arg3')

dict_tuples = {}

for ln in lines:
    if ln.startswith('#'):
        continue

    if not ln.strip():
        continue
    
    t = [x for x in ln.split(' ') if x]
    t[1]= int(t[1],16)
    t[2]= int(t[2],10)
    t[3:] = [(None, x)[x!='-'] for x in t[3:]]

    if t[0] not in dict_tuples.keys():
        dict_tuples[t[0]] = []

    element = {}
    for idx, var in enumerate(labels):
        if "Arg" in var:
            if 'Args' not in element.keys():
                element['Args'] = []
            element['Args'].append(t[idx])
        else:
            if (var == 'OpReg') and (t[idx] != None): 
               element[var] = int(t[idx])
            else:
                element[var] = t[idx]
   
    dict_tuples[t[0]].append(element) 

#print (dict_tuples)
