#########평균으로 결과 구한 코드#########
import numpy as np
from sklearn import datasets
from sklearn.pipeline import Pipeline
from sklearn.model_selection import train_test_split
import math
import pandas as pd
import csv

#load from kobill 
from konlpy.corpus import kobill
docs_ko = [kobill.open(i).read() for i in kobill.fileids()]
#Tokenize
from konlpy.tag import Okt; t = Okt()
pos = lambda d: ['/'.join(p) for p in t.pos(d)]
texts_ko = [pos(doc) for doc in docs_ko]

from gensim.models import word2vec
wv_model_ko = word2vec.Word2Vec(texts_ko)
wv_model_ko.init_sims(replace=True)
wv_model_ko.save('ko_word2vec_e.model')

from konlpy.tag import Kkma
from konlpy.utils import pprint
kkma = Kkma()

##결과 저장 파일 열기
f = open("average_result.txt", 'wt')
num = 500
f.write("ntop : "+str(num)+'\n')

##형태소 분석 파일 열기
van_f = open("verb.txt",'rt', encoding='UTF8')

#opinion mining에서 필요한 파일 열기
om_f = open("conntojava.txt", "wt", encoding="UTF8")

##average계산
def calcAverage(List, wordClass, word_count):
    result = 0
    for i in range(len(List)):
        result = result + float(List[i][wordClass])
    return result/word_count

##max
def Mmax(pos, neg, neu):
    result = max(pos,neg,neu)
    if result == pos :
        return "POS"
    elif result == neg:
        return "NEG"
    else:
        return "NEU"
    
    
#sample.csv를 불러와서 내용을 리스트에 넣음.

with open('sample1.csv') as data:
    lines = data.readlines()

dataList = []
for line in lines:
    line = line.strip()
    dataList.append(line.split(','))


'''
with open('bso.csv') as data:
    lines = data.readlines()
    
for line in lines:
    line = line.strip()
    dataList.append(line.split(','))'''

    

##단어입력

data1=[]
while True:
    
    line = van_f.readline()
    if not line:
        break
    a2=line
    a = pos(line)
    
    try:
        b = (wv_model_ko.most_similar(a, topn=num))
    #    print("##word2vec결과")
    #    print(b)
     #   print("---------------")
    except:
        continue


## list of slicing result
  #  print("##품사판별해서 출력")
    onlyWordList=list()
    for i in range(len(b)) :
        s = b[i][0].split('/')
        if s[1]=="Noun" or s[1]=="KoreanParticle" or s[1]=="Modifier" or s[1]=="Adjective" or s[1]=="Adverb":
          #  print(s[0])
            onlyWordList.append(s)
        elif s[1]=="Verb":
         #   print(s[0])
            onlyWordList.append((kkma.pos(s[0]))[0])


   # print("---------------")
    #print("##kkma 적용한 리스트")
 #   print(onlyWordList)
   # print("---------------")

##sample.csv와 (가공한)word2vec 결과 매칭.
  #  print("##감성사전과 word2vec매칭")
    dlist = np.array(dataList)
    setd = set(dlist[:,0])
    wlist = np.array(onlyWordList)
    setw = set(wlist[:,0])

    matching_set = list(setd&setw)
    matching_list = list()

    for i in range(len(matching_set)):
        for j in range(len(dataList)):
            if matching_set[i]==dataList[j][0]: #and dataList[j][1]=='NNG':
                matching_list.append([dataList[j][2],dataList[j][3],dataList[j][4],dataList[j][5]])
    

##average
    poscount = 0
    negcount = 0
    neucount = 0
    wordcount = len(matching_list)

    for i in range(len(matching_list)) :
        if matching_list[i][3] == "POS" :
            poscount = poscount+1
        elif matching_list[i][3] == "NEG" :
            negcount = negcount+1
        else :
            neucount = neucount+1
  #  print(poscount, negcount, neucount)


    
    presult = calcAverage(matching_list,2,wordcount)
    nresult = calcAverage(matching_list,0,wordcount)
    neresult = calcAverage(matching_list,1,wordcount)

    final_result = Mmax(presult,nresult,neresult)

 #   print(presult,"  ",nresult,"  ",neresult,"  ",final_result)
    a1=str(a).split('/')
    data1.append([str(a2), str('--'), str(nresult),str(neresult),str(presult)])
    
    string = str(a)+"  " + str(presult)+"  "+str(nresult)+"  "+str(neresult)+"  "+final_result+'\n'
    
    f.write(string)
    
    if final_result=="POS" :
        ret = "1"
    elif final_result == "NEG" :
        ret = "-1"
    else :
        ret = "0"
    om_f.write(str(a2))
    om_f.write(ret+"\n")

dataframe=pd.DataFrame(data1)
dataframe.to_csv("bso.csv",header=False, index=False, mode='a')

f.write("----------------------------------------------------------------------------------------------------"+'\n')    
f.close() 
om_f.close()
van_f.close()