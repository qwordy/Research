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

confs = {
  'hadoop': '0.868 ' + u'\u2713',
  'cassandra': '0.094 ' + u'\u2717',
  'flink': '0.844 ' + u'\u2713',
  'netty': '0.910 ' + u'\u2713',
  'tomcat': '0.745 ' + u'\u2713',
  'lucene-solr': '0.896 ' + u'\u2713',
  'mahout': '0'
}

def draw2(project):
    a, b, c, d, e = np.loadtxt(project, unpack=True)
    f = e / d
    
    font = {'size': 17}
    
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
    plt.figtext(0.15, 0.8, 'Correlation=' + confs[project], fontdict=font)
    #if confs[project] > 0.7:

    plt.xlim(c.min(), c.max())
    plt.ylim(0, 1)
    
    plt.savefig(project + '.pdf')
    plt.close()
    

projects = ['hadoop', 'cassandra', 'flink', 'netty', 'tomcat', 'lucene-solr', 'mahout']
for project in projects:
    draw2(project)
