!org 0x1234

                 
    # asdf

dd label1, label2

:label1
db "a"
db 1, 2, 3
dw 1234, 0x123

dw label1, label2

:label2
dd 0x12345678
dq 0x1234567812345678
dw 01234

dw label1, label2

dw 1

nop
nop
int3
nop
wait
mov eax, edx
int 12
rsm 
enter 0xaaaa, 0xbb
mov al, 12