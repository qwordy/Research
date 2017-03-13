import numpy as np
import matplotlib.pyplot as plt
import os

for filename in os.listdir('data'):
  print filename
  a, b = np.loadtxt('data/' + filename, unpack=True)
  print type(b)
  plt.cla()
  plt.plot(a, b)
  plt.xlim(a.min(), a.max())
  plt.ylim(0)
  plt.title(filename)
  plt.savefig('fig/' + filename + '.pdf')
  #plt.show()