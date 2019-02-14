fname = __file__.split('.')[0]

current = '-'
res = []
print(res)
with open('%s.txt' % fname,'r') as fp:
    bracket = False
    for line in fp:
        present = line.rstrip("\n")
        if present != current:
            if(len(res) == 0):
                res.append('-')
            if current == '+':
                bracket = False
                res.append(')')
                res.append('-')
            else:
                bracket = True
                res.append('(')
                res.append('-')
            current = present
        else:
            res.append('-')

    if bracket:
        res.append(')')
            
print(''.join(res))