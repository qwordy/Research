import numpy as np
import matplotlib.pyplot as plt

def draw(project):
    a, b, c, d, e = np.loadtxt(project, unpack=True)

    plt.cla()
    plt.plot(c, d)
    plt.plot(c, e)
    plt.title(project.capitalize())
    plt.xlabel('Time (month)')
    plt.ylabel('Number of commits')
    plt.xlim(c.min(), c.max())
    plt.ylim(0)
    plt.savefig(project + '.pdf')
    #plt.show()

projects = ['hadoop', 'cassandra', 'flink', 'netty', 'tomcat', 'lucene-solr', 'mahout']
for project in projects:
    draw(project)
