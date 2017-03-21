import numpy as np
import matplotlib.pyplot as plt

def draw(project):
    a, b, c, d, e = np.loadtxt(project, unpack=True)
    f = e / d
    
    plt.cla()
    plt.plot(c, d)
    plt.plot(c, e)
    plt.plot(c, f)
    plt.title(project.capitalize())
    plt.xlabel('Time (month)')
    plt.ylabel('Number of commits')
    plt.xlim(c.min(), c.max())
    plt.ylim(0)
    plt.savefig(project + '.pdf')
    #plt.show()

def draw2(project):
    a, b, c, d, e = np.loadtxt(project, unpack=True)
    f = e / d
    
    font = {'size': 16}
    
    plt.subplot(2, 1, 1)
    plt.cla()
    plt.plot(c, d)
    plt.plot(c, e)
    #plt.title(project.capitalize())
    plt.ylabel('Number of commits', fontdict=font)
    plt.xlim(c.min(), c.max())
    plt.ylim(0)
    
    plt.subplot(2, 1, 2)
    plt.cla()
    plt.plot(c, f)
    plt.xlabel('Time (month)', fontdict=font)
    plt.ylabel('Percentage', fontdict=font)
    plt.xlim(c.min(), c.max())
    plt.ylim(0, 1)
    
    plt.savefig(project + '.pdf')
    

projects = ['hadoop', 'cassandra', 'flink', 'netty', 'tomcat', 'lucene-solr', 'mahout']
for project in projects:
    draw2(project)
