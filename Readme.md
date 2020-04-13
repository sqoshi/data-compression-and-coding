Aby uruchomić listę 2(lab 03_04) należy podać filepath pliku do zakodowania bezpośrednio do maina jako argument lub odkomentować linijkę 
 
 i podać input przez command line, a nastepnie uruchomic:
 
 ``python3 AdaptiveArithmeticCodingWithScaling.py input
``

Aby uruchomić listę 3(lab 05_06) należy w klasie Driver umieścić w metodzie main podać:

`` String filepath =
 "/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/pan-tadeusz-czyli-ostatni-zajazd-na-litwie.txt";``
 
 i w zmiennej
 
 `` String type = "Fibbo";``

Wybrać typ kodowania indexów _{"EliasOmega","EliasGamma","EliasDelta", "Fibbo"}_,

Ostatecznie, zrobiłem tak, żeby było Pani jak najwygodniej:

####L2: python3 AdaptiveArithmeticCodingWithScaling.py filepath

####L3: javac Driver.java
####java Driver type filepath

Chociaż w przypaku L3 javy polecałbym wykorzystać IDE, mogą być problemy z release version 55/57 na niektórych urządzeniach.Wtedy należy w maine Driver'a podać manulanie path i type.

    
