import types 
import time

class SchedulingRequest:
    def __init__(self, request_type, request_args):
        print('# constructor')
        self.type = request_type
        self.args = request_args

def my_sleep(t):
    yield SchedulingRequest("sleep", {"time":t})

def task1():
    print("task1 1")
    yield from my_sleep(15)
    print("task1 2")
    yield 1.2

def task2():
    print("task2 1")
    yield 2.1
    print("task2 2")
    yield 2.2  

def task3():
    print("task3 1")

list_of_tasks = [
    task1,
    task2,
    task3
]

list_of_sleeping_tasks = [

]

while list_of_tasks or list_of_sleeping_tasks:

    if list_of_tasks:
        task = list_of_tasks.pop(0)
    elif list_of_sleeping_tasks:
        now = time.time()
        for index, (wake_up_time, task) in enumerate(list_of_sleeping_tasks):
            if(now > wake_up_time):
                list_of_sleeping_tasks.pop(index)
                break
        else:
            time.sleep(0.01)            
            continue

    else:
        assert False, "should never happen" 

    #print (task, list_of_tasks)

    if type(task) is types.FunctionType:
        return_value = task()

        if type(return_value) is not types.GeneratorType:
            continue

        assert type(return_value) is types.GeneratorType

        task = return_value

    try:
        ret = next(task)
        if type(ret) is SchedulingRequest:
            if ret.type == "sleep":
                wake_up_time = time.time() + ret.args["time"]
                list_of_sleeping_tasks.append((wake_up_time, task))
    except StopIteration:
        continue

    list_of_tasks.append(task)
