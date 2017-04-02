import numpy as np
import matplotlib.pyplot as plt
import os

def line():
  for filename in os.listdir('data'):
    print filename
    a, b = np.loadtxt('data/' + filename, unpack=True)
    if type(a) == np.float64:
      continue
    xmin = a[0]
    xmax = a[-1]
    c = []
    d = []
    for i in range(0, len(a)):
      if i > 0:
        for j in range(int(a[i - 1] + 1), int(a[i])):
          c.append(j - xmin + 1)
          d.append(0)
      c.append(a[i] - xmin + 1)
      d.append(b[i])
    
    e = np.array(c)
    f = np.array(d)
    plt.cla()
    plt.plot(e, f)
    plt.xlim(1, xmax - xmin + 1)
    plt.ylim(0)
    
    strs = filename.split()
    title = strs[0]
    if strs[1] == 'add':
      title += ' #Add'
    else:
      title += ' #Del'
    plt.title(title)
    plt.xlabel('Time (month)')
    plt.ylabel('Count')
    plt.savefig('fig/' + filename + '.pdf')
    #plt.show()

def test():
  a = np.array([1, 2, 3, 4])
  b = np.array([2, 4, 5, 6])
  plt.plot(a, b)
  #plt.show()
  for i in a:
    print i

def histogram():
  for filename in os.listdir('data'):
    print filename
    a, b = np.loadtxt('data/' + filename, unpack=True)

def prepareData():
  lastClass = ''
  for filename in os.listdir('data'):
    print filename
    file = open('data/' + filename)
    strs = filename.split()
    if strs[0] != lastClass:
      if lastClass != '':
        writeFile(lastClass, dict)
      dict = {}
      lastClass = strs[0]
    for line in file:
      nums = line.split()
      key = int(nums[0])
      value = int(nums[1])
      dict[key] = dict.get(key, 0) + value
      #print key, dict[key]
  writeFile(lastClass, dict)
    
def writeFile(name, dict):
  out = open('newdata/' + name, 'w')
  print 'write ' + name
  for key in dict:
    out.write(str(key) + ' ' + str(dict[key]) + '\n')
  out.close()
  
def draw():
  for filename in os.listdir('newdata'):
    print filename
    a, b = np.loadtxt('newdata/' + filename, unpack=True)
    if type(a) == np.float64:
      continue
    xmin = a[0]
    xmax = a[-1]
    c = []
    d = []
    for i in range(0, len(a)):
      if i > 0:
        for j in range(int(a[i - 1] + 1), int(a[i])):
          c.append(j - xmin + 1)
          d.append(0)
      c.append(a[i] - xmin + 1)
      d.append(b[i])
    
    e = np.array(c)
    f = np.array(d)
    #print e
    #print f
    plt.cla()
    plt.plot(e, f)
    plt.xlim(1, xmax - xmin + 1)
    plt.ylim(0)
    plt.title(filename)
    plt.xlabel('Time (month)')
    plt.ylabel('Count')
    plt.savefig('newfig/' + filename + '.pdf')

def sort():
  for filename in os.listdir('newdata'):
    path = 'newdata/' + filename
    os.system('sort -n -k 1 ' + path + ' -o ' + path)

sort()
draw()
#prepareData()
#line()
#test()