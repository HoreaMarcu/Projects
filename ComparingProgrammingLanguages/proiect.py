import time
import random
import threading
 
sum = 0
class thread(threading.Thread): # thread class from geeksforgeeks: https://www.geeksforgeeks.org/how-to-create-a-new-thread-in-python/
    def __init__(self, thread_name, thread_ID):
        global steps1
        global steps2
        threading.Thread.__init__(self)
        self.thread_name = thread_name
        self.thread_ID = thread_ID
        steps1 += 2
        steps2 += 2
 
        # helper function to execute the threads
    def run(self):
        global sum
        global steps2
        threadLock.acquire()
        #print(str(self.thread_name))
        sum += self.thread_ID
        steps2 += 3
        time.sleep(0.001)
        threadLock.release()



a = [0 for x in range(102)]
steps = 0
startTime = time.time()
for i in range (0, 100):
        a[i] = random.randint(0,99)
        steps += 1

for i in range (0,100):
    for j in range(i, 100):
        if a[i] > a[j]:
            aux = a[i]
            a[i] = a[j]
            a[j] = aux
            steps += 3
endTime = time.time()
totalTime = endTime - startTime
print("Number of steps needed to sort a randomly generated array: " + str(steps) + ", in " + str(totalTime) + " seconds.")

startTime = time.time()
threadLock = threading.Lock()
steps1 = 0
steps2 = 0
threads = [thread("threadName", 0) for x in range (100)]
for i in range (0,100):
    threads[i] = thread("ThreadNumber " + str(i), i)
    steps1 += 1
    steps2 += 1
    threads[i].start()



endTime = time.time()
totalTime = endTime - startTime
time.sleep(0.0001)
print("Number of steps needed to create 100 threads: " + str(steps1))
print("Number of steps needed to add 100 numbers using 100 threads: " + str(steps2))
print("This process of thread migration has taken " + str(totalTime) + " seconds.")






