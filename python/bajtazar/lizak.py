from enum import Enum

class Color(Enum):
    T = 2
    W = 1
    

lollipop = {}
with open('lizak.txt','r') as fp:  
    for cnt, line in enumerate(fp):
        if cnt == 0:
            lollipop["segments"] = line.rstrip("\n")
        else:
            if "parts" not in lollipop.keys():
                lollipop["parts"] = []
            lollipop["parts"].append(int(line))

print(lollipop)

collection = {}

def collect(left, right, sum):
    if sum not in collection.keys():
        collection[sum] = (left, right)

    if(sum >= 3):
        if lollipop["segments"][left]=='T':
            collect(left + 1, right , sum-2)
        elif lollipop["segments"][right]=='T':  
            collect(left, right - 1 , sum-2)  
        else:     
            collect(left + 1, right - 1 , sum-2)  

wholeSum = lollipop["segments"].count('T') * 2 + lollipop["segments"].count('W') 
left=lollipop["segments"].find('W')
right=lollipop["segments"].rfind('W')
n = len(lollipop["segments"])-1

print(left,right,n)

collect(0, n, wholeSum)


if left != -1 and right-1 < n-left:
    collect(left+1, n, wholeSum - 2*(left)-1 )
else:    
    collect(left+1, right-1, wholeSum - 2*(n-right)-1)

print(collection)

for toCheck in lollipop["parts"]:
    if(toCheck not in collection.keys()):
        print("NIE")
    else:    
        print(collection[toCheck])