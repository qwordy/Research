def main():
  file1 = open('rq3.txt')
  file2 = open('rq3minus.txt')
  dict = {}
  add(file1, dict)
  add(file2, dict)
  #file3 = open('rq3new.txt', 'w')
  sum = 0
  for key in dict:
    #file3.write(key + ' ' + str(dict[key]) + '\n')
    sum += dict[key]
  print sum
  #file3.close()

def add(file, dict):
  for line in file:
    strs = line.split()
    name = strs[0]
    count = int(strs[1])
    dict[name] = dict.get(name, 0) + count

main()